package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.am16.common.model.players.PlayArea;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.util.Position;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class used to model objective cards that award points based on special patterns of cards present on the player's board.
 */
@JsonDeserialize(using = PatternObjective.Deserializer.class)
public final class PatternObjective extends ObjectiveCard {

    @Serial
    private static final long serialVersionUID = -8127300713970652154L;

    private final CardPattern pattern;

    /**
     * Constructs a new objective card with the given name, that gives the specified amount of points when the given pattern is present on the player's board.
     * @param name The card's name.
     * @param points The points given by this card.
     * @param pattern The pattern of cards required that has to be present on the board for this card to give points.
     */
    public PatternObjective(String name, int points, CardPattern pattern) {
        super(name, points);
        this.pattern = pattern;
    }

    @Override
    public int evaluatePoints(PlayArea playArea) {
        Set<Position> usedPositions = new HashSet<>();
        int foundPatterns = 0;

        for (int y = playArea.getMaxY(); y >= playArea.getMinY(); y--) {
            for (int x = playArea.getMinX(); x <= playArea.getMaxX(); x++) {
                if (checkPatternMatch(playArea.getField(), x, y, usedPositions)) {
                    foundPatterns++;
                }
            }
        }

        return foundPatterns*getPoints();
    }

    /**
     * Checks if the pattern of this card matches with the field, starting from the given coordinates.
     * @param field The player's field.
     * @param startX The starting x-coordinate.
     * @param startY The starting y-coordinate.
     * @param usedPositions Set of positions which cannot be reused in this pattern because they were already used for this objective. The new positions will be added if this pattern matches.
     * @return <code>true</code> if a match is found in the field at the given coordinates.
     */
    private boolean checkPatternMatch(Map<Position, BoardCard> field, int startX, int startY, Set<Position> usedPositions) {
        Set<Position> tempUsedPositions = new HashSet<>();
        Position currPos = new Position(startX, startY);

        for(int i = 0; i < pattern.types.length; i++) {
            if(usedPositions.contains(currPos))
                return false;

            if(!field.containsKey(currPos))
                return false;

            if(field.get(currPos).getType() != pattern.types[i])
                return false;

            tempUsedPositions.add(currPos);

            if (i != pattern.types.length-1)
                currPos = currPos.addOffset(pattern.offsets[i]);
        }

        usedPositions.addAll(tempUsedPositions);

        return true;
    }

    @Override
    public String toString() {
        return "PatternObjective{" +
                "name=" + getName() + ", " +
                "points=" + getPoints() + ", " +
                "pattern=" + pattern +
                "}";
    }

    /**
     * Class used to model card patterns used by {@link PatternObjective}.
     */
    public static class CardPattern implements Serializable {
        @Serial
        private static final long serialVersionUID = -2999566300628971026L;

        private final ResourceType[] types;
        private final Position[] offsets;

        /**
         * Constructs a new Pattern. For example, a rising diagonal pattern (like a /) of 3 FUNGI cards would have:
         * <br>
         * <code>types = {FUNGI, FUNGI, FUNGI}</code>
         * <br>
         * <code>offsets = {(1, -1),(1, -1)}</code>
         * @param types The card types present in this pattern.
         * @param offsets The offsets that specify the position jumps from one card to the next.
         */
        @JsonCreator
        public CardPattern(
                @JsonProperty("types") ResourceType[] types,
                @JsonProperty("offsets") Position[] offsets) {
            this.types = types;
            this.offsets = offsets;
        }

        @Override
        public String toString() {
            return "Pattern{" +
                    "types=" + Arrays.toString(types) + ", " +
                    "offsets=" + Arrays.toString(offsets) + "}";
        }
    }

    /**
     * Deserializer class for {@link PatternObjective}. Used to handle different deserialization logic based on whether the {@link CardRegistry} has already been initialized.
     */
    public static class Deserializer extends StdDeserializer<PatternObjective> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        @Serial
        private static final long serialVersionUID = -993192541415075995L;

        protected Deserializer() {
            super(PatternObjective.class);
        }

        /**
         * Deserializes the {@link PatternObjective}, returning the instance already present in the {@link CardRegistry} if it has already been initialized.
         * @param p Parser used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link PatternObjective}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public PatternObjective deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);
            String name = node.get("name").asText();
            if (CardRegistry.isInitialized()) {
                return (PatternObjective) CardRegistry.getRegistry().getObjectiveCardFromName(name);
            }
            int points = node.get("points").asInt();
            CardPattern pattern = mapper.readValue(node.get("pattern").toString(), CardPattern.class);
            return new PatternObjective(name, points, pattern);
        }
    }
}
