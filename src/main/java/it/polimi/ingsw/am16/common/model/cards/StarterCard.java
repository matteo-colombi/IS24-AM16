package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class used to model starter cards.
 */
public final class StarterCard extends BoardCard {
    /**
     * Constructs a new starter card with the given name and sides.
     * @param name The card's name
     * @param frontSide The card's front side.
     * @param backSide The card's back side.
     */
    @JsonCreator
    public StarterCard(
            @JsonProperty("name") String name,
            @JsonProperty("frontSide") CardSide frontSide,
            @JsonProperty("backSide") CardSide backSide,
            @JsonProperty("type") ResourceType type) {
        super(name, frontSide, backSide, type);
    }

    @Override
    public String toString() {
        return "StarterCard{" +
                "name=" + getName() + ", " +
                "frontSide=" + getFrontSide().getCorners() + ", " +
                "backSide={" + getBackSide().getPermanentResourcesGiven() + ", " + getBackSide().getCorners() + "}, " +
                "}";
    }
}
