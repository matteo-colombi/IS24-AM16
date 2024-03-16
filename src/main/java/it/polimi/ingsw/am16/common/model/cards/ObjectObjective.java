package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.players.PlayArea;

import java.util.Map;

/**
 * Class used to model objective cards that award points based on the objects visible on the player's board.
 */
public final class ObjectObjective extends ObjectiveCard {

    private final Map<ObjectType, Integer> objectsRequired;

    /**
     * Constructs a new objective card with the given name, that requires the specified objects in order to give points.
     * @param name The card's name.
     * @param points The points given by this card.
     * @param objectsRequired Map containing the amounts of each object required for this card to award points.
     */
    @JsonCreator
    public ObjectObjective(
            @JsonProperty("name") String name,
            @JsonProperty("points") int points,
            @JsonProperty("objectRequired") Map<ObjectType, Integer> objectsRequired) {
        super(name, points);
        this.objectsRequired = objectsRequired;
    }

    /**
     * Calculates the points given by this card based on the objects visible on the player's board.
     * @param playArea The player's play area, including the board state.
     * @return The points given by this card based on the objects visible on the player's board
     */
    @Override
    public int evaluatePoints(PlayArea playArea) {
        int multiplier = Integer.MAX_VALUE;
        for(ObjectType object : playArea.getObjectCounts().keySet()) {
            multiplier = Math.min(multiplier, playArea.getObjectCounts().get(object) / objectsRequired.get(object));
        }
        return getPoints()*multiplier;
    }

    @Override
    public String toString() {
        return "\nObjectObjective{" +
                "name=" + getName() + ", " +
                "points=" + getPoints() + ", " +
                "objectsRequired=" + objectsRequired +
                "}";
    }
}
