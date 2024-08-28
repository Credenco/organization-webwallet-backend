package com.credenco.webwallet.backend.service.credential.dto;

import com.credenco.webwallet.backend.domain.IssuerCredentialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CredentialTypeDto {

    private String credentialConfigurationId;
    private String credentialTypeConfigurationUrl;
    private DisplayProperties issuerDisplay;
    private DisplayProperties credentialTypeDisplay;
    private String issueUrl;
    private String vcType;


}
