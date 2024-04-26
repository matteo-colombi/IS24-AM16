package it.polimi.ingsw.am16.common.model.players;

import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface to give external classes access to {@link PlayArea}-type objects. It contains all the methods
 * a play area is supposed to show to the outside
 */
public interface PlayAreaModel {

    /**
     * @return A {@link List} containing the {@link Position}s of the cards in the order they were placed in.
     */
    List<Position> getPlacementOrder();

    /**
     * @return The player's field.
     */
    Map<Position, BoardCard> getField();

    /**
     * @return The visible side of the cards.
     */
    Map<BoardCard, SideType> getActiveSides();

    /**
     * Checks whether a move is legal or not.
     *
     * @param playedCard         The card chosen by the player.
     * @param side               The visible side of the card.
     * @param playedCardPosition The position chosen by the player to place the card.
     * @return <code>true</code> if the move is legal otherwise returns <code>false</code>.
     */
    boolean checkLegalMove(PlayableCard playedCard, SideType side, Position playedCardPosition);

    /**
     * @return A map containing the amounts of each resource in the play area.
     */
    Map<ResourceType, Integer> getResourceCounts();

    /**
     * @return A map containing the amounts of each object in the play area.
     */
    Map<ObjectType, Integer> getObjectCounts();

    /**
     * @return The x-coordinate of the left-most played card.
     */
    int getMinX();

    /**
     * @return The x-coordinate of the right-most played card.
     */
    int getMaxX();

    /**
     * @return The y-coordinate of the down-most played card.
     */
    int getMinY();

    /**
     * @return The Y coordinate of the up-most played card.
     */
    int getMaxY();

    /**
     * @return The set of positions in which a card can be placed.
     */
    Set<Position> getPlaceablePositions();

    /**
     * @return The set of placeable positions that were added with the last card placement.
     */
    Set<Position> getAddedPlaceablePositions();

    /**
     * @return The set of positions that were removed from placeablePositions with the last card placement.
     */
    Set<Position> getRemovedPlaceablePositions();

    /**
     * @return Whether the player has no valid moves to make.
     */
    boolean isDeadlocked();

}