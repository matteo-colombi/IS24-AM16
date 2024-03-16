package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum that includes the game's objects.
 */
public enum ObjectType implements Cornerable {
    @JsonProperty("inkwell")
public enum ObjectType {
    INKWELL,

    @JsonProperty("manuscript")
    MANUSCRIPT,

    @JsonProperty("quill")
    QUILL
}
