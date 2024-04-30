package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SignalDisconnection extends Payload {
    private final String whoDisconnected;

    public SignalDisconnection(String whoDisconnected) {
        this.whoDisconnected = whoDisconnected;
    }

    public String getWhoDisconnected() {
        return whoDisconnected;
    }
}
