package it.polimi.ingsw.am16.common.model.cards;

import it.polimi.ingsw.am16.common.model.players.PlayArea;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class used to model objective cards that award points based on special patterns of cards present on the player's board.
 */
public final class PatternObjective extends ObjectiveCard {

    private final CardPattern pattern;

    /**
     * Constructs a new objective card with the given id and name, that gives the specified amount of points when the given pattern is present on the player's board.
     * @param id The card's numerical id.
     * @param name The card's name.
     * @param points The points given by this card.
     * @param pattern The pattern of cards required that has to be present on the board for this card to give points.
     */
    public PatternObjective(int id, String name, int points, CardPattern pattern) {
        super(id, name, points);
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
        for (int x = playArea.getMinX(); x < playArea.getMaxX(); x++) {
            for (int y = playArea.getMinY(); y < playArea.getMaxY(); y++) {
                if (checkPatternMatch(playArea.getField(), x, y, usedPositions)) {
                    foundPatterns++;
                }
            }
        }
        return foundPatterns*getPoints();
    }

    private boolean checkPatternMatch(Map<Position, BoardCard> field, int startX, int startY, Set<Position> usedPositions) {
        Set<Position> tempUsedPositions = new HashSet<>();
        Position currPos = new Position(startX, startY);
        for(int i = 0; i<pattern.types.length-1; i++) {
            //FIXME find a way to remove the instanceof and cast. Maybe we can add a special ResourceType.STARTER used for starter cards and put getType in BoardCard?
            //Downside to the ResourceType.STARTER: you can use it where you shouldn't and mess stuff up.
            if(usedPositions.contains(currPos))
                return false;
            if(field.get(currPos) instanceof StarterCard)
                return false;
            if(((PlayableCard)field.get(currPos)).getType() != pattern.types[i])
                return false;
            tempUsedPositions.add(currPos);
            if (i != pattern.types.length-1)
                currPos = currPos.addOffset(pattern.offsets[i]);
        }
        usedPositions.addAll(tempUsedPositions);
        return true;
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
        public CardPattern(ResourceType[] types, Position[] offsets) {
            this.types = types;
            this.offsets = offsets;
        }
    }
}
