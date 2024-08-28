package com.credenco.webwallet.backend.service.credential.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CredentialDisplayDto {

    private DisplayProperties issuerDisplay;
    private DisplayProperties credentialTypeDisplay;

    private Map<String, DisplayProperties> credentialSubjectDisplay;

}
