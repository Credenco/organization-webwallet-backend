package com.credenco.webwallet.backend.service.verify.apiclient;

import com.credenco.webwallet.backend.domain.PresentationDefinition;
import com.credenco.webwallet.backend.service.credential.dto.PresentationDefinitionDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class VerifierApiClientImpl implements VerifierApiClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public VerifierApiClientImpl(@Qualifier("verifierApi") final RestTemplate restTemplate,
                                 final ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public String startPresentationExchange(PresentationDefinitionApiDto presentationDefinition, URI walletAddress) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        if (walletAddress != null) {
            headers.add("authorizeBaseUrl", walletAddress.toString());
        } else {
            headers.add("authorizeBaseUrl", "openid://");
        }
        headers.add("responseMode", "direct_post");
        headers.add("successRedirectUri", presentationDefinition.getSuccessRedirectUrl());
        //headers.add("openId4VPProfile", "EBSIV3");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("vp_policies", presentationDefinition.getVpPolicies());
        requestBody.put("vc_policies", presentationDefinition.getVcPolicies());
        requestBody.put("request_credentials", presentationDefinition.getCredentialTypes());
        requestBody.put("presentation_definition", presentationDefinition.getPresentationDefinition());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        log.info("{}", objectMapper.writeValueAsString(requestBody));

        ResponseEntity<String> response = restTemplate.exchange("/openid4vc/verify", HttpMethod.POST, request, String.class);

        return replaceClientUrls(response.getBody(), presentationDefinition.getClientUrl());
    }

    private String replaceClientUrls(String body, String clientUrl) {
        final var encodedOldClientUrl = URLEncoder.encode("http://localhost:3000");
        final var encodedNewClientUrl = URLEncoder.encode(clientUrl);

        return body.replaceAll(encodedOldClientUrl, encodedNewClientUrl);
    }

    @Override
    public JsonNode getSessionInfo(String sessionId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            String sessionInfo = restTemplate.getForObject("/openid4vc/session/" + sessionId, String.class);

            return objectMapper.readTree(sessionInfo);
        } catch (Exception e) {
            log.error("Error retrieving status.", e);
            throw new RuntimeException("Error retrieving status.");
        }
    }
}
