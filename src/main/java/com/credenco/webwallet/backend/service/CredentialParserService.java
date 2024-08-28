package com.credenco.webwallet.backend.service;

import com.credenco.webwallet.backend.domain.Credential;
import com.credenco.webwallet.backend.domain.CredentialFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class CredentialParserService {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public JsonNode extractCredentialSubject(final Credential credential) {
        if (credential.getDocument() == null) {
            return null;
        }
        return extractCredentialSubject(credential.getDocument(), credential.getCredentialFormat());
    }

    @SneakyThrows
    public JsonNode extractCredentialSubject(final String document, final CredentialFormat credentialFormat) {
        final JsonNode credential = extractCredential(document, credentialFormat);
        return credential.get("vc").get("credentialSubject");
    }

    @SneakyThrows
    public JsonNode extractCredential(final String document, final CredentialFormat credentialFormat) {
        if (CredentialFormat.JWT.equals(credentialFormat)) {
            String[] chunks = document.split("\\.");
            final byte[] payload = Base64.getUrlDecoder().decode(chunks[1]);
            return objectMapper.readTree(new String(payload, StandardCharsets.UTF_8));
        }
        return null;
    }

}
