package com.credenco.webwallet.backend.rest.webmanage.dto.issuer;

import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerCredentialDisplay;
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
public class CredentialIssuerCredentialDisplayDto {

    private Long id;
    private String displayName;
    private String backgroundColor;
    private String textColor;
    private String backgroundImageUrl;
    private String backgroundAltText;
    private String locale;

    public static List<CredentialIssuerCredentialDisplayDto> from(List<CredentialIssuerCredentialDisplay> credentialIssuerCredentialDisplays) {
        if (credentialIssuerCredentialDisplays == null) {
            return List.of();
        }
        return credentialIssuerCredentialDisplays.stream()
                .map(CredentialIssuerCredentialDisplayDto::from)
                .toList();
    }

    public static CredentialIssuerCredentialDisplayDto from(CredentialIssuerCredentialDisplay credentialIssuerCredentialDisplay) {
        return CredentialIssuerCredentialDisplayDto.builder()
                .id(credentialIssuerCredentialDisplay.getId())
                .displayName(credentialIssuerCredentialDisplay.getDisplayName())
                .backgroundColor(credentialIssuerCredentialDisplay.getBackgroundColor())
                .textColor(credentialIssuerCredentialDisplay.getTextColor())
                .backgroundImageUrl(credentialIssuerCredentialDisplay.getBackgroundImageUrl())
                .backgroundAltText(credentialIssuerCredentialDisplay.getBackgroundAltText())
                .locale(credentialIssuerCredentialDisplay.getLocale())
                .build();
    }
}
