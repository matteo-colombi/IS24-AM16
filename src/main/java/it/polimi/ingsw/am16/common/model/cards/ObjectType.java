package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum that includes the game's objects.
 */
public enum ObjectType {
    @JsonProperty("inkwell")
    INKWELL(CornerType.INKWELL),
    @JsonProperty("manuscript")
    MANUSCRIPT(CornerType.MANUSCRIPT),
    @JsonProperty("quill")
    QUILL(CornerType.QUILL);

    private final CornerType corner;

    ObjectType(CornerType corner) {
        this.corner = corner;
    }

    public CornerType mappedCorner() {
        return corner;
    }
}
