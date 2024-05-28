package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;
import java.util.List;

/**
 * Event that is fired in the GUI when the server has established the order in which players will play.
 */
public class SetStartOrderEvent extends Event {

    @Serial
    private static final long serialVersionUID = -184384523633311585L;

    private final List<String> usernames;

    /**
     * @param usernames The usernames of the players, in the order in which they will play.
     */
    public SetStartOrderEvent(List<String> usernames) {
        super(GUIEventTypes.SET_START_ORDER_EVENT);
        this.usernames = usernames;
    }

    /**
     * @return The usernames of the players, in the order in which they will play.
     */
    public List<String> getUsernames() {
        return usernames;
    }
}
