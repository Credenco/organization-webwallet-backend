package com.credenco.webwallet.backend.rest.webmanage.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PresentationOfferDto {
    final String verifierHost;
    final String presentationRequest;
    final JsonNode presentationDefinition;
    final List<CredentialDto> matchedCredentials;
}
