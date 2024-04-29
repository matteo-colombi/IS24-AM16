package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class JoinGame extends Payload {
    private final String gameId;
    private final String username;

    public JoinGame(String gameId, String username) {
        this.gameId = gameId;
        this.username = username;
    }
}
