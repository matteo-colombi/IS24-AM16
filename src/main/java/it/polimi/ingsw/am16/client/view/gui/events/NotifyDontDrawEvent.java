package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when the game is about to finish, so cards should no longer be drawn from this point on.
 */
public class NotifyDontDrawEvent extends Event {

    @Serial
    private static final long serialVersionUID = -4942796202168399257L;

    public NotifyDontDrawEvent() {
        super(GUIEventTypes.NOTIFY_DONT_DRAW_EVENT);
    }
}
