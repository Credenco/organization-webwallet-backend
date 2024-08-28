package com.credenco.webwallet.backend.rest.webmanage;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.domain.IssuedCredential;
import com.credenco.webwallet.backend.repository.IssuedCredentialRepository;
import com.credenco.webwallet.backend.rest.webmanage.dto.CredentialDto;
import com.credenco.webwallet.backend.rest.webmanage.dto.CredentialDtoMapper;
import com.credenco.webwallet.backend.service.credential.CredentialDisplayService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/issuedCredential")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class IssuedCredentialController {

    private final CredentialDisplayService credentialDisplayService;
    private final CredentialDtoMapper credentialDtoMapper;
    private final IssuedCredentialRepository issuedCredentialRepository;

    @GetMapping
    public PagedModel findCredentials(LocalPrincipal principal, @RequestParam(value = "q", required = false, defaultValue = "") String searchText, @RequestParam(value = "locale", required = false, defaultValue = "nl-NL") String locale,
                                      Pageable pageable) {
        var allSpecification = new ArrayList<Specification<IssuedCredential>>();
        allSpecification.add(issuedCredentialRepository.hasWallet(principal.getWallet()));
        if (!searchText.isEmpty()) {
            allSpecification.addAll(
                    Arrays.stream(searchText.split(" "))
                            .map(text -> issuedCredentialRepository.containsSearchText(text))
                            .toList()
            );
        }
        final Specification<IssuedCredential> specification = issuedCredentialRepository.allOf(allSpecification);
        final Page<IssuedCredential> items = issuedCredentialRepository.findAll(specification, pageable);

        return new PagedModel(items.map(credential -> credentialDtoMapper.from(credential, credentialDisplayService.getCredentialDisplayDto(credential.getIssuer().getConfigurationContent(),
                                                                                                                                            credential.getCredentialIssuerCredentialDefinition().getCredentialConfigurationId(), locale))));
    }


    @GetMapping("/{issuedCredentialId}")
    public CredentialDto getIssuedCredential(LocalPrincipal principal, @RequestParam(value = "locale", required = false, defaultValue = "nl-NL") String locale, @PathVariable Long issuedCredentialId) {
        final Optional<IssuedCredential> credential = issuedCredentialRepository.findByWalletAndId(principal.getWallet(), issuedCredentialId);
        if (credential.isEmpty()) {
            throw new IllegalArgumentException("IssuedCredential not found");
        }
        return credentialDtoMapper.from(credential.get(),
                                        credentialDisplayService.getCredentialDisplayDto(credential.get().getIssuer().getConfigurationContent(), credential.get().getCredentialIssuerCredentialDefinition().getCredentialConfigurationId(), locale)
        );
    }


}
