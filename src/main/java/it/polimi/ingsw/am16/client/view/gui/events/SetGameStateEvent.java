package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.game.GameState;
import javafx.event.Event;

import java.io.Serial;

public class SetGameStateEvent extends Event {

    @Serial
    private static final long serialVersionUID = -8925643157856129979L;

    private final GameState state;

    public SetGameStateEvent(GameState state) {
        super(GUIEventTypes.SET_GAME_STATE_EVENT);
        this.state = state;
    }

    public GameState getState() {
        return state;
    }
}
