package com.credenco.webwallet.backend.service.credential.waltid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenIDProviderMetadata {

    private String issuer;

    @JsonProperty("credential_issuer")
    private String credentialIssuer;

    private List<DisplayProperties> display;

    @JsonProperty("credential_configurations_supported")
    private Map<String, CredentialConfigurationSupported> credentialConfigurationsSupported;

    @JsonProperty("credentials_supported")
    private List<CredentialConfigurationSupported> credentialsSupported;
}
