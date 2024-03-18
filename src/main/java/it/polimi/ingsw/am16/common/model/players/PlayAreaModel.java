package it.polimi.ingsw.am16.common.model.players;

import it.polimi.ingsw.am16.common.model.cards.BoardCard;
import it.polimi.ingsw.am16.common.model.cards.CardSide;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.List;
import java.util.Map;

/**
 * Interface to give external classes access to PlayArea-type objects. It contains all the methods
 * a play area is supposed to show to the outside
 */
public interface PlayAreaModel {

    /**
     * @return The placement order of the cards
     */
    List<Position> getPlacementOrder();

    /**
     * @return The player's field
     */
    Map<Position, BoardCard> getField();

    /**
     * @return The visible side of the cards
     */
    Map<BoardCard, CardSide> getActiveSides();

    /**
     * Checks whether a move is legal or not.
     *
     * @param playedCard         The card chosen by the player.
     * @param side               The visible side of the card.
     * @param playedCardPosition The position chosen by the player to place the card.
     * @return True if the move is legal otherwise returns False.
     */
    boolean checkLegalMove(PlayableCard playedCard, SideType side, Position playedCardPosition);
}
