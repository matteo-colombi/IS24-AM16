package it.polimi.ingsw.am16.common.model.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Class used to handle the receiving and sending of chat messages.
 */
public class Chat implements ChatModel {

    private final String username;
    private final List<ChatMessage> messages;
    private ChatManager chatManager;

    /**
     * Creates a new chat for the player with the given username.
     * @param username The player's username.
     */
    public Chat(String username) {
        this.username = username;
        this.chatManager = null;
        messages = new ArrayList<>();
    }

    /**
     * Subscribes this chat to the given {@link ChatManager}. This allows it to receive messages from the chat manager.
     * This method overwrites the current chat manager if there is one.
     * @param chatManager The chat manager to subscribe this chat to.
     */
    public void subscribe(ChatManager chatManager) {
        this.chatManager = chatManager;
        this.chatManager.subscribe(username, this);
    }

    /**
     * Unsubscribes this chat from its chat manager. This method does nothing if the chat is not subscribed to any chat manager.
     */
    public void unsubscribe() {
        if (chatManager != null)
            chatManager.unsubscribe(username);
    }

    /**
     * Sends a new message to the given users.
     * This method does nothing if the chat is not subscribed to any manager.
     * @param text The message's body text.
     * @param receiverUsernames The users to send this message to.
     */
    @Override
    public void sendMessage(String text, Set<String> receiverUsernames) {
        if (chatManager != null)
            chatManager.sendMessage(username, text, receiverUsernames);
    }

    /**
     * Sends a new message to all the users subscribed to this chat's chat manager.
     * This method does nothing if the chat is not subscribed to any manager.
     * @param text The message's body text.
     */
    @Override
    public void sendMessage(String text) {
        if (chatManager != null)
            chatManager.sendMessage(username, text);
    }

    /**
     * Adds the given message to the chat's list of received messages.
     * @param message The message to add.
     */
    public void receiveMessage(ChatMessage message) {
        messages.add(message);
    }

    /**
     * @return The chat's list of received and sent messages.
     */
    @Override
    public List<ChatMessage> getMessages() {
        return List.copyOf(messages);
    }
}
