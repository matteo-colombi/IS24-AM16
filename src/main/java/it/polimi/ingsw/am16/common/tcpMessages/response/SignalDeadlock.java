package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SignalDeadlock extends Payload {
    private final String username;

    public SignalDeadlock(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
