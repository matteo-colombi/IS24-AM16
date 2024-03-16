package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class used to model resource cards.
 */
public final class ResourceCard extends PlayableCard {

    /**
     * Constructs a new resource card with the given name, sides and resource type.
     * @param name The card's name.
     * @param frontSide The card's front side.
     * @param backSide The card's back side.
     * @param type The card's resource type.
     */
    @JsonCreator
    public ResourceCard(
            @JsonProperty("name") String name,
            @JsonProperty("frontSide") CardSide frontSide,
            @JsonProperty("backSide") CardSide backSide,
            @JsonProperty("type") ResourceType type) {
        super(name, frontSide, backSide, type);
    }

    @Override
    public String toString() {
        return "ResourceCard{" +
                "name="+getName() + ", " +
                "frontSide="+getFrontSide() + ", " +
                "backSide="+getBackSide() + ", " +
                "type="+getType() +
                "}\n";
    }
}
