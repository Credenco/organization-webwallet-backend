package com.credenco.webwallet.backend.rest.webmanage.dto;

import com.credenco.webwallet.backend.domain.Did;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DidDtoMapper {

    private final ObjectMapper objectMapper;

    public DidDto from(Did did) {
        JsonNode document = getJsonDocument(did);
        return DidDto.builder()
                .id(did.getId())
                .did(did.getDid())
                .type(did.getType())
                .displayName(did.getDisplayName())
                .createdAt(did.getCreatedAt())
                .createdBy(did.getCreatedBy())
                .lastModifiedAt(did.getLastModifiedAt())
                .lastModifiedBy(did.getLastModifiedBy())
                .document(document)
                .services(mapServices(document.get("service")))
                .build();
    }

    private List<ServiceDto> mapServices(JsonNode serviceNode) {
        if (serviceNode == null) {
            return List.of();
        }
        List<ServiceDto> services = new ArrayList<>();
        Iterator<JsonNode> serviceElements = serviceNode.elements();
        while (serviceElements.hasNext()) {
            JsonNode service = serviceElements.next();
            services.add(ServiceDto.builder()
                                 .serviceId(service.get("id").asText())
                                 .serviceType(service.get("type").asText())
                                 .serviceEndpoint(service.get("serviceEndpoint").asText())
                                 .build());
        }
        return services;
    }

    public List<DidDto> from(List<Did> dids) {
        return dids.stream()
                .map(this::from)
                .toList();
    }

    @SneakyThrows
    private JsonNode getJsonDocument(Did did) {
        return objectMapper.readTree(did.getDocument());
    }
}
