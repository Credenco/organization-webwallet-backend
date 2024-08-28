package com.credenco.webwallet.backend.rest.webmanage.dto.issuer;

import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerDisplay;
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
public class CredentialIssuerDisplayDto {

    private Long id;
    private String displayName;
    private String logoUrl;
    private String logoAltText;
    private String locale;

    public static List<CredentialIssuerDisplayDto> from(List<CredentialIssuerDisplay> credentialIssuerDisplays) {
        if (credentialIssuerDisplays == null) {
            return List.of();
        }
        return credentialIssuerDisplays.stream()
                .map(CredentialIssuerDisplayDto::from)
                .toList();
    }

    public static CredentialIssuerDisplayDto from(CredentialIssuerDisplay credentialIssuerDisplay) {
        return CredentialIssuerDisplayDto.builder()
                .id(credentialIssuerDisplay.getId())
                .displayName(credentialIssuerDisplay.getDisplayName())
                .logoUrl(credentialIssuerDisplay.getLogoUrl())
                .logoAltText(credentialIssuerDisplay.getLogoAltText())
                .locale(credentialIssuerDisplay.getLocale())
                .build();
    }
}
