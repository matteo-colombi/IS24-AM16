package it.polimi.ingsw.am16.common.model.chat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Record that holds all the information about a chat message.
 * @param senderUsername The user who sent the message.
 * @param receiverUsernames The users the message was sent to.
 * @param text The message's body text.
 * @param timestamp The timestamp when this message was sent.
 */
public record ChatMessage(String senderUsername, Set<String> receiverUsernames, String text, Date timestamp) {

    /**
     * Formats the message in a user-friendly way, including the time when it was sent, the sender and the body.
     * @return The formatted message.
     */
    @Override
    public String toString() {
        return String.format("[%s] %s: %s", SimpleDateFormat.getTimeInstance().format(timestamp), senderUsername, text);
    }
}
