package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when a player has chosen their color.
 */
public class SetColorEvent extends Event {

    @Serial
    private static final long serialVersionUID = -8794431100559967434L;

    private final String username;
    private final PlayerColor color;

    /**
     *
     * @param username The username of the player whose color is being given.
     * @param color The color chosen by the specified player.
     */
    public SetColorEvent(String username, PlayerColor color) {
        super(GUIEventTypes.SET_COLOR_EVENT);
        this.username = username;
        this.color = color;
    }

    /**
     *
     * @return The username of the player whose color is being given.
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return The color chosen by the specified player.
     */
    public PlayerColor getColor() {
        return color;
    }
}
