package it.polimi.ingsw.am16.common.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.io.StringWriter;
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
    private static final int[] yDisplacements = {1, 1, -1, -1};

    /**
     * Gives the 4 Positions of this Position's neighbours, diagonally from each corner.
     *
     * @return List of the neighbours.
     */
    @JsonIgnore
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

    /**
     * Calculates the component-based difference of the two positions.
     * @param other The other position.
     * @return the difference of the two positions.
     */
    public Position getOffset(Position other) {
        return new Position(other.x - x, other.y - y);
    }

    /**
     * Checks whether the given point is in the top left corner of <code>this</code>.
     * @param other The point to compare.
     * @return <code>true</code> if other is in the top left corner, <code>false</code> otherwise.
     */
    public boolean neighbourIsTopLeft(Position other) {
        return getOffset(other).equals(new Position(-1, 1));
    }

    /**
     * Checks whether the given point is in the top right corner of <code>this</code>.
     * @param other The point to compare.
     * @return <code>true</code> if other is in the top right corner, <code>false</code> otherwise.
     */
    public boolean neighbourIsTopRight(Position other) {
        return getOffset(other).equals(new Position(1, 1));
    }

    /**
     * Checks whether the given point is in the bottom right corner of <code>this</code>.
     * @param other The point to compare.
     * @return <code>true</code> if other is in the bottom right corner, <code>false</code> otherwise.
     */
    public boolean neighbourIsBottomRight(Position other) {
        return getOffset(other).equals(new Position(1, -1));
    }

    /**
     * Checks whether the given point is in the bottom left corner of <code>this</code>.
     * @param other The point to compare.
     * @return <code>true</code> if other is in the bottom left corner, <code>false</code> otherwise.
     */
    public boolean neighbourIsBottomLeft(Position other) {
        return getOffset(other).equals(new Position(-1, -1));
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

    @Override
    public String toString() {
        return String.format("Position{x = %d, y = %d}", x, y);
    }

    /**
     * DOCME
     */
    public static class PositionSerializer extends JsonSerializer<Position> {
        private ObjectMapper mapper = new ObjectMapper();

        /**
         * DOCME
         * @param value Value to serialize; can <b>not</b> be null.
         * @param gen Generator used to output resulting Json content
         * @param serializers Provider that can be used to get serializers for
         *   serializing Objects value contains, if any.
         * @throws IOException
         */
        @Override
        public void serialize(Position value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            StringWriter writer = new StringWriter();
            mapper.writeValue(writer, value);
            gen.writeFieldName(writer.toString());
        }
    }
}
