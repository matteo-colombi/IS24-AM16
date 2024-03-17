package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum that includes the game's resource types.
 */
public enum ResourceType {
    @JsonProperty("animal")
    ANIMAL(CornerType.ANIMAL),
    @JsonProperty("fungi")
    FUNGI(CornerType.FUNGI),
    @JsonProperty("insect")
    INSECT(CornerType.INSECT),
    @JsonProperty("plant")
    PLANT(CornerType.PLANT);

    private final CornerType corner;

    ResourceType(CornerType corner) {
        this.corner = corner;
    }

    public CornerType mappedCorner() {
        return corner;
    }
}
