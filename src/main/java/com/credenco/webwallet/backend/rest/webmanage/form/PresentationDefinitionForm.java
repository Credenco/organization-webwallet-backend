package com.credenco.webwallet.backend.rest.webmanage.form;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class PresentationDefinitionForm {

    private Long id;
    private String externalKey;
    private String name;
    private String description;
    private String purpose;
    private String notes;
    private List<PresentationDefinitionPolicyForm> policies;
    private List<CredentialTypeForm> credentialTypes;
    private String successRedirectUrl;
    private String errorRedirectUrl;
    private String clientUrl;
}
