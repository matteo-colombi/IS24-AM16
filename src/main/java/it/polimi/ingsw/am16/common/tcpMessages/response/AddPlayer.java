package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class AddPlayer extends Payload {
    private final String username;

    public AddPlayer(String player) {
        this.username = player;
    }
}
