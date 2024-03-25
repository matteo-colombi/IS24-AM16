package it.polimi.ingsw.am16.common.model.cards.decks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polimi.ingsw.am16.common.model.cards.Card;
import it.polimi.ingsw.am16.common.util.RNG;

import java.util.*;

/**
 * Class used to model Decks of the given card type.
 * @param <T> The Card type contained in this Deck.
 */
public abstract class Deck<T extends Card> {

    private final List<T> cards;

    /**
     * Construct a new empty deck.
     */
    public Deck() {
        this.cards = new ArrayList<>();
    }

    /**
     * Initializes the deck with all the cards of the given type.
     * Since this is an abstract method, it is up to the implementer to actually add all the right cards.
     */
    public abstract void initialize();

    /**
     * Draws and removes the first card from the deck.
     * @return The first card in the deck, or <code>null</code> if the deck is empty.
     */
    public T drawCard() {
        try{
            return cards.removeFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Gives the top card of the deck, without actually removing it.
     * @return The first card in the deck, or <code>null</code> if the deck is empty.
     */
    public T peekTop() {
        try {
            return cards.getFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Adds the given card to the bottom of the deck.
     * @param card
     */
    public void addCard(T card) {
        cards.add(card);
    }

    /**
     * Adds the given cards to the bottom of the deck, in the order they are given
     * @param newCards The cards to be added.
     */
    public void addCards(List<T> newCards) {
        cards.addAll(newCards);
    }

    /**
     * Randomly shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards, RNG.getRNG());
    }

    /**
     * @return The number of cards present in the deck.
     */
    @JsonIgnore
    public int getDeckSize() {
        return cards.size();
    }

    /**
     * Checks if the deck has run out of cards.
     * @return <code>true</code> if the deck is empty, <code>false</code> otherwise.
     */
    @JsonIgnore
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * @return The list of cards in the deck.
     */
    public List<T> getCards() {
        return cards;
    }

    /**
     * DOCME
     * @param cards
     */
    protected void setCards(List<T> cards) {
        this.cards.clear();
        this.cards.addAll(cards);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deck<?> deck = (Deck<?>) o;

        return cards.equals(deck.cards);
    }

    @Override
    public int hashCode() {
        return cards.hashCode();
    }
}
