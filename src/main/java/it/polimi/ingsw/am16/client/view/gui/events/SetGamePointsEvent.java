package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when the number of game points of a player changes.
 */
public class SetGamePointsEvent extends Event {

    @Serial
    private static final long serialVersionUID = -9102165827673286989L;

    private final String username;
    private final int gamePoints;

    /**
     *
     * @param username The username of the player whose game points are being given.
     * @param gamePoints The number of game points of the specified player.
     */
    public SetGamePointsEvent(String username, int gamePoints) {
        super(GUIEventTypes.SET_GAME_POINTS_EVENT);
        this.username = username;
        this.gamePoints = gamePoints;
    }

    /**
     *
     * @return The username of the player whose game points are being given.
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return The number of game points of the specified player.
     */
    public int getGamePoints() {
        return gamePoints;
    }
}
