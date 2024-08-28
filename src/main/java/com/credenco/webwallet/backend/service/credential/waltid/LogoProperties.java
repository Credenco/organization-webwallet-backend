package com.credenco.webwallet.backend.service.credential.waltid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogoProperties {

    private String url;
    @JsonProperty("alt_text")
    private String altText;

}
