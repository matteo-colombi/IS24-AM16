package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

public class ErrorEvent extends Event {
    @Serial
    private static final long serialVersionUID = -7822510836152498850L;

    private final String errorMsg;

    public ErrorEvent(String errorMsg) {
        super(GUIEventTypes.ERROR_EVENT);
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
