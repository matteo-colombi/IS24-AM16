package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;

/**
 * Class used to model the card's that can be given to the player to play during the game.
 */
@JsonDeserialize(using = PlayableCard.Deserializer.class)
public abstract class PlayableCard extends BoardCard {

    /**
     * Constructs a new playable card with the given name, sides and of the given resource type.
     * @param name The card's name.
     * @param frontSide The card's front side.
     * @param backSide The card's back side.
     * @param type The card's resource type.
     */
    public PlayableCard(String name, CardSide frontSide, CardSide backSide, ResourceType type) {
        super(name, frontSide, backSide, type);
    }

    /**
     * Custom deserializer for {@link PlayableCard}. Used to deserialize these cards when they are already in the {@link CardRegistry}.
     */
    public static class Deserializer extends StdDeserializer<PlayableCard> {

        private static final ObjectMapper mapper = JsonMapper.INSTANCE.getObjectMapper();

        protected Deserializer() {
            super(PlayableCard.class);
        }

        /**
         * Deserializes a {@link PlayableCard} from the given JSON. This deserializer calls the deserializers for {@link ResourceCard} and {@link GoldCard}.
         * @param p Parsed used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link PlayableCard}.
         * @throws IOException Thrown if an exception occurs when reading from the input data, or if the given card does not conform to the naming standards.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public PlayableCard deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);
            String name = node.get("name").asText();
            if (name.contains("resource")) {
                return mapper.readValue(node.toString(), ResourceCard.class);
            } else if (name.contains("gold")){
                return mapper.readValue(node.toString(), GoldCard.class);
            }
            throw new IOException("Card name not valid: " + name);
        }
    }
}
