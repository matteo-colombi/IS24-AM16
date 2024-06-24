package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the client to the server to create a new game.
 */
public class CreateGame extends Payload {
    private final String username;
    private final int numPlayers;

    /**
     *
     * @param username The username with which the client wants to join the game.
     * @param numPlayers The expected number of players in the newly created game.
     */
    @JsonCreator
    public CreateGame(@JsonProperty("username") String username, @JsonProperty("numPlayers") int numPlayers) {
        this.username = username;
        this.numPlayers = numPlayers;
    }

    /**
     *
     * @return The username with which the client wants to join the game.
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return The expected number of players in the newly created game.
     */
    public int getNumPlayers() {
        return numPlayers;
    }
}
