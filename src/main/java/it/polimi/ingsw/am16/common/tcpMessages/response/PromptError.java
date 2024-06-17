package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.ErrorType;

/**
 * Message sent by the server to inform the client that an error has occurred.
 */
public class PromptError extends Payload {
    private final String errorMessage;
    public final ErrorType errorType;

    /**
     * @param errorMessage The error message to be displayed.
     * @param errorType The type of error that occured.
     */
    @JsonCreator
    public PromptError(@JsonProperty("errorMessage") String errorMessage, @JsonProperty("errorType") ErrorType errorType) {
        this.errorMessage = errorMessage;
        this.errorType = errorType;
    }

    /**
     * @return The error message to be displayed.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return The type of this error.
     */
    public ErrorType getErrorType() {
        return errorType;
    }
}
