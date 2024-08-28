package com.credenco.webwallet.backend.rest.webpublic;

import com.credenco.webwallet.backend.service.IssuedCredentialService;
import com.credenco.webwallet.backend.service.issue.CredentialIssueService;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController()
@RequestMapping("/public")
@Slf4j
@PreAuthorize("permitAll()")
@CrossOrigin(originPatterns = "*")
public class OidcServerController {
    private final CredentialIssueService credentialIssueService;
    private final IssuedCredentialService issuedCredentialService;
    private final RestTemplate restTemplate;


    public OidcServerController(@Qualifier("issuerApi") final RestTemplate restTemplate,
                                final CredentialIssueService credentialIssueService, final IssuedCredentialService issuedCredentialService) {
        this.restTemplate = restTemplate;
        this.credentialIssueService = credentialIssueService;
        this.issuedCredentialService = issuedCredentialService;
    }

    @GetMapping(path="/{walletExternalKey}/{credentialIssuerDefinitionExternalKey}/.well-known/openid-credential-issuer")
    public JsonNode getIssuerMetadata(@PathVariable String walletExternalKey, @PathVariable String credentialIssuerDefinitionExternalKey) {
        return credentialIssueService.getIssuerConfiguration(walletExternalKey, credentialIssuerDefinitionExternalKey);
    }

    @GetMapping(path="/{walletExternalKey}/{credentialIssuerDefinitionExternalKey}/.well-known/openid-configuration")
    public JsonNode getOpenIdConfiguration(@PathVariable String walletExternalKey, @PathVariable String credentialIssuerDefinitionExternalKey) {
        return credentialIssueService.getIssuerConfiguration(walletExternalKey, credentialIssuerDefinitionExternalKey);
    }

    @GetMapping(path="/{walletExternalKey}/{credentialIssuerDefinitionExternalKey}/.well-known/oauth-authorization-server")
    public JsonNode getOauthAuthorizationServer(@PathVariable String walletExternalKey, @PathVariable String credentialIssuerDefinitionExternalKey) {
        return credentialIssueService.getIssuerConfiguration(walletExternalKey, credentialIssuerDefinitionExternalKey);
    }

    @GetMapping(path="/{walletExternalKey}/{credentialIssuerDefinitionExternalKey}/authorize")
    public ResponseEntity<Object> authorize(@PathVariable String walletExternalKey,
                                            @PathVariable String credentialIssuerDefinitionExternalKey,
                                            @RequestHeader MultiValueMap<String, String> headers,
                                            @RequestParam MultiValueMap<String,String> params) {
        HttpEntity<Map<String, List<String>>> request = new HttpEntity<>(null, headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(restTemplate.getUriTemplateHandler().expand("/authorize").toString());
        params.forEach(uriBuilder::queryParam);

        URI uri = uriBuilder.build().toUri();

        return restTemplate.exchange(uri, HttpMethod.GET, request, Object.class);
    }

    @PostMapping(path="/{walletExternalKey}/{credentialIssuerDefinitionExternalKey}/token")
    public ResponseEntity<?> token(@PathVariable String walletExternalKey,
                                   @PathVariable String credentialIssuerDefinitionExternalKey,
                                   @RequestHeader MultiValueMap<String, String> headers,
                                   @RequestParam MultiValueMap<String,String> params) {
        HttpEntity<Map<String, List<String>>> request = new HttpEntity<>(params, headers);
        return restTemplate.exchange("/token", HttpMethod.POST, request, String.class);
    }

    @PostMapping(path="/{walletExternalKey}/{credentialIssuerDefinitionExternalKey}/par")
    public ResponseEntity<?> par(@PathVariable String walletExternalKey,
                                 @PathVariable String credentialIssuerDefinitionExternalKey,
                                 @RequestHeader MultiValueMap<String, String> headers,
                                 @RequestParam MultiValueMap<String,String> params) {
        HttpEntity<Map<String, List<String>>> request = new HttpEntity<>(params, headers);
        return restTemplate.exchange("/par", HttpMethod.POST, request, Object.class);
    }

    @GetMapping(path="/{walletExternalKey}/{credentialIssuerDefinitionExternalKey}/jwks")
    public ResponseEntity<?> jwks(@PathVariable String walletExternalKey,
                                  @PathVariable String credentialIssuerDefinitionExternalKey,
                                  @RequestHeader MultiValueMap<String, String> headers,
                                  @RequestParam MultiValueMap<String,String> params) {
        HttpEntity<Map<String, List<String>>> request = new HttpEntity<>(params, headers);
        return restTemplate.exchange("/jwks", HttpMethod.GET, request, Object.class);
    }

    @PostMapping(path="/{walletExternalKey}/{credentialIssuerDefinitionExternalKey}/direct_post")
    public ResponseEntity<?> directPost(@PathVariable String walletExternalKey,
                                        @PathVariable String credentialIssuerDefinitionExternalKey,
                                        @RequestHeader MultiValueMap<String, String> headers,
                                        @RequestParam MultiValueMap<String,String> params) {
        HttpEntity<Map<String, List<String>>> request = new HttpEntity<>(params, headers);
        return restTemplate.exchange("/direct_post", HttpMethod.POST, request, Object.class);
    }

    @PostMapping(path="/{walletExternalKey}/{credentialIssuerDefinitionExternalKey}/credential")
    public ResponseEntity<?> credential(@PathVariable String walletExternalKey,
                                             @PathVariable String credentialIssuerDefinitionExternalKey,
                                             @RequestHeader MultiValueMap<String, String> headers,
                                             @RequestBody JsonNode params) {
        HttpEntity<JsonNode> request = new HttpEntity<>(params, headers);
        final ResponseEntity<JsonNode> responseEntity = restTemplate.exchange("/credential", HttpMethod.POST, request, JsonNode.class);
        issuedCredentialService.storeIssuedCredential(walletExternalKey, credentialIssuerDefinitionExternalKey, responseEntity.getBody());
        return responseEntity;
    }

    @PostMapping(path="/{walletExternalKey}/{credentialIssuerDefinitionExternalKey}/credential_deferred")
    public ResponseEntity<?> credentialDeferred(@PathVariable String walletExternalKey,
                                                     @PathVariable String credentialIssuerDefinitionExternalKey,
                                                     @RequestHeader MultiValueMap<String, String> headers,
                                                     @RequestBody JsonNode params) {
        HttpEntity<JsonNode> request = new HttpEntity<>(params, headers);
        return restTemplate.exchange("/credential_deferred", HttpMethod.POST, request, JsonNode.class);
    }

    @PostMapping(path="/{walletExternalKey}/{credentialIssuerDefinitionExternalKey}/batch_credential")
    public ResponseEntity<?> batch_credential(@PathVariable String walletExternalKey,
                                          @PathVariable String credentialIssuerDefinitionExternalKey,
                                          @RequestHeader MultiValueMap<String, String> headers,
                                              @RequestBody JsonNode params) {
        HttpEntity<JsonNode> request = new HttpEntity<>(params, headers);
        return restTemplate.exchange("/batch_credential", HttpMethod.POST, request, JsonNode.class);
    }
}
