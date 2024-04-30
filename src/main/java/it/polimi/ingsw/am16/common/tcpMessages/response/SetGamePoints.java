package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SetGamePoints extends Payload {
    private final String whosePoints;
    private final int gamePoints;

    @JsonCreator
    public SetGamePoints(@JsonProperty("whosePoints") String whosePoints, @JsonProperty("gamePoints") int gamePoints) {
        this.whosePoints = whosePoints;
        this.gamePoints = gamePoints;
    }

    public String getWhosePoints() {
        return whosePoints;
    }

    public int getGamePoints() {
        return gamePoints;
    }
}
