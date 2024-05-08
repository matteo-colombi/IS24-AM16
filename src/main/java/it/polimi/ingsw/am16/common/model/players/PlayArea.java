package it.polimi.ingsw.am16.common.model.players;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.util.Position;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representing the player's play area. Contains info about card placement and resource/object counts.
 */
@JsonDeserialize(using = PlayArea.Deserializer.class)
public class PlayArea implements PlayAreaModel, Serializable {
    @Serial
    private static final long serialVersionUID = -5968965401567479628L;

    private int cardCount;
    private final Map<CornerType, Integer> resourceAndObjectCounts;
    private final List<Position> cardPlacementOrder;
    @JsonSerialize(keyUsing = Position.Serializer.class)
    private final Map<Position, BoardCard> field;
    @JsonSerialize(keyUsing = BoardCard.BoardCardSerializer.class, contentUsing = CardSide.Serializer.class)
    private final Map<BoardCard, CardSide> activeSides;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    private final Set<Position> legalPositions;
    private final Set<Position> illegalPositions;
    private final Set<Position> addedLegalPositions;
    private final Set<Position> removedLegalPositions;

    /**
     * Creates a play area, initializing its player, its other attributes are set to standard values.
     */
    public PlayArea() {
        this.cardCount = 0;
        this.resourceAndObjectCounts = new EnumMap<>(CornerType.class);
        this.cardPlacementOrder = new ArrayList<>();
        this.field = new HashMap<>();
        this.activeSides = new HashMap<>();
        this.minX = 0;
        this.maxX = 0;
        this.minY = 0;
        this.maxY = 0;

        legalPositions = new HashSet<>();
        illegalPositions = new HashSet<>();
        addedLegalPositions = new HashSet<>();
        removedLegalPositions = new HashSet<>();

        for (CornerType cornerType : CornerType.values()) {
            resourceAndObjectCounts.put(cornerType, 0);
        }
    }

    /**
     * Constructs a new PlayArea object, setting all the attributes to the ones given as parameters.
     * This constructor is private because it should only be used by {@link PlayArea.Deserializer}.
     * @param cardCount The PlayArea's card count.
     * @param resourceAndObjectCounts The {@link Map} containing all the resources and objects currently visible on the player's field.
     * @param cardPlacementOrder The {@link List} containing the {@link Position}s in of the placed cards, in the order they were placed in.
     * @param field The player's play field.
     * @param activeSideTypes The {@link Map} containing which side every card was played on.
     * @param minX The minimum x-coordinate of a card in the player's play field.
     * @param maxX The maximum x-coordinate of a card in the player's play field.
     * @param minY The minimum y-coordinate of a card in the player's play field.
     * @param maxY The maximum y-coordinate of a card in the player's play field.
     * @param legalPositions The set of positions in which a card can be placed.
     * @param illegalPositions The set of positions in which a card cannot be placed.
     */
    private PlayArea(
            int cardCount,
            Map<CornerType, Integer> resourceAndObjectCounts,
            List<Position> cardPlacementOrder,
            Map<Position, BoardCard> field,
            Map<BoardCard, SideType> activeSideTypes,
            int minX,
            int maxX,
            int minY,
            int maxY,
            Set<Position> legalPositions,
            Set<Position> illegalPositions) {
        this.cardCount = cardCount;
        this.resourceAndObjectCounts = resourceAndObjectCounts;
        this.cardPlacementOrder = cardPlacementOrder;
        this.field = field;
        Map<BoardCard, CardSide> activeSides = new HashMap<>();
        activeSideTypes.forEach((card, value) -> activeSides.put(card, card.getCardSideBySideType(value)));
        this.activeSides = activeSides;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.legalPositions = legalPositions;
        this.illegalPositions = illegalPositions;
        this.addedLegalPositions = new HashSet<>();
        this.removedLegalPositions = new HashSet<>();
    }

    //region Local Getter and Setter

    /**
     * @return How many cards have already been played.
     */
    public int getCardCount() {
        return cardCount;
    }

