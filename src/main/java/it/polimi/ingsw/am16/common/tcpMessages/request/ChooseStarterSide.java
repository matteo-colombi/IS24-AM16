package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class ChooseStarterSide extends Payload {
    private final SideType side;

    @JsonCreator
    public ChooseStarterSide(@JsonProperty("side") SideType side) {
        this.side = side;
    }

    public SideType getSide() {
        return side;
    }
}
