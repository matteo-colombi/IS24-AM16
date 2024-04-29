package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SetGameState extends Payload {
    private final GameState gameState;

    public SetGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
