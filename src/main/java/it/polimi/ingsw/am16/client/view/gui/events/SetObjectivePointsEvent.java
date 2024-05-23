package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

public class SetObjectivePointsEvent extends Event {

    @Serial
    private static final long serialVersionUID = -9102165827673286989L;

    private final String username;
    private final int objectivePoints;

    public SetObjectivePointsEvent(String username, int objectivePoints) {
        super(GUIEventTypes.SET_OBJECTIVE_POINTS_EVENT);
        this.username = username;
        this.objectivePoints = objectivePoints;
    }

    public String getUsername() {
        return username;
    }

    public int getObjectivePoints() {
        return objectivePoints;
    }
}
