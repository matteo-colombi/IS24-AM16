package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

import java.util.List;

public class PromptObjectiveChoice extends Payload {
    private final List<ObjectiveCard> possiblePersonalObjectives;

    public PromptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) {
        this.possiblePersonalObjectives = possiblePersonalObjectives;
    }
}
