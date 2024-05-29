package it.polimi.ingsw.am16.common.model.cards;

import it.polimi.ingsw.am16.common.util.Position;

/**
 * Enum that contains the types of corner of a card. Used to avoid hard-coded indices.
 */
public enum CornersIdx{
    TOP_LEFT(new Position(-1, 1)),
    TOP_RIGHT(new Position(1, 1)),
    BOTTOM_RIGHT(new Position(1, -1)),
    BOTTOM_LEFT(new Position(-1, -1));

    private final Position offset;

    CornersIdx(Position offset) {
        this.offset = offset;
    }

    /**
     *
     * @return The offset of this corner.
     */
    public Position getOffset() {
        return offset;
    }
}