    /**
     * @return A map containing the amounts of each resource in the play area.
     */
    @Override
    @JsonIgnore
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
     * @return A map containing the amounts of each object in the play area.
     */
    @Override
    @JsonIgnore
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
     * @return The {@link Map} containing the resources and objects currently visible on the player's field.
     */
    public Map<CornerType, Integer> getResourceAndObjectCounts() {
        return resourceAndObjectCounts;
    }

    /**
     * @return The x-coordinate of the left-most played card.
     */
    @Override
    public int getMinX() {
        return minX;
    }

    /**
     * @return The x-coordinate of the right-most played card.
     */
    @Override
    public int getMaxX() {
        return maxX;
    }

    /**
     * @return The y-coordinate of the down-most played card.
     */
    @Override
    public int getMinY() {
        return minY;
    }

    /**
     * @return The Y coordinate of the up-most played card.
     */
    @Override
    public int getMaxY() {
        return maxY;
    }

    /**
     * @return The set of positions in which a card can be placed.
     */
    @Override
    public Set<Position> getLegalPositions() {
        return new HashSet<>(legalPositions);
    }

    /**
     * @return The set of positions in which a card cannot be placed.
     */
    @Override
    @SuppressWarnings("unused") //Suppressing because this method is used by Jackson, but it doesn't get detected
    public Set<Position> getIllegalPositions() {
        return new HashSet<>(illegalPositions);
    }

    /**
     * @return The set of placeable positions that were added with the last card placement.
     */
    @JsonIgnore
    public Set<Position> getAddedLegalPositions() {
        return Set.copyOf(addedLegalPositions);
    }

    /**
     * @return The set of positions that were removed from placeablePositions with the last card placement.
     */
    @JsonIgnore
    public Set<Position> getRemovedLegalPositions() {
        return Set.copyOf(removedLegalPositions);
    }

    /**
     * @return Whether the player has no valid moves to make.
     */
    @Override
    public boolean isDeadlocked() {
        return legalPositions.isEmpty();
    }

    //endregion

    //region Local Methods

    /**
     * Places the starter card at the origin (0, 0) of the play area and updates the amounts of resources.
     * If the starter card was already placed, this method does nothing.
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
     * Places the played card at the given position of the play area, updates the amounts of resources and objects,
     * updates the bounds and awards points to the player.
     *
     * @param playedCard         The card chosen by the player.
     * @param side               The visible side of the card.
     * @param playedCardPosition The position chosen by the player to place the card.
     * @throws IllegalMoveException Thrown if the move is not permitted. The check is done using <code>checkLegalMove(...)</code>.
     */
    public void playCard(PlayableCard playedCard, SideType side, Position playedCardPosition) throws IllegalMoveException {
        if (!checkLegalMove(playedCard, side, playedCardPosition))
            throw new IllegalMoveException("Illegal move");

        updateField(playedCard, side, playedCardPosition);
        updateCounts(playedCard, playedCardPosition);
        updateBounds(playedCardPosition);
    }

    /**
     * @param playedCard The placed card.
     * @return The number of game points awarded by the given card.
     */
    public int awardGamePoints(PlayableCard playedCard) {
        CardSide activeSide = activeSides.get(playedCard);

        return activeSide.getAwardedPoints(this);
    }

    /**
     * Increases the card count, adds the new position to the placement order list,
     * maps the played card to the position and the visible side to the card. Updates the
     * placeablePositions set.
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

        legalPositions.remove(playedCardPosition);
        illegalPositions.add(playedCardPosition);

        addedLegalPositions.clear();
        removedLegalPositions.clear();

        Map<CornersIdx, CornerType> corners = activeSide.getCorners();

        for(CornersIdx idx : corners.keySet()) {
            Position neighbourPosition = playedCardPosition.addOffset(idx.getOffset());

            if (!illegalPositions.contains(neighbourPosition)) {
                if (corners.get(idx) == CornerType.BLOCKED) {
                    removedLegalPositions.add(neighbourPosition);
                } else {
                    addedLegalPositions.add(neighbourPosition);
                }
            }
        }

        legalPositions.addAll(addedLegalPositions);
        legalPositions.removeAll(removedLegalPositions);
        illegalPositions.addAll(removedLegalPositions);
    }

    /**
     * Updates the amounts of resources and objects by following these three steps: <br>
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

            assert corner != null;

            if (corner == CornerType.BLOCKED || corner == CornerType.EMPTY)
                continue;

            resourceAndObjectCounts.merge(corner, -1, Integer::sum);
        }
    }

    /**
     * Updates the bounds of the play area.
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
     * @return A {@link List} containing the {@link Position}s of the cards in the order they were placed in.
     */
    @Override
    public List<Position> getPlacementOrder() {
        return List.copyOf(cardPlacementOrder);
    }

