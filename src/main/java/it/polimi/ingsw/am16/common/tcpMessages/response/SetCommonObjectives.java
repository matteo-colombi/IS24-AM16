package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SetCommonObjectives extends Payload {
    private final ObjectiveCard[] commonObjectives;

    @JsonCreator
    public SetCommonObjectives(@JsonProperty("commonObjectives") ObjectiveCard[] commonObjectives) {
        this.commonObjectives = commonObjectives;
    }

    public ObjectiveCard[] getCommonObjectives() {
        return commonObjectives;
    }
}
