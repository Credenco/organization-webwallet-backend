package com.credenco.webwallet.backend.rest.webmanage.form;

import com.credenco.webwallet.backend.domain.PresentationDefinitionPolicyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class PresentationDefinitionPolicyForm {

    private Long id;
    private PresentationDefinitionPolicyType presentationDefinitionPolicyType;
    private String name;
    private String args;


}
