package com.credenco.webwallet.backend.rest.webmanage.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ServiceDto {
    private String serviceId;
    private String serviceType;
    private String serviceEndpoint;
}
