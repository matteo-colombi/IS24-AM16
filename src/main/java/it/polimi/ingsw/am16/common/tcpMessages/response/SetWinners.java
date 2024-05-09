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
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.decks.GoldCardsDeck;
import it.polimi.ingsw.am16.common.model.cards.decks.ObjectiveCardsDeck;
import it.polimi.ingsw.am16.common.model.cards.decks.ResourceCardsDeck;
import it.polimi.ingsw.am16.common.model.cards.decks.StarterCardsDeck;
import it.polimi.ingsw.am16.common.model.game.Game;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = SetWinners.Deserializer.class)
public class SetWinners extends Payload {
    private final List<String> winnerUsernames;

    @JsonCreator
    public SetWinners(@JsonProperty("winnerUsernames") List<String> winnerUsernames) {
        this.winnerUsernames = winnerUsernames;
    }

    public List<String> getWinnerUsernames() {
        return winnerUsernames;
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

            return new SetWinners(
                    winnerUsernames
            );
        }
    }
}
