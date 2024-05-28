package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import javafx.event.Event;

import java.io.Serial;
import java.util.List;
import java.util.Map;

public class SetWinnersEvent extends Event {

    @Serial
    private static final long serialVersionUID = -2539754234054233423L;

    private final List<String> winnerUsernames;
    private final Map<String, ObjectiveCard> personalObjectives;

    public SetWinnersEvent(List<String> winnerUsernames, Map<String, ObjectiveCard> personalObjectives) {
        super(GUIEventTypes.SET_WINNERS_EVENT);
        this.winnerUsernames = winnerUsernames;
        this.personalObjectives = personalObjectives;
    }

    public List<String> getWinnerUsernames() {
        return winnerUsernames;
    }

    public Map<String, ObjectiveCard> getPersonalObjectives() {
        return personalObjectives;
    }
}
