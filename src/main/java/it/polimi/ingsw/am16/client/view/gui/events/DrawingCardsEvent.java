package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when cards are being drawn for the game.
 */
public class DrawingCardsEvent extends Event {

    @Serial
    private static final long serialVersionUID = -2338969884661936452L;

    public DrawingCardsEvent() {
        super(GUIEventTypes.DRAWING_CARDS_EVENT);
    }
}
