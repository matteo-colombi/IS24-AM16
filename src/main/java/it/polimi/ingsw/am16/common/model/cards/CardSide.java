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
    private final List<ResourceType> cost;
    private final List<ResourceType> permanentResourcesGiven;
    private final PointMultiplierPolicy pointMultiplierPolicy;
    private final SideType side;
    private final Map<CornersIdx, Cornerable> corners;

    public enum CornersIdx{
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT
    }

    // TODO
    enum PointMultiplierPolicy {
        /**
         * Used by cards that give a fixed amount of points.
         */
        @JsonProperty("static")
        STATIC_POLICY(playArea -> {
            return 1;
        }),

        /**
         * Used by cards that give a proportional amount of points based on the number of covered corners.
         */
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
        @JsonProperty("inkwell")
        INKWELL_POLICY(playArea -> {
            Map<ObjectType, Integer> objCounts = playArea.getObjectCounts();

            return objCounts.get(ObjectType.INKWELL);
        }),

        /**
         * Used by cards that give a proportional amount of points based on the number of visible {@link ObjectType}<code>.MANUSCRIPT</code>.
         */
        @JsonProperty("manuscript")
        MANUSCRIPT_POLICY(playArea -> {
            Map<ObjectType, Integer> objCounts = playArea.getObjectCounts();

            return objCounts.get(ObjectType.MANUSCRIPT);
        }),

        /**
         * Used by cards that give a proportional amount of points based on the number of visible {@link ObjectType}<code>.QUILL</code>.
         */
        @JsonProperty("quill")
        QUILL_POLICY(playArea -> {
            Map<ObjectType, Integer> objCounts = playArea.getObjectCounts();

            return objCounts.get(ObjectType.QUILL);
        });

        private final Function<PlayArea, Integer> policy;

        PointMultiplierPolicy(Function<PlayArea, Integer> policy) {
            this.policy = policy;
        }

        /**
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
            @JsonProperty("cost") ResourceType[] cost,
            @JsonProperty("permanentResourcesGiven") ResourceType[] permanentResourcesGiven,
            @JsonProperty("pointMultiplierPolicy") PointMultiplierPolicy pointMultiplierPolicy,
            @JsonProperty("sideType") SideType side,
            @JsonProperty("corners") Cornerable[] corners) {
        this.points = points;
        this.cost = List.of(cost);
        this.permanentResourcesGiven = List.of(permanentResourcesGiven);
        this.pointMultiplierPolicy = pointMultiplierPolicy;
        this.side = side;

        this.corners = new HashMap<>();
        this.corners.put(CornersIdx.TOP_LEFT, corners[CornersIdx.TOP_LEFT.ordinal()]);
        this.corners.put(CornersIdx.TOP_RIGHT, corners[CornersIdx.TOP_RIGHT.ordinal()]);
        this.corners.put(CornersIdx.BOTTOM_RIGHT, corners[CornersIdx.BOTTOM_RIGHT.ordinal()]);
        this.corners.put(CornersIdx.BOTTOM_LEFT, corners[CornersIdx.BOTTOM_LEFT.ordinal()]);
    }

    public int getPoints() {
        return points;
    }

    public List<ResourceType> getCost() {
        return cost;
    }

    public List<ResourceType> getPermanentResourcesGiven() {
        return permanentResourcesGiven;
    }

    PointMultiplierPolicy getPointMultiplierPolicy() {
        return pointMultiplierPolicy;
    }

    public SideType getSide() {
        return side;
    }

    public Map<CornersIdx, Cornerable> getCorners() {
        return corners;
    }

    // TODO
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

    @JsonCreator
    static CardSide commonSidesFactory(String name) {
        return commonSides.get(name);
    }

    static void addCommonSide(String name, CardSide cardSide) {
        commonSides.put(name, cardSide);
    }
}
