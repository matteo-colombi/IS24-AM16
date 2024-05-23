package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

public class SetGamePointsEvent extends Event {

    @Serial
    private static final long serialVersionUID = -9102165827673286989L;

    private final String username;
    private final int gamePoints;

    public SetGamePointsEvent(String username, int gamePoints) {
        super(GUIEventTypes.SET_GAME_POINTS_EVENT);
        this.username = username;
        this.gamePoints = gamePoints;
    }

    public String getUsername() {
        return username;
    }

    public int getGamePoints() {
        return gamePoints;
    }
}
