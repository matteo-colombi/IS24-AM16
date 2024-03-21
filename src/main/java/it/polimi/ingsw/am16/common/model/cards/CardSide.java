package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.players.PlayArea;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.*;
import java.util.function.Function;

/**
 * Class containing information about a card's side.
 */
public class CardSide {
    private final int points;
    private final Map<ResourceType, Integer> cost;
    private final Map<ResourceType, Integer> permanentResourcesGiven;
    private final PointMultiplierPolicy pointMultiplierPolicy;
    private final SideType side;
    private final Map<CornersIdx, CornerType> corners;

    /**
     * Enum containing the existing policies to award points.
     */
    enum PointMultiplierPolicy {
        /**
         * Used by cards that give a fixed amount of points.
         */
        @SuppressWarnings("unused") //Suppressing because it's being used by the JSON files, but it goes undetected.
        @JsonProperty("static")
        STATIC_POLICY(playArea -> {
            return 1;
        }),

        /**
         * Used by cards that give a proportional amount of points based on the number of covered corners.
         */
        @SuppressWarnings("unused") //Suppressing because it's being used by the JSON files, but it goes undetected.
        @JsonProperty("corners_covered")
        CORNERS_COVERED_POLICY(playArea -> {
            int neighbourCounter = 0;

            for (Position neighbour : playArea.getPlacementOrder().getLast().getNeighbours()) {
                if (playArea.getField().containsKey(neighbour)) {
                    neighbourCounter++;
                }
            }

            assert neighbourCounter > 0;

            return neighbourCounter;
        }),

        /**
         * Used by cards that give a proportional amount of points based on the number of visible {@link ObjectType}<code>.INKWELL</code>.
         */
        @SuppressWarnings("unused") //Suppressing because it's being used by the JSON files, but it goes undetected.
        @JsonProperty("inkwell")
        INKWELL_POLICY(playArea -> {
            Map<ObjectType, Integer> objCounts = playArea.getObjectCounts();

            return objCounts.get(ObjectType.INKWELL);
        }),

        /**
         * Used by cards that give a proportional amount of points based on the number of visible {@link ObjectType}<code>.MANUSCRIPT</code>.
         */
        @SuppressWarnings("unused") //Suppressing because it's being used by the JSON files, but it goes undetected.
        @JsonProperty("manuscript")
        MANUSCRIPT_POLICY(playArea -> {
            Map<ObjectType, Integer> objCounts = playArea.getObjectCounts();

            return objCounts.get(ObjectType.MANUSCRIPT);
        }),

        /**
         * Used by cards that give a proportional amount of points based on the number of visible {@link ObjectType}<code>.QUILL</code>.
         */
        @SuppressWarnings("unused") //Suppressing because it's being used by the JSON files, but it goes undetected.
        @JsonProperty("quill")
        QUILL_POLICY(playArea -> {
            Map<ObjectType, Integer> objCounts = playArea.getObjectCounts();

            return objCounts.get(ObjectType.QUILL);
        });

        private final Function<PlayArea, Integer> policy;

        /**
         * Constructs a new PointMultiplierPolicy with the given function.
         * @param policy The policy to be applied.
         */
        PointMultiplierPolicy(Function<PlayArea, Integer> policy) {
            this.policy = policy;
        }

        /**
         * Evaluates the policy based on <code>playArea</code> data.
         *
         * @param playArea The player's play area.
         * @return The multiplier for the card's points.
         */
        public int evaluate(PlayArea playArea) {
            return policy.apply(playArea);
        }
    }

