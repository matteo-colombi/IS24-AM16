package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

public class DrawingCardsEvent extends Event {

    @Serial
    private static final long serialVersionUID = -2338969884661936452L;

    public DrawingCardsEvent() {
        super(GUIEventTypes.DRAWING_CARDS_EVENT);
    }
}
