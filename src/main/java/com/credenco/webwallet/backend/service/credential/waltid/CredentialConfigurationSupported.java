package com.credenco.webwallet.backend.service.credential.waltid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


// To be replace by WaltId class when draft 13 compatible
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CredentialConfigurationSupported {

    private String id;
    private List<DisplayProperties> display;

    @JsonProperty("credential_definition")
    private CredentialDefinition credentialDefinition;

    // Backward compatibility
    @JsonProperty("types")
    private List<String> types;
}
