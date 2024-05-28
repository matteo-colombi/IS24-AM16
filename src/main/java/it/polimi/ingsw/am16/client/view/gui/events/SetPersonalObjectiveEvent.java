package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when the player's personal objective is set.
 */
public class SetPersonalObjectiveEvent extends Event {

    @Serial
    private static final long serialVersionUID = 8735521703188172935L;

    private final ObjectiveCard personalObjective;

    /**
     * @param personalObjective The player's personal objective.
     */
    public SetPersonalObjectiveEvent(ObjectiveCard personalObjective) {
        super(GUIEventTypes.SET_PERSONAL_OBJECTIVE_EVENT);
        this.personalObjective = personalObjective;
    }

    /**
     * @return The player's personal objective.
     */
    public ObjectiveCard getPersonalObjective() {
        return personalObjective;
    }
}
