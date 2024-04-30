package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SetObjectivePoints extends Payload {
    private final String whosePoints;
    private final int objectivePoints;

    @JsonCreator
    public SetObjectivePoints(@JsonProperty("whosePoints") String whosePoints, @JsonProperty("objectivePoints") int objectivePoints) {
        this.whosePoints = whosePoints;
        this.objectivePoints = objectivePoints;
    }

    public String getWhosePoints() {
        return whosePoints;
    }

    public int getObjectivePoints() {
        return objectivePoints;
    }
}
