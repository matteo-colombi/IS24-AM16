package it.polimi.ingsw.am16.common.model.chat;

import java.util.List;
import java.util.Set;

/**
 * Interface that defines the method used by the client to interact with the chat.
 */
public interface ChatModel {

    /**
     * Sends a new message to the given users.
     * This method does nothing if the chat is not subscribed to any manager.
     * @param text The message's body text.
     * @param receiverUsernames The users to send this message to.
     */
    void sendMessage(String text, Set<String> receiverUsernames);

    /**
     * Sends a new message to all the users subscribed to this chat's chat manager.
     * This method does nothing if the chat is not subscribed to any manager.
     * @param text The message's body text.
     */
    void sendMessage(String text);

    /**
     * @return The chat's list of received and sent messages.
     */
    List<ChatMessage> getMessages();
}
