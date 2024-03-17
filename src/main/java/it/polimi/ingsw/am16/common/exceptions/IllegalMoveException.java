package it.polimi.ingsw.am16.common.exceptions;

/**
 * Exception for handling illegal moves that reached the model.
 */
public class IllegalMoveException extends Exception{
    /**
     * Construct a new IllegalMoveException with the given message.
     * @param message the error message.
     */
    public IllegalMoveException(String message) {
        super(message);
    }
}
