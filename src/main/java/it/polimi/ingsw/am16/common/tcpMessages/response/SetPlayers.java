package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.tcpMessages.Payload;

import java.util.List;

public class SetPlayers extends Payload {
    private final List<String> usernames;

    public SetPlayers(List<String> usernames) {
        this.usernames = usernames;
    }
}
