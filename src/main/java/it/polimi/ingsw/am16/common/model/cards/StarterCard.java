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
 * Class used to model starter cards.
 */
@JsonDeserialize(using = StarterCard.Deserializer.class)
public final class StarterCard extends BoardCard {
    /**
     * Constructs a new starter card with the given name and sides.
     * @param name The card's name
     * @param frontSide The card's front side.
     * @param backSide The card's back side.
     */
    public StarterCard(String name, CardSide frontSide, CardSide backSide, ResourceType type) {
        super(name, frontSide, backSide, type);
    }

    @Override
    public String toString() {
        return "StarterCard{" +
                "name=" + getName() + ", " +
                "frontSide=" + getFrontSide().getCorners() + ", " +
                "backSide={" + getBackSide().getPermanentResourcesGiven() + ", " + getBackSide().getCorners() + "}" +
                "}";
    }

    /**
     * Deserializer class for {@link StarterCard}. Used to handle different deserialization logic based on whether the {@link CardRegistry} has already been initialized.
     */
    static class Deserializer extends StdDeserializer<StarterCard> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        protected Deserializer() {
            super(StarterCard.class);
        }

        /**
         * Deserializes the {@link StarterCard}, returning the instance already present in the {@link CardRegistry} if it has already been initialized.
         * @param p Parsed used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link StarterCard}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public StarterCard deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);
            String name = node.get("name").asText();
            if (CardRegistry.isInitialized()) {
                return CardRegistry.getRegistry().getStarterCardFromName(name);
            }
            CardSide frontSide = mapper.readValue(node.get("frontSide").toString(), CardSide.class);
            CardSide backSide = mapper.readValue(node.get("backSide").toString(), CardSide.class);
            return new StarterCard(name, frontSide, backSide, null);
        }
    }
}
