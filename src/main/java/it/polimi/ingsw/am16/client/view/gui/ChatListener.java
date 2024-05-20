package it.polimi.ingsw.am16.client.view.gui;

import it.polimi.ingsw.am16.common.model.chat.ChatMessage;

import java.util.List;

public interface ChatListener {

    void receiveMessage(ChatMessage message);

    void receiveMessages(List<ChatMessage> messages);

}
