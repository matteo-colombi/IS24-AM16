package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

public class AddPlayerEvent extends Event {

    @Serial
    private static final long serialVersionUID = -4369342855624059046L;

    private final String username;

    public AddPlayerEvent(String username) {
        super(GUIEventTypes.ADD_PLAYER_EVENT);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
