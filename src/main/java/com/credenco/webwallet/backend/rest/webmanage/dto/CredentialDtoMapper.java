package com.credenco.webwallet.backend.rest.webmanage.dto;

import com.credenco.webwallet.backend.domain.Credential;
import com.credenco.webwallet.backend.domain.IssuedCredential;
import com.credenco.webwallet.backend.service.CredentialParserService;
import com.credenco.webwallet.backend.service.credential.CredentialService;
import com.credenco.webwallet.backend.service.credential.dto.CredentialDisplayDto;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CredentialDtoMapper {

    private final CredentialParserService credentialParserService;

    @SneakyThrows
    public CredentialDto from(Credential credential, CredentialDisplayDto credentialDisplayDto) {
        final var vcDocument = credentialParserService.extractCredential(credential.getDocument(), credential.getCredentialFormat()).get("vc");
        return CredentialDto.builder()
                .id(credential.getId())
                .validFrom(credential.getValidFrom())
                .validUntil(credential.getValidUntil())
                .issuanceDate(credential.getIssuanceDate())
                .displayProperties(credentialDisplayDto)
                .notes(credential.getNotes())
                .credentialSubject(credentialParserService.extractCredentialSubject(credential))
                .document(vcDocument)
                .termsOfUse(mapTermsOfUse(vcDocument.get("termsOfUse")))
                .status(credential.getStatus().name())
                .build();
    }

    @SneakyThrows
    public CredentialDto from(IssuedCredential credential, CredentialDisplayDto credentialDisplayDto) {
        final var vcDocument = credentialParserService.extractCredential(credential.getDocument(), credential.getCredentialFormat()).get("vc");
        return CredentialDto.builder()
                .id(credential.getId())
                .validFrom(credential.getValidFrom())
                .validUntil(credential.getValidUntil())
                .issuanceDate(credential.getIssuanceDate())
                .displayProperties(credentialDisplayDto)
                .credentialSubject(credentialParserService.extractCredentialSubject(credential.getDocument(), credential.getCredentialFormat()))
                .document(vcDocument)
                .termsOfUse(mapTermsOfUse(vcDocument.get("termsOfUse")))
                .build();
    }

    private List<TermsOfUseDto> mapTermsOfUse(JsonNode termsOfUse) {
        if (termsOfUse == null) {
            return List.of();
        }
        List<TermsOfUseDto> results = new ArrayList<>();
        if (termsOfUse.isArray()) {
            Iterator<JsonNode> termsOfUseElements = termsOfUse.elements();
            while (termsOfUseElements.hasNext()) {
                JsonNode termsOfUseElement = termsOfUseElements.next();
                results.add(mapTermsOfUseElement(termsOfUseElement));
            }
        } else {
            results.add(mapTermsOfUseElement(termsOfUse));
        }
        return results;
    }

    private TermsOfUseDto mapTermsOfUseElement(JsonNode termsOfUseElement) {
        return TermsOfUseDto.builder()
                .id(termsOfUseElement.get("id").asText())
                .type(termsOfUseElement.get("type").asText())
                .build();
    }

    private String getIssuerLogoAltText(final CredentialDisplayDto credentialDisplayDto) {
        if (credentialDisplayDto.getIssuerDisplay() == null) {
            return null;
        }
        if (credentialDisplayDto.getIssuerDisplay().getLogo() == null) {
            return null;
        }
        return credentialDisplayDto.getIssuerDisplay().getLogo().getAltText();
    }

    private String getIssuerLogoUrl(final CredentialDisplayDto credentialDisplayDto) {
        if (credentialDisplayDto.getIssuerDisplay() == null) {
            return null;
        }
        if (credentialDisplayDto.getIssuerDisplay().getLogo() == null) {
            return null;
        }
        return credentialDisplayDto.getIssuerDisplay().getLogo().getUrl();
    }

    private String getIssuerName(final CredentialDisplayDto credentialDisplayDto) {
        if (credentialDisplayDto.getIssuerDisplay() == null) {
            return null;
        }
        return credentialDisplayDto.getIssuerDisplay().getName();
    }

}
