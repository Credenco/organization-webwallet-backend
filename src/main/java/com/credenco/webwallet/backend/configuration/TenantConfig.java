package com.credenco.webwallet.backend.configuration;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "tenant")
@NoArgsConstructor
@Setter
@Getter
@Builder
@AllArgsConstructor
public class TenantConfig {

    private List<TenantConfigEntry> config;

    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class TenantConfigEntry {
        private String externalKey;
        private String jwtKeyPrivate;
        private String jwtKeyPublic;
    }
}
