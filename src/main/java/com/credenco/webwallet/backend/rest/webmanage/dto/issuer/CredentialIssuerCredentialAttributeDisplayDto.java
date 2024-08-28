package com.credenco.webwallet.backend.rest.webmanage.dto.issuer;

import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerCredentialAttributeDisplay;
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
public class CredentialIssuerCredentialAttributeDisplayDto {

    private Long id;
    private String displayName;
    private String locale;

    public static List<CredentialIssuerCredentialAttributeDisplayDto> from(List<CredentialIssuerCredentialAttributeDisplay> credentialIssuerCredentialAttributeDisplays) {
        if (credentialIssuerCredentialAttributeDisplays == null) {
            return List.of();
        }
        return credentialIssuerCredentialAttributeDisplays.stream()
                .map(CredentialIssuerCredentialAttributeDisplayDto::from)
                .toList();
    }

    public static CredentialIssuerCredentialAttributeDisplayDto from(CredentialIssuerCredentialAttributeDisplay credentialIssuerCredentialAttributeDisplay) {
        return CredentialIssuerCredentialAttributeDisplayDto.builder()
                .id(credentialIssuerCredentialAttributeDisplay.getId())
                .displayName(credentialIssuerCredentialAttributeDisplay.getDisplayName())
                .locale(credentialIssuerCredentialAttributeDisplay.getLocale())
                .build();
    }
}
