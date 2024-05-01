package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = SetStartOrder.Deserializer.class)
public class SetStartOrder extends Payload {
    private final List<String> usernames;

    @JsonCreator
    public SetStartOrder(@JsonProperty("usernames") List<String> usernames) {
        this.usernames = usernames;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    /**
     * Deserializer used to reload a SetStartOrder message from a JSON file.
     */
    public static class Deserializer extends StdDeserializer<SetStartOrder> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        protected Deserializer() {
            super(SetStartOrder.class);
        }

        /**
         * Reloads a {@link SetStartOrder} object from the given JSON.
         * @param p Parsed used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link SetStartOrder}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public SetStartOrder deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);

            TypeReference<ArrayList<String>> usernamesTypeRef = new TypeReference<>() {};
            List<String> usernames = mapper.readValue(node.get("usernames").toString(), usernamesTypeRef);

            return new SetStartOrder(
                    usernames
            );
        }
    }
}
