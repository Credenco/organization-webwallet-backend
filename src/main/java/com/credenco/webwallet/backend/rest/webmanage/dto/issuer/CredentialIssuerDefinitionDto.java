package com.credenco.webwallet.backend.rest.webmanage.dto.issuer;

import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerCredentialDefinition;
import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerDefinition;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
public class CredentialIssuerDefinitionDto {

    private Long id;
    private String externalKey;
    private String name;
    private String description;
    private String openIdCredentialIssuerUrl;
    private List<CredentialIssuerDisplayDto> displays = new ArrayList<>();
    private List<CredentialIssuerCredentialDefinitionDto> credentialDefinitions = new ArrayList<>();
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastModifiedAt;
    private String lastModifiedBy;

    public static List<CredentialIssuerDefinitionDto> from(List<CredentialIssuerDefinition> credentialIssuerDefinitions) {
        if (credentialIssuerDefinitions == null) {
            return List.of();
        }
        return credentialIssuerDefinitions.stream()
                .map(CredentialIssuerDefinitionDto::from)
                .toList();
    }

    public static CredentialIssuerDefinitionDto from(CredentialIssuerDefinition credentialIssuerDefinition) {
        return CredentialIssuerDefinitionDto.builder()
                .id(credentialIssuerDefinition.getId())
                .externalKey(credentialIssuerDefinition.getExternalKey())
                .name(credentialIssuerDefinition.getName())
                .description(credentialIssuerDefinition.getDescription())
                .openIdCredentialIssuerUrl(credentialIssuerDefinition.getIssuerUrl() + "/.well-known/openid-credential-issuer")
                .displays(CredentialIssuerDisplayDto.from(credentialIssuerDefinition.getDisplays()))
                .credentialDefinitions(CredentialIssuerCredentialDefinitionDto.from(credentialIssuerDefinition.getCredentialDefinitions()))
                .createdAt(credentialIssuerDefinition.getCreatedAt())
                .createdBy(credentialIssuerDefinition.getCreatedBy())
                .lastModifiedAt(credentialIssuerDefinition.getLastModifiedAt())
                .lastModifiedBy(credentialIssuerDefinition.getLastModifiedBy())
                .build();
    }
}
