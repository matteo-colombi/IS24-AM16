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

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public Map<ResourceType, Integer> getResourceCounts() {
        Map<ResourceType, Integer> resourceCounts = new HashMap<>();

        Integer animalCounts = resourceAndObjectCounts.get(CornerType.ANIMAL);
        Integer fungiCounts = resourceAndObjectCounts.get(CornerType.FUNGI);
        Integer insectCounts = resourceAndObjectCounts.get(CornerType.INSECT);
        Integer plantCounts = resourceAndObjectCounts.get(CornerType.PLANT);

        resourceCounts.put(ResourceType.ANIMAL, animalCounts);
        resourceCounts.put(ResourceType.FUNGI, fungiCounts);
        resourceCounts.put(ResourceType.INSECT, insectCounts);
        resourceCounts.put(ResourceType.PLANT, plantCounts);

        return resourceCounts;
    }

    public Map<ObjectType, Integer> getObjectCounts() {
        Map<ObjectType, Integer> objectCounts = new HashMap<>();

        Integer inkwellCounts = resourceAndObjectCounts.get(CornerType.INKWELL);
        Integer manuscriptCounts = resourceAndObjectCounts.get(CornerType.MANUSCRIPT);
        Integer quillCounts = resourceAndObjectCounts.get(CornerType.QUILL);

        objectCounts.put(ObjectType.INKWELL, inkwellCounts);
        objectCounts.put(ObjectType.MANUSCRIPT, manuscriptCounts);
        objectCounts.put(ObjectType.QUILL, quillCounts);

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
        activeSides.put(playedCard, playedCard.getSideByType(side));
    }

    /**
     * TODO write documentation
     *
     * @param playedCard
     * @param side
     * @param playedCardPosition
     */
    public void updateCounts(BoardCard playedCard, SideType side, Position playedCardPosition) {
        CardSide activeSide = activeSides.get(playedCard);

        if (side == SideType.FRONT) {
            for (CornerType corner : activeSide.getCorners().values()) {
                if (corner == CornerType.BLOCKED || corner == CornerType.EMPTY)
                    continue;

                resourceAndObjectCounts.merge(corner, 1, Integer::sum);
            }
        } else {
            //TODO implementare la parte relativa alle risorse permanenti

        }

        //TODO implementare la parte relativa agli angoli coperti dalla nuova carta

        for (Position neighbourPosition : playedCardPosition.getNeighbours()) {
            if (!field.containsKey(neighbourPosition))
                continue;

            BoardCard neighbourCard = field.get(neighbourPosition);
            CardSide neighbourCardSide = activeSides.get(neighbourCard);

            //FIXME è osceno, cambialo appena possibile
            if (playedCardPosition.getOffset(neighbourPosition).equals(new Position(-1, -1))) {
                neighbourCardSide.getCorners().get(CornersIdx.BOTTOM_RIGHT);



            }
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
