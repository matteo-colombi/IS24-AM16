package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;

/**
 * Class used to model the cards that can be placed on the player's board.
 */
@JsonDeserialize(using = BoardCard.Deserializer.class, keyUsing = BoardCard.BoardCardKeyDeserializer.class)
public abstract class BoardCard extends Card {

    private final CardSide frontSide;
    private final CardSide backSide;
    private final ResourceType type;

    /**
     * Constructs a new card with the given name and sides.
     * @param name The card's name.
     * @param frontSide The card's front side.
     * @param backSide The card's back side.
     * @param type The card's type. Set to <code>null</code> for starter cards.
     */
    public BoardCard(String name, CardSide frontSide, CardSide backSide, ResourceType type) {
        super(name);
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.type = type;
    }

    /**
     * @return The card's front side.
     */
    @JsonIgnore
    public CardSide getFrontSide() {
        return frontSide;
    }

    /**
     * @return The card's back side.
     */
    @JsonIgnore
    public CardSide getBackSide() {
        return backSide;
    }

    /**
     * Returns the card's type.
     * @return The card's type. Returns <code>null</code> if this is a starter card.
     */
    @JsonIgnore
    public ResourceType getType() {
        return type;
    }

    /**
     * Returns the corresponding side of this card.
     * @param sideType The side to be returned.
     * @return the card's side corresponding to the <code>sideType</code> given.
     */
    public CardSide getCardSideBySideType(SideType sideType) {
        if (sideType == SideType.FRONT)
            return getFrontSide();

        return getBackSide();
    }

    /**
     * DOCME
     */
    public static class BoardCardSerializer extends JsonSerializer<BoardCard> {

        private static final ObjectMapper mapper = JsonMapper.INSTANCE.getObjectMapper();

        /**
         * DOCME
         * @param value Value to serialize; can <b>not</b> be null.
         * @param gen Generator used to output resulting Json content
         * @param serializers Provider that can be used to get serializers for
         *   serializing Objects value contains, if any.
         * @throws IOException
         */
        @Override
        public void serialize(BoardCard value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeFieldName(mapper.writeValueAsString(value));
        }
    }

    /**
     * DOCME
     */
    public static class Deserializer extends StdDeserializer<BoardCard> {

        private static final ObjectMapper mapper = JsonMapper.INSTANCE.getObjectMapper();

        protected Deserializer() {
            super(BoardCard.class);
        }

        /**
         * DOCME
         * @param p Parsed used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return
         * @throws IOException
         * @throws JacksonException
         */
        @Override
        public BoardCard deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);
            String name = node.get("name").asText();
            if (name.contains("starter")) {
                return mapper.readValue(node.toString(), StarterCard.class);
            }
            return mapper.readValue(node.toString(), PlayableCard.class);
        }
    }

    /**
     * DOCME
     */
    public static class BoardCardKeyDeserializer extends KeyDeserializer {

        private static final ObjectMapper mapper = JsonMapper.INSTANCE.getObjectMapper();

        /**
         * DOCME
         * @param key
         * @param ctxt
         * @return
         * @throws IOException
         */
        @Override
        public BoardCard deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            return mapper.readValue(key, BoardCard.class);
        }
    }
}
