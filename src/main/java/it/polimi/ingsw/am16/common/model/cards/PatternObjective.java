package it.polimi.ingsw.am16.common.model.cards;

import it.polimi.ingsw.am16.common.model.PlayArea;
import it.polimi.ingsw.am16.common.util.Position;

/**
 * Class used to model objective cards that award points based on special patterns of cards present on the player's board.
 */
public final class PatternObjective extends ObjectiveCard {

    private final Pattern pattern;

    /**
     * Constructs a new objective card with the given id and name, that gives the specified amount of points when the given pattern is present on the player's board.
     * @param id The card's numerical id.
     * @param name The card's name.
     * @param points The points given by this card.
     * @param pattern The pattern of cards required that has to be present on the board for this card to give points.
     */
    public PatternObjective(int id, String name, int points, Pattern pattern) {
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
        //TODO implement this when PlayArea has the necessary functions.
        return 0;
    }

    /**
     * Class used to model card patterns.
     */
    public static class Pattern {
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
        public Pattern(ResourceType[] types, Position[] offsets) {
            this.types = types;
            this.offsets = offsets;
        }
    }
}
