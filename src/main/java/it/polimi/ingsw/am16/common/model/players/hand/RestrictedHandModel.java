package it.polimi.ingsw.am16.common.model.players.hand;

import it.polimi.ingsw.am16.common.model.cards.PlayableCardType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;

/**
 * DOCME
 */
public interface RestrictedHandModel {
    /**
     * @return the number of cards in hand.
     */
    int getSize();

    //DOCME
    /**
     *
     * @param index
     * @return
     */
    ResourceType getResourceType(int index);

    /**
     *
     * @param index
     * @return
     */
    PlayableCardType getCardType(int index);
}
