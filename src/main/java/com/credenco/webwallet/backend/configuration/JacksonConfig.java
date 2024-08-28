package com.credenco.webwallet.backend.configuration;

import com.credenco.webwallet.backend.rest.webmanage.serializer.KotlinJsonElementDeserializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss";

    @Bean
    @Primary
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.timeZone(TimeZone.getTimeZone("Europe/Amsterdam"));
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            builder.deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)));

            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
            builder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
            builder.serializationInclusion(JsonInclude.Include.NON_NULL);

            builder.deserializers(new KotlinJsonElementDeserializer(JsonObject.class));
            builder.deserializers(new KotlinJsonElementDeserializer(JsonElement.class));
        };
    }

}
