package it.polimi.ingsw.am16.common.model.players.hand;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles a player's hand of resource and gold cards.
 */

public class Hand implements HandModel{
    /**
     * The list of cards contained in a player's hand. There will always be 3 cards in this
     * list, exception made for the time period between placing a card on the board and
     * picking/drawing a new one, or after placing a card during the last turn, in which
     * replacing the newly placed card won't affect the game state anymore.
     */
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




}
