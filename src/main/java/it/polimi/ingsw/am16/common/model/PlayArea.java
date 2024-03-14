package it.polimi.ingsw.am16.common.model;

import it.polimi.ingsw.am16.common.model.cards.BoardCard;
import it.polimi.ingsw.am16.common.model.cards.CardSide;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.List;
import java.util.Map;

/**
 * //TODO write documentation
 */
public class PlayArea implements PlayAreaModel {
    //TODO implement everything


    /**
     * TODO write documentation
     * @return
     */
    @Override
    public List<Position> getPlacementOrder() {
        //TODO implement this
        return null;
    }

    /**
     * TODO write documentation
     * @return
     */
    @Override
    public Map<Position, BoardCard> getField() {
        //TODO implement this
        return null;
    }

    /**
     * TODO write documentation
     * @return
     */
    @Override
    public Map<BoardCard, CardSide> getActiveSides() {
        //TODO implement this
        return null;
    }

    /**
     * TODO write documentation
     * @param playedCard
     * @param newCardPos
     * @return
     */
    @Override
    public boolean checkLegalMove(PlayableCard playedCard, Position newCardPos) {
        //TODO implement this
        return false;
    }
}
