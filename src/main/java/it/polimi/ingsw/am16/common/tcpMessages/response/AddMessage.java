package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to inform the client that a new message has arrived in their chat.
 */
public class AddMessage extends Payload {
    private final ChatMessage message;

    /**
     *
     * @param message The new message.
     */
    @JsonCreator
    public AddMessage(@JsonProperty("message") ChatMessage message) {
        this.message = message;
    }

    /**
     *
     * @return The new message.
     */
    public ChatMessage getMessage() {
        return message;
    }
}
