package it.polimi.ingsw.am16.common.model.players;

import it.polimi.ingsw.am16.common.model.cards.BoardCard;
import it.polimi.ingsw.am16.common.model.cards.CardSide;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.List;
import java.util.Map;

/**
 * DOCME: write documentation
 */
public interface PlayAreaModel {

    /**
     * DOCME: write documentation
     * @return
     */
    List<Position> getPlacementOrder();

    /**
     * DOCME: write documentation
     * @return
     */
    Map<Position, BoardCard> getField();

    /**
     * DOCME: write documentation
     * @return
     */
    Map<BoardCard, CardSide> getActiveSides();

    /**
     * DOCME: write documentation
     * @param playedCard
     * @param side
     * @param playedCardPosition
     * @return
     */
    boolean checkLegalMove(PlayableCard playedCard, SideType side, Position playedCardPosition);
}
