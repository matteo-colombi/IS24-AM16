package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when a new player joins the current lobby.
 */
public class AddPlayerEvent extends Event {

    @Serial
    private static final long serialVersionUID = -4369342855624059046L;

    private final String username;

    /**
     * @param username The new player's username.
     */
    public AddPlayerEvent(String username) {
        super(GUIEventTypes.ADD_PLAYER_EVENT);
        this.username = username;
    }

    /**
     * @return The new player's username.
     */
    public String getUsername() {
        return username;
    }
}
