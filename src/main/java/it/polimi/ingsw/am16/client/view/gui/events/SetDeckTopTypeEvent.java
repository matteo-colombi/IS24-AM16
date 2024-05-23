package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.PlayableCardType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import javafx.event.Event;

import java.io.Serial;

public class SetDeckTopTypeEvent extends Event {

    @Serial
    private static final long serialVersionUID = -4903522413166563449L;

    private final PlayableCardType whichDeck;
    private final ResourceType resourceType;

    public SetDeckTopTypeEvent(PlayableCardType whichDeck, ResourceType resourceType) {
        super(GUIEventTypes.SET_DECK_TOP_TYPE_EVENT);
        this.whichDeck = whichDeck;
        this.resourceType = resourceType;
    }

    public PlayableCardType getWhichDeck() {
        return whichDeck;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }
}
