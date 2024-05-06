package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class AddPlayer extends Payload {
    private final String username;

    @JsonCreator
    public AddPlayer(@JsonProperty("player") String player) {
        this.username = player;
    }

    public String getUsername() {
        return username;
    }
}
