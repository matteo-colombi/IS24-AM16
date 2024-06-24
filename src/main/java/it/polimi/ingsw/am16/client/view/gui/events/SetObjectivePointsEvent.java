package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when the number of objective points scored by a player is calculated.
 */
public class SetObjectivePointsEvent extends Event {

    @Serial
    private static final long serialVersionUID = -9102165827673286989L;

    private final String username;
    private final int objectivePoints;

    /**
     *
     * @param username The username of the player whose objective points are being given.
     * @param objectivePoints The number of objective points scored by the specified player.
     */
    public SetObjectivePointsEvent(String username, int objectivePoints) {
        super(GUIEventTypes.SET_OBJECTIVE_POINTS_EVENT);
        this.username = username;
        this.objectivePoints = objectivePoints;
    }

    /**
     * @return The username of the player whose objective points are being given.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return The number of objective points scored by the specified player.
     */
    public int getObjectivePoints() {
        return objectivePoints;
    }
}
