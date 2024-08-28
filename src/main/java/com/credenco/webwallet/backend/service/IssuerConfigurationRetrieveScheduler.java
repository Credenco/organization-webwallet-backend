package com.credenco.webwallet.backend.service;

import com.credenco.webwallet.backend.domain.Issuer;
import com.credenco.webwallet.backend.repository.IssuerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssuerConfigurationRetrieveScheduler {

    private final IssuerRepository issuerRepository;
    private final IssuerService issuerService;

    @Scheduled(initialDelay = 10000, fixedDelayString = "PT24H")
    public void retrieveIssuerConfigurations() {
        issuerRepository.findAll().forEach(issuer -> retrieveAndStoreIssuerConfiguration(issuer));
    }

    private void retrieveAndStoreIssuerConfiguration(final Issuer issuer) {
        try {
            final RestTemplate restTemplate = new RestTemplate();
             final String configurationContent = restTemplate.getForObject(issuer.getConfigurationUrl(), String.class);
//            final String configurationContent = """
//                    {"credential_issuer":"https://agent.belastingdienst.demo.sphereon.com","credential_endpoint":"https://agent.belastingdienst.demo.sphereon.com/credentials","display":[{"name":"Belastingdienst","description":"Belastingdienst Issuer"}],"credential_configurations_supported":{"Woonplaatsverklaring":{"format":"jwt_vc_json","cryptographic_binding_methods_supported":["did:jwk"],"cryptographic_suites_supported":["ES256"],"display":[{"name":"Woonplaatsverklaring","description":"Woonplaatsverklaring Belastingdienst","background_color":"rgba(0, 0, 0, 0.2)","text_color":"#FBFBFB","logo":{"url":"https://i.ibb.co/pyZpF8m/Belastingdienst.png","alt_text":"Belastingdienst logo"},"background_image":{"url":"https://i.ibb.co/jWF45Xw/grant-ritchie.png","alt_text":"Skyscrapers background"}},{"locale":"en-US","name":"Residence statement","description":"Residence statement Belastingdienst","background_color":"rgba(0, 0, 0, 0.2)","text_color":"#FBFBFB","logo":{"url":"https://i.ibb.co/pyZpF8m/Belastingdienst.png","alt_text":"Belastingdienst logo"},"background_image":{"url":"https://i.ibb.co/jWF45Xw/grant-ritchie.png","alt_text":"Skyscrapers background"}},{"locale":"nl-NL","name":"Woonplaatsverklaring","description":"Woonplaatsverklaring Belastingdienst","background_color":"rgba(0, 0, 0, 0.2)","text_color":"#FBFBFB","logo":{"url":"https://i.ibb.co/pyZpF8m/Belastingdienst.png","alt_text":"Belastingdienst logo"},"background_image":{"url":"https://i.ibb.co/jWF45Xw/grant-ritchie.png","alt_text":"Skyscrapers background"}}],"credential_definition":{"type":["VerifiableCredential","Woonplaatsverklaring"]}},"Omzetbelasting":{"format":"jwt_vc_json","cryptographic_binding_methods_supported":["did:jwk"],"cryptographic_suites_supported":["ES256"],"display":[{"name":"Omzetbelasting","description":"Omzetbelastingverklaring Belastingdienst","background_color":"rgba(0, 0, 0, 0.2)","text_color":"#FBFBFB","logo":{"url":"https://i.ibb.co/pyZpF8m/Belastingdienst.png","alt_text":"Belastingdienst logo"},"background_image":{"url":"https://i.ibb.co/c6hQwRP/hector-j-rivas.jpg","alt_text":"Mirrors background"}},{"locale":"en-US","name":"Tax registration statement","description":"Tax registration statement Belastingdienst","background_color":"rgba(0, 0, 0, 0.2)","text_color":"#FBFBFB","logo":{"url":"https://i.ibb.co/pyZpF8m/Belastingdienst.png","alt_text":"Belastingdienst logo"},"background_image":{"url":"https://i.ibb.co/c6hQwRP/hector-j-rivas.jpg","alt_text":"Mirrors background"}},{"locale":"nl-NL","name":"Omzetbelasting","description":"Omzetbelastingverklaring Belastingdienst","background_color":"rgba(0, 0, 0, 0.2)","text_color":"#FBFBFB","logo":{"url":"https://i.ibb.co/pyZpF8m/Belastingdienst.png","alt_text":"Belastingdienst logo"},"background_image":{"url":"https://i.ibb.co/c6hQwRP/hector-j-rivas.jpg","alt_text":"Mirrors background"}}],"credential_definition":{"type":["VerifiableCredential","Omzetbelasting"]}}},"credential_supplier_config":{"templates_base_dir":"templates","template_mappings":[{"credential_types":["Woonplaatsverklaring"],"template_path":"Woonplaatsverklaring.hbs","format":"jwt_vc_json"},{"credential_types":["Omzetbelasting"],"template_path":"Omzetbelasting.hbs","format":"jwt_vc_json"}]}}
//                    """;
            issuerService.updateIssuerConfiguration(issuer, configurationContent);
            log.info("Updated issuer configuration. Issue id {}, url: {}", issuer.getId(), issuer.getConfigurationUrl());
        } catch (Exception e) {
            log.error("Error retrieving issuer configuration. Issue id {}, url: {}. Error: {}", issuer.getId(), issuer.getConfigurationUrl(), e.getMessage());
        }
    }
}
