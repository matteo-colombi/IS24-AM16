package it.polimi.ingsw.am16.client.view.gui.events;

import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in GUI when information about a game which is being resumed is done being sent.
 */
public class RejoinInformationStartEvent extends Event {

    @Serial
    private static final long serialVersionUID = -8786802541332131820L;

    public RejoinInformationStartEvent() {
        super(GUIEventTypes.REJOIN_INFORMATION_START_EVENT);
    }    
}
