package it.polimi.ingsw.am16.common.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Record used to keep track of coordinates on game boards.
 *
 * @param x x-coordinate of the point.
 * @param y y-coordinate of the point.
 */
public record Position(@JsonProperty("x") int x, @JsonProperty("y") int y) {
    private static final int numNeighbours = 4;
    private static final int[] xDisplacements = {-1, 1, 1, -1};
    private static final int[] yDisplacements = {-1, -1, 1, 1};

    /**
     * Gives the 4 Positions of this Position's neighbours, diagonally from each corner.
     *
     * @return List of the neighbours.
     */
    public List<Position> getNeighbours() {
        List<Position> neighbours = new ArrayList<>();

        for (int i = 0; i < numNeighbours; i++) {
            neighbours.add(new Position(x + xDisplacements[i], y + yDisplacements[i]));
        }

        return neighbours;
    }

    /**
     * Adds this position with the given offset position, obtained by summing each x-coordinate and each y-coordinate.
     *
     * @param offset The offset to add.
     * @return The new position.
     */
    public Position addOffset(Position offset) {
        return new Position(x + offset.x, y + offset.y);
    }

    public Position getOffset(Position other) {
        return new Position(other.x - x, other.y - y);
    }

    public boolean isTopLeft(Position other) {
        return getOffset(other).equals(new Position(-1, -1));
    }

    public boolean isTopRight(Position other) {
        return getOffset(other).equals(new Position(1, -1));
    }

    public boolean isBottomRight(Position other) {
        return getOffset(other).equals(new Position(1, 1));
    }

    public boolean isBottomLeft(Position other) {
        return getOffset(other).equals(new Position(-1, 1));
    }

    /**
     * Checks equality between tho positions.
     * Two {@link Position} objects are equal if both the <code>x</code> coordinates match
     * and the <code>y</code> coordinates match.
     *
     * @param o The reference object with which to compare.
     * @return the result of the equality check.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;
    }

    /**
     * Calculates the hashcode of the position based on the <code>x</code> and <code>y</code> coordinates.
     *
     * @return the calculated hashcode.
     */
    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
