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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Message sent by the server to tell the client who the winners of the game are. This message also contains information about each player's personal objective.
 */
@JsonDeserialize(using = SetWinners.Deserializer.class)
public class SetWinners extends Payload {
    private final List<String> winnerUsernames;
    private final Map<String, ObjectiveCard> personalObjectives;

    /**
     * @param winnerUsernames The list of players (which only contains multiple usernames in the case of a tie) who have won the game.
     * @param personalObjectives A map containing each player's personal objective, which was kept secret for the duration of the game.
     */
    @JsonCreator
    public SetWinners(@JsonProperty("winnerUsernames") List<String> winnerUsernames, @JsonProperty("personalObjectives") Map<String, ObjectiveCard> personalObjectives) {
        this.winnerUsernames = winnerUsernames;
        this.personalObjectives = personalObjectives;
    }

    /**
     * @return The list of players (which only contains multiple usernames in the case of a tie) who have won the game.
     */
    public List<String> getWinnerUsernames() {
        return winnerUsernames;
    }

    /**
     * @return A map containing each player's personal objective, which was kept secret for the duration of the game.
     */
    public Map<String, ObjectiveCard> getPersonalObjectives() {
        return personalObjectives;
    }

    /**
     * Deserializer used to reload a SetWinners message from a JSON file.
     */
    public static class Deserializer extends StdDeserializer<SetWinners> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        @Serial
        private static final long serialVersionUID = 3239793850141577087L;

        protected Deserializer() {
            super(SetWinners.class);
        }

        /**
         * Reloads a {@link SetWinners} object from the given JSON.
         * @param p Parser used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link SetWinners}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public SetWinners deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);

            TypeReference<ArrayList<String>> winnersTypeRef = new TypeReference<>() {};
            List<String> winnerUsernames = mapper.readValue(node.get("winnerUsernames").toString(), winnersTypeRef);

            TypeReference<HashMap<String, ObjectiveCard>> objectivesTypeRef = new TypeReference<>() {};
            Map<String, ObjectiveCard> personalObjectives = mapper.readValue(node.get("personalObjectives").toString(), objectivesTypeRef);

            return new SetWinners(
                    winnerUsernames,
                    personalObjectives
            );
        }
    }
}
