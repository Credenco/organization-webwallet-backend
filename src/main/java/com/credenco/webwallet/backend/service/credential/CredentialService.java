package com.credenco.webwallet.backend.service.credential;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.domain.Credential;
import com.credenco.webwallet.backend.domain.CredentialFormat;
import com.credenco.webwallet.backend.domain.CredentialStatus;
import com.credenco.webwallet.backend.domain.HistoryAction;
import com.credenco.webwallet.backend.domain.HistoryEvent;
import com.credenco.webwallet.backend.domain.Issuer;
import com.credenco.webwallet.backend.repository.CredentialRepository;
import com.credenco.webwallet.backend.service.CredentialParserService;
import com.credenco.webwallet.backend.service.HistoryService;
import com.credenco.webwallet.backend.service.IssuerService;
import com.credenco.webwallet.backend.service.SearchTextService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.bmc.util.internal.StringUtils;
import id.walt.credentials.schemes.JwsSignatureScheme;
import id.walt.credentials.verification.Verifier;
import id.walt.credentials.verification.models.PolicyRequest;
import id.walt.credentials.verification.models.PolicyResult;
import id.walt.credentials.verification.policies.EbsiTrustedIssuerPolicy;
import id.walt.credentials.verification.policies.ExpirationDatePolicy;
import id.walt.credentials.verification.policies.JwtSignaturePolicy;
import id.walt.credentials.verification.policies.NotBeforeDatePolicy;
import id.walt.ebsi.did.DidEbsiService;
import id.walt.oid4vc.responses.CredentialResponse;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.BuildersKt;
import kotlinx.serialization.json.JsonElement;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CredentialService {
    private final CredentialParserService credentialParserService;
    private final IssuerService issuerService;
    private final HistoryService historyService;
    private final CredentialRepository credentialRepository;
    private final SearchTextService searchTextService;
    private final ObjectMapper objectMapper;

    public List<Credential> storeCredentials(LocalPrincipal principal,
                                             List<CredentialResponse> credentialResponses,
                                             String issuerUrl,
                                             String did,
                                             String credentialConfigurationid,
                                             Map<String, Object> additionalInfo) {
        log.info("Get issuer metadata");
        var providerMetadataUri = issuerUrl + "/.well-known/openid-credential-issuer";

        log.info("Getting provider metadata from: {}", providerMetadataUri);
        final RestTemplate restTemplate = new RestTemplate();
        final String configurationContent = restTemplate.getForObject(providerMetadataUri, String.class);

        final LocalDateTime now = LocalDateTime.now();
        final Issuer issuer = issuerService.getOrCreateIssuer(principal.getWallet(), providerMetadataUri, configurationContent);
        List<Credential> credentials = credentialResponses.stream()
                .map(credentialResponse -> Credential.builder()
                        .wallet(principal.getWallet())
                        .issuanceDate(now)
                        .validFrom(now)
                        .document(credentialResponse.getCredential().toString().replace("\"", ""))
                        .credentialConfiguration(configurationContent)
                        .issuer(issuer)
                        .credentialFormat(CredentialFormat.JWT)
                        .credentialConfigurationId(getConfigurationId(credentialConfigurationid, credentialResponse.getCredential(), CredentialFormat.JWT))
                        .searchData(searchTextService.buildSearchText(CredentialFormat.JWT, credentialResponse.getCredential().toString().replace("\"", ""), credentialConfigurationid, issuer))
                        .createdAt(now)
                        .createdBy(principal.getName())
                        .lastModifiedAt(now)
                        .lastModifiedBy(principal.getName())
                        .status(determineCredentialStatus(credentialResponse.getCredential().toString().replace("\"", "")))
                        .build())
                .toList();
        credentialRepository.saveAll(credentials);

        try {
            historyService.addHistory(principal,
                                      HistoryEvent.CREDENTIAL,
                                      HistoryAction.OFFER_ACCEPTED,
                                      issuerUrl,
                                      createEvidence(did, additionalInfo),
                                      credentials);
        } catch (Exception e) {
            log.error("History could not be written", e);
        }
        return credentials;
    }

    @SneakyThrows
    public CredentialStatus verifyCredential(final Credential credential) {
        credential.setStatus(determineCredentialStatus(credential.getDocument()));
        credentialRepository.save(credential);
        return credential.getStatus();
    }

    @SneakyThrows
    private static CredentialStatus determineCredentialStatus(String credential) {
        List<PolicyRequest> policies = List.of(//new PolicyRequest(new JwtSignaturePolicy(), null),
                                               new PolicyRequest(new ExpirationDatePolicy(), null),
                                               new PolicyRequest(new NotBeforeDatePolicy(), null),
                                               new PolicyRequest(new EbsiTrustedIssuerPolicy(), null));

        List<PolicyResult> results = BuildersKt.runBlocking(
                EmptyCoroutineContext.INSTANCE,
                (scope, continuation) ->  Verifier.INSTANCE.verifyCredential(credential,
                                                                             policies,
                                                                             Map.of(),
                                                                             continuation));
        final boolean isValid = results.stream().allMatch(PolicyResult::isSuccess);
        return isValid ? CredentialStatus.VALID: CredentialStatus.INVALID;
    }

    @SneakyThrows
    private String getConfigurationId(String credentialConfigurationid, JsonElement credential, CredentialFormat format) {
        if (!StringUtils.isEmpty(credentialConfigurationid)) {
            return credentialConfigurationid;
        }
        JsonNode types = credentialParserService.extractCredential(credential.toString(), format).get("vc").get("type");
        if (types.isArray()) {
            int size = types.size();
            return types.get(size - 1).asText();
        }
        return "";
    }

    @SneakyThrows
    private String createEvidence(final String did, final Map<String, Object> additionalInfo) {
        Map<String, Object> evidence = new HashMap<>();
        evidence.put("did", did);
        if (additionalInfo != null) {
            evidence.putAll(additionalInfo);
        }
        return objectMapper.writeValueAsString(evidence);
    }
}
