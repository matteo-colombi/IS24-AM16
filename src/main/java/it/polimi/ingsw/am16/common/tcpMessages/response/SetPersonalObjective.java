package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SetPersonalObjective extends Payload {
    private final ObjectiveCard personalObjective;

    public SetPersonalObjective(ObjectiveCard personalObjective) {
        this.personalObjective = personalObjective;
    }
}
