package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when a player has deadlocked themselves, and thus their turn is being skipped.
 */
public class SignalDeadlockEvent extends Event {

    @Serial
    private static final long serialVersionUID = 6513877544733219639L;

    private final String username;

    /**
     * @param username The username of the player who has deadlocked themselves.
     */
    public SignalDeadlockEvent(String username) {
        super(GUIEventTypes.SIGNAL_DEADLOCK_EVENT);
        this.username = username;
    }

    /**
     * @return The username of the player who has deadlocked themselves.
     */
    public String getUsername() {
        return username;
    }
}
