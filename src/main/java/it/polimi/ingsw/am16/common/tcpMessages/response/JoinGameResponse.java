package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to inform the client that their request to join a game was successful, together with information on the joined game.
 */
public class JoinGameResponse extends Payload {
    private final String gameId;
    private final String username;
    private final int numPlayers;

    /**
     *
     * @param gameId The join game's id.
     * @param username The username with which the player joined the game.
     * @param numPlayers The number of players expected to join the game.
     */
    @JsonCreator
    public JoinGameResponse(@JsonProperty("gameId") String gameId, @JsonProperty("username") String username, @JsonProperty("numPlayers") int numPlayers) {
        this.gameId = gameId;
        this.username = username;
        this.numPlayers = numPlayers;
    }

    public String getGameId() {
        return gameId;
    }

    public String getUsername() {
        return username;
    }

    public int getNumPlayers() {
        return numPlayers;
    }
}
