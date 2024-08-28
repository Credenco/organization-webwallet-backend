package com.credenco.webwallet.backend.rest.webmanage;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.repository.WalletRepository;
import com.credenco.webwallet.backend.service.issue.CredentialIssueService;
import com.credenco.webwallet.backend.service.verify.CredentialVerifyService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController()
@RequestMapping("/api/credential/verify")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CredentialVerifyController {

    private final CredentialVerifyService credentialVerifyService;
    private final WalletRepository walletRepository;

    @PostMapping(path = "/{presentationConfigurationId}", consumes = APPLICATION_JSON_VALUE)
    public String verify(LocalPrincipal principal, @PathVariable String presentationConfigurationId, @RequestBody Map<String, String> parameters) {

        return credentialVerifyService.buildVcOpenIdPresentationOfferUrl(principal.getWallet(), presentationConfigurationId, parameters);
    }

    @GetMapping(path = "/session/{sessionId}", produces = APPLICATION_JSON_VALUE)
    public Map<String, CredentialVerifyService.PresentedCredential> getSession(LocalPrincipal principal, @PathVariable String sessionId, @RequestParam (required = false, defaultValue = "true") boolean savePresentation) {
        return credentialVerifyService.getStatus(principal.getWallet(), sessionId, savePresentation);
    }
}
