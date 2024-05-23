package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import javafx.event.Event;

import java.io.Serial;

public class SetCommonObjectivesEvent extends Event {

    @Serial
    private static final long serialVersionUID = -7057541720955694292L;

    private final ObjectiveCard[] commonObjectives;

    public SetCommonObjectivesEvent(ObjectiveCard[] commonObjectives) {
        super(GUIEventTypes.SET_COMMON_OBJECTIVES_EVENT);
        this.commonObjectives = commonObjectives;
    }

    public ObjectiveCard[] getCommonObjectives() {
        return commonObjectives;
    }
}
