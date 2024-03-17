package it.polimi.ingsw.am16.common.model.players.hand;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;

/**
 * Interface for use by the controller and view to read a Hand's contents.
 */
public interface HandModel {
    /**
     * @return the number of cards in hand.
     */
    int getSize();

    /**
     *
     * @param index Index of the card in the list of cards representing the hand.
     * @return the card corresponding to the index.
     */
    PlayableCard getCard(int index);
}
