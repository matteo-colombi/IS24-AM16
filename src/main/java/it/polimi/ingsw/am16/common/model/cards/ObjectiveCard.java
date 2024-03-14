package it.polimi.ingsw.am16.common.model.cards;

import it.polimi.ingsw.am16.common.model.PlayArea;

public abstract class ObjectiveCard extends Card {
    private final int points;

    public ObjectiveCard(int id, String name, int points) {
        super(id, name);
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public abstract int evaluatePoints(PlayArea playArea);
}
