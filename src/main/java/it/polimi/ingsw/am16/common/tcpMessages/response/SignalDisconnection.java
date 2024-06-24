package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to inform the client that a player has disconnected.
 */
public class SignalDisconnection extends Payload {
    private final String whoDisconnected;

    /**
     * @param whoDisconnected The username of the player who disconnected.
     */
    @JsonCreator
    public SignalDisconnection(@JsonProperty("whoDisconnected") String whoDisconnected) {
        this.whoDisconnected = whoDisconnected;
    }

    /**
     * @return The username of the player who disconnected.
     */
    public String getWhoDisconnected() {
        return whoDisconnected;
    }
}
