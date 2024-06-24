package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to signal the client that a player has deadlocked themselves, and thus their turn is skipped.
 */
public class SignalDeadlock extends Payload {
    private final String username;

    /**
     * @param username The username of the player who deadlocked himself.
     */
    @JsonCreator
    public SignalDeadlock(@JsonProperty("username") String username) {
        this.username = username;
    }

    /**
     * @return The username of the player who deadlocked himself.
     */
    public String getUsername() {
        return username;
    }

}
