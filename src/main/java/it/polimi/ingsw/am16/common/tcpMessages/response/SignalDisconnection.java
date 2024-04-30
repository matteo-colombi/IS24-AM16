package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SignalDisconnection extends Payload {
    private final String whoDisconnected;

    @JsonCreator
    public SignalDisconnection(@JsonProperty("whoDisconnected") String whoDisconnected) {
        this.whoDisconnected = whoDisconnected;
    }

    public String getWhoDisconnected() {
        return whoDisconnected;
    }
}
