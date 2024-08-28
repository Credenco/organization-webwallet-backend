package com.credenco.webwallet.backend.service.credential.waltid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DisplayProperties {

    private String name;
    private String locale;
    private LogoProperties logo;
    private String description;
    @JsonProperty("background_image")
    private BackgroundImage backgroundImage;
    @JsonProperty("background_color")
    private String backgroundColor;
    @JsonProperty("text_color")
    private String textColor;

}
