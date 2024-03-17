package it.polimi.ingsw.am16.common.model.players.hand;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles a player's hand of resource and gold cards.
 */
public class Hand implements HandModel{
    private final List<PlayableCard> cards;

    /**
     * Creates a new hand, initializing the list of cards to a new ArrayList.
     */
    public Hand() {
        this.cards = new ArrayList<>();
    }

    /**
     *
     * @return The list of cards in the hand.
     */
    public List<PlayableCard> getCards() {
        return this.cards;
    }

    /**
     *
     * @return The number of cards in the hand.
     */
    @Override
    public int getSize(){
        return this.cards.size();
    }

    /**
     *
     * @param index Index of the card in the list of cards representing the hand
     * @return The card corresponding to the index
     */
    @Override
    public PlayableCard getCard(int index){
        return this.cards.get(index);
    }

    /**
     * TODO write doc
     * @param card
     */
    public void removeCard(PlayableCard card) {
        this.cards.remove(card);
    }

    /**
     * TODO write doc
     * @param card
     */
    public void addCard(PlayableCard card) {
        this.cards.add(card);
    }

    @Override
    public String toString() {
        return cards.toString();
    }

}
