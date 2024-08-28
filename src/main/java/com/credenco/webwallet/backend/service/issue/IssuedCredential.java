package com.credenco.webwallet.backend.service.issue;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IssuedCredential {
    private final String issuerUrl;
    private final String credential;
}
