package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to inform the client that an error has occurred.
 */
public class PromptError extends Payload {
    private final String errorMessage;

    /**
     * @param errorMessage A message describing the error.
     */
    @JsonCreator
    public PromptError(@JsonProperty("errorMessage") String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return A message describing the error.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
