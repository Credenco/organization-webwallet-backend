package com.credenco.webwallet.backend.rest.webmanage;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.repository.WalletRepository;
import com.credenco.webwallet.backend.service.issue.CredentialIssueService;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController()
@RequestMapping("/api/credential/issue")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CredentialIssueController {

    private final CredentialIssueService credentialIssueService;
    private final WalletRepository walletRepository;

    @PostMapping(path = "/{credentialConfigurationId}", consumes = APPLICATION_JSON_VALUE)
    public String issue(LocalPrincipal principal, @PathVariable String credentialConfigurationId, @RequestBody Map<String, Object> credentialsSubject) {

        return credentialIssueService.buildVcOpenIdCredentialOfferUrl(principal.getWallet(), credentialConfigurationId, credentialsSubject);
    }

}
