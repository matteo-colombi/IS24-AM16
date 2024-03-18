package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.players.PlayArea;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class used to model objective cards that award points based on special patterns of cards present on the player's board.
 */
public final class PatternObjective extends ObjectiveCard {

    private final CardPattern pattern;

    /**
     * Constructs a new objective card with the given name, that gives the specified amount of points when the given pattern is present on the player's board.
     * @param name The card's name.
     * @param points The points given by this card.
     * @param pattern The pattern of cards required that has to be present on the board for this card to give points.
     */
    @JsonCreator
    public PatternObjective(
            @JsonProperty("name") String name,
            @JsonProperty("points") int points,
            @JsonProperty("pattern") CardPattern pattern) {
        super(name, points);
        this.pattern = pattern;
    }

    /**
     * Calculates the points given by this card based on the player's board state.
     * @param playArea The player's play area, including the board state.
     * @return The number of points given by this card based on the patterns present on the board.
     */
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
    public static class CardPattern {
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
}
