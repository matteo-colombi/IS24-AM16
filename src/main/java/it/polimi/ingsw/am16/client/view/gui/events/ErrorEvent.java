package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when an error occurs.
 */
public class ErrorEvent extends Event {
    @Serial
    private static final long serialVersionUID = -7822510836152498850L;

    private final String errorMsg;

    /**
     * @param errorMsg The error message.
     */
    public ErrorEvent(String errorMsg) {
        super(GUIEventTypes.ERROR_EVENT);
        this.errorMsg = errorMsg;
    }

    /**
     * @return The error message.
     */
    public String getErrorMsg() {
        return errorMsg;
    }
}
