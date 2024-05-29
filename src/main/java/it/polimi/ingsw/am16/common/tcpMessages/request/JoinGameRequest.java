package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the client to the server to join an existing game.
 */
public class JoinGameRequest extends Payload {
    private final String gameId;
    private final String username;

    /**
     *
     * @param gameId The id of the game that the player wants to join.
     * @param username The username with which the player wants to join the game.
     */
    @JsonCreator
    public JoinGameRequest(@JsonProperty("gameId") String gameId, @JsonProperty("username") String username) {
        this.gameId = gameId;
        this.username = username;
    }

    /**
     *
     * @return The id of the game that the player wants to join.
     */
    public String getGameId() {
        return gameId;
    }

    /**
     *
     * @return The username with which the player wants to join the game.
     */
    public String getUsername() {
        return username;
    }
}
