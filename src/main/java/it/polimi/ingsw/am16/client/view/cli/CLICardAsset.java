package it.polimi.ingsw.am16.client.view.cli;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.SideType;

public record CLICardAsset(@JsonProperty("front") CLIText front, @JsonProperty("back") CLIText back) {

    public CLIText getSide(SideType sideType) {
        if (sideType == SideType.FRONT)
            return front;
        return back;
    }

    @Override
    public String toString() {
        return "CLIAsset{" +
                "front=" + front +
                ", back=" + back +
                '}';
    }
}
