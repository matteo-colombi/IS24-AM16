package it.polimi.ingsw.am16.common.model.chat;

import java.util.List;

/**
 * Interface that defines the method used by the client to interact with the chat.
 */
public interface ChatModel {
    /**
     * @return The chat's list of received and sent messages.
     */
    List<ChatMessage> getMessages();

    /**
     * Adds the given message to the chat's list of received messages.
     * @param message The message to add.
     */
    void receiveMessage(ChatMessage message);

}
