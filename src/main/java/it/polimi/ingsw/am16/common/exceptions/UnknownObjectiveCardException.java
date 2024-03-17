package it.polimi.ingsw.am16.common.exceptions;

/**
 * Exception thrown by Player when setting an Objective Card that is not in the options given.
 */
public class UnknownObjectiveCardException extends Exception {
    /**
     * Construct a new UnknownObjectiveCardException with the given message,
     * @param message the error message.
     */
    public UnknownObjectiveCardException(String message) {
        super(message);
    }
}
