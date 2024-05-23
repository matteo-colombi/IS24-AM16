package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;
import java.util.List;

public class SetWinnersEvent extends Event {

    @Serial
    private static final long serialVersionUID = -2539754234054233423L;

    private final List<String> winnerUsernames;

    public SetWinnersEvent(List<String> winnerUsernames) {
        super(GUIEventTypes.SET_WINNERS_EVENT);
        this.winnerUsernames = winnerUsernames;
    }

    public List<String> getWinnerUsernames() {
        return winnerUsernames;
    }
}
