package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum that includes {@link Cornerable} elements that are neither Resources nor Objects.
 */
public enum SpecialType implements Cornerable {
    @JsonProperty("blocked")
    BLOCKED,

    @JsonProperty("empty")
    EMPTY
}
