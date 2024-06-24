package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to inform the client that the state of the game has changed.
 */
public class SetGameState extends Payload {

    private final GameState gameState;

    /**
     *
     * @param gameState The new game state.
     */
    @JsonCreator
    public SetGameState(@JsonProperty("gameState") GameState gameState) {
        this.gameState = gameState;
    }

    /**
     * @return The new game state.
     */
    public GameState getGameState() {
        return gameState;
    }
}
