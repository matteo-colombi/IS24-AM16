package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * Class used to model resource cards.
 */
@JsonDeserialize(using = ResourceCard.Deserializer.class)
public final class ResourceCard extends PlayableCard {

    /**
     * Constructs a new resource card with the given name, sides and resource type.
     * @param name The card's name.
     * @param frontSide The card's front side.
     * @param backSide The card's back side.
     * @param type The card's resource type.
     */
    public ResourceCard(String name, CardSide frontSide, CardSide backSide, ResourceType type) {
        super(name, frontSide, backSide, type);
    }

    @Override
    public String toString() {
        return "ResourceCard{" +
                "name="+getName() + ", " +
                "frontSide="+getFrontSide() + ", " +
                "backSide="+getBackSide() + ", " +
                "type="+getType() +
                "}";
    }

    /**
     * Deserializer class for {@link ResourceCard}. Used to handle different deserialization logic based on whether the {@link CardRegistry} has already been initialized.
     */
    static class Deserializer extends StdDeserializer<ResourceCard> {

        private static final ObjectMapper mapper = JsonMapper.INSTANCE.getObjectMapper();

        protected Deserializer() {
            super(ResourceCard.class);
        }

        /**
         * Deserializes the {@link ResourceCard}, returning the instance already present in the {@link CardRegistry} if it has already been initialized.
         * @param p Parsed used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link ResourceCard}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public ResourceCard deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);
            String name = node.get("name").asText();
            if (CardRegistry.isInitialized()) {
                return CardRegistry.getResourceCardFromName(name);
            }
            CardSide frontSide = mapper.readValue(node.get("frontSide").toString(), CardSide.class);
            CardSide backSide = mapper.readValue(node.get("backSide").toString(), CardSide.class);
            ResourceType type = mapper.readValue(node.get("type").toString(), ResourceType.class);
            return new ResourceCard(name, frontSide, backSide, type);
        }
    }
}
