package it.polimi.ingsw.am16.common.model.game;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum that includes the five game states.
 */
public enum GameState {
    /**
     * Game state indicating that the lobby is still waiting for players to join. This is used when a new game is created and when games are reloaded from JSON files.
     */
    @JsonProperty("joining")
    JOINING,

    /**
     * Game state indicating that the game is in its initialization phase: the players are choosing their starter card side, color, objective card, and the cards are being distributed.
     */
    @JsonProperty("init")
    INIT,

    /**
     * Game state indicating that the actual game has started.
     */
    @JsonProperty("started")
    STARTED,

    /**
     * Game state indicating that the game is about to end, and the final round is currently being played.
     */
    @JsonProperty("final_round")
    FINAL_ROUND,

    /**
     * Game state indicating that the game has ended. When players leave the game, it will be deleted.
     */
    @JsonProperty("ended")
    ENDED
}
