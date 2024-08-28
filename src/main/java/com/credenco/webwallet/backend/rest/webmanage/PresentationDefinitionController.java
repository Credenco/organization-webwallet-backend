package com.credenco.webwallet.backend.rest.webmanage;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.domain.PresentationDefinition;
import com.credenco.webwallet.backend.domain.PresentationDefinitionCredentialType;
import com.credenco.webwallet.backend.domain.PresentationDefinitionPolicy;
import com.credenco.webwallet.backend.domain.PresentationDefinitionPolicyType;
import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.repository.PresentationDefinitionRepository;
import com.credenco.webwallet.backend.rest.webmanage.form.CredentialTypeForm;
import com.credenco.webwallet.backend.rest.webmanage.form.PresentationDefinitionForm;
import com.credenco.webwallet.backend.service.credential.CredentialTypeService;
import com.credenco.webwallet.backend.service.credential.dto.CredentialTypeDto;
import com.credenco.webwallet.backend.service.credential.dto.PresentationDefinitionDto;
import java.time.LocalDateTime;
import java.util.List;

import com.credenco.webwallet.backend.service.credential.dto.PresentationDefinitionDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/presentationDefinition")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PresentationDefinitionController {

    private final PresentationDefinitionRepository presentationDefinitionRepository;
    private final CredentialTypeService credentialTypeService;
    private final PresentationDefinitionDtoMapper presentationDefinitionDtoMapper;

    @GetMapping
    public PagedModel<PresentationDefinitionDto> find(LocalPrincipal principal,
                                                      @RequestParam(value = "q", required = false, defaultValue = "") String searchText,
                                                      @RequestParam(value = "locale", required = false, defaultValue = "nl-NL") String locale,
                                                      Pageable pageable) {
        final Page<PresentationDefinition> items = presentationDefinitionRepository.searchAllByWallet(principal.getWallet(), searchText, pageable);
        return new PagedModel(items.map(presentationDefinition -> presentationDefinitionDtoMapper.from(presentationDefinition, getCredentialTypes(principal.getWallet(), presentationDefinition.getCredentialTypes(), locale), locale)));

    }

    @GetMapping(path = "/{presentationDefinitionId}")
    public PresentationDefinitionDto find(LocalPrincipal principal,
                                                      @PathVariable("presentationDefinitionId") Long presentationDefinitionId,
                                                      @RequestParam(value = "locale", required = false, defaultValue = "nl-NL") String locale,
                                                      Pageable pageable) {
        final PresentationDefinition presentationDefinition = presentationDefinitionRepository.findByWalletAndId(principal.getWallet(), presentationDefinitionId)
                .orElseThrow(() -> new IllegalArgumentException("Presentation definition not found"));
        return presentationDefinitionDtoMapper.from(presentationDefinition, getCredentialTypes(principal.getWallet(), presentationDefinition.getCredentialTypes(), locale), locale);

    }

    @PostMapping
    @Transactional
    public PresentationDefinitionDto addOrUpdate(LocalPrincipal principal, @RequestBody PresentationDefinitionForm presentationDefinitionForm, @RequestParam(value = "locale", required = false, defaultValue = "nl-NL") String locale) {
        final PresentationDefinition presentationDefinition = (presentationDefinitionForm.getId() != null) ?
                presentationDefinitionRepository.findByWalletAndId(principal.getWallet(), presentationDefinitionForm.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Presentation definition not found")) : new PresentationDefinition();
        presentationDefinition.setWallet(principal.getWallet());
        presentationDefinition.setName(presentationDefinitionForm.getName());
        presentationDefinition.setExternalKey(presentationDefinitionForm.getExternalKey());
        presentationDefinition.setDescription(presentationDefinitionForm.getDescription());
        presentationDefinition.setPurpose(presentationDefinitionForm.getPurpose());
        presentationDefinition.setNotes(presentationDefinitionForm.getNotes());
        setCredentialTypes(presentationDefinition, presentationDefinitionForm.getCredentialTypes());
        presentationDefinition.setSuccessRedirectUrl(presentationDefinitionForm.getSuccessRedirectUrl());
        presentationDefinition.setErrorRedirectUrl(presentationDefinitionForm.getErrorRedirectUrl());
        presentationDefinition.setClientUrl(presentationDefinitionForm.getClientUrl());
        presentationDefinition.setCreatedAt(presentationDefinition.getCreatedAt() == null ? LocalDateTime.now() : presentationDefinition.getCreatedAt());
        presentationDefinition.setCreatedBy(presentationDefinition.getCreatedBy() == null ? principal.getUserName() : presentationDefinition.getCreatedBy());
        presentationDefinition.setLastModifiedAt(presentationDefinition.getLastModifiedAt() == null ? LocalDateTime.now() : presentationDefinition.getLastModifiedAt());
        presentationDefinition.setLastModifiedBy(presentationDefinition.getLastModifiedBy() == null ? principal.getUserName() : presentationDefinition.getLastModifiedBy());

        presentationDefinition.addPolicy(PresentationDefinitionPolicy.builder()
                                                 .presentationDefinitionPolicyType(PresentationDefinitionPolicyType.VC)
                                                 .name("signature")
                                                 .build());
        presentationDefinition.addPolicy(PresentationDefinitionPolicy.builder()
                                                 .presentationDefinitionPolicyType(PresentationDefinitionPolicyType.VC)
                                                 .name("expired")
                                                 .build());
        presentationDefinition.addPolicy(PresentationDefinitionPolicy.builder()
                                                 .presentationDefinitionPolicyType(PresentationDefinitionPolicyType.VP)
                                                 .name("signature")
                                                 .build());
        presentationDefinition.addPolicy(PresentationDefinitionPolicy.builder()
                                                 .presentationDefinitionPolicyType(PresentationDefinitionPolicyType.VP)
                                                 .name("expired")
                                                 .build());

        final PresentationDefinition savedPresenationDefinition = presentationDefinitionRepository.save(presentationDefinition);

        return presentationDefinitionDtoMapper.from(savedPresenationDefinition, getCredentialTypes(principal.getWallet(), savedPresenationDefinition.getCredentialTypes(), locale), locale);
    }

    private void setCredentialTypes(final PresentationDefinition presentationDefinition, final List<CredentialTypeForm> formCredentialTypes) {
        final List<PresentationDefinitionCredentialType> currentCredentialTypes = presentationDefinition.getCredentialTypes();
        final List<PresentationDefinitionCredentialType> newCredentialTypes = formCredentialTypes.stream()
                .map(credentialTypeForm -> findExistingOrAdd(currentCredentialTypes, credentialTypeForm, presentationDefinition))
                .toList();
        currentCredentialTypes.clear();
        currentCredentialTypes.addAll(newCredentialTypes);
    }

    private PresentationDefinitionCredentialType findExistingOrAdd(final List<PresentationDefinitionCredentialType> currentPresenationDefintions, final CredentialTypeForm credentialTypeForm, final PresentationDefinition presentationDefinition) {
        return currentPresenationDefintions.stream()
                .filter(presentationDefinitionCredentialType -> (presentationDefinitionCredentialType.getCredentialTypeConfigurationUrl()
                                                                         .equals(credentialTypeForm.getCredentialTypeConfigurationUrl()) && presentationDefinitionCredentialType.getCredentialConfigurationId()
                                                                         .equals(credentialTypeForm.getCredentialConfigurationId())))
                .findFirst()
                .orElse(PresentationDefinitionCredentialType.builder()
                                .credentialConfigurationId(credentialTypeForm.getCredentialConfigurationId())
                                .credentialTypeConfigurationUrl(credentialTypeForm.getCredentialTypeConfigurationUrl())
                                .vcType(credentialTypeForm.getVcType())
                                .presentationDefinition(presentationDefinition)
                                .build()
                );
    }

    private List<CredentialTypeDto> getCredentialTypes(Wallet wallet, final List<PresentationDefinitionCredentialType> credentialTypes, final String locale) {
        return credentialTypeService.findAllCredentialTypes(wallet, credentialTypes, locale);
    }

}
