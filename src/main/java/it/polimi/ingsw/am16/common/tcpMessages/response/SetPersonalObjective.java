package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SetPersonalObjective extends Payload {
    private final ObjectiveCard personalObjective;

    @JsonCreator
    public SetPersonalObjective(@JsonProperty("personalObjective") ObjectiveCard personalObjective) {
        this.personalObjective = personalObjective;
    }

    public ObjectiveCard getPersonalObjective() {
        return personalObjective;
    }
}
