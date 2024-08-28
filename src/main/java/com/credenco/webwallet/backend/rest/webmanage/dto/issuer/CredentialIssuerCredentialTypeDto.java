package com.credenco.webwallet.backend.rest.webmanage.dto.issuer;

import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerCredentialType;
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
public class CredentialIssuerCredentialTypeDto {

    private Long id;
    private String credentialType;
    private int typeOrder;

    public static List<CredentialIssuerCredentialTypeDto> from(List<CredentialIssuerCredentialType> credentialIssuerCredentialTypes) {
        if (credentialIssuerCredentialTypes == null) {
            return List.of();
        }
        return credentialIssuerCredentialTypes.stream()
                .map(CredentialIssuerCredentialTypeDto::from)
                .toList();
    }

    public static CredentialIssuerCredentialTypeDto from(CredentialIssuerCredentialType credentialIssuerCredentialType) {
        return CredentialIssuerCredentialTypeDto.builder()
                .id(credentialIssuerCredentialType.getId())
                .credentialType(credentialIssuerCredentialType.getCredentialType())
                .typeOrder(credentialIssuerCredentialType.getTypeOrder())
                .build();
    }
}
