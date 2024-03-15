package it.polimi.ingsw.am16.common.model;

import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * //TODO write documentation
 */
public class PlayArea implements PlayAreaModel {
    //TODO implement everything
    private final Player player;
    private int cardCount;
    private final Map<ResourceType, Integer> resourceCounts;
    private final Map<ObjectType, Integer> objectCounts;
    private final List<Position> cardPlacementOrder;
    private final Map<Position, BoardCard> field;
    private final Map<BoardCard, CardSide> activeSides;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    public PlayArea(Player player) {
        this.player = player;
        this.cardCount = 0;
        this.resourceCounts = new HashMap<>();
        this.objectCounts = new HashMap<>();
        this.cardPlacementOrder = new ArrayList<>();
        this.field = new HashMap<>();
        this.activeSides = new HashMap<>();
        this.minX = 0;
        this.maxX = 0;
        this.minY = 0;
        this.maxY = 0;
    }

    //region Local Getter and Setter

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public Map<ResourceType, Integer> getResourceCounts() {
        return resourceCounts;
    }

    public Map<ObjectType, Integer> getObjectCounts() {
        return objectCounts;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    //endregion

    //region Local Methods

    /**
     * TODO write documentation
     *
     * @param starterCard
     * @param side
     */
    public void setStarterCard(StarterCard starterCard, SideType side){

    }

    /**
     * TODO write documentation
     *
     * @param playedCard
     * @param newCardPosition
     */
    public void playCard(PlayableCard playedCard, Position newCardPosition, SideType side) {
    }

    /**
     * TODO write documentation
     *
     * @param playedCard
     * @param newCardPosition
     */
    public void updateCounts(PlayableCard playedCard, Position newCardPosition) {

    }

    //endregion

    //region PlayAreaModel

    /**
     * TODO write documentation
     *
     * @return
     */
    @Override
    public List<Position> getPlacementOrder() {
        //TODO implement this
        return null;
    }

    /**
     * TODO write documentation
     *
     * @return
     */
    @Override
    public Map<Position, BoardCard> getField() {
        //TODO implement this
        return null;
    }

    /**
     * TODO write documentation
     *
     * @return
     */
    @Override
    public Map<BoardCard, CardSide> getActiveSides() {
        //TODO implement this
        return null;
    }

    /**
     * TODO write documentation
     *
     * @param playedCard
     * @param newCardPos
     * @param side
     * @return
     */
    @Override
    public boolean checkLegalMove(PlayableCard playedCard, Position newCardPos, SideType side) {
        //TODO implement this
        return false;
    }

    //endregion

}
