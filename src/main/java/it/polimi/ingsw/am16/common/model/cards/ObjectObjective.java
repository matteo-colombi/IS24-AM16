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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to model objective cards that award points based on the objects visible on the player's board.
 */
@JsonDeserialize(using = ObjectObjective.Deserializer.class)
public final class ObjectObjective extends ObjectiveCard {

    private final Map<ObjectType, Integer> objectsRequired;

    /**
     * Constructs a new objective card with the given name, that requires the specified objects in order to give points.
     * @param name The card's name.
     * @param points The points given by this card.
     * @param objectsRequired Map containing the amounts of each object required for this card to award points.
     */
    public ObjectObjective(String name, int points, Map<ObjectType, Integer> objectsRequired) {
        super(name, points);
        objectsRequired.putIfAbsent(ObjectType.INKWELL, 0);
        objectsRequired.putIfAbsent(ObjectType.MANUSCRIPT, 0);
        objectsRequired.putIfAbsent(ObjectType.QUILL, 0);
        this.objectsRequired = Collections.unmodifiableMap(objectsRequired);
    }

    /**
     * DOCME
     * @param name
     * @return
     */

    /**
     * Calculates the points given by this card based on the objects visible on the player's board.
     * @param playArea The player's play area, including the board state.
     * @return The points given by this card based on the objects visible on the player's board
     */
    @Override
    public int evaluatePoints(PlayArea playArea) {
        int multiplier = Integer.MAX_VALUE;
        for(ObjectType object : playArea.getObjectCounts().keySet()) {
            if (objectsRequired.get(object) != 0)
                multiplier = Math.min(multiplier, playArea.getObjectCounts().get(object) / objectsRequired.get(object));
        }
        return getPoints()*multiplier;
    }

    @Override
    public String toString() {
        return "ObjectObjective{" +
                "name=" + getName() + ", " +
                "points=" + getPoints() + ", " +
                "objectsRequired=" + objectsRequired +
                "}";
    }

    /**
     * DOCME
     */
    public static class Deserializer extends StdDeserializer<ObjectObjective> {

        private static final ObjectMapper mapper = new ObjectMapper();

        protected Deserializer() {
            super(ObjectObjective.class);
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
        public ObjectObjective deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);
            String name = node.get("name").toString();
            if (CardRegistry.isInitialized()) {
                return (ObjectObjective) CardRegistry.getObjectiveCardFromName(name);
            }
            int points = node.get("points").asInt();
            TypeReference<HashMap<ObjectType,Integer>> typeRef = new TypeReference<>() {};
            Map<ObjectType, Integer> objectsRequired = mapper.readValue(node.get("objectRequired").toString(), typeRef);
            return new ObjectObjective(name, points, objectsRequired);
        }
    }
}
