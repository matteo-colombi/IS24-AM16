package it.polimi.ingsw.am16.common.model.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * DOCME
 */
public class Chat implements ChatModel {

    private final String username;
    private final List<ChatMessage> messages;
    private ChatManager chatManager;

    /**
     * DOCME
     * @param username
     */
    public Chat(String username) {
        this.username = username;
        this.chatManager = null;
        messages = new ArrayList<>();
    }

    /**
     * DOCME
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * DOCME
     * @param chatManager
     */
    @Override
    public void subscribe(ChatManager chatManager) {
        this.chatManager = chatManager;
        this.chatManager.subscribe(username, this);
    }

    /**
     * DOCME
     */
    @Override
    public void unsubscribe() {
        if (chatManager != null)
            chatManager.unsubscribe(username);
    }

    /**
     * DOCME
     * @param text
     * @param receiverUsernames
     */
    @Override
    public void sendMessage(String text, Set<String> receiverUsernames) {
        if (chatManager != null)
            chatManager.sendMessage(username, text, receiverUsernames);
    }

    /**
     * DOCME
     * @param text
     */
    @Override
    public void sendMessage(String text) {
        if (chatManager != null)
            chatManager.sendMessage(username, text);
    }

    /**
     * DOCME
     * @param message
     */
    public void receiveMessage(ChatMessage message) {
        messages.add(message);
    }

    /**
     * DOCME
     * @return
     */
    @Override
    public List<ChatMessage> getMessages() {
        return List.copyOf(messages);
    }
}
