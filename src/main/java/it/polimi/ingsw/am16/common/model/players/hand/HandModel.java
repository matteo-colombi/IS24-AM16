package it.polimi.ingsw.am16.common.model.players.hand;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;

public interface HandModel {
    /**
     *
     * @return the number of cards in hand
     */
    public int getSize();

    /**
     *
     * @param index index of the card in the list of cards representing the hand
     * @return the wanted card
     */
    public PlayableCard getCard(int index);
}
