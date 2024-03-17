package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum that includes the game's objects.
 */
public enum ObjectType {
    @JsonProperty("inkwell")
    INKWELL,
    @JsonProperty("manuscript")
    MANUSCRIPT,
    @JsonProperty("quill")
    QUILL;

    private CornerType corner;

    public CornerType mappedCorner() {
        return corner;
    }

    public static void bindToCorners() {
        MANUSCRIPT.corner = CornerType.MANUSCRIPT;
        INKWELL.corner = CornerType.INKWELL;
        QUILL.corner = CornerType.QUILL;
    }
}