    /**
     * @return The player's field.
     */
    @Override
    public Map<Position, BoardCard> getField() {
        return Map.copyOf(field);
    }

    /**
     * @return The visible side of the cards.
     */
    @Override
    @JsonIgnore
    public Map<BoardCard, SideType> getActiveSides() {
        return activeSides.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue().getSideType()));
    }

    /**
     * Checks whether a move is legal or not by following these three steps: <br>
     * - checks if the position is already occupied
     * - checks if the playedCard has been placed isolated from the rest of the field; <br>
     * - checks if the playedCard has been placed over a blocked corner; <br>
     * - checks if the playedCard cost is satisfied.
     *
     * @param playedCard         The card chosen by the player.
     * @param side               The visible side of the card.
     * @param playedCardPosition The position chosen by the player to place the card.
     * @return <code>true</code> if the move is legal otherwise returns <code>false</code>.
     */
    @Override
    public boolean checkLegalMove(PlayableCard playedCard, SideType side, Position playedCardPosition) {
        // checks if the position is already occupied
        if (field.containsKey(playedCardPosition))
            return false;

        if (!legalPositions.contains(playedCardPosition))
            return false;

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

    //region Serializer

    /**
     * Custom deserializer for {@link PlayArea}. Used for reloading a saved game from JSON.
     */
    static class Deserializer extends StdDeserializer<PlayArea> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        @Serial
        private static final long serialVersionUID = -3881814883370100568L;

        public Deserializer() {
            super(PlayArea.class);
        }

        /**
         * Deserializes the {@link PlayArea} from the given JSON.
         * @param p Parser used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link PlayArea}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public PlayArea deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode playAreaNode = p.getCodec().readTree(p);

            int cardCount = playAreaNode.get("cardCount").asInt();

            TypeReference<HashMap<CornerType, Integer>> typeReferenceResObjCounts = new TypeReference<>(){};
            Map<CornerType, Integer> resourceAndObjectCounts = mapper.readValue(playAreaNode.get("resourceAndObjectCounts").toString(), typeReferenceResObjCounts);

            TypeReference<ArrayList<Position>> typeReferenceCardPlacementOrder = new TypeReference<>() {};
            List<Position> cardPlacementOrder = mapper.readValue(playAreaNode.get("placementOrder").toString(), typeReferenceCardPlacementOrder);

            TypeReference<HashMap<Position, BoardCard>> typeReferenceField = new TypeReference<>() {};
            Map<Position, BoardCard> field = mapper.readValue(playAreaNode.get("field").toString(), typeReferenceField);

            TypeReference<HashMap<BoardCard, SideType>> typeReferenceActiveSides = new TypeReference<>() {};
            Map<BoardCard, SideType> activeSideTypes = mapper.readValue(playAreaNode.get("activeSides").toString(), typeReferenceActiveSides);

            int minX = playAreaNode.get("minX").asInt();
            int maxX = playAreaNode.get("maxX").asInt();
            int minY = playAreaNode.get("minY").asInt();
            int maxY = playAreaNode.get("maxY").asInt();

            TypeReference<HashSet<Position>> typeReferenceLegalPositions = new TypeReference<>() {};
            TypeReference<HashSet<Position>> typeReferenceIllegalPositions = new TypeReference<>() {};
            Set<Position> legalPositions = mapper.readValue(playAreaNode.get("legalPositions").toString(), typeReferenceLegalPositions);
            Set<Position> illegalPositions = mapper.readValue(playAreaNode.get("illegalPositions").toString(), typeReferenceIllegalPositions);

            return new PlayArea(cardCount, resourceAndObjectCounts, cardPlacementOrder, field, activeSideTypes, minX, maxX, minY, maxY, legalPositions, illegalPositions);
        }
    }

    //endregion

}
