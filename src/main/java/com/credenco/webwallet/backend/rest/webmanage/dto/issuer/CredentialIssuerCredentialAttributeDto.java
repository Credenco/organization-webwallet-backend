package com.credenco.webwallet.backend.rest.webmanage.dto.issuer;

import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerCredentialAttribute;
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
public class CredentialIssuerCredentialAttributeDto {

    private Long id;
    private List<CredentialIssuerCredentialAttributeDisplayDto> displays = new ArrayList<>();
    private String attributeName;
    private int attributeOrder;
    private boolean mandatory;

    public static List<CredentialIssuerCredentialAttributeDto> from(List<CredentialIssuerCredentialAttribute> credentialIssuerCredentialAttributes) {
        if (credentialIssuerCredentialAttributes == null) {
            return List.of();
        }
        return credentialIssuerCredentialAttributes.stream()
                .map(CredentialIssuerCredentialAttributeDto::from)
                .toList();
    }

    public static CredentialIssuerCredentialAttributeDto from(CredentialIssuerCredentialAttribute credentialIssuerCredentialAttribute) {
        return CredentialIssuerCredentialAttributeDto.builder()
                .id(credentialIssuerCredentialAttribute.getId())
                .displays(CredentialIssuerCredentialAttributeDisplayDto.from(credentialIssuerCredentialAttribute.getDisplays()))
                .attributeName(credentialIssuerCredentialAttribute.getAttributeName())
                .attributeOrder(credentialIssuerCredentialAttribute.getAttributeOrder())
                .mandatory(credentialIssuerCredentialAttribute.isMandatory())
                .build();
    }
}
