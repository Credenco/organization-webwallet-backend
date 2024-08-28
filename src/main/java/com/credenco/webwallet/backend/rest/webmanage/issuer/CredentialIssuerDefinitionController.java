package com.credenco.webwallet.backend.rest.webmanage.issuer;

import com.credenco.webwallet.backend.configuration.security.LocalPrincipal;
import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerDefinition;
import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerDisplay;
import com.credenco.webwallet.backend.repository.CredentialIssuerDefinitionRepository;
import com.credenco.webwallet.backend.rest.FormToJpaCollectionMerger;
import com.credenco.webwallet.backend.rest.webmanage.CredentialController;
import com.credenco.webwallet.backend.rest.webmanage.dto.issuer.CredentialIssuerDefinitionDto;
import com.credenco.webwallet.backend.rest.webmanage.form.issuer.CredentialIssuerDefinitionForm;
import com.credenco.webwallet.backend.rest.webmanage.form.issuer.CredentialIssuerDisplayForm;
import com.credenco.webwallet.backend.rest.webpublic.OidcServerController;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController()
@RequestMapping("/api/credentialIssuerDefinition")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CredentialIssuerDefinitionController {

    private final CredentialIssuerDefinitionRepository credentialIssuerDefinitionRepository;
    final FormToJpaCollectionMerger<CredentialIssuerDisplayForm, CredentialIssuerDisplay> displayMerger = new FormToJpaCollectionMerger<>();

    @GetMapping
    public PagedModel<CredentialIssuerDefinitionDto> find(LocalPrincipal principal, @RequestParam(value = "q", required = false, defaultValue = "") String searchText, Pageable pageable) {
        var items = credentialIssuerDefinitionRepository.findByWalletAndNameLike(principal.getWallet(), "%" + searchText + "%", pageable);
        return new PagedModel(items.map(CredentialIssuerDefinitionDto::from));
    }

    @GetMapping("/{credentialIssuerDefinitionId}")
    public CredentialIssuerDefinitionDto get(LocalPrincipal principal, @PathVariable Long credentialIssuerDefinitionId) {
        var item = credentialIssuerDefinitionRepository.findByWalletAndId(principal.getWallet(), credentialIssuerDefinitionId).orElseThrow(() -> new IllegalArgumentException("Credential issuer definition not found"));

        return CredentialIssuerDefinitionDto.from(item);
    }

    @PostMapping("/{credentialIssuerDefinitionId}")
    public CredentialIssuerDefinitionDto update(LocalPrincipal principal, @PathVariable Long credentialIssuerDefinitionId, @RequestBody CredentialIssuerDefinitionForm credentialIssuerDefinitionForm) {
        log.info("QQQ credentialIssuerDefinitionForm: " + credentialIssuerDefinitionForm);
        var item = credentialIssuerDefinitionRepository.findByWalletAndId(principal.getWallet(), credentialIssuerDefinitionId).orElseThrow(() -> new IllegalArgumentException("Credential issuer definition not found"));
        final CredentialIssuerDefinition savedCredentialIssuerDefinition = updateCredentialIssuerDefinition(item, credentialIssuerDefinitionForm);
        return CredentialIssuerDefinitionDto.from(savedCredentialIssuerDefinition);
    }

    @PostMapping()
    public CredentialIssuerDefinitionDto add(LocalPrincipal principal, @RequestBody CredentialIssuerDefinitionForm credentialIssuerDefinitionForm) {
        log.info("QQQ credentialIssuerDefinitionForm: " + credentialIssuerDefinitionForm);
        final CredentialIssuerDefinition savedCredentialIssuerDefinition = updateCredentialIssuerDefinition(new CredentialIssuerDefinition(), credentialIssuerDefinitionForm);
        return CredentialIssuerDefinitionDto.from(savedCredentialIssuerDefinition);
    }

    @SneakyThrows
    private CredentialIssuerDefinition updateCredentialIssuerDefinition(CredentialIssuerDefinition credentialIssuerDefinition, CredentialIssuerDefinitionForm credentialIssuerDefinitionForm) {
        credentialIssuerDefinition.setName(credentialIssuerDefinitionForm.getName());
        credentialIssuerDefinition.setDescription(credentialIssuerDefinitionForm.getDescription());
        credentialIssuerDefinition.setExternalKey(credentialIssuerDefinitionForm.getExternalKey());
        Link openIdCredentialIssuerUrl = linkTo(WebMvcLinkBuilder.methodOn(OidcServerController.class)
                                        .getIssuerMetadata(credentialIssuerDefinition.getWallet().getExternalKey(), credentialIssuerDefinition.getExternalKey())).withSelfRel();
        //final String path = new URL(openIdCredentialIssuerUrl.getHref()).getPath();
        //credentialIssuerDefinition.setIssuerUrl(openIdCredentialIssuerUrl.getHref().replace(path, ""));
        // Temp hack. Should be:
        credentialIssuerDefinition.setIssuerUrl(openIdCredentialIssuerUrl.getHref().replace("/.well-known/openid-credential-issuer", ""));
        displayMerger.buildJpaCollection(
                credentialIssuerDefinitionForm.getDisplays(),
                credentialIssuerDefinition.getDisplays(),
                (credentialIssuerDisplayForm, credentialIssuerDisplay) -> equals(credentialIssuerDisplayForm.getId(), credentialIssuerDisplay.getId()),
                (form, entity) -> {
                    entity.setDisplayName(form.getDisplayName());
                    entity.setLogoUrl(form.getLogoUrl());
                    entity.setLogoAltText(form.getLogoAltText());
                    entity.setLocale(form.getLocale());
                    return entity;
                },
                (displayForm) -> {
                    var display = new CredentialIssuerDisplay();
                    display.setCredentialIssuerDefinition(credentialIssuerDefinition);
                    display.setDisplayName(displayForm.getDisplayName());
                    display.setLogoUrl(displayForm.getLogoUrl());
                    display.setLogoAltText(displayForm.getLogoAltText());
                    display.setLocale(displayForm.getLocale());
                    return display;
                });
        credentialIssuerDefinitionRepository.save(credentialIssuerDefinition);
        return credentialIssuerDefinition;
    }

    private Boolean equals(final Object item1, final Object item2) {
        if ((item1 == null) || (item2 == null)){
            return false;
        }
        return item1.equals(item2);
    }

}
