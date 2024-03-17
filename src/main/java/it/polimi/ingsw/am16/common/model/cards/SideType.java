package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum that includes the types of sides of a card.
 */
public enum SideType {
    @JsonProperty("front")
    FRONT,

    @JsonProperty("back")
    BACK
}
