package com.credenco.webwallet.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplatesConfig {

    @Bean(name = "issuerApi")
    RestTemplate getIssuerRestTemplate(IssuerApiConfiguration issuerApiConfiguration) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(issuerApiConfiguration.getUrl()));
        restTemplate.getInterceptors().add((request, body, clientHttpRequestExecution) -> {
            HttpHeaders headers = request.getHeaders();
            if (!headers.containsKey("Authorization")) {
                String token = issuerApiConfiguration.getBearerToken().toLowerCase().startsWith("bearer") ? issuerApiConfiguration.getBearerToken() : "Bearer " + issuerApiConfiguration.getBearerToken();
                request.getHeaders().add("Authorization", token);
            }

            return clientHttpRequestExecution.execute(request, body);

        });
        return restTemplate;
    }

    @Bean(name = "verifierApi")
    RestTemplate getVerifierRestTemplate(VerifierApiConfiguration verifierApiConfiguration) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(verifierApiConfiguration.getUrl()));
        restTemplate.getInterceptors().add((request, body, clientHttpRequestExecution) -> {
            HttpHeaders headers = request.getHeaders();
            if (!headers.containsKey("Authorization")) {
                String token = verifierApiConfiguration.getBearerToken().toLowerCase().startsWith("bearer") ? verifierApiConfiguration.getBearerToken() : "Bearer " + verifierApiConfiguration.getBearerToken();
                request.getHeaders().add("Authorization", token);
            }

            return clientHttpRequestExecution.execute(request, body);

        });
        return restTemplate;
    }

}
