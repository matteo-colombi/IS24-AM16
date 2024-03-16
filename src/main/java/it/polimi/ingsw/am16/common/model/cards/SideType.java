package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TODO write doc
 */
public enum SideType {
    @JsonProperty("front")
    FRONT,

    @JsonProperty("back")
    BACK
}
