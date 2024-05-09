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
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = SetHand.Deserializer.class)
public class SetHand extends Payload {
    private final List<PlayableCard> hand;

    @JsonCreator
    public SetHand(@JsonProperty("hand") List<PlayableCard> hand) {
        this.hand = hand;
    }

    public List<PlayableCard> getHand() {
        return hand;
    }

    /**
     * Deserializer used to reload a SetHand message from a JSON file.
     */
    public static class Deserializer extends StdDeserializer<SetHand> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        @Serial
        private static final long serialVersionUID = 1541535148993634001L;

        protected Deserializer() {
            super(SetHand.class);
        }

        /**
         * Reloads a {@link SetHand} object from the given JSON.
         * @param p Parser used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link SetHand}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public SetHand deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);

            TypeReference<ArrayList<PlayableCard>> handTypeRef = new TypeReference<>() {};
            List<PlayableCard> hand = mapper.readValue(node.get("hand").toString(), handTypeRef);

            return new SetHand(
                    hand
            );
        }
    }
}
