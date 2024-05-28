package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class JoinGameResponse extends Payload {
    private final String gameId;
    private final String username;
    private final int numPlayers;

    @JsonCreator
    public JoinGameResponse(@JsonProperty("gameId") String gameId, @JsonProperty("username") String username, @JsonProperty("numPlayers") int numPlayers) {
        this.gameId = gameId;
        this.username = username;
        this.numPlayers = numPlayers;
    }

    public String getGameId() {
        return gameId;
    }

    public String getUsername() {
        return username;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
