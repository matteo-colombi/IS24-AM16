package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

public class SignalGameSuspensionEvent extends Event {

    @Serial
    private static final long serialVersionUID = -8978718796191812284L;

    private final String whoDisconnected;

    public SignalGameSuspensionEvent(String whoDisconnected) {
        super(GUIEventTypes.SIGNAL_GAME_SUSPENSION_EVENT);
        this.whoDisconnected = whoDisconnected;
    }

    public String getWhoDisconnected() {
        return whoDisconnected;
    }
}
