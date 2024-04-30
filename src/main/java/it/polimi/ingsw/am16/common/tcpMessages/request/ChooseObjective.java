package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class ChooseObjective extends Payload {
    private final ObjectiveCard objective;

    @JsonCreator
    public ChooseObjective(@JsonProperty("objective") ObjectiveCard objective) {
        this.objective = objective;
    }

    public ObjectiveCard getObjective() {
        return objective;
    }
}
