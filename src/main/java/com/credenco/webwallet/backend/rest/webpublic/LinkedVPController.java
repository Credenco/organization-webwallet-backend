package com.credenco.webwallet.backend.rest.webpublic;

import com.credenco.webwallet.backend.repository.LinkedPresentationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/linkedvp")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("permitAll()")
public class LinkedVPController {
    private final LinkedPresentationRepository linkedPresentationRepository;

    @GetMapping(path="/{walletExternalKey}/{externalKey}")
    public String getLinkedVerifiablePresentation(@PathVariable String walletExternalKey, @PathVariable String externalKey) {
        return linkedPresentationRepository.findByWalletExternalKeyAndExternalKey(walletExternalKey, externalKey).orElseThrow().getDocument();
    }
}
