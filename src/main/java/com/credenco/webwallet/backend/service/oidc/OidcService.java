package com.credenco.webwallet.backend.service.oidc;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.domain.Credential;
import com.credenco.webwallet.backend.domain.Did;
import com.credenco.webwallet.backend.domain.HistoryAction;
import com.credenco.webwallet.backend.domain.HistoryEvent;
import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.repository.CredentialRepository;
import com.credenco.webwallet.backend.repository.DidRepository;
import com.credenco.webwallet.backend.repository.WalletKeyRepository;
import com.credenco.webwallet.backend.service.HistoryService;
import com.credenco.webwallet.backend.service.credential.CredentialService;
import com.credenco.webwallet.backend.service.oidc.exchange.PresentationDefinitionFilterParser;
import com.credenco.webwallet.backend.service.oidc.exchange.strategies.DescriptorPresentationDefinitionMatchStrategy;
import com.credenco.webwallet.backend.service.oidc.exchange.strategies.FilterPresentationDefinitionMatchStrategy;
import com.credenco.webwallet.backend.service.oidc.exchange.strategies.PresentationDefinitionMatchStrategy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.walt.oid4vc.OpenID4VCI;
import id.walt.oid4vc.data.CredentialOffer;
import id.walt.oid4vc.data.GrantType;
import id.walt.oid4vc.data.OpenIDProviderMetadata;
import id.walt.oid4vc.data.dif.PresentationDefinition;
import id.walt.oid4vc.providers.CredentialWalletConfig;
import id.walt.oid4vc.providers.OpenIDClientConfig;
import id.walt.oid4vc.requests.AuthorizationRequest;
import id.walt.oid4vc.requests.BatchCredentialRequest;
import id.walt.oid4vc.requests.CredentialOfferRequest;
import id.walt.oid4vc.requests.CredentialRequest;
import id.walt.oid4vc.requests.TokenRequest;
import id.walt.oid4vc.responses.BatchCredentialResponse;
import id.walt.oid4vc.responses.CredentialResponse;
import id.walt.oid4vc.responses.TokenResponse;
import java.net.URI;
import java.net.URLDecoder;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.BuildersKt;
import kotlinx.serialization.json.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import static io.ktor.http.QueryKt.parseQueryString;

@Slf4j
@Service
@RequiredArgsConstructor
public class OidcService {
    private final WalletKeyRepository walletKeyRepository;
    private final DidRepository didRepository;
    private final CredentialRepository credentialRepository;
    private Map<String, OidcCredentialWallet> credentialWallets = new HashMap<>();
    private final ObjectMapper objectMapper;
    private final HistoryService historyService;
    private final CredentialService credentialService;

    public CredentialOffer resolveCredentialOffer(final LocalPrincipal principal, final String credentialOfferUrl) {
        final Wallet wallet = principal.getWallet();
        final var credentialOffer = getOrCreateOidcWallet(wallet, wallet.getExternalKey())
                .resolveCredentialOffer(CredentialOfferRequest.Companion.fromHttpParameters(parseURL(credentialOfferUrl)));

        try {
            historyService.addHistory(principal,
                                      HistoryEvent.CREDENTIAL,
                                      HistoryAction.OFFER_RECEIVED,
                                      credentialOffer.getCredentialIssuer(),
                                      objectMapper.writeValueAsString(Map.of("offer", credentialOffer)),
                                      null);
        } catch (Exception e) {
            log.error("History could not be written", e);
        }
        return credentialOffer;
    }

    public List<Credential> acceptOfferRequest(final LocalPrincipal principal, final String credentialOfferUrl, final Did did) {
        OidcCredentialWallet credentialWallet = getOrCreateOidcWallet(principal.getWallet(), did.getDid());
        CredentialOffer credentialOffer = credentialWallet
                .resolveCredentialOffer(CredentialOfferRequest.Companion.fromHttpParameters(parseURL(credentialOfferUrl)));

        List<CredentialResponse> credentialResponses = processCredentialOffer(credentialOffer, credentialWallet, "test-client");
        return credentialService.storeCredentials(principal,
                                                  credentialResponses,
                                                  credentialOffer.getCredentialIssuer(),
                                                  did.getDid(),
                                                  credentialOffer.getCredentialConfigurationIds().stream().findFirst().get(),
                                                  Map.of("offer", credentialOffer));
    }

    public static MultiValueMap<String, String> parseURL(String urlString) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(urlString);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

        builder.build().getQueryParams()
                .forEach((key, values) -> multiValueMap.put(key, values.stream()
                .map(URLDecoder::decode).toList()));

