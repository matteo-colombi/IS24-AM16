package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.am16.common.model.players.PlayArea;

/**
 * Class used to model objective cards.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PatternObjective.class, name = "pattern"),
        @JsonSubTypes.Type(value = ResourceObjective.class, name = "resource"),
        @JsonSubTypes.Type(value = ObjectObjective.class, name = "object")
})
public abstract class ObjectiveCard extends Card {
    private final int points;

    /**
     * Constructs a new objective card with the given numerical id, name, and that gives the specified amount of points.
     * @param name The card's name.
     * @param points The number of points given by this card according to the game's rules.
     */
    public ObjectiveCard(String name, int points) {
        super(name);
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
