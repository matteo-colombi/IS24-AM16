package it.polimi.ingsw.am16.common.model.players.hand;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;

import java.util.List;

/**
 * Interface for use by the controller and view to read a Hand's contents.
 */
public interface HandModel {

    /**
     * @return A {@link List} of {@link RestrictedCard}, used to grant views limited access to the hand information.
     */
    List<RestrictedCard> getRestrictedVersion();

    /**
     * @return the list of cards in the hand.
     */
    List<PlayableCard> getCards();
}
