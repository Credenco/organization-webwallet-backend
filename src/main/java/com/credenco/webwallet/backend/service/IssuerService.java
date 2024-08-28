package com.credenco.webwallet.backend.service;

import com.credenco.webwallet.backend.domain.Issuer;
import com.credenco.webwallet.backend.domain.IssuerCredentialType;
import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.repository.IssuerRepository;
import com.credenco.webwallet.backend.service.credential.waltid.OpenIDProviderMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssuerService {

    private final IssuerRepository issuerRepository;
    private final SearchTextService searchTextService;
    private final ObjectMapper objectMapper;

    @Transactional
    public Issuer getOrCreateIssuer(Wallet wallet, String providerMetadataUri, String configurationContent) {
        final var issuer = issuerRepository.findFirstByWalletAndConfigurationUrl(wallet, providerMetadataUri);
        return issuer.orElseGet(() -> addIssuer(wallet, providerMetadataUri, configurationContent));
    }

    @Transactional
    public Issuer updateIssuerConfiguration(final Issuer issuer, String issuerConfigurationContent) {
        try {
            final var issuerFromDb = issuerRepository.findById(issuer.getId()).orElseThrow(() -> new RuntimeException("Issuer " + issuer.getId() + " not found"));
            issuerFromDb.setConfigurationContent(issuerConfigurationContent);
            issuerFromDb.getCredentialTypes().clear();
            issuerFromDb.getCredentialTypes().addAll(buildCredentialTypes(issuerConfigurationContent, issuerFromDb));
            return issuerRepository.save(issuerFromDb);
        } catch (Exception e) {
            log.error("Error updateIssuerConfiguration issuer.id: {}, issuerUrl: {}, error: {}", issuer.getId(), issuer.getIssueUrl(), e.getMessage(), e);
            return issuer;
        }
    }

    private @NotNull Issuer addIssuer(final Wallet wallet, final String providerMetadataUri, final String configurationContent) {
        var issuer = Issuer.builder()
                .wallet(wallet)
                .did("")
                .configurationUrl(providerMetadataUri)
                .configurationContent(configurationContent)
                .issueUrl("")
                .skipIssueToWalletConfirmation(false)
                .createdAt(LocalDateTime.now())
                .createdBy("System")
                .lastModifiedAt(LocalDateTime.now())
                .lastModifiedBy("System")
                .build();
        final Issuer savedIssuer = issuerRepository.save(issuer);
        return updateIssuerConfiguration(savedIssuer, configurationContent);
    }


    @SneakyThrows
    private List<IssuerCredentialType> buildCredentialTypes(final String configurationContent, final Issuer issuer) {
        final OpenIDProviderMetadata openIDProviderMetadata = objectMapper.readValue(configurationContent, OpenIDProviderMetadata.class);
        if (openIDProviderMetadata.getCredentialConfigurationsSupported() == null) {
            log.info("No credential configurations supported found in issuer configuration content: {}", configurationContent);
            return List.of();
        }
        return openIDProviderMetadata.getCredentialConfigurationsSupported().entrySet().stream()
                .map(entry -> IssuerCredentialType.builder()
                        .issuer(issuer)
                        .credentialConfigurationId(entry.getKey())
                        .vcType(entry.getValue().getCredentialDefinition().getType().getLast())
                        .searchData(searchTextService.buildSearchText(configurationContent, entry.getKey()))
                        .build())
                .toList();
    }
}
