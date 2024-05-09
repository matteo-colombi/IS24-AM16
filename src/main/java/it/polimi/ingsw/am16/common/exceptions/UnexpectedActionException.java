package it.polimi.ingsw.am16.common.exceptions;

import java.io.Serial;

/**
 * Exception thrown by the model when commands reach it that are not permissible for some reason, specified in the message.
 */
public class UnexpectedActionException extends Exception {
    @Serial
    private static final long serialVersionUID = -1492224704445449030L;

    /**
     * Construct a new UnexpectedActionException with the given message.
     * @param message the error message.
     */
    public UnexpectedActionException(String message) {
        super(message);
    }
}
