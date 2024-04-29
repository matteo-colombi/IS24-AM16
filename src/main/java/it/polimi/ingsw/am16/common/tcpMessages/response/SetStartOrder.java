package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.tcpMessages.Payload;

import java.util.List;

public class SetStartOrder extends Payload {
    private final List<String> usernames;

    public SetStartOrder(List<String> usernames) {
        this.usernames = usernames;
    }
}
