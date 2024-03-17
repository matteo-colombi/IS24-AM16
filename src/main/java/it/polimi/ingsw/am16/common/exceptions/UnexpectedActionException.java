package it.polimi.ingsw.am16.common.exceptions;

/**
 * Exception thrown by the model when commands reach it that are not permissible for some reason, specified in the message.
 */
public class UnexpectedActionException extends Exception {
    /**
     * Construct a new UnexpectedActionException with the given message.
     * @param message the error message.
     */
    public UnexpectedActionException(String message) {
        super(message);
    }
}
