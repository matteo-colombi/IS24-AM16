package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when a player disconnects from a game, and the game is being suspended as a result.
 */
public class SignalGameSuspensionEvent extends Event {

    @Serial
    private static final long serialVersionUID = -8978718796191812284L;

    private final String whoDisconnected;

    /**
     * @param whoDisconnected The username of the player who disconnected.
     */
    public SignalGameSuspensionEvent(String whoDisconnected) {
        super(GUIEventTypes.SIGNAL_GAME_SUSPENSION_EVENT);
        this.whoDisconnected = whoDisconnected;
    }

    /**
     * @return The username of the player who disconnected.
     */
    public String getWhoDisconnected() {
        return whoDisconnected;
    }
}
