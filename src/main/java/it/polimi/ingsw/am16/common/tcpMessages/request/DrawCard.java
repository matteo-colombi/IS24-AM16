package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class DrawCard extends Payload {
    private final DrawType drawType;

    @JsonCreator
    public DrawCard(@JsonProperty("drawType") DrawType drawType) {
        this.drawType = drawType;
    }

    public DrawType getDrawType() {
        return drawType;
    }
}
