package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import javafx.event.Event;

import java.io.Serial;
import java.util.List;

public class PromptObjectiveChoiceEvent extends Event {

    @Serial
    private static final long serialVersionUID = -7873542994544268223L;

    private final List<ObjectiveCard> possiblePersonalObjectives;

    public PromptObjectiveChoiceEvent(List<ObjectiveCard> possiblePersonalObjectives) {
        super(GUIEventTypes.PROMPT_OBJECTIVE_CHOICE_EVENT);
        this.possiblePersonalObjectives = possiblePersonalObjectives;
    }

    public List<ObjectiveCard> getPossiblePersonalObjectives() {
        return possiblePersonalObjectives;
    }
}
