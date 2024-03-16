package it.polimi.ingsw.am16.common.model.players.hand;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;

public interface HandModel {
    /**
     *
     * @return The number of cards in hand.
     */
    int getSize();

    /**
     *
     * @param index Index of the card in the list of cards representing the hand.
     * @return The card corresponding to the index
     */
    PlayableCard getCard(int index);
}
