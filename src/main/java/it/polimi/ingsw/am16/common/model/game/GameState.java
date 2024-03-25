package it.polimi.ingsw.am16.common.model.game;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum that includes the four game states.
 */
public enum GameState {
    @JsonProperty("joining")
    JOINING,

    @JsonProperty("init")
    INIT,

    @JsonProperty("started")
    STARTED,

    @JsonProperty("final_round")
    FINAL_ROUND,

    @JsonProperty("ended")
    ENDED
}
