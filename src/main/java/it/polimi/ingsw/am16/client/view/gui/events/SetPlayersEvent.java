package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;
import java.util.List;

public class SetPlayersEvent extends Event {

    @Serial
    private static final long serialVersionUID = -7611636345940518019L;

    private final List<String> usernames;

    public SetPlayersEvent(List<String> usernames) {
        super(GUIEventTypes.SET_PLAYERS_EVENT);
        this.usernames = usernames;
    }

    public List<String> getUsernames() {
        return usernames;
    }
}
