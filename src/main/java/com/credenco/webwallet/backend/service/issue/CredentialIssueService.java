package com.credenco.webwallet.backend.service.issue;

import com.credenco.webwallet.backend.domain.CredentialIssueTemplate;
import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.domain.WalletKey;
import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerCredentialAttribute;
import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerCredentialDefinition;
import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerCredentialType;
import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerDefinition;
import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerDisplay;
import com.credenco.webwallet.backend.repository.CredentialIssueTemplateRepository;
import com.credenco.webwallet.backend.repository.CredentialIssuerDefinitionRepository;
import com.credenco.webwallet.backend.service.did.KeyService;
import com.credenco.webwallet.backend.service.issue.apiclient.IssuerApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.walt.credentials.issuance.Issuer;
import id.walt.credentials.vc.vcs.W3CVC;
import id.walt.crypto.keys.Key;
import id.walt.oid4vc.data.AttributeDefinition;
import id.walt.oid4vc.data.CredentialDefinition;
import id.walt.oid4vc.data.CredentialFormat;
import id.walt.oid4vc.data.CredentialSupported;
import id.walt.oid4vc.data.DisplayProperties;
import id.walt.oid4vc.data.GrantType;
import id.walt.oid4vc.data.LogoProperties;
import id.walt.oid4vc.data.OpenIDProviderMetadata;
import id.walt.oid4vc.data.ResponseMode;
import id.walt.oid4vc.data.ResponseType;
import id.walt.oid4vc.data.SubjectType;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.BuildersKt;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialIssueService {
    private final CredentialIssuerDefinitionRepository credentialIssuerDefinitionRepository;
    private final CredentialIssueTemplateRepository credentialIssueTemplateRepository;
    private final IssuerApiClient issuerApiClient;
    private final ObjectMapper objectMapper;

    public String buildVcOpenIdCredentialOfferUrl(final Wallet wallet, final String credentialConfigurationId, final Map<String, Object> credentialsSubject) {
        final CredentialIssueTemplate credentialIssueTemplate = credentialIssueTemplateRepository.findByWalletAndCredentialConfigurationId(wallet, credentialConfigurationId).orElseThrow(() -> new IllegalArgumentException("Template " + credentialConfigurationId + " for wallet " + wallet.getId() + " not found."));
        return issuerApiClient.openid4vcJwtIssue(credentialIssueTemplate.getTemplate(),
                                                 credentialIssueTemplate.getCredentialConfigurationId(),
                                                 credentialsSubject,
                                                 credentialIssueTemplate.getIssuerDid(),
                                                 "https://wallet.acc.credenco.com/public/" + credentialIssueTemplate.getWallet().getExternalKey() + "/" + credentialIssueTemplate.getCredentialIssuerDefinition().getExternalKey());//credentialIssueTemplate.getCredentialIssuerDefinition().getIssuerUrl());
    }

    @SneakyThrows
    public IssuedCredential issueCredential(final Wallet wallet, final String credentialConfigurationId, final Map<String, Object> credentialSubject) {
        final CredentialIssueTemplate credentialIssueTemplate = credentialIssueTemplateRepository
                .findByWalletAndCredentialConfigurationId(wallet, credentialConfigurationId)
                .orElseThrow(() -> new IllegalArgumentException("Template " + credentialConfigurationId + " for wallet " + wallet.getId() + " not found."));
        final String issuerKid = getIssuerKid(credentialIssueTemplate);
        W3CVC vc = W3CVC.Companion.fromJson(credentialIssueTemplate.getTemplate());

        String credential = BuildersKt.runBlocking(
                EmptyCoroutineContext.INSTANCE,
                (scope, continuation) ->  Issuer.INSTANCE.mergingJwtIssue(vc,
                                                                          getIssuanceKey(credentialIssueTemplate.getIssuerDid().getWalletKey()),
                                                                          credentialIssueTemplate.getIssuerDid().getDid(),
                                                                          issuerKid,
                                                                          credentialSubject.get("id").toString(),
                                                                          getCredentialData(credentialSubject),
                                                                          Map.of(),
                                                                          Map.of(),
                                                                          true,
                                                                          continuation));
        return IssuedCredential.builder()
                .issuerUrl(credentialIssueTemplate.getCredentialIssuerDefinition().getIssuerUrl())
                .credential(credential)
                .build();
    }



    private String getIssuerKid(final CredentialIssueTemplate template) {
        final String did = template.getIssuerDid().getDid();
        if (did.startsWith("did:ebsi")) {
            final String issuerKeyId = template.getIssuerDid().getWalletKey().getKid();
            return did + "#" + issuerKeyId;
        }
        return did;
    }

    @SneakyThrows
    private Key getIssuanceKey(final @NotNull WalletKey walletKey) {
        return KeyService.INSTANCE.deserializeKey(walletKey.getDocument());
    }

    @SneakyThrows
    private JsonObject getCredentialData(final Map<String, Object> credentialsSubject) {
        credentialsSubject.putIfAbsent("id", "<subjectDid>");
        final String now = Instant.now().toString();

        Map<String, Object> credentialData = new HashMap<>();
        credentialData.put("id", "<uuid>");
        credentialData.put("issuer", "<issuerDid>");
        credentialData.put("credentialSubject", credentialsSubject);
        credentialData.put("issuanceDate", now);
        credentialData.put("issued", now);
        credentialData.put("validFrom", now);
        credentialData.put("expirationDate", "<timestamp-in:365d>");

        return (JsonObject) Json.Default.parseToJsonElement(objectMapper.writeValueAsString(credentialData));
    }

    @SneakyThrows
    public JsonNode getIssuerConfiguration(String walletExternalKey, String credentialIssuerDefinitionExternalKey) {
        final var issuerDefinition = credentialIssuerDefinitionRepository.findByWalletExternalKeyAndExternalKey(walletExternalKey, credentialIssuerDefinitionExternalKey).orElseThrow();

        final var metadata = new OpenIDProviderMetadata(
                issuerDefinition.getIssuerUrl(),                // issuer
                issuerDefinition.getIssuerUrl() + "/authorize", // authorization_endpoint
                issuerDefinition.getIssuerUrl() + "/par",       // pushed_authorization_request_endpoint
                issuerDefinition.getIssuerUrl() + "/token",     // token_endpoint
                null,                                           // userinfo_endpoint
                issuerDefinition.getIssuerUrl() + "/jwks",      // jwks_uri
                null,                                           // registration_endpoint
                Set.of("openid"),                               // scopes_supported
                Set.of(ResponseType.Code, ResponseType.VpToken, ResponseType.IdToken),// response_types_supported, (EBSI) this is required one  https://www.rfc-editor.org/rfc/rfc8414.html#section-2
                Set.of(ResponseMode.query, ResponseMode.fragment),   // response_modes_supported
                Set.of(GrantType.authorization_code, GrantType.pre_authorized_code), // grant_types_supported
                null,                                           // acr_values_supported
                Set.of(SubjectType.valueOf("public")),      // subject_types_supported
                Set.of("ES256"),// id_token_signing_alg_values_supported, (EBSI) https://openid.net/specs/openid-connect-self-issued-v2-1_0.html#name-self-issued-openid-provider-
                null, // id_token_encryption_alg_values_supported
                null, // id_token_encryption_enc_values_supported
                null, // userinfo_signing_alg_values_supported
                null, // userinfo_encryption_alg_values_supported
                null, // userinfo_encryption_enc_values_supported
                null, // request_object_signing_alg_values_supported
                null, // request_object_encryption_alg_values_supported
                null, // request_object_encryption_enc_values_supported
                null, // token_endpoint_auth_methods_supported
                null, // token_endpoint_auth_signing_alg_values_supported
                null, // display_values_supported
                null, // claim_types_supported
                null, // claims_supported
                null, // service_documentation
                null, // claims_locales_supported
                null, // ui_locales_supported
                false, // claims_parameter_supported
                false, // request_parameter_supported
                true, // request_uri_parameter_supported
                false, // require_request_uri_registration
                null, // op_policy_uri
                null, // op_tos_uri
                issuerDefinition.getIssuerUrl(),// credential_issuer, (EBSI) this should be just "$baseUrl"  https://openid.net/specs/openid-4-verifiable-credential-issuance-1_0.html#section-11.2.1
                issuerDefinition.getIssuerUrl() + "/credential", // credential_endpoint
                getCredentialConfigurationSupported(issuerDefinition), // credential_configurations_supported
                issuerDefinition.getIssuerUrl() + "/batch_credential", // batch_credential_endpoint
                issuerDefinition.getIssuerUrl() + "/credential_deferred", // deferred_credential_endpoint
                null, // authorization_servers
                getDisplayProperties(issuerDefinition), // display
                null, // presentation_definition_uri_supported
                null, // client_id_schemes_supported
                issuerDefinition.getIssuerUrl(), // authorization_server,
                Map.of());

        return objectMapper.readTree(metadata.toJSONString());
    }

    private Map<String, CredentialSupported> getCredentialConfigurationSupported(final CredentialIssuerDefinition issuerDefinition) {
        return issuerDefinition.getCredentialDefinitions().stream()
                .collect(Collectors.toMap(CredentialIssuerCredentialDefinition::getCredentialConfigurationId, this::getCredentialSupported));
    }

    private List<DisplayProperties> getDisplayProperties(final CredentialIssuerDefinition issuerDefinition) {
        final Optional<CredentialIssuerDisplay> defaultDisplay = issuerDefinition.getDisplays().stream()
                .filter(display -> display.getLocale() == null)
                .findFirst();
        return issuerDefinition.getDisplays().stream()
                .map(display -> merge(defaultDisplay, display))
                .toList();
    }

    private DisplayProperties merge(final Optional<CredentialIssuerDisplay> defaultDisplay, final CredentialIssuerDisplay displayProperties) {
        if (defaultDisplay.isEmpty()) {
            return new DisplayProperties(displayProperties.getDisplayName(),
                                         displayProperties.getLocale(),
                                         getLogoProperties(displayProperties.getLogoUrl(), displayProperties.getLogoAltText()),
                                         null,
                                         null,
                                         null,
                                         Map.of());
        }
        final CredentialIssuerDisplay defaultDisplayValue = defaultDisplay.get();
        return new DisplayProperties((displayProperties.getDisplayName() == null) ? defaultDisplayValue.getDisplayName() : displayProperties.getDisplayName(),
                                     (displayProperties.getLocale() == null) ? defaultDisplayValue.getLocale() : displayProperties.getLocale(),
                                     getLogoProperties(
                                             (displayProperties.getLogoUrl() == null) ? defaultDisplayValue.getLogoUrl() : displayProperties.getLogoUrl(),
                                             (displayProperties.getLogoAltText() == null) ? defaultDisplayValue.getLogoAltText() : displayProperties.getLogoAltText()
                                     ),
                                     null,
                                     null,
                                     null,
                                     Map.of());
    }

    private LogoProperties getLogoProperties(final String logoUrl, final String logoAltText) {
        if (logoUrl != null) {
            return new LogoProperties(logoUrl, logoAltText, Map.of());
        }
        return null;
    }

    private CredentialSupported getCredentialSupported(final CredentialIssuerCredentialDefinition credentialDefinition) {
        return new CredentialSupported(CredentialFormat.valueOf(credentialDefinition.getFormat()),
                                       null,
                                       Set.of("did:web", "did:jwk", "did:ebsi"),
                                       Set.of("EdDSA", "ES256", "ES256K", "RSA"),
                                       getDisplayProperties(credentialDefinition),
                                       null,
                                       getCredentialDefinition(credentialDefinition),
                                       null,
                                       null,
                                       null,
                                       null,
                                       null,
                                       Map.of()
        );
    }

    private List<DisplayProperties> getDisplayProperties(final CredentialIssuerCredentialDefinition credentialDefinition) {
        return credentialDefinition.getDisplays().stream()
                .map(display -> new DisplayProperties(display.getDisplayName(),
                                                      display.getLocale(),
                                                      null,
                                                      null,
                                                      display.getBackgroundColor(),
                                                      display.getTextColor(),
                                                      display.getBackgroundImageUrl() != null ? Map.of("background_image", getLogoProperties(display.getBackgroundImageUrl(), display.getBackgroundAltText()).toJSON()):Map.of()))
                .toList();
    }

    private CredentialDefinition getCredentialDefinition(CredentialIssuerCredentialDefinition credentialDefinition) {
        return new CredentialDefinition(credentialDefinition.getTypes().stream().map(CredentialIssuerCredentialType::getCredentialType).toList(),
                                        getCredentialSubject(credentialDefinition),
                                        Map.of());
    }

    private Map<String, AttributeDefinition> getCredentialSubject(CredentialIssuerCredentialDefinition credentialDefinition) {
        return credentialDefinition.getAttributes().stream()
                .collect(Collectors.toMap(CredentialIssuerCredentialAttribute::getAttributeName,
                                          this::getAttributeDefinition,
                                          (existing, replacement) -> existing,
                                          LinkedHashMap::new));
    }

    private AttributeDefinition getAttributeDefinition(CredentialIssuerCredentialAttribute attribute) {
        return new AttributeDefinition(
                attribute.isMandatory(),
                null,
                getDisplayProperties(attribute),
                Map.of());
    }

    private List<DisplayProperties> getDisplayProperties(final CredentialIssuerCredentialAttribute attribute) {
        return attribute.getDisplays().stream()
                .map(display -> new DisplayProperties(display.getDisplayName(),
                                                      display.getLocale(),
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      Map.of()))
                .toList();
    }

}
