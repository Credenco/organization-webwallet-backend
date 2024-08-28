package com.credenco.webwallet.backend.service.verify.apiclient;

import com.credenco.webwallet.backend.domain.Did;
import com.credenco.webwallet.backend.domain.PresentationDefinition;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;
import java.net.URL;
import java.util.Map;

public interface VerifierApiClient {

    String startPresentationExchange(PresentationDefinitionApiDto presentationDefinition, URI walletAddress);

    JsonNode getSessionInfo(String sessionId);
}
