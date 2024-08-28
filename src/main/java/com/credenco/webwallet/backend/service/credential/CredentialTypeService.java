package com.credenco.webwallet.backend.service.credential;

import com.credenco.webwallet.backend.domain.IssuerCredentialType;
import com.credenco.webwallet.backend.domain.PresentationDefinitionCredentialType;
import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.repository.IssuerCredentialTypeRepository;
import com.credenco.webwallet.backend.repository.IssuerRepository;
import com.credenco.webwallet.backend.service.credential.dto.CredentialTypeDto;
import com.credenco.webwallet.backend.service.credential.dto.DisplayProperties;
import com.credenco.webwallet.backend.service.credential.waltid.OpenIDProviderMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CredentialTypeService {

    private final IssuerRepository issuerRepository;
    private final IssuerCredentialTypeRepository issuerCredentialTypeRepository;
    private final CredentialDisplayService credentialDisplayService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public PagedModel<CredentialTypeDto> findAllCredentialTypes(final Wallet wallet, String searchText, final String locale, final Pageable pageable) {
        var allSpecification = new ArrayList<Specification<IssuerCredentialType>>();
        allSpecification.add(issuerCredentialTypeRepository.hasWallet(wallet));
        if (!searchText.isEmpty()) {
            allSpecification.addAll(
                    Arrays.stream(searchText.split(" "))
                            .map(text -> issuerCredentialTypeRepository.containsSearchText(text))
                            .toList()
            );
        }
        final Specification<IssuerCredentialType> specification = issuerCredentialTypeRepository.allOf(allSpecification);
        final Page<IssuerCredentialType> items = issuerCredentialTypeRepository.findAll(specification, pageable);

        return new PagedModel(items.map(issuerCredentialType -> getSupportedTypes(issuerCredentialType, locale).findFirst()));
    }

    public List<CredentialTypeDto> findAllCredentialTypes(final Wallet wallet, List<PresentationDefinitionCredentialType> presentationDefinitionCredentialTypes, String locale) {
        final List<String> keys = presentationDefinitionCredentialTypes.stream()
                .map(pdct -> pdct.getCredentialTypeConfigurationUrl() + "_" + pdct.getCredentialConfigurationId())
                .toList();
        return issuerCredentialTypeRepository.findAllByTypeKeys(wallet, keys).stream()
                .flatMap(issuerCredentialType -> getSupportedTypes(issuerCredentialType, locale))
                .distinct().toList();
    }

    @SneakyThrows
    private Stream<CredentialTypeDto> getSupportedTypes(final IssuerCredentialType issuerCredentialType, String locale) {
        final OpenIDProviderMetadata openIDProviderMetadata = objectMapper.readValue(issuerCredentialType.getIssuer().getConfigurationContent(), OpenIDProviderMetadata.class);
        final DisplayProperties issuerDisplayProperties = credentialDisplayService.getIssuerDisplayProperties(openIDProviderMetadata, locale);

        return openIDProviderMetadata.getCredentialConfigurationsSupported().entrySet().stream()
                .filter(ct -> ct.getKey().equalsIgnoreCase(issuerCredentialType.getCredentialConfigurationId()))
                .map(entry -> CredentialTypeDto.builder()
                        .credentialConfigurationId(entry.getKey())
                        .credentialTypeConfigurationUrl(issuerCredentialType.getIssuer().getConfigurationUrl())
                        .vcType(issuerCredentialType.getVcType())
                        .issueUrl(issuerCredentialType.getIssuer().getIssueUrl())
                        .issuerDisplay(issuerDisplayProperties)
                        .credentialTypeDisplay(credentialDisplayService.getDisplayByLocale(entry.getValue().getDisplay(), locale))
                        .build()
                );
    }

    @SneakyThrows
    public List<CredentialTypeDto> getOfferedCredentialsTypes(final String issuerUrl, final Set<String> configurationIds, String locale) {
        try {
            final RestTemplate restTemplate = new RestTemplate();
            final String configurationContent = restTemplate.getForObject(issuerUrl + "/.well-known/openid-credential-issuer", String.class);

            final OpenIDProviderMetadata openIDProviderMetadata = objectMapper.readValue(configurationContent, OpenIDProviderMetadata.class);
            final DisplayProperties issuerDisplayProperties = credentialDisplayService.getIssuerDisplayProperties(openIDProviderMetadata, locale);

            return configurationIds.stream()
                    .map(configurationId -> CredentialTypeDto.builder()
                        .issueUrl(issuerUrl)
                        .issuerDisplay(issuerDisplayProperties)
                        .credentialTypeDisplay(credentialDisplayService.getDisplayByLocale(getDisplayForConfigurationId(openIDProviderMetadata, configurationId), locale))
                        .build())
                    .toList();
        } catch (Exception e) {
            log.error("Error retrieving issuer configuration url: {}", issuerUrl + "/.well-known/openid-credential-issuer");
            throw new RuntimeException("Unable to retrieve issuer configuration");
        }
    }

    private List<com.credenco.webwallet.backend.service.credential.waltid.DisplayProperties> getDisplayForConfigurationId(final OpenIDProviderMetadata openIDProviderMetadata, String configurationId) {
        return openIDProviderMetadata.getCredentialConfigurationsSupported().get(configurationId).getDisplay();
    }
}
