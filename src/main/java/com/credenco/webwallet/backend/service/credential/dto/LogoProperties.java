package com.credenco.webwallet.backend.service.credential.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogoProperties {

    private String url;
    private String altText;

    public static LogoProperties from(final com.credenco.webwallet.backend.service.credential.waltid.LogoProperties logo) {
        if (logo == null) {
            return null;
        }
        return LogoProperties.builder()
                .url(logo.getUrl())
                .altText(logo.getAltText())
                .build();
    }
}
