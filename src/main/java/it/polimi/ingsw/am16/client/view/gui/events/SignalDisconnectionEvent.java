package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when a player disconnects from the current lobby.
 */
public class SignalDisconnectionEvent extends Event {

    @Serial
    private static final long serialVersionUID = -2641412573741062119L;

    private final String whoDisconnected;

    /**
     * @param whoDisconnected The username of the player who disconnected.
     */
    public SignalDisconnectionEvent(String whoDisconnected) {
        super(GUIEventTypes.SIGNAL_DISCONNECTION_EVENT);
        this.whoDisconnected = whoDisconnected;
    }

    /**
     * @return The username of the player who disconnected.
     */
    public String getWhoDisconnected() {
        return whoDisconnected;
    }
}