    /**
     * Creates a new card side.
     *
     * @param points                  The base points scored when the card is played.
     * @param cost                    The resources needed to play the card.
     * @param permanentResourcesGiven The permanent resources given by the card.
     * @param pointMultiplierPolicy   The policy to use to multiply the base points.
     * @param side                    The side type (front or back) of the card.
     * @param corners                 The corners of this card. Must be in the order TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT.
     */
    @JsonCreator
    CardSide(
            @JsonProperty("points") int points,
            @JsonProperty("cost") EnumMap<ResourceType, Integer> cost,
            @JsonProperty("permanentResourcesGiven") EnumMap<ResourceType, Integer> permanentResourcesGiven,
            @JsonProperty("pointMultiplierPolicy") PointMultiplierPolicy pointMultiplierPolicy,
            @JsonProperty("sideType") SideType side,
            @JsonProperty("corners") CornerType[] corners) {
        this.points = points;

        cost.putIfAbsent(ResourceType.FUNGI, 0);
        cost.putIfAbsent(ResourceType.PLANT, 0);
        cost.putIfAbsent(ResourceType.ANIMAL, 0);
        cost.putIfAbsent(ResourceType.INSECT, 0);
        this.cost = Collections.unmodifiableMap(cost);

        permanentResourcesGiven.putIfAbsent(ResourceType.FUNGI, 0);
        permanentResourcesGiven.putIfAbsent(ResourceType.PLANT, 0);
        permanentResourcesGiven.putIfAbsent(ResourceType.ANIMAL, 0);
        permanentResourcesGiven.putIfAbsent(ResourceType.INSECT, 0);
        this.permanentResourcesGiven = Collections.unmodifiableMap(permanentResourcesGiven);

        this.pointMultiplierPolicy = pointMultiplierPolicy;
        this.side = side;

        this.corners = new EnumMap<>(CornersIdx.class);
        this.corners.put(CornersIdx.TOP_LEFT, corners[CornersIdx.TOP_LEFT.ordinal()]);
        this.corners.put(CornersIdx.TOP_RIGHT, corners[CornersIdx.TOP_RIGHT.ordinal()]);
        this.corners.put(CornersIdx.BOTTOM_RIGHT, corners[CornersIdx.BOTTOM_RIGHT.ordinal()]);
        this.corners.put(CornersIdx.BOTTOM_LEFT, corners[CornersIdx.BOTTOM_LEFT.ordinal()]);
    }

    /**
     * @return The card's base points.
     */
    @JsonIgnore
    public int getPoints() {
        return points;
    }

    /**
     * @return The card's cost.
     */
    @JsonIgnore
    public Map<ResourceType, Integer> getCost() {
        return cost;
    }

    /**
     * @return The card's permanent resources.
     */
    @JsonIgnore
    public Map<ResourceType, Integer> getPermanentResourcesGiven() {
        return permanentResourcesGiven;
    }

    /**
     * @return The card's side type.
     */
    public SideType getSide() {
        return side;
    }

    /**
     * @return The card's corners.
     */
    @JsonIgnore
    public Map<CornersIdx, CornerType> getCorners() {
        return corners;
    }

    /**
     * Calculates the points given by this card upon placement in the given <code>playArea</code>.
     * @param playArea The player's play area.
     * @return The awarded points.
     */
    @JsonIgnore
    public int getAwardedPoints(PlayArea playArea) {
        return points * pointMultiplierPolicy.evaluate(playArea);
    }

    @Override
    public String toString() {
        return "CardSide{" +
                "points=" + points +
                ", cost=" + cost +
                ", permanentResourcesGiven=" + permanentResourcesGiven +
                ", pointMultiplierPolicy=" + pointMultiplierPolicy +
                ", side=" + side +
                ", corners=" + corners +
                '}';
    }

    private static final Map<String, CardSide> commonSides = new HashMap<>();

    /**
     * Returns a previously added {@link CardSide}, given the name. Used to allow reuse of identical CardSides in different cards.
     *
     * @param name The name of the side to retrieve.
     * @return the side with the given name, or <code>null</code> if there is no such side.
     */
    @JsonCreator
    static CardSide commonSidesFactory(String name) {
        return commonSides.get(name);
    }

    /**
     * Adds a {@link CardSide} with the given name to a common side registry. Used to allow reuse of identical CardSides in different cards.
     *
     * @param name     The name of the side to add to the registry.
     * @param cardSide The CardSide to add to the registry.
     */
    static void addCommonSide(String name, CardSide cardSide) {
        commonSides.put(name, cardSide);
    }
}
