package com.credenco.webwallet.backend.rest.webmanage;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.domain.Credential;
import com.credenco.webwallet.backend.domain.Did;
import com.credenco.webwallet.backend.repository.DidRepository;
import com.credenco.webwallet.backend.repository.WalletKeyRepository;
import com.credenco.webwallet.backend.rest.webmanage.dto.CredentialDto;
import com.credenco.webwallet.backend.rest.webmanage.dto.CredentialDtoMapper;
import com.credenco.webwallet.backend.rest.webmanage.dto.DidDto;
import com.credenco.webwallet.backend.rest.webmanage.dto.DidDtoMapper;
import com.credenco.webwallet.backend.rest.webmanage.dto.PresentationOfferDto;
import com.credenco.webwallet.backend.rest.webmanage.dto.PresentationRequestDto;
import com.credenco.webwallet.backend.service.credential.CredentialDisplayService;
import com.credenco.webwallet.backend.service.credential.CredentialTypeService;
import com.credenco.webwallet.backend.service.credential.dto.CredentialTypeDto;
import com.credenco.webwallet.backend.service.did.WalletDidService;
import com.credenco.webwallet.backend.service.oidc.OidcService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.walt.oid4vc.data.CredentialOffer;
import id.walt.oid4vc.requests.AuthorizationRequest;
import id.walt.oid4vc.requests.CredentialOfferRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController()
@RequestMapping("/api/exchange")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ExchangeController {
    private final CredentialDisplayService credentialDisplayService;
    private final CredentialDtoMapper credentialDtoMapper;
    private final CredentialTypeService credentialTypeService;
    private final DidRepository didRepository;
    private final OidcService oidcService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @PostMapping(value = "/resolveCredentialOffer", consumes = TEXT_PLAIN_VALUE)
    public List<CredentialTypeDto> resolveCredentialOffer(LocalPrincipal principal,
                                                          @RequestParam(value = "locale", required = false, defaultValue = "en-US") String locale,
                                                          @RequestBody String credentialOfferUrl) {
        CredentialOffer credentialOffer = oidcService.resolveCredentialOffer(principal, credentialOfferUrl);
        return credentialTypeService.getOfferedCredentialsTypes(credentialOffer.getCredentialIssuer(), credentialOffer.getCredentialConfigurationIds(), locale);
    }

    @SneakyThrows
    @PostMapping(value = "/acceptOfferRequest", consumes = TEXT_PLAIN_VALUE)
    public List<CredentialDto> acceptOfferRequest(LocalPrincipal principal,
                                                  @RequestParam Long didId,
                                                  @RequestParam(value = "locale", required = false, defaultValue = "en-US") String locale,
                                                  @RequestBody String credentialOfferUrl) {
        final Optional<Did> did = didRepository.findByIdAndWallet(didId, principal.getWallet());
        if (did.isEmpty()) {
            throw new IllegalArgumentException("Unknown did specified");
        }

        List<Credential> credentials = oidcService.acceptOfferRequest(principal, credentialOfferUrl, did.get());
        return credentials.stream()
                .map(credential -> credentialDtoMapper.from(credential, credentialDisplayService.getCredentialDisplayDto(credential, locale)))
                .toList();
    }

    @SneakyThrows
    @PostMapping(value = "/resolvePresentationOffer", consumes = TEXT_PLAIN_VALUE)
    public PresentationOfferDto resolvePresentationOffer(LocalPrincipal principal,
                                                         @RequestParam(value = "locale", required = false, defaultValue = "en-US") String locale,
                                                         @RequestBody String presentationOfferUrl) {
        final AuthorizationRequest authorizationRequest = oidcService.resolvePresentationOffer(principal, presentationOfferUrl);
        List<Credential> credentials = oidcService.matchCredentialsForPresentationDefinition(principal.getWallet(), authorizationRequest.getPresentationDefinition());
        return PresentationOfferDto.builder()
                .verifierHost(getVerifierHost(authorizationRequest))
                .presentationRequest(authorizationRequest.toHttpQueryString())
                .presentationDefinition(objectMapper.readTree(authorizationRequest.getPresentationDefinition().toJSONString()))
                .matchedCredentials(credentials.stream()
                                            .map(credential -> credentialDtoMapper.from(credential, credentialDisplayService.getCredentialDisplayDto(credential, locale)))
                                            .toList())
                .build();
    }

    @SneakyThrows
    @PostMapping(value = "/acceptPresentationRequest", consumes = APPLICATION_JSON_VALUE)
    public String acceptPresentationRequest(LocalPrincipal principal,
                                                  @RequestParam(value = "locale", required = false, defaultValue = "en-US") String locale,
                                                  @RequestBody PresentationRequestDto presentationRequestDto) {
        final String redirectUrl = oidcService.acceptPresentationRequest(principal, presentationRequestDto.getPresentationRequest(), presentationRequestDto.getSelectedCredentials());
        return redirectUrl;
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
