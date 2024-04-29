package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class Turn extends Payload {
    private final String username;

    public Turn(String username) {
        this.username = username;
    }
}
