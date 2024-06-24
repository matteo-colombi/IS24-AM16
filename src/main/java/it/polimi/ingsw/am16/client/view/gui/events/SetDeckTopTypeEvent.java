package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.PlayableCardType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when the type of the card on top of a deck changes.
 */
public class SetDeckTopTypeEvent extends Event {

    @Serial
    private static final long serialVersionUID = -4903522413166563449L;

    private final PlayableCardType whichDeck;
    private final ResourceType resourceType;

    /**
     *
     * @param whichDeck The deck on top of which the given card type is to be found.
     * @param resourceType The type of the card on top of the specified deck.
     */
    public SetDeckTopTypeEvent(PlayableCardType whichDeck, ResourceType resourceType) {
        super(GUIEventTypes.SET_DECK_TOP_TYPE_EVENT);
        this.whichDeck = whichDeck;
        this.resourceType = resourceType;
    }

    /**
     *
     * @return The deck on top of which the given card type is to be found.
     */
    public PlayableCardType getWhichDeck() {
        return whichDeck;
    }

    /**
     *
     * @return The type of the card on top of the specified deck.
     */
    public ResourceType getResourceType() {
        return resourceType;
    }
}
