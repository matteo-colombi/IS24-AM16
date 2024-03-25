package it.polimi.ingsw.am16.common.model.players;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum that contains the player's color options.
 */
public enum PlayerColor {
    @JsonProperty("red")
    RED,

    @JsonProperty("green")
    GREEN,

    @JsonProperty("blue")
    BLUE,

    @JsonProperty("yellow")
    YELLOW
}
