package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

public class ChoosingColorsEvent extends Event {

    @Serial
    private static final long serialVersionUID = 7002666614362540059L;

    public ChoosingColorsEvent() {
        super(GUIEventTypes.CHOOSING_COLORS_EVENT);
    }
}
