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

    public static void bindToCorners() {
        FUNGI.corner = CornerType.FUNGI;
        PLANT.corner = CornerType.PLANT;
        ANIMAL.corner = CornerType.ANIMAL;
        INSECT.corner = CornerType.INSECT;
    }

    public CornerType mappedCorner() {
        return corner;
    }
}
