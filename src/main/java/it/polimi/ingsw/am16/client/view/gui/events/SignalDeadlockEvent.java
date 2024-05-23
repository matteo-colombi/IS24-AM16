package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

public class SignalDeadlockEvent extends Event {

    @Serial
    private static final long serialVersionUID = 6513877544733219639L;

    private final String username;

    public SignalDeadlockEvent(String username) {
        super(GUIEventTypes.SIGNAL_DEADLOCK_EVENT);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
