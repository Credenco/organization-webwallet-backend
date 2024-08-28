package com.credenco.webwallet.backend.service.did;

import com.credenco.webwallet.backend.configuration.EbsiConfigurations;
import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.domain.CredentialFormat;
import com.credenco.webwallet.backend.domain.Did;
import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.domain.WalletKey;
import com.credenco.webwallet.backend.repository.DidRepository;
import com.credenco.webwallet.backend.repository.WalletKeyRepository;
import com.credenco.webwallet.backend.repository.WalletRepository;
import com.credenco.webwallet.backend.service.CredentialParserService;
import com.credenco.webwallet.backend.service.credential.CredentialService;
import com.credenco.webwallet.backend.service.issue.CredentialIssueService;
import com.credenco.webwallet.backend.service.issue.IssuedCredential;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.walt.did.dids.document.DidDocument;
import id.walt.did.dids.registrar.DidResult;
import id.walt.ebsi.EbsiEnvironment;
import id.walt.ebsi.accreditation.AccreditationClient;
import id.walt.ebsi.did.DidEbsiService;
import id.walt.ebsi.did.DidRegistrationOptions;
import id.walt.oid4vc.responses.CredentialResponse;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.BuildersKt;
import kotlinx.datetime.Instant;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Clock;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EbsiDidService {
    private final static String VC_ONBOARDING_CONFIG_ID = "EBSIOnboarding";
    private final static String VC_TRUSTED_ISSUER_CONFIG_ID = "EBSITrustedIssuer";
    private final static SecureRandom random = new SecureRandom();

    private final WalletRepository walletRepository;
    private final WalletKeyRepository walletKeyRepository;
    private final CredentialIssueService credentialIssueService;
    private final EbsiConfigurations ebsiConfigurations;
    private final CredentialService credentialService;
    private final DidRepository didRepository;
    private final CredentialParserService credentialParserService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public DidResult registerDid(final LocalPrincipal principal,
                                 final WalletKey mainKey,
                                 final WalletKey vcSigningKey,
                                 final EbsiEnvironment ebsiEnvironment,
                                 final String taoWalletAddress) {
        final String newDid = DidEbsiService.INSTANCE.generateRandomDid();

        final IssuedCredential vcToOnboard = createVcToOnboard(taoWalletAddress, newDid);

        final var accreditationClient = getAccreditationClient(vcSigningKey, newDid, ebsiEnvironment, vcToOnboard);

        CredentialResponse credentialResponse = BuildersKt.runBlocking(
                EmptyCoroutineContext.INSTANCE,
                (scope, continuation) ->  accreditationClient.getAuthorisationToOnboard(continuation));

        // Store the one time credential for demo purposes.
        credentialService.storeCredentials(principal, List.of(credentialResponse), accreditationClient.getTrustedIssuer(), newDid, null, null);

        final var did = BuildersKt.runBlocking(
                EmptyCoroutineContext.INSTANCE,
                (scope, continuation) ->  DidEbsiService.INSTANCE.generateAndRegisterDid(accreditationClient,
                                                                                         KeyService.INSTANCE.deserializeKey(mainKey.getDocument()),
                                                                                         KeyService.INSTANCE.deserializeKey(vcSigningKey.getDocument()),
                                                                                         createRegistrationOptions(ebsiConfigurations.getConfig(ebsiEnvironment)),
                                                                                         continuation));

        return new DidResult(newDid, new DidDocument((JsonObject)Json.Default.parseToJsonElement(resolveDid(did.toString(), ebsiEnvironment.name()))));
    }

    @SneakyThrows
    public void registerIssuer(final LocalPrincipal principal,
                                    final WalletKey mainKey,
                                    final WalletKey vcSigningKey,
                                    final EbsiEnvironment ebsiEnvironment,
                                    final String didId,
                                    final String schemaId,
                                    final String taoWalletAddress) {
        final IssuedCredential vcToRegisterIssuer = createVcToRegisterTrustedIssuer(taoWalletAddress, didId, schemaId, ebsiEnvironment);

        final var accreditationClient = getAccreditationClient(vcSigningKey, didId, ebsiEnvironment, vcToRegisterIssuer);
        CredentialResponse credentialResponse = BuildersKt.runBlocking(
                EmptyCoroutineContext.INSTANCE,
                (scope, continuation) ->  accreditationClient.getAccreditationToAttest(continuation));

        // Store the one time credential for demo purposes.
        credentialService.storeCredentials(principal, List.of(credentialResponse), accreditationClient.getTrustedIssuer(), didId, null, null);

        BuildersKt.runBlocking(
                EmptyCoroutineContext.INSTANCE,
                (scope, continuation) ->  DidEbsiService.INSTANCE.getTrustedIssuerAccreditation(accreditationClient,
                                                                                         KeyService.INSTANCE.deserializeKey(mainKey.getDocument()),
                                                                                         KeyService.INSTANCE.deserializeKey(vcSigningKey.getDocument()),
                                                                                         createRegistrationOptions(ebsiConfigurations.getConfig(ebsiEnvironment)),
                                                                                         continuation));
    }

    private IssuedCredential createVcToOnboard(String taoWalletAddress, String newDid) {
        if (taoWalletAddress == null || taoWalletAddress.length() == 0) {
            return null;
        }
        final Wallet taoWallet = getTaoWallet(taoWalletAddress);

        final Map<String, Object> credentialSubject = new HashMap<>();
        credentialSubject.put("id", newDid);

        return credentialIssueService.issueCredential(taoWallet, VC_ONBOARDING_CONFIG_ID, credentialSubject);
    }

    @SneakyThrows
    private IssuedCredential createVcToRegisterTrustedIssuer(String taoWalletAddress,
                                                             String did,
                                                             String schemaId,
                                                             EbsiEnvironment ebsiEnvironment) {
        final String reservedAttributeId = generateRandomAttributeId();
        final Wallet taoWallet = getTaoWallet(taoWalletAddress);

        final Map<String, Object> credentialSubject = new HashMap<>();
        credentialSubject.put("id", did);
        credentialSubject.put("reservedAttributeId", reservedAttributeId);

        final Map<String, Object> accreditedFor = new HashMap<>();
        accreditedFor.put("schemaId", schemaId);
        accreditedFor.put("types", List.of("VerifiableCredential", "VerifiableAttestation", "CTRevocable"));
        accreditedFor.put("limitJurisdiction", "https://publications.europa.eu/resource/authority/atu/NLD");

        credentialSubject.put("accreditedFor", List.of(accreditedFor));

        IssuedCredential vcToRegisterIssuer = credentialIssueService.issueCredential(taoWallet, VC_TRUSTED_ISSUER_CONFIG_ID, credentialSubject);
        preregisterAttributeId(taoWallet, did, ebsiEnvironment, vcToRegisterIssuer, reservedAttributeId);

        return vcToRegisterIssuer;
//        return IssuedCredential.builder()
//                .credential("eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImRpZDplYnNpOnppMUZBd1dIaHdNcXZWZDhTUTJtdXZyIzMyRnRsai15RmpPNHA4Z0w2T3N3eWRBcGRVNTV2djQ0MnctVElnVENJOUkifQ.eyJpc3MiOiJkaWQ6ZWJzaTp6aTFGQXdXSGh3TXF2VmQ4U1EybXV2ciIsInN1YiI6ImRpZDplYnNpOnpyVG41UnoxaVlaWVo4anVmSGp2N3BXIiwidmMiOnsiQGNvbnRleHQiOlsiaHR0cHM6Ly93d3cudzMub3JnLzIwMTgvY3JlZGVudGlhbHMvdjEiXSwidHlwZSI6WyJWZXJpZmlhYmxlQ3JlZGVudGlhbCIsIlZlcmlmaWFibGVBdHRlc3RhdGlvbiIsIlZlcmlmaWFibGVBY2NyZWRpdGF0aW9uIiwiVmVyaWZpYWJsZUFjY3JlZGl0YXRpb25Ub0F0dGVzdCJdLCJpc3N1ZXIiOiJkaWQ6ZWJzaTp6aTFGQXdXSGh3TXF2VmQ4U1EybXV2ciIsImlzc3VhbmNlRGF0ZSI6IjIwMjQtMDctMDdUMDk6MzU6NTIuMzgyMDIzWiIsImlzc3VlZCI6IjIwMjQtMDctMDdUMDk6MzU6NTIuMzgyMDIzWiIsInZhbGlkRnJvbSI6IjIwMjQtMDctMDdUMDk6MzU6NTIuMzgyMDIzWiIsImV4cGlyYXRpb25EYXRlIjoiMjAyNS0wNy0wN1QwOTozNTo1Mi4zOTQxODhaIiwiY3JlZGVudGlhbFN1YmplY3QiOnsiaWQiOiJkaWQ6ZWJzaTp6clRuNVJ6MWlZWllaOGp1ZkhqdjdwVyIsInJlc2VydmVkQXR0cmlidXRlSWQiOiJhMzg4ZWNiM2JlMGU1NzIxNjlhOGFiZDhlY2ExYjE2YTUyYjEzYTE1MGE1ZDIxMDUwNTgzNjgxY2JkZTQ2MjNmIiwiYWNjcmVkaXRlZEZvciI6W3sidHlwZXMiOlsiVmVyaWZpYWJsZUNyZWRlbnRpYWwiLCJWZXJpZmlhYmxlQXR0ZXN0YXRpb24iLCJDVFJldm9jYWJsZSJdLCJzY2hlbWFJZCI6Imh0dHBzOi8vYXBpLXBpbG90LmVic2kuZXUvdHJ1c3RlZC1zY2hlbWFzLXJlZ2lzdHJ5L3YzL3NjaGVtYXMvMHhlMTgzYzE3OTlmN2U5OWNiMWVkZjYwYzAyNTNjNDBjYzIxOTUxNGU2YTVmNTdiODEyYTkzZWRlMmE0NTZmM2NjIiwibGltaXRKdXJpc2RpY3Rpb24iOiJodHRwczovL3B1YmxpY2F0aW9ucy5ldXJvcGEuZXUvcmVzb3VyY2UvYXV0aG9yaXR5L2F0dS9OTEQifV19LCJ0ZXJtc09mVXNlIjpbeyJpZCI6Imh0dHBzOi8vYXBpLXBpbG90LmVic2kuZXUvdHJ1c3RlZC1pc3N1ZXJzLXJlZ2lzdHJ5L3Y1L2lzc3VlcnMvZGlkOmVic2k6emkxRkF3V0hod01xdlZkOFNRMm11dnIvYXR0cmlidXRlcy9hNzZhZGIzZjVlMjM0MWI2ZTY5OGU4ZDhmMTg0MzUwN2E3ZjBjNmU0YjgxMDM5ZTgzYzRlZDcxZmVjZGZjOWUxIiwidHlwZSI6Iklzc3VhbmNlQ2VydGlmaWNhdGUifV0sImNyZWRlbnRpYWxTY2hlbWEiOnsiaWQiOiJodHRwczovL2FwaS1waWxvdC5lYnNpLmV1L3RydXN0ZWQtc2NoZW1hcy1yZWdpc3RyeS92My9zY2hlbWFzL3ozTWdVRlVrYjcyMnVxNHgzZHY1eUFKbW5ObXpERmVLNVVDOHg4M1FvZUxKTSIsInR5cGUiOiJGdWxsSnNvblNjaGVtYVZhbGlkYXRvcjIwMjEifSwiaWQiOiJ1cm46dXVpZDpjNTliNTMwZS00YmE5LTQ2ODctYWZkNy1mM2M5OGI5NDRkZTgifSwianRpIjoidXJuOnV1aWQ6YzU5YjUzMGUtNGJhOS00Njg3LWFmZDctZjNjOThiOTQ0ZGU4IiwiZXhwIjoxNzUxODgwOTUyLCJpYXQiOjE3MjAzNDQ5NTIsIm5iZiI6MTcyMDM0NDk1Mn0.NgAKH83MexhOSMcBQ6FHTG_tWAxQ-cxhJyUiJ64St8jPPri0JRvgqY2kXTwwfyF_eFzdQXXafR9oFeSa-s_ryQ")
//                .issuerUrl("http://localhost:8088/public/45297556-95d0-4bcf-99e7-7528a11b50ff/ebsi")
//                .build();
    }

    private void preregisterAttributeId(Wallet taoWallet, String did, EbsiEnvironment ebsiEnvironment, IssuedCredential vcToRegisterIssuer, String reservedAttributeId)
            throws InterruptedException {
        final JsonNode credential = credentialParserService.extractCredential(vcToRegisterIssuer.getCredential(), CredentialFormat.JWT);
        final String taoDidId = credential.get("vc").get("issuer").asText();
        final String taoAttributeUrl = credential.get("vc").get("termsOfUse").get(0).get("id").asText();
        final String taoAttributeId = taoAttributeUrl.substring(taoAttributeUrl.lastIndexOf("/") + 1);

        final Did taoDid = didRepository.findByDidAndWallet(taoDidId, taoWallet).orElseThrow();
        final WalletKey taoMainKey = getMainKey(taoDid);
        final WalletKey taoSigningKey = getSigningKey(taoDid);

        final EbsiConfigurations.EbsiConfig ebsiConfig = ebsiConfigurations.getConfig(ebsiEnvironment);
        BuildersKt.runBlocking(
                EmptyCoroutineContext.INSTANCE,
                (scope, continuation) ->  DidEbsiService.INSTANCE.setAttributeMetadata(did,
                                                                                       KeyService.INSTANCE.deserializeKey(taoMainKey.getDocument()),
                                                                                       KeyService.INSTANCE.deserializeKey(taoSigningKey.getDocument()),
                                                                                       "0x"+ reservedAttributeId,
                                                                                       3,
                                                                                       taoDidId,
                                                                                       "0x"+ taoAttributeId,
                                                                                       ebsiConfig.getEnvironment(),
                                                                                       ebsiConfig.getDidRegistryVersion(),
                                                                                       ebsiConfig.getDidRegistryVersion() - 1,
                                                                                       continuation));
        Thread.sleep(10000);
    }

    @SneakyThrows
    private WalletKey getMainKey(final Did taoDid) {
        final JsonNode didDocument = objectMapper.readTree(taoDid.getDocument());
        final String assertionMethod = didDocument.get("capabilityInvocation").get(0).asText();
        return walletKeyRepository.findByKid(assertionMethod.substring(assertionMethod.lastIndexOf("#") + 1)).orElseThrow();
    }

    @SneakyThrows
    private WalletKey getSigningKey(final Did taoDid) {
        final JsonNode didDocument = objectMapper.readTree(taoDid.getDocument());
        final String assertionMethod = didDocument.get("assertionMethod").get(0).asText();
        return walletKeyRepository.findByKid(assertionMethod.substring(assertionMethod.lastIndexOf("#") + 1)).orElseThrow();
    }

    private String generateRandomAttributeId() {
        final byte[] randomBytes = new byte[32];
        random.nextBytes(randomBytes);
        return Hex.encodeHexString(randomBytes);
    }

    private @NotNull Wallet getTaoWallet(String taoWalletAddress) {
        final Wallet taoWallet = walletRepository.findByExternalKey(taoWalletAddress.substring(taoWalletAddress.lastIndexOf("/") + 1)).orElseThrow();
        return taoWallet;
    }

    private @NotNull AccreditationClient getAccreditationClient(final WalletKey vcSigningKey,
                                                                final String newDid,
                                                                final EbsiEnvironment ebsiEnvironment,
                                                                final IssuedCredential credential) {
        if (ebsiEnvironment == EbsiEnvironment.conformance) {
            EbsiConfigurations.EbsiConfig ebsiConfig = ebsiConfigurations.getConfig(ebsiEnvironment);
            return new AccreditationClient(ebsiConfig.getWalletClientUrl(),
                                           newDid,
                                           KeyService.INSTANCE.deserializeKey(vcSigningKey.getDocument()),
                                           ebsiConfig.getTaoIssuerUrl(),
                                           getWalletClientUri(vcSigningKey.getWallet(),"/jwks", ebsiConfig),
                                           "oidc://",
                                           getWalletClientUri(vcSigningKey.getWallet(), "", ebsiConfig));
        } else {
            return new PregeneratedVCAccreditationClient(newDid, KeyService.INSTANCE.deserializeKey(vcSigningKey.document), credential.getIssuerUrl(), credential.getCredential(), credential.getCredential());
        }
    }

    @SneakyThrows
    public String resolveDid(final String didId, final String ebsiEnvironment) {
        final EbsiConfigurations.EbsiConfig ebsiConfig = ebsiConfigurations.getConfig(EbsiEnvironment.valueOf(ebsiEnvironment));

        return BuildersKt.runBlocking(
                EmptyCoroutineContext.INSTANCE,
                (scope, continuation) ->  DidEbsiService.INSTANCE.resolveDid(didId,
                                                                             ebsiConfig.getEnvironment(),
                                                                             ebsiConfig.getDidRegistryVersion(),
                                                                             continuation)).toString();
    }

    @SneakyThrows
    public String addService(final String didId,
                             final String ebsiEnvironment,
                             final WalletKey walletKey,
                             final String serviceId,
                             final String serviceType,
                             final String serviceEndpoint) {
        final String service = String.format("""
                                           {
                                               "id":"%s#%s",
                                               "type": "%s",
                                               "serviceEndpoint": "%s"
                                           }
                                       """, didId, serviceId, serviceType, serviceEndpoint);
        final EbsiConfigurations.EbsiConfig ebsiConfig = ebsiConfigurations.getConfig(EbsiEnvironment.valueOf(ebsiEnvironment));
        BuildersKt.runBlocking(
                EmptyCoroutineContext.INSTANCE,
                (scope, continuation) ->  DidEbsiService.INSTANCE.addService(didId,
                                                                             KeyService.INSTANCE.deserializeKey(walletKey.getDocument()),
                                                                             service,
                                                                             ebsiConfig.getEnvironment(),
                                                                             ebsiConfig.getDidRegistryVersion(),
                                                                             ebsiConfig.getDidRegistryVersion() - 1,
                                                                             continuation));

        return resolveDid(didId, ebsiEnvironment);
    }

    private DidRegistrationOptions createRegistrationOptions(final EbsiConfigurations.EbsiConfig ebsiConfig) {
        return new DidRegistrationOptions(
                ebsiConfig.getEnvironment(),
                ebsiConfig.getDidRegistryVersion(),
                ebsiConfig.getDidRegistryVersion() - 1,
                Instant.Companion.fromEpochMilliseconds(Clock.systemUTC().millis()),
                Instant.Companion.fromEpochMilliseconds(Clock.systemUTC().millis() + 365*24*3600));
    }

    @NotNull
    private String getWalletClientUri(Wallet wallet, String subpath, EbsiConfigurations.EbsiConfig ebsiConfig) {
        return String.format("%s/api/oidc%s/%s", ebsiConfig.getWalletClientUrl(), subpath, wallet.getExternalKey());
    }
}
