package com.credenco.webwallet.backend.service.credential.dto;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static org.springframework.util.StringUtils.hasText;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisplayProperties {

    private String name;
    private LogoProperties logo;
    private String description;
    private BackgroundImage backgroundImage;
    private String backgroundColor;
    private String textColor;

    public static DisplayProperties from(Optional<com.credenco.webwallet.backend.service.credential.waltid.DisplayProperties> displayProperties) {
        if (displayProperties.isEmpty()) {
            return null;
        }
        return from(displayProperties.get());
    }

    public static DisplayProperties from(com.credenco.webwallet.backend.service.credential.waltid.DisplayProperties displayProperties) {
        if (displayProperties == null) {
            return null;
        }
        return DisplayProperties.builder()
                .name(displayProperties.getName())
                .logo(LogoProperties.from(displayProperties.getLogo()))
                .description(displayProperties.getDescription())
                .backgroundImage(BackgroundImage.from(displayProperties.getBackgroundImage()))
                .backgroundColor(displayProperties.getBackgroundColor())
                .textColor(displayProperties.getTextColor())
                .build();
    }

    public static DisplayProperties from(Optional<com.credenco.webwallet.backend.service.credential.waltid.DisplayProperties> displayPropertiesOpt,
                                         Optional<com.credenco.webwallet.backend.service.credential.waltid.DisplayProperties> defaultDisplayPropertiesOpt) {
        if (displayPropertiesOpt.isEmpty()) {
            return from(defaultDisplayPropertiesOpt);
        }
        final com.credenco.webwallet.backend.service.credential.waltid.DisplayProperties displayProperties = displayPropertiesOpt.get();
        final com.credenco.webwallet.backend.service.credential.waltid.DisplayProperties defaultDisplayProperties = defaultDisplayPropertiesOpt.get();

        return DisplayProperties.builder()
                .name(hasText(displayProperties.getName()) ? displayProperties.getName() : defaultDisplayProperties.getName())
                .logo(displayProperties.getLogo() != null ? LogoProperties.from(displayProperties.getLogo()) : LogoProperties.from(defaultDisplayProperties.getLogo()))
                .description(hasText(displayProperties.getDescription()) ? displayProperties.getDescription() : defaultDisplayProperties.getDescription())
                .backgroundImage(displayProperties.getBackgroundImage() != null ? BackgroundImage.from(displayProperties.getBackgroundImage()) : BackgroundImage.from(defaultDisplayProperties.getBackgroundImage()))
                .backgroundColor(hasText(displayProperties.getBackgroundColor()) ? displayProperties.getBackgroundColor() : defaultDisplayProperties.getBackgroundColor())
                .textColor(hasText(displayProperties.getTextColor()) ? displayProperties.getTextColor() : defaultDisplayProperties.getTextColor())
                .build();
    }
}
