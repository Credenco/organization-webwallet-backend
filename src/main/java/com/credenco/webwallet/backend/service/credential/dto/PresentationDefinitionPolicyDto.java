package com.credenco.webwallet.backend.service.credential.dto;

import com.credenco.webwallet.backend.domain.PresentationDefinitionPolicy;
import com.credenco.webwallet.backend.domain.PresentationDefinitionPolicyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class PresentationDefinitionPolicyDto {

    private Long id;
    private PresentationDefinitionPolicyType presentationDefinitionPolicyType;
    private String name;
    private String args;

    public static PresentationDefinitionPolicyDto from(PresentationDefinitionPolicy presentationDefinitionPolicy) {
        return PresentationDefinitionPolicyDto.builder()
                .id(presentationDefinitionPolicy.getId())
                .presentationDefinitionPolicyType(presentationDefinitionPolicy.getPresentationDefinitionPolicyType())
                .name(presentationDefinitionPolicy.getName())
                .args(presentationDefinitionPolicy.getArgs())
                .build();
    }
}
