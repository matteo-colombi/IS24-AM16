package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import javafx.event.Event;

import java.io.Serial;
import java.util.List;

public class AddChatMessagesEvent extends Event {

    @Serial
    private static final long serialVersionUID = -7262158996823452636L;

    private final List<ChatMessage> messages;

    public AddChatMessagesEvent(List<ChatMessage> messages) {
        super(GUIEventTypes.ADD_CHAT_MESSAGES_EVENT);
        this.messages = messages;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }
}
