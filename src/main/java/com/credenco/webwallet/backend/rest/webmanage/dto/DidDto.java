package com.credenco.webwallet.backend.rest.webmanage.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class DidDto {
    private Long id;
    private String did;
    private String type;
    private String displayName;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastModifiedAt;
    private String lastModifiedBy;
    private JsonNode document;
    private List<ServiceDto> services;
    private String environment;
}
