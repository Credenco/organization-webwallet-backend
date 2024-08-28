package com.credenco.webwallet.backend;

import id.walt.crypto.keys.Key;
import id.walt.crypto.keys.KeyType;
import id.walt.crypto.keys.jwk.JWKKey;
import id.walt.did.helpers.WaltidServices;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.BuildersKt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
@EnableScheduling
@EnableAsync
@EntityScan(basePackageClasses = WebwalletBackendApplication.class)
@EnableConfigurationProperties
@ConfigurationPropertiesScan
public class WebwalletBackendApplication {


    public static void main(String[] args) {
        try {
            BuildersKt.runBlocking(
                    EmptyCoroutineContext.INSTANCE,
                    (scope, continuation) -> WaltidServices.INSTANCE.init(continuation)
            );

            Key didKey = BuildersKt.runBlocking(
                    EmptyCoroutineContext.INSTANCE,
                    (scope, continuation) -> JWKKey.Companion.generate(KeyType.secp256k1, null, continuation));

        } catch (InterruptedException e) {
            throw new RuntimeException("Walt.id could not be initiated", e);
        }

        SpringApplication.run(WebwalletBackendApplication.class, args);
    }
}
