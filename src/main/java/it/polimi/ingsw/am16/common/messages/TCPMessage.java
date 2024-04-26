package it.polimi.ingsw.am16.common.messages;

import com.fasterxml.jackson.annotation.JsonCreator;

public abstract class TCPMessage {

    private MessageType messageType;
    private Payload payload;

    @JsonCreator
    public TCPMessage(MessageType messageType, Payload payload) {
        this.messageType = messageType;
        this.payload = payload;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Payload getPayload() {
        return payload;
    }
}
