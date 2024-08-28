package com.credenco.webwallet.backend.rest.webmanage.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PresentationRequestDto {
    final String presentationRequest;
    final List<String> selectedCredentials;
}
