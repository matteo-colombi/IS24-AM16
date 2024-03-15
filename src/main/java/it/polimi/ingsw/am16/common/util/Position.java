package it.polimi.ingsw.am16.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Record used to keep track of coordinates on game boards.
 * @param x x-coordinate of the point.
 * @param y y-coordinate of the point.
 */
public record Position(int x, int y) {
    private static final int numNeighbours = 4;
    private static final int[] xDisplacements = {-1, 1, -1, 1};
    private static final int[] yDisplacements = {-1, -1, 1, 1};

    /**
     * Gives the 4 Positions of this Position's neighbours, diagonally from each corner.
     * @return List of the neighbours.
     */
    public List<Position> getNeighbours() {
        List<Position> neighbours = new ArrayList<>();
        for(int i = 0; i<numNeighbours; i++) {
            neighbours.add(new Position(x+xDisplacements[i], y+yDisplacements[i]));
        }
        return neighbours;
    }

    /**
     * Adds this position with the given offset position, obtained by summing each x-coordinate and each y-coordinate.
     * @param offset The offset to add.
     * @return The new position.
     */
    public Position addOffset(Position offset) {
        return new Position(x + offset.x, y + offset.y);
    }

    /**
     * Checks equality between tho positions.
     * Two {@link Position} objects are equal if both the <code>x</code> coordinates match
     * and the <code>y</code> coordinates match.
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
     * @return the calculated hashcode.
     */
    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

}
