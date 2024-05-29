package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to inform a client that the number of objective points of a player has changed.
 */
public class SetObjectivePoints extends Payload {
    private final String whosePoints;
    private final int objectivePoints;

    /**
     * @param whosePoints The username of the player whose game points are being given.
     * @param objectivePoints The given player's number of objective points.
     */
    @JsonCreator
    public SetObjectivePoints(@JsonProperty("whosePoints") String whosePoints, @JsonProperty("objectivePoints") int objectivePoints) {
        this.whosePoints = whosePoints;
        this.objectivePoints = objectivePoints;
    }

    /**
     * @return The username of the player whose game points are being given.
     */
    public String getWhosePoints() {
        return whosePoints;
    }

    /**
     * @return The given player's number of objective points.
     */
    public int getObjectivePoints() {
        return objectivePoints;
    }
}
