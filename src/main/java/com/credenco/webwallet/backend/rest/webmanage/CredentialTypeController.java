package com.credenco.webwallet.backend.rest.webmanage;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.service.credential.CredentialTypeService;
import com.credenco.webwallet.backend.service.credential.dto.CredentialTypeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/credential/type")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CredentialTypeController {

    private final CredentialTypeService credentialTypeService;

    @GetMapping
    public PagedModel<CredentialTypeDto> find(LocalPrincipal principal,
                                              @RequestParam(value = "q", required = false, defaultValue = "") String searchText,
                                              @RequestParam(value = "locale", required = false, defaultValue = "nl-NL") String locale,
                                              Pageable pageable) {
        return credentialTypeService.findAllCredentialTypes(principal.getWallet(), searchText, locale, pageable);
    }

}
