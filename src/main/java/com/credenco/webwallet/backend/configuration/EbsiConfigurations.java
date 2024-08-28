package com.credenco.webwallet.backend.configuration;

import id.walt.ebsi.EbsiEnvironment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@ConfigurationProperties(prefix = "external-services.ebsi")
@Configuration
@EnableConfigurationProperties
@Getter
@Setter
public class EbsiConfigurations {
    private Map<String, EbsiConfig> environments;

    public EbsiConfig getConfig(EbsiEnvironment ebsiEnvironment) {
        return environments.get(ebsiEnvironment.name());
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Setter
    @Getter
    @Builder
    public static class EbsiConfig {
        private String environment;
        private int didRegistryVersion;
        private String walletClientUrl;
        private String taoIssuerUrl;

        public EbsiEnvironment getEnvironment() {
            return EbsiEnvironment.valueOf(environment);
        }
    }
}
