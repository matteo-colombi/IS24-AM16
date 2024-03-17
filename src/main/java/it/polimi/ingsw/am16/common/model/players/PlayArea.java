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
    private final Player player;
    private int cardCount;
    private final Map<CornerType, Integer> resourceAndObjectCounts;
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
        this.resourceAndObjectCounts = new HashMap<>();
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

    public Map<ResourceType, Integer> getResourceCounts() {
        Map<ResourceType, Integer> resourceCounts = new HashMap<>();

        for (ResourceType resource : ResourceType.values()) {
            CornerType mappedCorner = resource.mappedCorner();
            Integer counter = resourceAndObjectCounts.get(mappedCorner);

            resourceCounts.put(resource, counter);
        }

        return resourceCounts;
    }

    public Map<ObjectType, Integer> getObjectCounts() {
        Map<ObjectType, Integer> objectCounts = new HashMap<>();

        for (ObjectType object : ObjectType.values()) {
            CornerType mappedCorner = object.mappedCorner();
            Integer counter = resourceAndObjectCounts.get(mappedCorner);

            objectCounts.put(object, counter);
        }

        return objectCounts;
    }

    public int getMinX() {
        return minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxY() {
        return maxY;
    }

    //endregion

    //region Local Methods

    /**
     * TODO write documentation
     *
     * @param starterCard
     * @param side
     */
    public void setStarterCard(StarterCard starterCard, SideType side) {
        Position starterPosition = new Position(0, 0);

        if (field.containsKey(starterPosition))
            return;

        updateField(starterCard, side, starterPosition);
        updateCounts(starterCard, side, starterPosition);
    }

    /**
     * TODO write documentation
     *
     * @param playedCard
     * @param side
     * @param playedCardPosition
     */
    public void playCard(PlayableCard playedCard, SideType side, Position playedCardPosition) throws IllegalMoveException {
        if (!checkLegalMove(playedCard, side, playedCardPosition))
            throw new IllegalMoveException("Illegal move");

        updateField(playedCard, side, playedCardPosition);
        updateCounts(playedCard, side, playedCardPosition);
        updateBounds(playedCardPosition);
    }

    /**
     * TODO write documentation
     *
     * @param playedCard
     * @param side
     * @param playedCardPosition
     */
    private void updateField(BoardCard playedCard, SideType side, Position playedCardPosition) {
        cardCount++;

        cardPlacementOrder.add(playedCardPosition);
        field.put(playedCardPosition, playedCard);
        activeSides.put(playedCard, playedCard.getCardSideBySideType(side));
    }

    /**
     * TODO write documentation
     *
     * @param playedCard
     * @param side
     * @param playedCardPosition
     */
    private void updateCounts(BoardCard playedCard, SideType side, Position playedCardPosition) {
        CardSide activeSide = activeSides.get(playedCard);

        // increases the resources and objects that are in the corners of the new card
        for (CornerType corner : activeSide.getCorners().values()) {
            if (corner == CornerType.BLOCKED || corner == CornerType.EMPTY)
                continue;

            resourceAndObjectCounts.merge(corner, 1, Integer::sum);
        }

        // increases the resources that are permanent in the new card
        for (ResourceType resources : activeSide.getPermanentResourcesGiven().keySet()) {
            CornerType mappedCorner = resources.mappedCorner();

            resourceAndObjectCounts.merge(mappedCorner, 1, Integer::sum);
        }

        // decrements the resources and objects that have been hidden by the new card
        for (Position neighbourPosition : playedCardPosition.getNeighbours()) {
            if (!field.containsKey(neighbourPosition))
                continue;

            BoardCard neighbourCard = field.get(neighbourPosition);
            CardSide neighbourCardSide = activeSides.get(neighbourCard);
            Map<CornersIdx, CornerType> corners = neighbourCardSide.getCorners();
            CornerType corner = null;

            if (playedCardPosition.isTopLeft(neighbourPosition)) {
                corner = corners.get(CornersIdx.BOTTOM_RIGHT);
            } else if (playedCardPosition.isTopRight(neighbourPosition)) {
                corner = corners.get(CornersIdx.BOTTOM_LEFT);
            } else if (playedCardPosition.isBottomRight(neighbourPosition)) {
                corner = corners.get(CornersIdx.TOP_LEFT);
            } else if (playedCardPosition.isBottomLeft(neighbourPosition)) {
                corner = corners.get(CornersIdx.TOP_RIGHT);
            }

            // sono sicuro che diventi un tipo tra quelli disponibili ma non si sa mai
            assert corner != null;

            if (corner == CornerType.BLOCKED || corner == CornerType.EMPTY)
                continue;

            resourceAndObjectCounts.merge(corner, -1, Integer::sum);
        }
    }

    /**
     * TODO write documentation
     *
     * @param playedCardPosition
     */
    private void updateBounds(Position playedCardPosition) {
        minX = Math.min(minX, playedCardPosition.x());
        maxX = Math.max(maxX, playedCardPosition.x());
        minY = Math.min(minY, playedCardPosition.y());
        maxY = Math.max(maxY, playedCardPosition.y());
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
     * @param playedCardPosition
     * @return
     */
    @Override
    public boolean checkLegalMove(PlayableCard playedCard, SideType side, Position playedCardPosition) {
        // check if the playedCard has been placed over a blocked corner
        for (Position neighbourPosition : playedCardPosition.getNeighbours()) {
            if (!field.containsKey(neighbourPosition))
                continue;

            BoardCard neighbourCard = field.get(neighbourPosition);
            CardSide neighbourCardSide = activeSides.get(neighbourCard);
            Map<CornersIdx, CornerType> corners = neighbourCardSide.getCorners();

            if (playedCardPosition.isTopLeft(neighbourPosition)) {
                if (corners.get(CornersIdx.BOTTOM_RIGHT) == CornerType.BLOCKED) {
                    return false;
                }
            } else if (playedCardPosition.isTopRight(neighbourPosition)) {
                if (corners.get(CornersIdx.BOTTOM_LEFT) == CornerType.BLOCKED) {
                    return false;
                }
            } else if (playedCardPosition.isBottomRight(neighbourPosition)) {
                if (corners.get(CornersIdx.TOP_LEFT) == CornerType.BLOCKED) {
                    return false;
                }
            } else if (playedCardPosition.isBottomLeft(neighbourPosition)) {
                if (corners.get(CornersIdx.TOP_RIGHT) == CornerType.BLOCKED) {
                    return false;
                }
            }
        }

        // check if the playedCard cost is satisfied
        CardSide cardSide = playedCard.getCardSideBySideType(side);
        Map<ResourceType, Integer> cardCost = cardSide.getCost();

        for (ResourceType resource : cardCost.keySet()) {
            CornerType mappedCorner = resource.mappedCorner();

            if (cardCost.get(resource) > resourceAndObjectCounts.get(mappedCorner))
                return false;
        }

        return true;
    }

    //endregion

}
