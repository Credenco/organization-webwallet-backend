package com.credenco.webwallet.backend.service.credential.dto;

import com.credenco.webwallet.backend.domain.Did;
import com.credenco.webwallet.backend.domain.PresentationDefinition;
import com.credenco.webwallet.backend.rest.webmanage.dto.DidDto;
import com.credenco.webwallet.backend.service.credential.dto.PresentationDefinitionDto;
import com.credenco.webwallet.backend.service.credential.dto.PresentationDefinitionPolicyDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PresentationDefinitionDtoMapper {

    private final ObjectMapper objectMapper;

    public PresentationDefinitionDto from(PresentationDefinition presentationDefinition, List<CredentialTypeDto> credentialTypes, final String locale) {
        return PresentationDefinitionDto.builder()
                .id(presentationDefinition.getId())
                .externalKey(presentationDefinition.getExternalKey())
                .name(presentationDefinition.getName())
                .description(presentationDefinition.getDescription())
                .purpose(presentationDefinition.getPurpose())
                .notes(presentationDefinition.getNotes())
                .policies(presentationDefinition.getPolicies().stream()
                                  .map(PresentationDefinitionPolicyDto::from)
                                  .toList())
                .credentialTypes(credentialTypes)
                .successRedirectUrl(presentationDefinition.getSuccessRedirectUrl())
                .errorRedirectUrl(presentationDefinition.getErrorRedirectUrl())
                .clientUrl(presentationDefinition.getClientUrl())
                .pdDocument(objectMapper.valueToTree(presentationDefinition.generatePresentationDefinition()))
                .build();
    }
}