        return multiValueMap;
    }

    private OidcCredentialWallet getOrCreateOidcWallet(Wallet wallet, String walletExternalKey) {
        if (!credentialWallets.containsKey(walletExternalKey)) {
            credentialWallets.put(walletExternalKey, new OidcCredentialWallet(wallet, walletKeyRepository, credentialRepository, didRepository, new CredentialWalletConfig("http://blank"), walletExternalKey));
        }
        return credentialWallets.get(walletExternalKey);
    }

    @SneakyThrows
    private List<CredentialResponse> processCredentialOffer(final CredentialOffer credentialOffer,
                                                            final OidcCredentialWallet credentialWallet,
                                                            final String clientId) {
        log.info("Get issuer metadata");
        var providerMetadataUri =
                credentialWallet.getCIProviderMetadataUrl(credentialOffer.getCredentialIssuer());

        log.info("Getting provider metadata from: {}", providerMetadataUri);
        final RestTemplate restTemplate = new RestTemplate();
        final String configurationContent = restTemplate.getForObject(providerMetadataUri, String.class);

        final OpenIDProviderMetadata openIDProviderMetadata = OpenIDProviderMetadata.Companion.fromJSONString(configurationContent);

        log.info("Resolve offered credentials");
        var offeredCredentials = OpenID4VCI.INSTANCE.resolveOfferedCredentials(credentialOffer, openIDProviderMetadata);

        log.info("offeredCredentials: {}", offeredCredentials);

        log.debug("Fetch access token using pre-authorized code (skipping authorization step)");
        var tokenReq = new TokenRequest(
                GrantType.pre_authorized_code,
                clientId,
                credentialWallet.getConfig().getRedirectUri(),
                null,
                credentialOffer.getGrants().get(GrantType.pre_authorized_code.getValue()).getPreAuthorizedCode(),
                null,
                null,
                new HashMap<>());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create HttpEntity with headers and form parameters
        HttpEntity<Map<String, List<String>>> requestEntity = new HttpEntity<>(tokenReq.toHttpParameters().entrySet().stream()
                                                                                       .collect(LinkedMultiValueMap::new,
                                                                                                (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                                                                                                MultiValueMap::putAll), headers);

        log.info(tokenReq.toHttpParameters().toString());

        // Make the HTTP POST request
        String tokenEndpoint = openIDProviderMetadata.getTokenEndpoint() != null ?
                openIDProviderMetadata.getTokenEndpoint() : openIDProviderMetadata.getCredentialIssuer() + "/token";
        var tokenResp = TokenResponse.Companion.fromJSON(restTemplate.postForObject(tokenEndpoint, requestEntity, JsonObject.class));

        log.info(">>> Token response = success: {}}", tokenResp);

        log.info(">>> Token response = success: {}}", tokenResp.isSuccess());

        log.info("// receive credential");
        var nonce = tokenResp.getCNonce();


        log.info("Using issuer URL: {}", credentialOffer.getCredentialIssuer());
        val credReqs = offeredCredentials.stream().map(offeredCredential ->
                                                               CredentialRequest.Companion.forOfferedCredential(
                        offeredCredential, credentialWallet.generateDidProof(
                                credentialWallet.getDid(),
                                credentialOffer.getCredentialIssuer(),
                                nonce,
                                new OpenIDClientConfig(clientId, null, null, false)
                        )
                )).toList();
        log.info("credReqs: {}", credReqs);

        if (credReqs.size() >= 2) {
            var batchCredentialRequest = new BatchCredentialRequest(credReqs, new HashMap<>());

            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(tokenResp.getAccessToken());

            var batchCredentialResponse = BatchCredentialResponse.Companion.fromJSON(restTemplate.postForObject(openIDProviderMetadata.getBatchCredentialEndpoint(), new HttpEntity<>(batchCredentialRequest.toJSON(), headers), JsonObject.class));
            var credentialResponses =  batchCredentialResponse.getCredentialResponses();
            if (credentialResponses == null) {
                throw new IllegalArgumentException("No credential responses returned");
            }
            return credentialResponses;

        } else if (credReqs.size() == 1) {
            var credReq = credReqs.get(0);

            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(tokenResp.getAccessToken());


            log.info("tokenResp.getAccessToken(): {}", tokenResp.getAccessToken());
            log.info("openIDProviderMetadata.getCredentialEndpoint(): {} ", openIDProviderMetadata.getCredentialEndpoint());

            var credentialResponse = CredentialResponse.Companion.fromJSON(restTemplate.postForObject(openIDProviderMetadata.getCredentialEndpoint(), new HttpEntity<>(credReq.toJSON(), headers), JsonObject.class));
            return List.of(credentialResponse);
        } else {
            throw new IllegalStateException("No credentials offered");
        }
    }

    @SneakyThrows
    public AuthorizationRequest resolvePresentationOffer(final LocalPrincipal principal, final String presentationOfferUrl) {
        final Wallet wallet = principal.getWallet();
        final AuthorizationRequest authorizationRequest = BuildersKt.runBlocking(
                EmptyCoroutineContext.INSTANCE,
                (scope, continuation) -> getOrCreateOidcWallet(wallet, wallet.getExternalKey()).parsePresentationRequest(presentationOfferUrl, continuation)
        );

        try {
            historyService.addHistory(principal,
                                      HistoryEvent.PRESENTATION,
                                      HistoryAction.OFFER_RECEIVED,
                                      getVerifierHost(authorizationRequest),
                                      objectMapper.writeValueAsString(authorizationRequest),
                                      null);
        } catch (Exception e) {
            log.error("History could not be written", e);
        }

        return authorizationRequest;
    }

    @SneakyThrows
    public String acceptPresentationRequest(final LocalPrincipal principal,
                                            final String presentationRequest,
                                            final List<String> selectedCredentialIds) {
        final Wallet wallet = principal.getWallet();
        Did did = didRepository.findAllByWallet(wallet).getFirst();
        OidcCredentialWallet credentialWallet = getOrCreateOidcWallet(wallet, did.getDid());

        final var authReq = (AuthorizationRequest) BuildersKt.runBlocking(
                EmptyCoroutineContext.INSTANCE,
                (scope, continuation) -> AuthorizationRequest.Companion.fromHttpParametersAuto(parseQueryString(presentationRequest, 0, 1000, true)
                                                                                                                              .entries().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                                                                                                                      continuation)
        );

        log.debug("Auth req: " + authReq);

        log.debug("Using presentation request, selected credentials: ${parameter.selectedCredentials}");

        SessionAttributes.INSTANCE.getHACK_outsideMappedSelectedCredentialsPerSession().put(authReq.getState() + authReq.getPresentationDefinition(),  selectedCredentialIds);

        val presentationSession =
                credentialWallet.initializeAuthorization(authReq, Duration.ofSeconds(60), new HashSet<>(selectedCredentialIds));
        log.debug("Initialized authorization (VPPresentationSession): $presentationSession");

        log.debug("Resolved presentation definition: ${presentationSession.authorizationRequest!!.presentationDefinition!!.toJSONString()}");

        val tokenResponse = credentialWallet.processImplicitFlowAuthorization(presentationSession.getAuthorizationRequest());

        final RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Map<String, List<String>>> requestEntity = new HttpEntity<>(tokenResponse.toHttpParameters().entrySet().stream()
                                                                                       .collect(LinkedMultiValueMap::new,
                                                                                                (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                                                                                                MultiValueMap::putAll), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(getReponseUri(presentationSession.getAuthorizationRequest()), requestEntity, String.class );
        try {
            historyService.addHistory(principal,
                                      HistoryEvent.PRESENTATION,
                                      HistoryAction.OFFER_ACCEPTED,
                                      getVerifierHost(authReq),
                                      objectMapper.writeValueAsString(Map.of(
                                                                              "request",  authReq,
                                                                              "selected-credentials", selectedCredentialIds.stream().collect(Collectors.joining(",")),
                                                                              "success",  "true",
                                                                              "redirect", getRedirectUri(response.getBody()) != null ? getRedirectUri(response.getBody()) : "null")),
                                      null);
        } catch (Exception e) {
            log.error("History could not be written", e);
        }

        return getRedirectUri(response.getBody());
    }

    @SneakyThrows
    private String getRedirectUri(String body) {
        if (body == null || body.startsWith("http")) {
            return body;
        }
        final JsonNode redirectUri = objectMapper.readTree(body).get("redirect_uri");
        return redirectUri != null ? redirectUri.asText(): null;
    }

    @Nullable
    private static String getReponseUri(AuthorizationRequest authReq) {
        return authReq.getResponseUri() != null ? authReq.getResponseUri() : authReq.getRedirectUri();
    }

    public List<Credential> matchCredentialsForPresentationDefinition(final Wallet wallet, final PresentationDefinition presentationDefinition) {
        final List<Credential> credentialList = credentialRepository.findByWallet(wallet);
        final List<PresentationDefinitionMatchStrategy<List<? extends Credential>>> strategies = List.of(
                new FilterPresentationDefinitionMatchStrategy(new PresentationDefinitionFilterParser()),
                new DescriptorPresentationDefinitionMatchStrategy()
        );
        final List<Credential> matchedCredentials = new ArrayList<>();

        strategies.forEach(strategy -> {
            matchedCredentials.addAll(strategy.match(credentialList, presentationDefinition));
        });
        return matchedCredentials;
    }

    @SneakyThrows
    private String getVerifierHost(final AuthorizationRequest authorizationRequest) {
        if (authorizationRequest.getResponseUri() != null) {
            return new URI(authorizationRequest.getResponseUri()).getHost();
        } else if (authorizationRequest.getRedirectUri() != null) {
            return new URI(authorizationRequest.getRedirectUri()).getHost();
        }
        return "";
    }
}
