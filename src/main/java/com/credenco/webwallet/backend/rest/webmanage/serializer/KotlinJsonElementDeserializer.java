package com.credenco.webwallet.backend.rest.webmanage.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonElement;

import java.io.IOException;

public class KotlinJsonElementDeserializer extends StdDeserializer<JsonElement> {

    public KotlinJsonElementDeserializer() {
        this(null);
    }

    public KotlinJsonElementDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public JsonElement deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        return Json.Default.parseToJsonElement(node.toString());
    }
}
