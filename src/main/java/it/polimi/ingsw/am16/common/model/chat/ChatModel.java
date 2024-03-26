package it.polimi.ingsw.am16.common.model.chat;

import java.util.List;
import java.util.Set;

/**
 * DOCME
 */
public interface ChatModel {

    /**
     * DOCME
     * @param text
     * @param receiverUsernames
     */
    void sendMessage(String text, Set<String> receiverUsernames);

    /**
     * DOCME
     * @param text
     */
    void sendMessage(String text);

    /**
     * DOCME
     * @return
     */
    List<ChatMessage> getMessages();

    /**
     * DOCME
     */
    void unsubscribe();

    /**
     * DOCME
     * @param chatManager
     */
    void subscribe(ChatManager chatManager);

}
