package it.polimi.ingsw.am16.common.model;

import it.polimi.ingsw.am16.common.model.cards.BoardCard;
import it.polimi.ingsw.am16.common.model.cards.CardSide;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.List;
import java.util.Map;

/**
 * TODO write documentation
 */
public interface PlayAreaModel {

    /**
     * TODO write documentation
     * @return
     */
    List<Position> getPlacementOrder();

    /**
     * TODO write documentation
     * @return
     */
    Map<Position, BoardCard> getField();

    /**
     * TODO write documentation
     * @return
     */
    Map<BoardCard, CardSide> getActiveSides();

    /**
     * TODO write documentation
     * @param playedCard
     * @param newCardPos
     * @param side
     * @return
     */
    boolean checkLegalMove(PlayableCard playedCard, Position newCardPos, SideType side);
}
