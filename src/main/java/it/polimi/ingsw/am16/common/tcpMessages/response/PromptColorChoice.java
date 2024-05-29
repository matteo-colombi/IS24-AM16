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
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Message sent by the server to inform the client that it is the turn of their player to choose a color.
 */
@JsonDeserialize(using = PromptColorChoice.Deserializer.class)
public class PromptColorChoice extends Payload {
    private final List<PlayerColor> colorChoices;

    /**
     * @param colorChoices The possible colors from which the player can pick from.
     */
    @JsonCreator
    public PromptColorChoice(@JsonProperty("colorChoices") List<PlayerColor> colorChoices) {
        this.colorChoices = colorChoices;
    }

    /**
     * @return The possible colors from which the player can pick from.
     */
    public List<PlayerColor> getColorChoices() {
        return colorChoices;
    }

    /**
     * Deserializer used to reload a PromptColorChoice message from a JSON file.
     */
    public static class Deserializer extends StdDeserializer<PromptColorChoice> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        @Serial
        private static final long serialVersionUID = 7804485293386120441L;

        protected Deserializer() {
            super(PromptColorChoice.class);
        }

        /**
         * Reloads a {@link PromptColorChoice} object from the given JSON.
         * @param p Parser used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link PromptColorChoice}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public PromptColorChoice deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);

            TypeReference<ArrayList<PlayerColor>> colorChoicesTypeRef = new TypeReference<>() {};
            List<PlayerColor> colorChoices = mapper.readValue(node.get("colorChoices").toString(), colorChoicesTypeRef);

            return new PromptColorChoice(
                    colorChoices
            );
        }
    }
}
