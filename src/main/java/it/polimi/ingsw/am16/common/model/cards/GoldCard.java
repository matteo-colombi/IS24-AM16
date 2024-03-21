package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class used to model gold cards.
 */
public final class GoldCard extends PlayableCard {

    /**
     * Constructs a new gold card with the given name, sides and resource type.
     * @param name The card's name.
     * @param frontSide The card's front side.
     * @param backSide The card's back side.
     * @param type The card's resource type.
     */
    @JsonCreator
    public GoldCard(
            @JsonProperty("name") String name,
            @JsonProperty("frontSide") CardSide frontSide,
            @JsonProperty("backSide") CardSide backSide,
            @JsonProperty("type") ResourceType type) {
        super(name, frontSide, backSide, type);
    }

    @Override
    public String toString() {
        return "GoldCard{" +
                "name="+getName() + ", " +
                "frontSide="+getFrontSide() + ", " +
                "backSide="+getBackSide() + ", " +
                "type="+getType() +
                "}";
    }
}
