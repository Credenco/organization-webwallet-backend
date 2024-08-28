package com.credenco.webwallet.backend.configuration;

import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Builder
public class IssuerApiConfiguration {

    private final String url;
    private final String bearerToken;

    public IssuerApiConfiguration(@Value("${externalServices.issuerApi.url}") String url,
                                  @Value("${externalServices.issuerApi.bearerToken}") String bearerToken) {
        this.url = url;
        this.bearerToken = bearerToken;
    }
}
