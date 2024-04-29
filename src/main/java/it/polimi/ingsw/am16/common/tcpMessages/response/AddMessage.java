package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class AddMessage extends Payload {
    private final ChatMessage message;

    public AddMessage(ChatMessage message) {
        this.message = message;
    }
}
