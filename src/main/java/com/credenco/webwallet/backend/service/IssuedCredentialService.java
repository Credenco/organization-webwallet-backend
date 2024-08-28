package com.credenco.webwallet.backend.service;

import com.credenco.webwallet.backend.domain.CredentialFormat;
import com.credenco.webwallet.backend.domain.IssuedCredential;
import com.credenco.webwallet.backend.domain.Issuer;
import com.credenco.webwallet.backend.domain.Wallet;
import com.credenco.webwallet.backend.domain.issuer.CredentialIssuerCredentialDefinition;
import com.credenco.webwallet.backend.repository.CredentialIssuerCredentialDefinitionRepository;
import com.credenco.webwallet.backend.repository.IssuedCredentialRepository;
import com.credenco.webwallet.backend.repository.IssuerRepository;
import com.credenco.webwallet.backend.repository.WalletRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssuedCredentialService {

    private final IssuedCredentialRepository issuedCredentialRepository;
    private final WalletRepository walletRepository;
    private final CredentialParserService credentialParserService;
    private final CredentialIssuerCredentialDefinitionRepository credentialIssuerCredentialDefinitionRepository;
    private final ObjectMapper objectMapper;
    private final SearchTextService searchTextService;
    private final IssuerRepository issuerRepository;

    @SneakyThrows
    public IssuedCredential storeIssuedCredential(String issueingWalletExternalKey,
                                                  String credentialIssuerDefinitionExternalKey,
                                                  JsonNode credentialJson) {
        final Wallet wallet = walletRepository.findByExternalKey(issueingWalletExternalKey).orElseThrow(() -> new RuntimeException("Wallet not found"));

        var jsonString = objectMapper.writeValueAsString(credentialJson);
        if (credentialJson.get("format").asText().equalsIgnoreCase("jwt_vc_json")) {
            final JsonNode credentialNode = credentialParserService.extractCredential(credentialJson.get("credential").asText(), CredentialFormat.JWT);
            var types = StreamSupport.stream(credentialNode.get("vc").get("type").spliterator(), false)
                    .map(JsonNode::asText)
                    .toList();
            final CredentialIssuerCredentialDefinition credentialIssuerCredentialDefinition = findCredentialIssuerCredentialDefinitionByTypes(wallet, credentialIssuerDefinitionExternalKey, types);

            final Issuer issuer = findIssuer(wallet, credentialIssuerCredentialDefinition);
            final String searchText = searchTextService.buildSearchText(CredentialFormat.JWT, jsonString, credentialIssuerCredentialDefinition.getCredentialConfigurationId(), issuer);

            return issuedCredentialRepository.save(IssuedCredential.builder()
                                                           .wallet(wallet)
                                                           .credentialIssuerCredentialDefinition(credentialIssuerCredentialDefinition)
                                                           .credentialUuid(credentialNode.get("vc").get("id").asText())
                                                           .issuer(issuer)
                                                           .credentialFormat(CredentialFormat.JWT)
                                                           .document(jsonString)
                                                           .searchData(searchText)
                                                           .validFrom(Instant.ofEpochSecond(credentialNode.get("nbf").asLong()).atZone(ZoneId.systemDefault()).toLocalDateTime())
                                                           .issuanceDate(Instant.ofEpochSecond(credentialNode.get("iat").asLong()).atZone(ZoneId.systemDefault()).toLocalDateTime())
                                                           .build());
        }
        return null;
    }

    private Issuer findIssuer(final Wallet wallet, final CredentialIssuerCredentialDefinition credentialIssuerCredentialDefinition) {
        String configUrl = credentialIssuerCredentialDefinition.getCredentialIssuerDefinition().getIssuerUrl();
        configUrl = configUrl.endsWith("/") ? configUrl : configUrl + "/";
        configUrl = configUrl + ".well-known/openid-credential-issuer";
        return issuerRepository.findFirstByWalletAndConfigurationUrl(wallet, configUrl).orElseThrow(() -> new RuntimeException("Issuer not found"));
    }

    private CredentialIssuerCredentialDefinition findCredentialIssuerCredentialDefinitionByTypes(final Wallet issueingWallet, final String credentialIssuerDefinitionExternalKey, final List<String> types) {
        final List<CredentialIssuerCredentialDefinition> credentialIssuerCredentialDefinition = credentialIssuerCredentialDefinitionRepository.findByWalletExternalKeyAndExternalKey(issueingWallet.getExternalKey(),
                                                                                                                                                                                     credentialIssuerDefinitionExternalKey,
                                                                                                                                                                                     types.getLast());

        final List<String> lowerCasedTypes = types.stream()
                .map(t -> t.toLowerCase())
                .toList();
        return credentialIssuerCredentialDefinition.stream()
                .filter(cicd -> cicd.getTypes().size() == types.size())
                .filter(cicd -> cicd.getTypes().stream()
                        .allMatch(t -> lowerCasedTypes.contains(t.getCredentialType().toLowerCase())))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("CredentialIssuerCredentialDefinition not found"));
    }

}
