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
public record ChatMessage(String senderUsername, Set<String> receiverUsernames, String text, Date timestamp, boolean isPrivate) {

    /**
     * Formats the message in a user-friendly way, including the time when it was sent, the sender and the body.
     * @return The formatted message.
     */
    @Override
    public String toString() {
        return String.format("[%s] %s%s: %s",
                SimpleDateFormat.getTimeInstance().format(timestamp),
                senderUsername,
                isPrivate ? " (whispered)" : "",
                text
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMessage that = (ChatMessage) o;

        if (!senderUsername.equals(that.senderUsername)) return false;
        if (!receiverUsernames.equals(that.receiverUsernames)) return false;
        if (isPrivate != that.isPrivate) return false;
        if (!text.equals(that.text)) return false;
        return timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        int result = senderUsername.hashCode();
        result = 31 * result + receiverUsernames.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + timestamp.hashCode();
        return result;
    }
}
