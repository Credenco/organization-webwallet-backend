package com.credenco.webwallet.backend.configuration;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Builder
public class VerifierApiConfiguration {

    private final String url;
    private final String bearerToken;
    private final String successRedirectUrl;

    public VerifierApiConfiguration(@Value("${externalServices.verifierApi.url}") String url,
                                    @Value("${externalServices.verifierApi.bearerToken}") String bearerToken,
                                    @Value("${externalServices.verifierApi.successRedirectUrl}") String successRedirectUrl) {
        this.url = url;
        this.bearerToken = bearerToken;
        this.successRedirectUrl = successRedirectUrl;
    }
}
