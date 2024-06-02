package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.ErrorType;

public class PromptError extends Payload {
    private final String errorMessage;
    public final ErrorType errorType;

    /**
     *
     * @param errorMessage The error message to be displayed
     * @param errorType The type of error that occured
     */
    @JsonCreator
    public PromptError(@JsonProperty("errorMessage") String errorMessage, ErrorType errorType) {
        this.errorMessage = errorMessage;
        this.errorType = errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
