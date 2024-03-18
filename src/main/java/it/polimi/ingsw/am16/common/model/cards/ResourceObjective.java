package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.players.PlayArea;

/**
 * Class used to model objective cards that award points based on the resources visible on the player's board.
 */
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
    @JsonCreator
    public ResourceObjective(
            @JsonProperty("name") String name,
            @JsonProperty("points") int points,
            @JsonProperty("resourceType") ResourceType type,
            @JsonProperty("quantity") int quantity) {
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
}
