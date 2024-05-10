package it.polimi.ingsw.am16.server.controller;

import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.chat.ChatModel;
import it.polimi.ingsw.am16.server.VirtualView;

import java.util.*;

/**
 * Class used to handle the distribution of messages in a game.
 */
public class ChatController {

    private final Map<String, ChatModel> chats;
    private final VirtualView virtualView;

    /**
     * Creates a new empty chat manager.
     */
    public ChatController(VirtualView virtualView) {
        chats = new HashMap<>();
        this.virtualView = virtualView;
    }

    /**
     * Adds a new user's chat to the manager. This allows the chat to receive new messages from this manager.
     *
     * @param username The user's username.
     * @param chat     The user's chat.
     */
    public void subscribe(String username, ChatModel chat) {
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
    public void sendMessage(String senderUsername, String text, Set<String> receiverUsernames, boolean isPrivate) {
        boolean rick = text.equals("rick");
        ChatMessage message = new ChatMessage(senderUsername, receiverUsernames, text, new Date(), isPrivate);
        for(String username : receiverUsernames) {
            if (!username.equals(senderUsername) && chats.containsKey(username)) {
                if (!rick) chats.get(username).receiveMessage(message);

                virtualView.communicateNewMessage(username, message);
                virtualView.redrawView(username);
            }
        }
        if (chats.containsKey(senderUsername) && !rick) {
            chats.get(senderUsername).receiveMessage(message);
            virtualView.communicateNewMessage(senderUsername, message);
            virtualView.redrawView(senderUsername);
        }
    }

    /**
     * Sends a message to all users in this manager, including the user who created it.
     * @param senderUsername The sender's username.
     * @param text The text body of the message.
     */
    public void sendMessage(String senderUsername, String text) {
        sendMessage(senderUsername, text, new HashSet<>(chats.keySet()), false);
    }

    /**
     * Sends a message from the server to all chats registered in this server.
     * @param text The text body of the message.
     */
    public void broadcast(String text) {
        sendMessage("Server", text);
    }

    /**
     * DOCME
     */
    public void clear() {
        chats.clear();
    }
}
