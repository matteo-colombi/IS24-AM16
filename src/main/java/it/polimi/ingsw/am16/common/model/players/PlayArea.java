package it.polimi.ingsw.am16.common.model.players;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.*;

/**
 * Class representing the player's play area. Contains info about card placement and resource/object counts.
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

    /**
     * Creates a play area, initializing its player, its other attributes are set to standard values.
     *
     * @param player The player who owns the play area.
     */
    public PlayArea(Player player) {
        this.player = player;
        this.cardCount = 0;
        this.resourceAndObjectCounts = new EnumMap<>(CornerType.class);
        this.cardPlacementOrder = new ArrayList<>();
        this.field = new HashMap<>();
        this.activeSides = new HashMap<>();
        this.minX = 0;
        this.maxX = 0;
        this.minY = 0;
        this.maxY = 0;

        for (CornerType cornerType : CornerType.values()) {
            resourceAndObjectCounts.put(cornerType, 0);
        }
    }

    //region Local Getter and Setter

    /**
     * @return How many cards have already been played.
     */
    public int getCardCount() {
        return cardCount;
    }

    /**
     * @return The amount of each resource in the play area.
     */
    public Map<ResourceType, Integer> getResourceCounts() {
        Map<ResourceType, Integer> resourceCounts = new EnumMap<>(ResourceType.class);

        for (ResourceType resource : ResourceType.values()) {
            CornerType mappedCorner = resource.mappedCorner();
            Integer counter = resourceAndObjectCounts.get(mappedCorner);

            resourceCounts.put(resource, counter);
        }

        return resourceCounts;
    }

    /**
     * @return The amount of each object in the play area.
     */
    public Map<ObjectType, Integer> getObjectCounts() {
        Map<ObjectType, Integer> objectCounts = new EnumMap<>(ObjectType.class);

        for (ObjectType object : ObjectType.values()) {
            CornerType mappedCorner = object.mappedCorner();
            Integer counter = resourceAndObjectCounts.get(mappedCorner);
            objectCounts.put(object, counter);
        }

        return objectCounts;
    }

    /**
     * @return The X coordinate of the left-most played card.
     */
    public int getMinX() {
        return minX;
    }

    /**
     * @return The X coordinate of the right-most played card.
     */
    public int getMaxX() {
        return maxX;
    }

    /**
     * @return The Y coordinate of the down-most played card.
     */
    public int getMinY() {
        return minY;
    }

    /**
     * @return The Y coordinate of the up-most played card.
     */
    public int getMaxY() {
        return maxY;
    }

    //endregion

    //region Local Methods

    /**
     * Places the starter card at the origin (0; 0) of the play area and updates the amount of resources.
     *
     * @param starterCard The starter card chosen by the player.
     * @param side        The visible side of the starter card.
     */
    public void setStarterCard(StarterCard starterCard, SideType side) {
        Position starterPosition = new Position(0, 0);

        if (field.containsKey(starterPosition))
            return;

        updateField(starterCard, side, starterPosition);
        updateCounts(starterCard, starterPosition);
    }

    /**
     * Places the played card at the given position of the play area, updates the amount of resources and objects,
     * updates the bounds and awards points to the player.
     *
     * @param playedCard         The card chosen by the player.
     * @param side               The visible side of the card.
     * @param playedCardPosition The position chosen by the player to place the card.
     */
    public void playCard(PlayableCard playedCard, SideType side, Position playedCardPosition) throws IllegalMoveException {
        if (!checkLegalMove(playedCard, side, playedCardPosition))
            throw new IllegalMoveException("Illegal move");

        updateField(playedCard, side, playedCardPosition);
        updateCounts(playedCard, playedCardPosition);
        updateBounds(playedCardPosition);

        CardSide activeSide = activeSides.get(playedCard);
        int awardedPoints = activeSide.getAwardedPoints(this);

        player.addGamePoints(awardedPoints);
    }

    /**
     * Increases the card count, adds the new position to the placement order list,
     * maps the played card to the position and the visible side to the card.
     *
     * @param playedCard         The card chosen by the player.
     * @param side               The visible side of the card.
     * @param playedCardPosition The position chosen by the player to place the card.
     */
    private void updateField(BoardCard playedCard, SideType side, Position playedCardPosition) {
        CardSide activeSide = playedCard.getCardSideBySideType(side);

        cardCount++;

        cardPlacementOrder.add(playedCardPosition);
        field.put(playedCardPosition, playedCard);
        activeSides.put(playedCard, activeSide);
    }

    /**
     * Updates the amount of resources and objects by following these three steps: <br>
     * - increases the resources and objects that are in the corners of the new card; <br>
     * - increases the resources that are permanent in the new card; <br>
     * - decrements the resources and objects that have been hidden by the new card.
     *
     * @param playedCard         The card chosen by the player.
     * @param playedCardPosition The position chosen by the player to place the card.
     */
    private void updateCounts(BoardCard playedCard, Position playedCardPosition) {
        CardSide activeSide = activeSides.get(playedCard);

        // increases the resources and objects that are in the corners of the new card
        for (CornerType corner : activeSide.getCorners().values()) {
            if (corner == CornerType.BLOCKED || corner == CornerType.EMPTY)
                continue;

            resourceAndObjectCounts.merge(corner, 1, Integer::sum);
        }

        // increases the resources that are permanent in the new card
        Map<ResourceType, Integer> permanentResourcesGiven = activeSide.getPermanentResourcesGiven();

        for (ResourceType resource : permanentResourcesGiven.keySet()) {
            CornerType mappedCorner = resource.mappedCorner();
            Integer resourceCounts = permanentResourcesGiven.get(resource);

            resourceAndObjectCounts.merge(mappedCorner, resourceCounts, Integer::sum);
        }

        // decrements the resources and objects that have been hidden by the new card
        for (Position neighbourPosition : playedCardPosition.getNeighbours()) {
            if (!field.containsKey(neighbourPosition))
                continue;

            BoardCard neighbourCard = field.get(neighbourPosition);
            CardSide neighbourCardSide = activeSides.get(neighbourCard);
            Map<CornersIdx, CornerType> corners = neighbourCardSide.getCorners();
            CornerType corner = null;

            if (playedCardPosition.neighbourIsTopLeft(neighbourPosition)) {
                corner = corners.get(CornersIdx.BOTTOM_RIGHT);
            } else if (playedCardPosition.neighbourIsTopRight(neighbourPosition)) {
                corner = corners.get(CornersIdx.BOTTOM_LEFT);
            } else if (playedCardPosition.neighbourIsBottomRight(neighbourPosition)) {
                corner = corners.get(CornersIdx.TOP_LEFT);
            } else if (playedCardPosition.neighbourIsBottomLeft(neighbourPosition)) {
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
     * Updates the bounds
     *
     * @param playedCardPosition The position chosen by the player to place the card.
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
     * @return The placement order of the cards
     */
    @Override
    public List<Position> getPlacementOrder() {
        return List.copyOf(cardPlacementOrder);
    }

    /**
     * @return The player's field
     */
    @Override
    public Map<Position, BoardCard> getField() {
        return Map.copyOf(field);
    }

    /**
     * @return The visible side of the cards
     */
    @Override
    public Map<BoardCard, CardSide> getActiveSides() {
        return Map.copyOf(activeSides);
    }

    /**
     * Checks whether a move is legal or not by following these three steps: <br>
     * - checks if the playedCard has been placed isolated from the rest of the field; <br>
     * - checks if the playedCard has been placed over a blocked corner; <br>
     * - checks if the playedCard cost is satisfied.
     *
     * @param playedCard         The card chosen by the player.
     * @param side               The visible side of the card.
     * @param playedCardPosition The position chosen by the player to place the card.
     * @return True if the move is legal otherwise returns False.
     */
    @Override
    public boolean checkLegalMove(PlayableCard playedCard, SideType side, Position playedCardPosition) {
        List<Position> neighboursPositions = playedCardPosition.getNeighbours();

        // checks if the playedCard has been placed isolated from the rest of the field
        boolean isIsolated = true;

        for (Position neighbourPosition : neighboursPositions) {
            if (field.containsKey(neighbourPosition)) {
                isIsolated = false;
                break;
            }
        }

        if (isIsolated)
            return false;

        // checks if the playedCard has been placed over a blocked corner
        for (Position neighbourPosition : neighboursPositions) {
            if (!field.containsKey(neighbourPosition))
                continue;

            BoardCard neighbourCard = field.get(neighbourPosition);
            CardSide neighbourCardSide = activeSides.get(neighbourCard);
            Map<CornersIdx, CornerType> corners = neighbourCardSide.getCorners();

            if (playedCardPosition.neighbourIsTopLeft(neighbourPosition)) {
                if (corners.get(CornersIdx.BOTTOM_RIGHT) == CornerType.BLOCKED) {
                    return false;
                }
            } else if (playedCardPosition.neighbourIsTopRight(neighbourPosition)) {
                if (corners.get(CornersIdx.BOTTOM_LEFT) == CornerType.BLOCKED) {
                    return false;
                }
            } else if (playedCardPosition.neighbourIsBottomRight(neighbourPosition)) {
                if (corners.get(CornersIdx.TOP_LEFT) == CornerType.BLOCKED) {
                    return false;
                }
            } else if (playedCardPosition.neighbourIsBottomLeft(neighbourPosition)) {
                if (corners.get(CornersIdx.TOP_RIGHT) == CornerType.BLOCKED) {
                    return false;
                }
            }
        }

        // checks if the playedCard cost is satisfied
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
