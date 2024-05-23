package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

public class SignalDisconnectionEvent extends Event {

    @Serial
    private static final long serialVersionUID = -2641412573741062119L;

    private final String whoDisconnected;

    public SignalDisconnectionEvent(String whoDisconnected) {
        super(GUIEventTypes.SIGNAL_DISCONNECTION_EVENT);
        this.whoDisconnected = whoDisconnected;
    }

    public String getWhoDisconnected() {
        return whoDisconnected;
    }
}
