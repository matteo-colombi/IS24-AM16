package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when a new turn starts.
 */
public class TurnEvent extends Event {

    @Serial
    private static final long serialVersionUID = 6396455327876596882L;

    private final String username;

    /**
     * @param username The username of the player whose turn just started.
     */
    public TurnEvent(String username) {
        super(GUIEventTypes.TURN_EVENT);
        this.username = username;
    }

    /**
     * @return The username of the player whose turn just started.
     */
    public String getUsername() {
        return username;
    }
}
