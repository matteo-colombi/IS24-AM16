package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

import java.util.List;

public class AddMessages extends Payload {
    private final List<ChatMessage> messages;

    public AddMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
