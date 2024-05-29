package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the client to the server when the player wants to send a private message to another player.
 */
public class SendPrivateChatMessage extends Payload {
    private final String text;
    private final String receiver;

    /**
     *
     * @param text The message content.
     * @param receiver The username of the receiving player.
     */
    @JsonCreator
    public SendPrivateChatMessage(@JsonProperty("text") String text, @JsonProperty("receiver") String receiver) {
        this.text = text;
        this.receiver = receiver;
    }

    /**
     *
     * @return The message content.
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @return The username of the receiving player.
     */
    public String getReceiver() {
        return receiver;
    }
}
