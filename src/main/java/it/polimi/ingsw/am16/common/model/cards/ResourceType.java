package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum that includes the game's resource types.
 */
public enum ResourceType {
    @JsonProperty("animal")
    ANIMAL,
    @JsonProperty("fungi")
    FUNGI,
    @JsonProperty("insect")
    INSECT,
    @JsonProperty("plant")
    PLANT;

    private CornerType corner;

    /**
     * Method that must be called at least once that creates associations between {@link ResourceType} and {@link CornerType}.
     */
    public static void bindToCorners() {
        FUNGI.corner = CornerType.FUNGI;
        PLANT.corner = CornerType.PLANT;
        ANIMAL.corner = CornerType.ANIMAL;
        INSECT.corner = CornerType.INSECT;
    }

    /**
     * @return the corresponding {@link CornerType}.
     */
    public CornerType mappedCorner() {
        return corner;
    }
}
