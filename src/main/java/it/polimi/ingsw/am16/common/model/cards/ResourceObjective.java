package it.polimi.ingsw.am16.common.model.cards;

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
import it.polimi.ingsw.am16.common.model.players.PlayArea;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to model objective cards that award points based on the resources visible on the player's board.
 */
@JsonDeserialize(using = ResourceObjective.Deserializer.class)
public final class ResourceObjective extends ObjectiveCard {

    private final ResourceType type;
    private final int quantity;

    /**
     * Constructs a new objective card with the given name, that requires the specified resource and amount in order to give points.
     * @param name The card's name.
     * @param points The points given by this card.
     * @param type The resource type required by this card for it to give points.
     * @param quantity The quantity of the specified resource type required for the card to give points.
     */
    public ResourceObjective(String name,int points,ResourceType type,int quantity) {
        super(name, points);
        this.type = type;
        this.quantity = quantity;
    }

    /**
     * Calculates the points given by this card based on the resources visible on the player's board.
     * @param playArea The player's play area, including the board state.
     * @return The points given by this card based on the resources visible on the player's board.
     */
    @Override
    public int evaluatePoints(PlayArea playArea) {
        return getPoints()*(playArea.getResourceCounts().get(type)/quantity);
    }

    @Override
    public String toString() {
        return "ResourceObjective{" +
                "name=" + getName() + ", " +
                "points=" + getPoints() + ", " +
                "resourceType=" + type + ", " +
                "quantity=" + quantity +
                "}";
    }

    /**
     * DOCME
     */
    public static class Deserializer extends StdDeserializer<ResourceObjective> {

        private static final ObjectMapper mapper = JsonMapper.INSTANCE.getObjectMapper();

        protected Deserializer() {
            super(ResourceObjective.class);
        }

        /**
         * DOCME
         * @param p Parsed used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return
         * @throws IOException
         * @throws JacksonException
         */
        @Override
        public ResourceObjective deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);
            String name = node.get("name").asText();
            if (CardRegistry.isInitialized()) {
                return (ResourceObjective) CardRegistry.getObjectiveCardFromName(name);
            }
            int points = node.get("points").asInt();
            ResourceType type = mapper.readValue(node.get("resourceType").toString(), ResourceType.class);
            int quantity = node.get("quantity").asInt();
            return new ResourceObjective(name, points, type, quantity);
        }
    }
}
