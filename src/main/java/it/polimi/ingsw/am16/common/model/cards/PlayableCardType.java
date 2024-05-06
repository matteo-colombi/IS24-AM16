package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PlayableCardType {
    @JsonProperty("resource")
    RESOURCE,

    @JsonProperty("gold")
    GOLD
}
