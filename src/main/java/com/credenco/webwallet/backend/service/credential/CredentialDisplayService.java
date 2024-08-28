package com.credenco.webwallet.backend.service.credential;

import com.credenco.webwallet.backend.domain.Credential;
import com.credenco.webwallet.backend.service.credential.dto.CredentialDisplayDto;
import com.credenco.webwallet.backend.service.credential.dto.DisplayProperties;
import com.credenco.webwallet.backend.service.credential.waltid.AttributeDefinition;
import com.credenco.webwallet.backend.service.credential.waltid.CredentialConfigurationSupported;
import com.credenco.webwallet.backend.service.credential.waltid.OpenIDProviderMetadata;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
@Slf4j
public class CredentialDisplayService {

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public CredentialDisplayDto getCredentialDisplayDto(Credential credential, String locale) {
        return getCredentialDisplayDto(credential.getCredentialConfiguration(), credential.getCredentialConfigurationId(), locale);
    }

    @SneakyThrows
    public CredentialDisplayDto getCredentialDisplayDto(final String credentialConfiguration, final String credentialConfigurationId, final String locale) {
        final OpenIDProviderMetadata openIDProviderMetadata = objectMapper.readValue(credentialConfiguration, OpenIDProviderMetadata.class);

        final DisplayProperties issuerDisplayProperties = getIssuerDisplayProperties(openIDProviderMetadata, locale);
        return CredentialDisplayDto.builder()
                .issuerDisplay(issuerDisplayProperties)
                .credentialTypeDisplay(getCredentialTypeDisplay(openIDProviderMetadata, credentialConfigurationId, locale))
                .credentialSubjectDisplay(getCredentialsSubjectDisplay(openIDProviderMetadata, credentialConfigurationId, locale))
                .build();
    }


    public DisplayProperties getCredentialTypeDisplay(final OpenIDProviderMetadata openIDProviderMetadata, final String credentialConfigurationId, final String locale) {
        if (openIDProviderMetadata.getCredentialConfigurationsSupported() != null) {
            return openIDProviderMetadata.getCredentialConfigurationsSupported().entrySet().stream()
                    .filter(entry -> entry.getKey().equalsIgnoreCase(credentialConfigurationId))
                    .map(entry -> entry.getValue().getDisplay())
                    .map(displayProperties -> getDisplayByLocale(displayProperties, locale))
                    .findFirst().orElse(null);
        } else {
            // Backward compatibility
            return openIDProviderMetadata.getCredentialsSupported().stream()
                    .filter(configuration -> configuration.getTypes().contains(credentialConfigurationId))
                    .map(CredentialConfigurationSupported::getDisplay)
                    .map(displayProperties -> getDisplayByLocale(displayProperties, locale))
                    .findFirst().orElse(null);
        }
    }

    private Map<String, DisplayProperties> getCredentialsSubjectDisplay(final OpenIDProviderMetadata openIDProviderMetadata, final String credentialConfigurationId, final String locale) {
        if (openIDProviderMetadata.getCredentialConfigurationsSupported() != null) {
            return openIDProviderMetadata.getCredentialConfigurationsSupported().entrySet().stream()
                    .filter(entry -> entry.getKey().equalsIgnoreCase(credentialConfigurationId))
                    .map(entry -> entry.getValue().getCredentialDefinition().getCredentialSubject())
                    .map(displayProperties -> getDisplaysByLocale(displayProperties, locale))
                    .findFirst().orElse(null);
        }
        return null;
    }

    public DisplayProperties getIssuerDisplayProperties(OpenIDProviderMetadata openIDProviderMetadata, final String locale) {
        final var issuerDisplayProperties = getDisplayByLocale(openIDProviderMetadata.getDisplay(), locale);
        if (issuerDisplayProperties != null) {
            return issuerDisplayProperties;
        }
        return new DisplayProperties(getIssuerName(openIDProviderMetadata), null, null, null, "#C6C7F4", "#ffffff");
    }

    private String getIssuerName(OpenIDProviderMetadata openIDProviderMetadata) {
        if (openIDProviderMetadata.getCredentialIssuer() != null && openIDProviderMetadata.getCredentialIssuer().startsWith("https://api-conformance.ebsi.eu")) {
            return "EBSI Conformance";
        }
        return openIDProviderMetadata.getIssuer() != null ? openIDProviderMetadata.getIssuer() : openIDProviderMetadata.getCredentialIssuer();
    }

    private String replace(final JsonNode node, final String searchText, final String replaceValue) {
        if ((node == null) || (node.asText() == null)) {
            return null;
        }
        return node.asText().replace(searchText, replaceValue);
    }

    private Map<String, DisplayProperties> getDisplaysByLocale(final Map<String, AttributeDefinition> credentialSubject, final String locale) {
        if (credentialSubject == null) {
            return new HashMap<>();
        }
        return credentialSubject.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), getDisplayByLocale(entry.getKey(), entry.getValue().getDisplay(), locale)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private DisplayProperties getDisplayByLocale(final String key, final List<com.credenco.webwallet.backend.service.credential.waltid.DisplayProperties> display, final String locale) {
        final var result = getDisplayByLocale(display, locale);
        if (result == null) {
            return DisplayProperties.builder().name(key).build();
        }
        return result;
    }

    public DisplayProperties getDisplayByLocale(final List<com.credenco.webwallet.backend.service.credential.waltid.DisplayProperties> display, final String locale) {
        if (display == null || display.isEmpty()) {
            return null;
        }
        final Optional<com.credenco.webwallet.backend.service.credential.waltid.DisplayProperties> defaultDisplayProperties = display.stream()
                .filter(displayProperties -> !hasText(displayProperties.getLocale()))
                .findFirst();
        final Optional<com.credenco.webwallet.backend.service.credential.waltid.DisplayProperties> localeSpecificDisplayProperties = display.stream()
                .filter(displayProperties -> locale.equalsIgnoreCase(displayProperties.getLocale()))
                .findFirst();
        if (defaultDisplayProperties.isEmpty() && localeSpecificDisplayProperties.isEmpty()){
            return DisplayProperties.from(Optional.empty(), Optional.of(display.getFirst()));
        }
        return DisplayProperties.from(localeSpecificDisplayProperties, defaultDisplayProperties);
    }
}
