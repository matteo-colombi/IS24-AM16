package it.polimi.ingsw.am16.common.model.chat;

import java.util.*;

/**
 * Class used to handle the distribution of messages in a game.
 */
public class ChatManager {

    private final Map<String, Chat> chats;

    /**
     * Creates a new empty chat manager.
     */
    public ChatManager() {
        chats = new HashMap<>();
    }

    /**
     * Adds a new user's chat to the manager. This allows the chat to receive new messages from this manager.
     * @param username The user's username.
     * @param chat The user's chat.
     */
    public void subscribe(String username, Chat chat) {
        chats.put(username, chat);
    }

    /**
     * Removes the user's chat from the manager. The user will no longer receive messages distributed by this manager.
     * @param username The user to remove.
     */
    public void unsubscribe(String username) {
        chats.remove(username);
    }

    /**
     * Sends a message to the users given.
     * @param senderUsername The sender's username.
     * @param text The text body of the message.
     * @param receiverUsernames The usernames to send the message to.
     */
    public void sendMessage(String senderUsername, String text, Set<String> receiverUsernames) {
        ChatMessage message = new ChatMessage(senderUsername, receiverUsernames, text, new Date());
        for(String username : receiverUsernames) {
            if (chats.containsKey(username))
                chats.get(username).receiveMessage(message);
        }
        if (!receiverUsernames.contains(senderUsername)) {
            chats.get(senderUsername).receiveMessage(message);
        }
    }

    /**
     * Sends a message to all users in this manager, including the user who created it.
     * @param senderUsername The sender's username.
     * @param text The text body of the message.
     */
    public void sendMessage(String senderUsername, String text) {
        sendMessage(senderUsername, text, chats.keySet());
    }

    /**
     * Sends a message from the server to all chats registered in this server.
     * @param text The text body of the message.
     */
    public void broadcast(String text) {
        sendMessage("Server", text);
    }
}
