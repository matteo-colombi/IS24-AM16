package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to inform a client that the number of game points of a player has changed.
 */
public class SetGamePoints extends Payload {
    private final String whosePoints;
    private final int gamePoints;

    /**
     * @param whosePoints The username of the player whose game points are being given.
     * @param gamePoints The given player's number of game points.
     */
    @JsonCreator
    public SetGamePoints(@JsonProperty("whosePoints") String whosePoints, @JsonProperty("gamePoints") int gamePoints) {
        this.whosePoints = whosePoints;
        this.gamePoints = gamePoints;
    }

    /**
     * @return The username of the player whose game points are being given.
     */
    public String getWhosePoints() {
        return whosePoints;
    }

    /**
     * @return The given player's number of game points.
     */
    public int getGamePoints() {
        return gamePoints;
    }
}
