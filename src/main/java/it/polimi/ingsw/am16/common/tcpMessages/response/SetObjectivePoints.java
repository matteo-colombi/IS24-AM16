package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SetObjectivePoints extends Payload {
    private final String whosePoints;
    private final int objectivePoints;

    public SetObjectivePoints(String whosePoints, int objectivePoints) {
        this.whosePoints = whosePoints;
        this.objectivePoints = objectivePoints;
    }
}
