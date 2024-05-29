package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to inform the client that a new player has joined their lobby.
 */
public class AddPlayer extends Payload {
    private final String username;

    /**
     *
     * @param player The new player's username.
     */
    @JsonCreator
    public AddPlayer(@JsonProperty("player") String player) {
        this.username = player;
    }

    /**
     *
     * @return The new player's username.
     */
    public String getUsername() {
        return username;
    }
}
