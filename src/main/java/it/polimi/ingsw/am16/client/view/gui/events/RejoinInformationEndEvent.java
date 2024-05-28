package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when information about a game which is being resumed is about to be sent.
 */
public class RejoinInformationEndEvent extends Event {

    @Serial
    private static final long serialVersionUID = 141915731595851052L;

    public RejoinInformationEndEvent() {
        super(GUIEventTypes.REJOIN_INFORMATION_END_EVENT);
    }
}
