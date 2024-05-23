package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import javafx.event.Event;

import java.io.Serial;

public class SetColorEvent extends Event {

    @Serial
    private static final long serialVersionUID = -8794431100559967434L;

    private final String username;
    private final PlayerColor color;

    public SetColorEvent(String username, PlayerColor color) {
        super(GUIEventTypes.SET_COLOR_EVENT);
        this.username = username;
        this.color = color;
    }

    public String getUsername() {
        return username;
    }

    public PlayerColor getColor() {
        return color;
    }
}
