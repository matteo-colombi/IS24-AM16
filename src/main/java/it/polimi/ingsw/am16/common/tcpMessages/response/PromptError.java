package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class PromptError extends Payload {
    private final String errorMessage;

    public PromptError(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
