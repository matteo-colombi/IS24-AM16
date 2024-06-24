package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum that contains the possible types of {@link PlayableCard}.
 */
public enum PlayableCardType {
    @JsonProperty("resource")
    RESOURCE,

    @JsonProperty("gold")
    GOLD
}
