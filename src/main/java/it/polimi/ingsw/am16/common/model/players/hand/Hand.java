package it.polimi.ingsw.am16.common.model.players.hand;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * @return the list of cards in the hand.
     */
    public List<PlayableCard> getCards() {
        return new ArrayList<>(this.cards);
    }

    /**
     * @param card The card to check.
     * @return Whether the given card is in this hand.
     */
    public boolean contains(PlayableCard card) {
        return this.cards.contains(card);
    }


    /**
     * Removes the given card from the Hand. This method does nothing if the given card is not present in the hand.
     *
     * @param card The card to remove.
     * @return
     */
    public boolean removeCard(PlayableCard card) {
        return this.cards.remove(card);
    }

    /**
     * Adds the given card to the Hand.
     * @param card The card to add.
     */
    public void addCard(PlayableCard card) {
        this.cards.add(card);
    }

    /**
     * @return A {@link List} of {@link RestrictedCard}, used to grant views limited access to the hand information.
     */
    @Override
    @JsonIgnore
    public List<RestrictedCard> getRestrictedVersion() {
        return cards.stream().map(PlayableCard::getRestrictedVersion).collect(Collectors.toList());
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
