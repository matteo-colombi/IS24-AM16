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
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Message sent by the server to inform the client that the player has to choose their personal objective.
 */
@JsonDeserialize(using = PromptObjectiveChoice.Deserializer.class)
public class PromptObjectiveChoice extends Payload {
    private final List<ObjectiveCard> possiblePersonalObjectives;

    /**
     * @param possiblePersonalObjectives The possible objectives that the player can choose from.
     */
    @JsonCreator
    public PromptObjectiveChoice(@JsonProperty("possiblePersonalObjectives") List<ObjectiveCard> possiblePersonalObjectives) {
        this.possiblePersonalObjectives = possiblePersonalObjectives;
    }

    /**
     * @return The possible objectives that the player can choose from.
     */
    public List<ObjectiveCard> getPossiblePersonalObjectives() {
        return possiblePersonalObjectives;
    }

    /**
     * Deserializer used to reload a PromptObjectiveChoice message from a JSON file.
     */
    public static class Deserializer extends StdDeserializer<PromptObjectiveChoice> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        @Serial
        private static final long serialVersionUID = -1077105647186316858L;

        protected Deserializer() {
            super(PromptObjectiveChoice.class);
        }

        /**
         * Reloads a {@link PromptObjectiveChoice} object from the given JSON.
         * @param p Parser used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link PromptObjectiveChoice}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public PromptObjectiveChoice deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);

            TypeReference<ArrayList<ObjectiveCard>> possiblePersonalObjectivesTypeRef = new TypeReference<>() {};
            List<ObjectiveCard> possiblePersonalObjectives = mapper.readValue(node.get("possiblePersonalObjectives").toString(), possiblePersonalObjectivesTypeRef);

            return new PromptObjectiveChoice(
                    possiblePersonalObjectives
            );
        }
    }
}
