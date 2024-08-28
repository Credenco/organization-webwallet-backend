package com.credenco.webwallet.backend.rest.webmanage.dto.issuer;

import com.credenco.webwallet.backend.domain.CredentialIssueTemplate;
import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerCredentialDefinition;
import java.util.ArrayList;
import java.util.List;

import com.credenco.webwallet.backend.rest.webmanage.dto.DidDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CredentialIssuerCredentialDefinitionDto {

    private Long id;
    private String credentialConfigurationId;
    private String format;
    private List<CredentialIssuerCredentialDisplayDto> displays = new ArrayList<>();
    private List<CredentialIssuerCredentialTypeDto> types = new ArrayList<>();
    private List<CredentialIssuerCredentialAttributeDto> attributes = new ArrayList<>();
    private String template;
    private DidDto issuerDid;

    public static List<CredentialIssuerCredentialDefinitionDto> from(final List<CredentialIssuerCredentialDefinition> credentialDefinitions) {
        if (credentialDefinitions == null) {
            return List.of();
        }
        return credentialDefinitions.stream()
                .map(CredentialIssuerCredentialDefinitionDto::from)
                .toList();
    }

    public static CredentialIssuerCredentialDefinitionDto from(final CredentialIssuerCredentialDefinition credentialDefinition) {
        return CredentialIssuerCredentialDefinitionDto.builder()
                .id(credentialDefinition.getId())
                .credentialConfigurationId(credentialDefinition.getCredentialConfigurationId())
                .format(credentialDefinition.getFormat())
                .displays(CredentialIssuerCredentialDisplayDto.from(credentialDefinition.getDisplays()))
                .types(CredentialIssuerCredentialTypeDto.from(credentialDefinition.getTypes()))
                .attributes(CredentialIssuerCredentialAttributeDto.from(credentialDefinition.getAttributes()))
                .template(credentialDefinition.getCredentialIssueTemplate() != null ? credentialDefinition.getCredentialIssueTemplate().getTemplate() : null)
                .issuerDid(getIssuerDid(credentialDefinition.getCredentialIssueTemplate()))
                .build();
    }

    private static DidDto getIssuerDid(CredentialIssueTemplate template) {
        if (template == null) {
            return null;
        }
        return DidDto.builder()
                .id(template.getIssuerDid().getId())
                .did(template.getIssuerDid().getDid())
                .type(template.getIssuerDid().getType())
                .environment(template.getIssuerDid().getEnvironment())
                .build();
    }
}
