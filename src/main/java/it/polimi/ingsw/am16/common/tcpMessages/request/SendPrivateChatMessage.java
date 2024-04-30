package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SendPrivateChatMessage extends Payload {
    private final String text;
    private final String receiver;

    @JsonCreator
    public SendPrivateChatMessage(@JsonProperty("text") String text, @JsonProperty("receiver") String receiver) {
        this.text = text;
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public String getReceiver() {
        return receiver;
    }
}
