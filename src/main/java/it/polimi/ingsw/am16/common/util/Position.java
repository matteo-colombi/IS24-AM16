package it.polimi.ingsw.am16.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO write documentation
 * @param x
 * @param y
 */
public record Position(int x, int y) {
    private static final int numNeighbours = 4;
    private static final int[] xDisplacements = {-1, 1, -1, 1};
    private static final int[] yDisplacements = {-1, -1, 1, 1};

    /**
     * TODO write documentation
     * @return
     */
    public List<Position> getNeighbours() {
        List<Position> neighbours = new ArrayList<>();
        for(int i = 0; i<numNeighbours; i++) {
            neighbours.add(new Position(x+xDisplacements[i], y+yDisplacements[i]));
        }
        return neighbours;
    }

    /**
     * TODO write documentation
     * @param offset
     * @return
     */
    public Position addOffset(Position offset) {
        return new Position(x + offset.x, y + offset.y);
    }

}
