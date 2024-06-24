package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to the client to notify the start of a player's turn.
 */
public class Turn extends Payload {
    private final String username;

    /**
     * @param username The username of the player whose turn it is to play.
     */
    @JsonCreator
    public Turn(@JsonProperty("username") String username) {
        this.username = username;
    }

    /**
     * @return The username of the player whose turn it is to play.
     */
    public String getUsername() {
        return username;
    }
}
