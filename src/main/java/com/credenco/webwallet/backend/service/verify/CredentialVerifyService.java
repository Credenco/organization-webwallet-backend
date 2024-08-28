package com.credenco.webwallet.backend.service.verify;

import com.credenco.webwallet.backend.domain.PresentationDefinition;
import com.credenco.webwallet.backend.domain.ReceivedPresentation;
import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.repository.PresentationDefinitionRepository;
import com.credenco.webwallet.backend.repository.ReceivedPresentationRepository;
import com.credenco.webwallet.backend.service.verify.apiclient.PresentationDefinitionApiDto;
import com.credenco.webwallet.backend.service.verify.apiclient.VerifierApiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialVerifyService {

    private final VerifierApiClient verifierApiClient;
    private final ObjectMapper objectMapper;
    private final ReceivedPresentationRepository receivedPresentationRepository;
    private final PresentationDefinitionRepository presentationDefinitionRepository;

    @SneakyThrows
    public String buildVcOpenIdPresentationOfferUrl(final Wallet wallet, final String presentationDefinitionExternalKey, Map<String, String> parameters) {
        // For now presentation definition is static
        final String walletUrl = parameters.get("walletUrl");

        final PresentationDefinition presentationDefinition = presentationDefinitionRepository.findByWalletAndExternalKey(wallet, presentationDefinitionExternalKey)
                .orElseThrow(() -> new RuntimeException("Presentation definition " + presentationDefinitionExternalKey + " not found for wallet " + wallet.getId()));
        final PresentationDefinitionApiDto presentationDefinitionDto = PresentationDefinitionApiDto.builder()
                .purpose(presentationDefinition.getPurpose())
                .vpPolicies(List.of("signature", "expired"))
                .vcPolicies(List.of("signature", "expired"))
                .credentialTypes(presentationDefinition.getCredentialTypes().stream().map(t -> t.getVcType()).toList())
                .presentationDefinition(presentationDefinition.generatePresentationDefinition())
                .successRedirectUrl(parameters.getOrDefault("successUrl", presentationDefinition.getSuccessRedirectUrl()))
                .clientUrl(presentationDefinition.getClientUrl())
                .build();
        return verifierApiClient.startPresentationExchange(presentationDefinitionDto, walletUrl  != null ? new URI(walletUrl): null);
//        final PresentationDefinition presentationDefinition = getPresentationDefinition(presentationConfigurationId);
//        return verifierApiClient.startPresentationExchange(presentationDefinition, walletUrl  != null ? new URI(walletUrl): null);
    }

    @SneakyThrows
    public Map<String, PresentedCredential> getStatus(Wallet wallet, String sessionId, final boolean savePresentation) {
        final var presentationSessionInfo = verifierApiClient.getSessionInfo(sessionId);
        if (presentationSessionInfo.get("verificationResult") == null) {
            return null;
        }
        if (savePresentation) {
        final var now = LocalDateTime.now();
        final ReceivedPresentation receivedPresentation = ReceivedPresentation.builder()
                .wallet(wallet)
                .document(objectMapper.writeValueAsString(presentationSessionInfo))
                .receiveDate(now)
                .createdAt(now)
                .createdBy("System")
                .lastModifiedAt(now)
                .lastModifiedBy("System")
                .build();

        receivedPresentationRepository.save(receivedPresentation);
        }
        if (presentationSessionInfo.get("verificationResult").booleanValue()) {
            return retrieveCredentialsFromSubmission(presentationSessionInfo);
        }

        return null;
    }

    private Map<String, PresentedCredential> retrieveCredentialsFromSubmission(JsonNode presentationSessionInfo) throws JsonProcessingException {
        final JsonNode tokenResponse = presentationSessionInfo.get("tokenResponse");
        final String vpToken = tokenResponse.get("vp_token").asText();
        final String[] tokenParts = vpToken.split("\\.");
        final JsonNode presentation = objectMapper.readTree(new String(Base64.decodeBase64(tokenParts[1]))).get("vp");
        final Map<String, PresentedCredential> result = new HashMap<>();

        for (JsonNode descriptor: tokenResponse.get("presentation_submission").get("descriptor_map")) {
            String id = descriptor.get("id").asText();
            Object vc = JsonPath.parse(objectMapper.writeValueAsString(presentation)).read(descriptor.get("path").asText());
            JsonNode nestedPath = descriptor.get("path_nested");
            while (nestedPath != null) {
                id = nestedPath.get("id").asText();
                final var path = nestedPath.get("path").asText();
                vc = JsonPath.parse(vc).read(path.startsWith("$.vp.") ? path.substring(0, 2) + path.substring(4): path);
                nestedPath = nestedPath.get("path_nested");
            }

            String token = vc instanceof String ? (String)vc: objectMapper.readTree(JsonPath.parse(vc).jsonString()).get(0).asText();

            result.put(id, PresentedCredential.builder()
                    .token(token)
                    .credential(objectMapper.readTree(new String(Base64.decodeBase64(token.split("\\.")[1]))))
                    .build());
        }

        return result;
    }

    @Builder
    @Getter
    public static class PresentedCredential {
        private String token;
        private JsonNode credential;
    }
}
