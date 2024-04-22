package it.polimi.ingsw.am16.common.model.chat;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.server.controller.ChatController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to handle the receiving and sending of chat messages.
 */
@JsonDeserialize(using = Chat.Deserializer.class)
public class Chat implements ChatModel {

    private final int playerId;
    private final String username;
    private final List<ChatMessage> messages;
    private ChatController chatController;

    /**
     * Creates a new chat for the player with the given username.
     * @param username The player's username.
     */
    public Chat(int playerId, String username) {
        this.playerId = playerId;
        this.username = username;
        this.chatController = null;
        messages = new ArrayList<>();
    }

    /**
     * Reconstructs a chat from the given attributes. Used for JSON deserializing.
     * @param username The username of the player who this chat belongs to.
     * @param messages The messages present in this chat.
     */
    private Chat(
            int playerId,
            String username,
            List<ChatMessage> messages) {
        this.playerId = playerId;
        this.username = username;
        this.messages = messages;
        this.chatController = null;
    }

    /**
     * @return The player who this chat belongs to.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return The player's id.
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Subscribes this chat to the given {@link ChatController}. This allows it to receive messages from the chat manager.
     * This method overwrites the current chat manager if there is one.
     * @param chatController The chat manager to subscribe this chat to.
     */
    public void subscribe(ChatController chatController) {
        this.chatController = chatController;
        this.chatController.subscribe(playerId, username, this);
    }

    /**
     * Unsubscribes this chat from its chat manager. This method does nothing if the chat is not subscribed to any chat manager.
     */
    public void unsubscribe() {
        if (chatController != null)
            chatController.unsubscribe(username);
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

    /**
     * Deserializer used to reload a {@link Chat} object from JSON.
     */
    public static class Deserializer extends StdDeserializer<Chat> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        public Deserializer() {
            super(Chat.class);
        }

        /**
         * Reloads a {@link Chat} object from the given JSON.
         * @param p Parsed used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link Chat}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public Chat deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);

            int playerId = node.get("playerId").asInt();
            String username = node.get("username").asText();

            TypeReference<ArrayList<ChatMessage>> messagesTypeRef = new TypeReference<>() {};
            List<ChatMessage> messages = mapper.readValue(node.get("messages").toString(), messagesTypeRef);

            return new Chat(playerId, username, messages);
        }
    }
}
