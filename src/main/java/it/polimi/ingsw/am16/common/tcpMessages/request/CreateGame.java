package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class CreateGame extends Payload {
    private final String username;
    private final int numPlayers;

    @JsonCreator
    public CreateGame(@JsonProperty("username") String username, @JsonProperty("numPlayers") int numPlayers) {
        this.username = username;
        this.numPlayers = numPlayers;
    }

    public String getUsername() {
        return username;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
