package com.credenco.webwallet.backend.service.credential.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackgroundImage {

    private String url;

    public static BackgroundImage from(final com.credenco.webwallet.backend.service.credential.waltid.BackgroundImage backgroundImage) {
        if (backgroundImage == null) {
            return null;
        }
        return BackgroundImage.builder()
                .url(backgroundImage.getUrl())
                .build();
    }
}
