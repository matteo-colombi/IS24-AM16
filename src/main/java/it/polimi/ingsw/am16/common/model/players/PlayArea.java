package it.polimi.ingsw.am16.common.model.players;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.model.cards.*;
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

    private CardSide getCardSide(BoardCard card, SideType side) {
        if (side == SideType.FRONT)
            return card.getFrontSide();

        return card.getBackSide();
    }

    /**
     * TODO write documentation
     *
     * @param starterCard
     * @param side
     */
    public void setStarterCard(StarterCard starterCard, SideType side) {
        Position starterPosition = new Position(0, 0);

        updateField(starterCard, side, starterPosition);
    }

    /**
     * TODO write documentation
     *
     * @param playedCard
     * @param side
     * @param newCardPosition
     */
    public void playCard(PlayableCard playedCard, SideType side, Position newCardPosition) throws IllegalMoveException {
        if (!checkLegalMove(playedCard, side, newCardPosition))
            throw new IllegalMoveException("Illegal move");

        updateField(playedCard, side, newCardPosition);
        updateCounts(playedCard, side, newCardPosition);
        updateBounds(newCardPosition);
    }

    /**
     * TODO write documentation
     *
     * @param playedCard
     * @param side
     * @param newCardPosition
     */
    private void updateField(BoardCard playedCard, SideType side, Position newCardPosition) {
        cardCount++;

        cardPlacementOrder.add(newCardPosition);
        field.put(newCardPosition, playedCard);
        activeSides.put(playedCard, getCardSide(playedCard, side));
    }

    /**
     * TODO write documentation
     *
     * @param playedCard
     * @param side
     * @param newCardPosition
     */
    public void updateCounts(PlayableCard playedCard, SideType side, Position newCardPosition) {
        CardSide activeSide = activeSides.get(playedCard);

        // FIXME
        /*
        for (Cornerable corner : activeSide.getCorners().values()) {
            switch (corner) {
                case ResourceType.ANIMAL:
                case ResourceType.PLANT:
                case ResourceType.INSECT:
                case ResourceType.FUNGI: {
                    resourceCounts.merge((ResourceType) corner, 1, Integer::sum);
                    break;
                }
                case ObjectType.INKWELL:
                case ObjectType.MANUSCRIPT:
                case ObjectType.QUILL: {
                    objectCounts.merge((ObjectType) corner, 1, Integer::sum);
                    break;
                }
                default: {
                    break;
                }
            }
        }
         */

        //TODO implementare la parte relativa agli angoli coperti dalla nuova carta
    }

    /**
     * TODO write documentation
     *
     * @param newCardPosition
     */
    private void updateBounds(Position newCardPosition) {
        minX = Math.min(minX, newCardPosition.x());
        maxX = Math.max(maxX, newCardPosition.x());
        minY = Math.min(minY, newCardPosition.y());
        maxY = Math.max(maxY, newCardPosition.y());
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
        return List.copyOf(cardPlacementOrder);
    }

    /**
     * TODO write documentation
     *
     * @return
     */
    @Override
    public Map<Position, BoardCard> getField() {
        return Map.copyOf(field);
    }

    /**
     * TODO write documentation
     *
     * @return
     */
    @Override
    public Map<BoardCard, CardSide> getActiveSides() {
        return Map.copyOf(activeSides);
    }

    /**
     * TODO write documentation
     *
     * @param playedCard
     * @param side
     * @param newCardPosition
     * @return
     */
    @Override
    public boolean checkLegalMove(PlayableCard playedCard, SideType side, Position newCardPosition) {
        //TODO implement this
        return true;
    }

    //endregion

}
