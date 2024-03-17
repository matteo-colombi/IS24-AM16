package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.players.PlayArea;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.*;
import java.util.function.Function;

/**
 * TODO write documentation
 */
public class CardSide {
    // TODO implement
    private final int points;
    private final Map<ResourceType, Integer> cost;
    private final Map<ResourceType, Integer> permanentResourcesGiven;
    private final PointMultiplierPolicy pointMultiplierPolicy;
    private final SideType side;
    private final Map<CornersIdx, CornerType> corners;

    /**
     * TODO write documentation
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
         * TODO write documentation
         * @param policy
         */
        PointMultiplierPolicy(Function<PlayArea, Integer> policy) {
            this.policy = policy;
        }

        /**
         * TODO write documentation
         * @param playArea
         * @return
         */
        public int evaluate(PlayArea playArea) {
            return policy.apply(playArea);
        }
    }

    /**
     *
     * @param points
     * @param cost
     * @param permanentResourcesGiven
     * @param pointMultiplierPolicy
     * @param side
     * @param corners The corners of this card. Must be in the order TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT.
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
     * TODO write doc
     * @return
     */
    public int getPoints() {
        return points;
    }

    /**
     * TODO write doc
     * @return
     */
    public Map<ResourceType, Integer> getCost() {
        return cost;
    }

    /**
     * TODO write doc
     * @return
     */
    public Map<ResourceType, Integer> getPermanentResourcesGiven() {
        return permanentResourcesGiven;
    }

    /**
     * TODO write doc
     * @return
     */
    public SideType getSide() {
        return side;
    }

    /**
     * TODO write doc
     * @return
     */
    public Map<CornersIdx, CornerType> getCorners() {
        return corners;
    }

    /**
     * TODO write doc
     * @param playArea
     * @return
     */
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
     * TODO write doc
     * @param name
     * @return
     */
    @JsonCreator
    static CardSide commonSidesFactory(String name) {
        return commonSides.get(name);
    }

    /**
     * TODO write doc
     * @param name
     * @param cardSide
     */
    static void addCommonSide(String name, CardSide cardSide) {
        commonSides.put(name, cardSide);
    }
}
