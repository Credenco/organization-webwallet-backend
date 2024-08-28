package com.credenco.webwallet.backend.rest.webmanage.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class CredentialTypeForm {

    private String credentialConfigurationId;
    private String credentialTypeConfigurationUrl;
    private String vcType;

}
