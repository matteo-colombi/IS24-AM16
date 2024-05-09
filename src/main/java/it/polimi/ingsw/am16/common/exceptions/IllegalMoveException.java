package it.polimi.ingsw.am16.common.exceptions;

import java.io.Serial;

/**
 * Exception for handling illegal moves that reached the model.
 */
public class IllegalMoveException extends Exception{
    @Serial
    private static final long serialVersionUID = -19243296401499023L;

    /**
     * Construct a new IllegalMoveException with the given message.
     * @param message the error message.
     */
    public IllegalMoveException(String message) {
        super(message);
    }
}
