package com.credenco.webwallet.backend.service.credential.dto;

import com.credenco.webwallet.backend.domain.PresentationDefinition;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class PresentationDefinitionDto {

    private Long id;
    private String externalKey;
    private String name;
    private String description;
    private String purpose;
    private String notes;
    private List<PresentationDefinitionPolicyDto> policies;
    private List<CredentialTypeDto> credentialTypes;
    private String successRedirectUrl;
    private String errorRedirectUrl;
    private String clientUrl;
    private JsonNode pdDocument;
}
