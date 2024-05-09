package it.polimi.ingsw.am16.common.exceptions;

import java.io.Serial;

/**
 * Exception to handle a client's request to play their starter card when they have not received one yet.
 */
public class NoStarterCardException extends Exception{
    @Serial
    private static final long serialVersionUID = 271780966084729763L;

    /**
     * Construct a new NoStarterCardException with the given message.
     * @param message the error message.
     */
    public NoStarterCardException(String message) {
        super(message);
    }
}
