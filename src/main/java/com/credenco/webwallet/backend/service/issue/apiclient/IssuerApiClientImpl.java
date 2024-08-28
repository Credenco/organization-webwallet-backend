package com.credenco.webwallet.backend.service.issue.apiclient;

import com.credenco.webwallet.backend.domain.Did;
import com.credenco.webwallet.backend.domain.WalletKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class IssuerApiClientImpl implements IssuerApiClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public IssuerApiClientImpl(@Qualifier("issuerApi") final RestTemplate restTemplate,
                                 final ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public String openid4vcJwtIssue(String template,
                                    String credentialConfigurationId,
                                    Map<String, Object> credentialsSubject,
                                    Did issuerDid,
                                    String credentialIssuer) {
        try {
            Map<String, Object> requestBody = new LinkedHashMap<>();
            requestBody.put("issuerKey", getIssuanceKey(issuerDid.getWalletKey()));
            requestBody.put("issuerDid", issuerDid.getDid());
            requestBody.put("credentialData", objectMapper.readTree(template));
            if (credentialIssuer != null) {
                requestBody.put("credentialIssuer", credentialIssuer);
            }
            requestBody.put("credentialConfigurationId", credentialConfigurationId);
            requestBody.put("mapping", getCredentialData(credentialsSubject));

            HttpEntity<Map> request = new HttpEntity<>(requestBody);
            log.info("{}", objectMapper.writeValueAsString(requestBody));

            ResponseEntity<String> response = restTemplate.exchange("/openid4vc/jwt/issue", HttpMethod.POST, request, String.class);

            return response.getBody();
        } catch (Exception e) {
            log.error("Error issuing VC.", e);
            throw new RuntimeException("Error issuing VC.");
        }
    }


    @SneakyThrows
    private Object getIssuanceKey(final @NotNull WalletKey walletKey) {
        return objectMapper.readValue(walletKey.getDocument(), Map.class);
    }

    private Map<String, Object> getCredentialData(final Map<String, Object> credentialsSubject) {
        credentialsSubject.putIfAbsent("id", "<subjectDid>");

        final String now = Instant.now().toString();

        Map<String, Object> credentialData = new HashMap<>();
        credentialData.put("id", "<uuid>");
        credentialData.put("issuer", "<issuerDid>");
        credentialData.put("credentialSubject", credentialsSubject);
        credentialData.put("issuanceDate", now);
        credentialData.put("issued", now);
        credentialData.put("validFrom", now);

        credentialsSubject.putIfAbsent("id", "<subjectDid>");



        return credentialData;
    }

}
