package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

public class TurnEvent extends Event {

    @Serial
    private static final long serialVersionUID = 6396455327876596882L;

    private final String username;

    public TurnEvent(String username) {
        super(GUIEventTypes.TURN_EVENT);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
