package it.polimi.ingsw.am16.common.model.chat;

import java.util.*;

public class ChatManager {

    private final Map<String, Chat> chats;

    public ChatManager() {
        chats = new HashMap<>();
    }

    public void subscribe(String username, Chat chat) {
        chats.put(username, chat);
    }

    public void unsubscribe(String username) {
        chats.remove(username);
    }

    public void sendMessage(String senderUsername, String text, Set<String> receiverUsernames) {
        ChatMessage message = new ChatMessage(senderUsername, receiverUsernames, text, new Date());
        for(String username : receiverUsernames) {
            if (chats.containsKey(username))
                chats.get(username).receiveMessage(message);
        }
    }

    public void sendMessage(String senderUsername, String text) {
        ChatMessage message = new ChatMessage(senderUsername, chats.keySet(), text, new Date());
        for(String username : chats.keySet()) {
            if (chats.containsKey(username))
                chats.get(username).receiveMessage(message);
        }
    }

    public void broadcast(String text) {
        sendMessage("Server", text);
    }
}
