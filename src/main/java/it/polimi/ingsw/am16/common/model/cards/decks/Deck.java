package it.polimi.ingsw.am16.common.model.cards.decks;

import it.polimi.ingsw.am16.common.model.cards.Card;

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
     * Adds the given card to the deck.
     * @param card
     */
    public void addCard(T card) {
        cards.add(card);
    }

    /**
     * Randomly shuffles the deck.
     */
    public void shuffle() {
        //TODO maybe add a centralized RNG instead of using a new Random every time it's needed.
        Random r = new Random();
        Collections.shuffle(cards, r);
    }

    /**
     * @return The number of cards present in the deck.
     */
    public int getDeckSize() {
        return cards.size();
    }

    /**
     * @return The list of cards in the deck.
     */
    public List<T> getCards() {
        return cards;
    }
}
