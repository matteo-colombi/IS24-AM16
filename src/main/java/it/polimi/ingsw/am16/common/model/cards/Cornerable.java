package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;
import java.io.Serial;
import java.util.stream.Stream;

/**
 * Interface implemented by all classes that represent an element that can be on one of a card's corners.
 */
@JsonDeserialize(using = Cornerable.Deserializer.class, keyUsing = Cornerable.KeyDeserializer.class)
@JsonSerialize(using = Cornerable.Serializer.class, keyUsing = Cornerable.KeySerializer.class)
public interface Cornerable {
    // RIP Cornerable, we hardly knew ya. 16/03/2024

    //You're back! 09/05/2024

    /**
     * Custom deserializer for {@link Cornerable}.
     */
    class Deserializer extends StdDeserializer<Cornerable> {

        @Serial
        private static final long serialVersionUID = -8705803926228544623L;

        protected Deserializer() {
            super(Cornerable.class);
        }

        /**
         * Deserializes a {@link Cornerable}
         * @param jsonParser Parser used for reading JSON content
         * @param deserializationContext Context that can be used to access information about this deserialization activity.
         * @return The deserialized {@link BoardCard}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public Cornerable deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            JsonNode cornerNode = jsonParser.getCodec().readTree(jsonParser);

            return Stream.of(ResourceType.values(), ObjectType.values(), SpecialType.values())
                    .flatMap(Stream::of)
                    .filter(el -> el.name().equalsIgnoreCase(cornerNode.asText()))
                    .findAny()
                    .orElseThrow(() -> new IOException("Unknown corner type: " + cornerNode.asText()));
        }
    }

    /**
     * Custom serializer for {@link Cornerable} that serializes corner types with their name.
     */
    class Serializer extends JsonSerializer<Cornerable> {
        @Override
        public void serialize(Cornerable value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.toString().toLowerCase());
        }
    }

    /**
     * Key deserializer for when {@link Cornerable} is used as a Map key.
     */
    class KeyDeserializer extends com.fasterxml.jackson.databind.KeyDeserializer {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        /**
         * Deserializes the Cornerable using the standard {@link Cornerable} deserializer.
         * @param key The key to deserialize.
         * @param ctxt Context that can be used to access information about this deserialization activity.
         * @return The deserialized {@link BoardCard}.
         * @throws IOException Thrown if an exception occurs while writing the serialized data.
         */
        @Override
        public Cornerable deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            return mapper.readValue("\"" + key + "\"", Cornerable.class);
        }
    }

    class KeySerializer extends JsonSerializer<Cornerable> {
        @Override
        public void serialize(Cornerable value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeFieldName(value.toString().toLowerCase());
        }
    }
}
