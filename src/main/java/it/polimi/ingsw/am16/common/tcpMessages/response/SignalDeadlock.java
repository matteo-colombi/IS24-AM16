package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SignalDeadlock extends Payload {
    private final String username;

    @JsonCreator
    public SignalDeadlock(@JsonProperty("username") String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
