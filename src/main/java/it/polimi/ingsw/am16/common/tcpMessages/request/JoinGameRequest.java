package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class JoinGameRequest extends Payload {
    private final String gameId;
    private final String username;

    @JsonCreator
    public JoinGameRequest(@JsonProperty("gameId") String gameId, @JsonProperty("username") String username) {
        this.gameId = gameId;
        this.username = username;
    }

    public String getGameId() {
        return gameId;
    }

    public String getUsername() {
        return username;
    }
}
