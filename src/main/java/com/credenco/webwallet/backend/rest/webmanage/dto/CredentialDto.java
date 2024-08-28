package com.credenco.webwallet.backend.rest.webmanage.dto;

import com.credenco.webwallet.backend.service.credential.dto.CredentialDisplayDto;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CredentialDto {
    Long id;
    LocalDateTime validFrom;
    LocalDateTime validUntil;
    LocalDateTime issuanceDate;
    CredentialDisplayDto displayProperties;
    String notes;
    JsonNode credentialSubject;
    JsonNode document;
    List<TermsOfUseDto> termsOfUse;
    String status;
}
