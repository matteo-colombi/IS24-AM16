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
import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Message sent by the server to inform the client about the cards in another player's hand.
 */
@JsonDeserialize(using = SetOtherHand.Deserializer.class)
public class SetOtherHand extends Payload {
    private final String username;
    private final List<RestrictedCard> hand;

    /**
     *
     * @param username The username of the player whose hand is being communicated.
     * @param hand A restricted view of the other player's hand.
     */
    @JsonCreator
    public SetOtherHand(@JsonProperty("username") String username, @JsonProperty("hand") List<RestrictedCard> hand) {
        this.username = username;
        this.hand = hand;
    }

    /**
     * @return The username of the player whose hand is being communicated.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return A restricted view of the other player's hand.
     */
    public List<RestrictedCard> getHand() {
        return hand;
    }

    /**
     * Deserializer used to reload a SetOtherHand message from a JSON file.
     */
    public static class Deserializer extends StdDeserializer<SetOtherHand> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();
        
        @Serial
        private static final long serialVersionUID = 856787037370720064L;

        protected Deserializer() {
            super(SetOtherHand.class);
        }

        /**
         * Reloads a {@link SetOtherHand} object from the given JSON.
         * @param p Parser used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link SetOtherHand}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public SetOtherHand deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);

            String username = node.get("username").asText();

            TypeReference<ArrayList<RestrictedCard>> handTypeRef = new TypeReference<>() {};
            List<RestrictedCard> hand = mapper.readValue(node.get("hand").toString(), handTypeRef);

            return new SetOtherHand(
                    username,
                    hand
            );
        }
    }
}
