package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the client to the server when the player wants to send a public chat message.
 */
public class SendChatMessage extends Payload {
    private final String text;

    /**
     *
     * @param text The message content.
     */
    @JsonCreator
    public SendChatMessage(@JsonProperty("text") String text) {
        this.text = text;
    }

    /**
     *
     * @return The message content.
     */
    public String getText() {
        return text;
    }
}
