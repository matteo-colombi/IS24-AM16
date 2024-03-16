package it.polimi.ingsw.am16.common.model.cards;

import it.polimi.ingsw.am16.common.model.players.PlayArea;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final Map<CornersIdx, CornerType> corners;

    // TODO
    enum PointMultiplierPolicy {
        /**
         * Used by cards that give a fixed amount of points.
         */
        STATIC_POLICY(playArea -> {
            return 1;
        }),

        /**
         * Used by cards that give a proportional amount of points based on the number of covered corners.
         */
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
        INKWELL_POLICY(playArea -> {
            Map<ObjectType, Integer> objCounts = playArea.getObjectCounts();

            return objCounts.get(ObjectType.INKWELL);
        }),

        /**
         * Used by cards that give a proportional amount of points based on the number of visible {@link ObjectType}<code>.MANUSCRIPT</code>.
         */
        MANUSCRIPT_POLICY(playArea -> {
            Map<ObjectType, Integer> objCounts = playArea.getObjectCounts();

            return objCounts.get(ObjectType.MANUSCRIPT);
        }),

        /**
         * Used by cards that give a proportional amount of points based on the number of visible {@link ObjectType}<code>.QUILL</code>.
         */
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
     CardSide(int points,
                    List<ResourceType> cost,
                    List<ResourceType> permanentResourcesGiven,
                    PointMultiplierPolicy pointMultiplierPolicy,
                    SideType side,
                    CornerType[] corners) {
        this.points = points;
        this.cost = cost;
        this.permanentResourcesGiven = permanentResourcesGiven;
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

    public Map<CornersIdx, CornerType> getCorners() {
        return corners;
    }

    // TODO
    public int getAwardedPoints(PlayArea playArea) {
        return points * pointMultiplierPolicy.evaluate(playArea);
    }
}
