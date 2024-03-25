package it.polimi.ingsw.am16.common.model.players.hand;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles a player's hand of resource and gold cards.
 */
@JsonDeserialize(using = Hand.Deserializer.class)
public class Hand implements HandModel{
    private final List<PlayableCard> cards;

    /**
     * Creates a new hand, initializing the list of cards to a new ArrayList.
     */
    public Hand() {
        this.cards = new ArrayList<>();
    }

    private Hand(PlayableCard[] cards) {
        this.cards = new ArrayList<>(List.of(cards));
    }

    /**
     *
     * @return the list of cards in the hand.
     */
    public List<PlayableCard> getCards() {
        return this.cards;
    }

    /**
     *
     * @return the number of cards in the hand.
     */
    @Override
    @JsonIgnore
    public int getSize(){
        return this.cards.size();
    }

    /**
     *
     * @param index Index of the card in the list of cards representing the hand.
     * @return the card corresponding to the index.
     */
    @Override
    public PlayableCard getCard(int index){
        return this.cards.get(index);
    }

    /**
     * Removes the given card from the Hand. This method does nothing if the given card is not present in the hand.
     * @param card The card to remove.
     */
    public void removeCard(PlayableCard card) {
        this.cards.remove(card);
    }

    /**
     * Adds the given card to the Hand.
     * @param card The card to add.
     */
    public void addCard(PlayableCard card) {
        this.cards.add(card);
    }

    @Override
    public String toString() {
        return cards.toString();
    }

    /**
     * DOCME
     */
    public static class Deserializer extends StdDeserializer<Hand> {

        private static final ObjectMapper mapper = JsonMapper.INSTANCE.getObjectMapper();

        protected Deserializer() {
            super(Hand.class);
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
        public Hand deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);
            PlayableCard[] cards = mapper.readValue(node.get("cards").toString(), PlayableCard[].class);
            return new Hand(cards);
        }
    }
}
