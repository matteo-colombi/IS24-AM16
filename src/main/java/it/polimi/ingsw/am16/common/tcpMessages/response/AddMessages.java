package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@JsonDeserialize(using = AddMessages.Deserializer.class)
public class AddMessages extends Payload {
    private final List<ChatMessage> messages;

    @JsonCreator
    public AddMessages(@JsonProperty("messages") List<ChatMessage> messages) {
        this.messages = messages;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    /**
     * Deserializer used to reload a AddMessages message from a JSON file.
     */
    public static class Deserializer extends StdDeserializer<AddMessages> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        @Serial
        private static final long serialVersionUID = 5704199573489871246L;

        protected Deserializer() {
            super(AddMessages.class);
        }

        /**
         * Reloads a {@link AddMessages} object from the given JSON.
         * @param p Parser used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link AddMessages}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public AddMessages deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);

            TypeReference<ArrayList<ChatMessage>> messagesTypeRef = new TypeReference<>() {};
            List<ChatMessage> messages = mapper.readValue(node.get("messages").toString(), messagesTypeRef);

            return new AddMessages(
                    messages
            );
        }
    }
}
