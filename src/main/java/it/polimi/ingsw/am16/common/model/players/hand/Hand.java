package it.polimi.ingsw.am16.common.model.players.hand;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCardType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;

import java.io.IOException;
import java.util.ArrayList;
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

    /**
     * Constructs a new hand and populates it with the given cards.
     * This constructor is private because it should only be used by {@link Hand.Deserializer}.
     * @param cards The cards to populate the hand with.
     */
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
     * DOCME
     * @param index
     * @return
     */
    @Override
    public ResourceType getResourceType(int index) {
        return getCard(index).getType();
    }

    /**
     * DOCME
     * @param index
     * @return
     */
    @Override
    public PlayableCardType getCardType(int index) {
        return getCard(index).getPlayableCardType();
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
     * Curstom deserializer for {@link Hand}. Used to reload entire hands from JSON files.
     */
    public static class Deserializer extends StdDeserializer<Hand> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        protected Deserializer() {
            super(Hand.class);
        }

        /**
         * Deserializes a {@link Hand}, loading all the cards back from the given JSON. This method assumes that the {@link CardRegistry} has already been initialized.
         * @param p Parsed used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link Hand}
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public Hand deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);
            PlayableCard[] cards = mapper.readValue(node.get("cards").toString(), PlayableCard[].class);
            return new Hand(cards);
        }
    }
}
