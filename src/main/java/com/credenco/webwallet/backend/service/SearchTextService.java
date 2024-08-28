package com.credenco.webwallet.backend.service;

import com.credenco.webwallet.backend.domain.CredentialFormat;
import com.credenco.webwallet.backend.domain.Issuer;
import com.credenco.webwallet.backend.service.credential.CredentialDisplayService;
import com.credenco.webwallet.backend.service.credential.dto.CredentialDisplayDto;
import com.credenco.webwallet.backend.service.credential.waltid.AttributeDefinition;
import com.credenco.webwallet.backend.service.credential.waltid.CredentialConfigurationSupported;
import com.credenco.webwallet.backend.service.credential.waltid.DisplayProperties;
import com.credenco.webwallet.backend.service.credential.waltid.OpenIDProviderMetadata;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Service;
import static org.springframework.util.ObjectUtils.isEmpty;


@Service
@RequiredArgsConstructor
@Slf4j
public class SearchTextService {

    private final ObjectMapper objectMapper;
    private final CredentialParserService credentialParserService;
    private final CredentialDisplayService credentialDisplayService;

    public String buildSearchText(final CredentialFormat credentialFormat, String credentialDocument, final String credentialConfigurationId, Issuer issuer) {
        try {
            List<String> searchFields = new ArrayList<>();
            searchFields.addAll(createIssuerFields(issuer.getConfigurationContent()));
            if (!StringUtils.isEmpty(credentialConfigurationId)) {
                searchFields.addAll(getAllCredentialTypeDisplayNames(issuer.getConfigurationContent(), credentialConfigurationId));
                searchFields.addAll(
                        getAllValuesAndAllDisplayNamesOfAttributesWithAValue(credentialFormat, credentialDocument, credentialConfigurationId, issuer));
            }
            return searchFields.stream()
                    .distinct()
                    .filter(s -> s != null)
                    .collect(Collectors.joining(" "));
        } catch (Exception e) {
            return credentialDocument;
        }
    }

    @SneakyThrows
    public String buildSearchText(final String issuerConfigurationContent, final String credentialConfigurationId) {
            List<String> searchFields = new ArrayList<>();
            searchFields.addAll(createIssuerFields(issuerConfigurationContent));
            searchFields.addAll(getAllCredentialTypeDisplayNames(issuerConfigurationContent, credentialConfigurationId));
            searchFields.addAll(getAllCredentialTypeAttributeDisplayNames(issuerConfigurationContent, credentialConfigurationId));
            return searchFields.stream()
                    .filter(s -> s != null)
                    .distinct()
                    .collect(Collectors.joining(" "));
    }

    @SneakyThrows
    private Collection<String> getAllCredentialTypeDisplayNames(final String configurationContent, final String credentialConfigurationId) {
        final OpenIDProviderMetadata openIDProviderMetadata = objectMapper.readValue(configurationContent, OpenIDProviderMetadata.class);
        return openIDProviderMetadata.getCredentialConfigurationsSupported().entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(credentialConfigurationId))
                .map(entry -> entry.getValue().getDisplay())
                .flatMap(displayProperties -> displayProperties.stream()
                        .flatMap(displayProperty -> getDisplayPropertyNames(displayProperty).stream())
                        .distinct())
                .toList();

    }

    @SneakyThrows
    private Collection<String> getAllCredentialTypeAttributeDisplayNames(final String configurationContent, final String credentialConfigurationId) {
        final OpenIDProviderMetadata openIDProviderMetadata = objectMapper.readValue(configurationContent, OpenIDProviderMetadata.class);
        return openIDProviderMetadata.getCredentialConfigurationsSupported().entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(credentialConfigurationId))
                .map(entry -> entry.getValue().getCredentialDefinition().getCredentialSubject())
                .filter(entry -> entry != null)
                .flatMap(entry -> entry.entrySet().stream())
                .flatMap(entry -> entry.getValue().getDisplay().stream())
                .flatMap(displayProperty -> getDisplayPropertyNames(displayProperty).stream())
                .distinct()
                .toList();

    }

    private List<String> getDisplayPropertyNames(final DisplayProperties displayProperty) {
        List<String> result = new ArrayList<>();
        result.add(displayProperty.getName());
        result.add(displayProperty.getDescription());
        return result;
    }

    private String getDisplayPropertyName(final DisplayProperties displayProperty) {
        return displayProperty.getName();
    }

    @SneakyThrows
    private Collection<String> getAllValuesAndAllDisplayNamesOfAttributesWithAValue(final CredentialFormat credentialFormat, final String credentialDocument, final String credentialConfigurationId, final Issuer issuer) {
        final JsonNode jsonNode = credentialParserService.extractCredentialSubject(credentialDocument, credentialFormat);
        if (jsonNode != null) {
            final OpenIDProviderMetadata openIDProviderMetadata = objectMapper.readValue(issuer.getConfigurationContent(), OpenIDProviderMetadata.class);
            final Map<String, AttributeDefinition> credentialSubject = openIDProviderMetadata.getCredentialConfigurationsSupported().get(credentialConfigurationId).getCredentialDefinition().getCredentialSubject();
            var values = extractValues(jsonNode);
            final CredentialDisplayDto credentialDisplayDto = credentialDisplayService.getCredentialDisplayDto(issuer.getConfigurationContent(), credentialConfigurationId, "nl-NL");
            final List<String> list = values.stream()
                    .filter(entry -> !entry.getKey().equalsIgnoreCase("id"))
                    .flatMap(entry -> {
                        var result = new ArrayList();
                        result.add(entry.getValue());
                        if (credentialDisplayDto.getCredentialSubjectDisplay().containsKey(entry.getKey())) {
                            result.addAll(credentialSubject.get(entry.getKey()).getDisplay().stream().map(displayProperties -> displayProperties.getName()).toList());
                        } else {
                            result.add(entry.getKey()); // No displayProperties exists. Add the keyname
                        }
                        return result.stream();
                    })
                    .filter(o -> !isEmpty(o))
                    .distinct()
                    .toList();
            return list;
        }
        return null;
    }

    private List<Map.Entry<String, String>> extractValues(final JsonNode jsonNode) {
        List<Map.Entry<String, String>> values = new ArrayList<>();

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();
        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> field = fieldsIterator.next();
            if (field.getValue().isTextual()) {
                values.add(Map.entry(field.getKey(), field.getValue().asText()));
            } else if (field.getValue().isObject()) {
                values.addAll(extractValues(field.getValue()));
            } else if (field.getValue().isArray()) {
                for (JsonNode arrayElement : field.getValue()) {
                    if (arrayElement.isObject()) {
                        values.addAll(extractValues(arrayElement));
                    }
                }
            }
        }
        return values;
    }

    @SneakyThrows
    private Collection<String> createIssuerFields(final String configurationContent) {
        final JsonNode jsonNode = objectMapper.readTree(configurationContent);
        return StreamUtils.createStreamFromIterator(jsonNode.get("display").elements())
                .flatMap(jsonNode1 -> StreamUtils.createStreamFromIterator(jsonNode1.fields()))
                .filter(jsonNode1 -> (jsonNode1.getKey().equalsIgnoreCase("name")) || jsonNode1.getKey().equalsIgnoreCase("description"))
                .map(jsonNode1 -> jsonNode1.getValue().textValue())
                .distinct()
                .toList();
    }
}
