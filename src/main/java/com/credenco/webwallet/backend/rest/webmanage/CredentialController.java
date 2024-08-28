package com.credenco.webwallet.backend.rest.webmanage;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.domain.Credential;
import com.credenco.webwallet.backend.domain.HistoryAction;
import com.credenco.webwallet.backend.domain.HistoryEvent;
import com.credenco.webwallet.backend.repository.CredentialRepository;
import com.credenco.webwallet.backend.rest.webmanage.dto.CredentialDto;
import com.credenco.webwallet.backend.rest.webmanage.dto.CredentialDtoMapper;
import com.credenco.webwallet.backend.service.HistoryService;
import com.credenco.webwallet.backend.service.credential.CredentialDisplayService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.credenco.webwallet.backend.service.credential.CredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/credential")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CredentialController {

    private final CredentialDisplayService credentialDisplayService;
    private final CredentialDtoMapper credentialDtoMapper;
    private final CredentialRepository credentialRepository;
    private final HistoryService historyService;
    private final CredentialService credentialService;
//
//    @GetMapping
//    public List<CredentialDto> getCredentials(LocalPrincipal principal, @RequestParam(value = "locale", required = false, defaultValue = "nl-NL") String locale) {
//        final List<Credential> credentials = credentialRepository.findByWallet(principal.getWallet());
////        credentials.addAll(credentials);
////        credentials.addAll(credentials);
//        return credentials.stream()
//                .map(credential -> credentialDtoMapper.from(credential, credentialDisplayService.getCredentialDisplayDto(credential, locale)))
//                .toList();
//    }


    @GetMapping
    public PagedModel findCredentials(LocalPrincipal principal, @RequestParam(value = "q", required = false, defaultValue = "") String searchText, @RequestParam(value = "locale", required = false, defaultValue = "nl-NL") String locale,
                                      Pageable pageable) {
        var allSpecification = new ArrayList<Specification<Credential>>();
        allSpecification.add(credentialRepository.hasWallet(principal.getWallet()));
        if (!searchText.isEmpty()) {
            allSpecification.addAll(
                    Arrays.stream(searchText.split(" "))
                            .map(text -> credentialRepository.containsSearchText(text))
                            .toList()
            );
        }
        final Specification<Credential> specification = credentialRepository.allOf(allSpecification);
        final Page<Credential> items = credentialRepository.findAll(specification, pageable);

        return new PagedModel(items.map(credential -> credentialDtoMapper.from(credential, credentialDisplayService.getCredentialDisplayDto(credential, locale))));
    }


    @GetMapping("/{credentialId}")
    public CredentialDto getCredential(LocalPrincipal principal, @RequestParam(value = "locale", required = false, defaultValue = "nl-NL") String locale, @PathVariable Long credentialId) {
        final Optional<Credential> credential = credentialRepository.findByWalletAndId(principal.getWallet(), credentialId);
        if (credential.isEmpty()) {
            throw new IllegalArgumentException("Credential not found");
        }
        credentialService.verifyCredential(credential.get());
        return credentialDtoMapper.from(credential.get(), credentialDisplayService.getCredentialDisplayDto(credential.get(), locale));
    }

    @DeleteMapping("/{credentialId}")
    public void delete(LocalPrincipal principal, @PathVariable Long credentialId) {
        final Optional<Credential> credential = credentialRepository.findByWalletAndId(principal.getWallet(), credentialId);
        if (credential.isEmpty()) {
            throw new IllegalArgumentException("Credential not found");
        }
        credentialRepository.delete(credential.get());
        historyService.addHistory(principal,
                                  HistoryEvent.CREDENTIAL,
                                  HistoryAction.DELETED,
                                  null,
                                  null,
                                  List.of(credential.get()));
    }
}
