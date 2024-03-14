package it.polimi.ingsw.am16.common.model.cards;

import it.polimi.ingsw.am16.common.model.PlayArea;

/**
 * Class used to model objective cards.
 */
public abstract class ObjectiveCard extends Card {
    private final int points;

    /**
     * Constructs a new objective card with the given numerical id, name, and that gives the specified amount of points.
     * @param id The card's numerical id.
     * @param name The card's name.
     * @param points The number of points given by this card according to the game's rules.
     */
    public ObjectiveCard(int id, String name, int points) {
        super(id, name);
        this.points = points;
    }

    /**
     * @return The points given by this card.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Calculates the amount of points given by this card based on the board state.
     * @param playArea The player's play area, containing the board state.
     * @return The points given by this card based on the board state.
     */
    public abstract int evaluatePoints(PlayArea playArea);
}
