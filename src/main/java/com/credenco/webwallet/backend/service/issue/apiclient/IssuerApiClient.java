package com.credenco.webwallet.backend.service.issue.apiclient;

import com.credenco.webwallet.backend.domain.Did;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface IssuerApiClient {

    String openid4vcJwtIssue(String template, String credentialConfigurationId, Map<String, Object> credentialsSubject, Did issuerDid, String credentialIssuer);
}
