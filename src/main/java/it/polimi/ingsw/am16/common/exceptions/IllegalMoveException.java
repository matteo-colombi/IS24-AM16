package it.polimi.ingsw.am16.common.exceptions;

/**
 * Exception for handling illegal moves that reached the model
 */
public class IllegalMoveException extends Exception{
    /**
     * TODO
     * @param message error message for the exception to display
     */
    public IllegalMoveException(String message) {
        super(message);
    }
}
