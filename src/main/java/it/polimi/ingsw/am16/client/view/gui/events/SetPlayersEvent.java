package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;
import java.util.List;

/**
 * Event that is fired in the GUI when the list of players currently in a lobby is being given.
 */
public class SetPlayersEvent extends Event {

    @Serial
    private static final long serialVersionUID = -7611636345940518019L;

    private final List<String> usernames;

    /**
     * @param usernames The list of usernames of the current players.
     */
    public SetPlayersEvent(List<String> usernames) {
        super(GUIEventTypes.SET_PLAYERS_EVENT);
        this.usernames = usernames;
    }

    /**
     * @return The list of usernames of the current players.
     */
    public List<String> getUsernames() {
        return usernames;
    }
}
