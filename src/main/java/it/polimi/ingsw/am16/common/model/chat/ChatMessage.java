package it.polimi.ingsw.am16.common.model.chat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * DOCME
 * @param senderUsername
 * @param receiverUsernames
 * @param text
 * @param timestamp
 */
public record ChatMessage(String senderUsername, Set<String> receiverUsernames, String text, Date timestamp) {

    /**
     * DOCME
     * @return
     */
    @Override
    public String toString() {
        return String.format("[%s] %s: %s", SimpleDateFormat.getTimeInstance().format(timestamp), senderUsername, text);
    }
}
