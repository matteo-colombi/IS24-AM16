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

@JsonDeserialize(using = SetPlayers.Deserializer.class)
public class SetPlayers extends Payload {
    private final List<String> usernames;

    @JsonCreator
    public SetPlayers(@JsonProperty("usernames") List<String> usernames) {
        this.usernames = usernames;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    /**
     * Deserializer used to reload a SetPlayers message from a JSON file.
     */
    public static class Deserializer extends StdDeserializer<SetPlayers> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        protected Deserializer() {
            super(SetPlayers.class);
        }

        /**
         * Reloads a {@link SetPlayers} object from the given JSON.
         * @param p Parsed used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link SetPlayers}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public SetPlayers deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);

            TypeReference<ArrayList<String>> usernamesTypeRef = new TypeReference<>() {};
            List<String> usernames = mapper.readValue(node.get("winnerUsernames").toString(), usernamesTypeRef);

            return new SetPlayers(
                    usernames
            );
        }
    }
}
