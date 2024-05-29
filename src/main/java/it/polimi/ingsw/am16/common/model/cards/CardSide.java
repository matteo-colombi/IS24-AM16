package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import it.polimi.ingsw.am16.common.model.players.PlayArea;
import it.polimi.ingsw.am16.common.util.Position;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Class containing information about a card's side.
 */
public class CardSide implements Serializable {

    @Serial
    private static final long serialVersionUID = -3473183287037952137L;

    private final int points;
    private final Map<ResourceType, Integer> cost;
    private final Map<ResourceType, Integer> permanentResourcesGiven;
    private final PointMultiplierPolicy pointMultiplierPolicy;
    private final SideType sideType;
    private final Map<CornersIdx, Cornerable> corners;

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
     * @param sideType                    The side type (front or back) of the card.
     * @param corners                 The corners of this card. Must be in the order TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT.
     */
    @JsonCreator
    CardSide(
            @JsonProperty("points") int points,
            @JsonProperty("cost") EnumMap<ResourceType, Integer> cost,
            @JsonProperty("permanentResourcesGiven") EnumMap<ResourceType, Integer> permanentResourcesGiven,
            @JsonProperty("pointMultiplierPolicy") PointMultiplierPolicy pointMultiplierPolicy,
            @JsonProperty("sideType") SideType sideType,
            @JsonProperty("corners") Cornerable[] corners) {
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
        this.sideType = sideType;

        this.corners = new EnumMap<>(CornersIdx.class);
        this.corners.put(CornersIdx.TOP_LEFT, corners[CornersIdx.TOP_LEFT.ordinal()]);
        this.corners.put(CornersIdx.TOP_RIGHT, corners[CornersIdx.TOP_RIGHT.ordinal()]);
        this.corners.put(CornersIdx.BOTTOM_RIGHT, corners[CornersIdx.BOTTOM_RIGHT.ordinal()]);
        this.corners.put(CornersIdx.BOTTOM_LEFT, corners[CornersIdx.BOTTOM_LEFT.ordinal()]);
    }

    /**
     * @return The card's base points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return The card's cost.
     */
    public Map<ResourceType, Integer> getCost() {
        return cost;
    }

    /**
     * @return The card's permanent resources.
     */
    public Map<ResourceType, Integer> getPermanentResourcesGiven() {
        return permanentResourcesGiven;
    }

    /**
     * @return The card's side type.
     */
    public SideType getSideType() {
        return sideType;
    }

    /**
     * @return The card's corners.
     */
    public Map<CornersIdx, Cornerable> getCorners() {
        return corners;
    }

    /**
     * Calculates the points given by this card upon placement in the given <code>playArea</code>.
     * @param playArea The player's play area.
     * @return The awarded points.
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
                ", side=" + sideType +
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

    /**
     * Custom serializer for {@link CardSide}, used when saving a player's playing field. This serializer only saves the side type ("front" or "back").
     */
    public static class Serializer extends StdSerializer<CardSide> {
        @Serial
        private static final long serialVersionUID = -3085816960438795648L;

        protected Serializer() {
            super(CardSide.class);
        }

        /**
         * Serializes the given {@link CardSide} by extracting only the side type.
         * @param value Value to serialize; can <b>not</b> be null.
         * @param gen Generator used to output resulting Json content
         * @param provider Provider that can be used to get serializers for
         *   serializing Objects value contains, if any.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         */
        @Override
        public void serialize(CardSide value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            provider.findValueSerializer(SideType.class).serialize(value.getSideType(), gen, provider);
        }
    }
}
