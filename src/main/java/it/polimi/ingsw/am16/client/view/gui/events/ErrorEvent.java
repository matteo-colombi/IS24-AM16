package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.util.ErrorType;
import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when an error occurs.
 */
public class ErrorEvent extends Event {
    @Serial
    private static final long serialVersionUID = -7822510836152498850L;

    private final String errorMsg;
    public final ErrorType errorType;

    /**
     * @param errorMsg The error message.
     */
    public ErrorEvent(String errorMsg, ErrorType errorType) {
        super(GUIEventTypes.ERROR_EVENT);
        this.errorMsg = errorMsg;
        this.errorType = errorType;
    }

    /**
     * @return The error message.
     */
    public String getErrorMsg() {
        return errorMsg;
    }
}
