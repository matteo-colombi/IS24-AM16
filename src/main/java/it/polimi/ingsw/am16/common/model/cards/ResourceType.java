package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum that includes the game's resource types.
 */
public enum ResourceType implements Cornerable {
    @JsonProperty("animal")
    ANIMAL,
    @JsonProperty("fungi")
    FUNGI,
    @JsonProperty("insect")
    INSECT,
    @JsonProperty("plant")
    PLANT
}
