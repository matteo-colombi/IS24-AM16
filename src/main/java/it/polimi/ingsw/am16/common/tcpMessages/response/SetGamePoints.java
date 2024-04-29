package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SetGamePoints extends Payload {
    private final String whosePoints;
    private final int gamePoints;

    public SetGamePoints(String whosePoints, int gamePoints) {
        this.whosePoints = whosePoints;
        this.gamePoints = gamePoints;
    }

}
