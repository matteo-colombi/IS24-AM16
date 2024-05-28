package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when a card has to be removed from another player's hand.
 */
public class RemoveCardFromOtherHandEvent extends Event {

    @Serial
    private static final long serialVersionUID = 5425980578824482580L;

    private final String username;
    private final RestrictedCard cardToRemove;

    /**
     *
     * @param username The username from whose hand the card should be removed.
     * @param cardToRemove The restricted view of the card to be removed.
     */
    public RemoveCardFromOtherHandEvent(String username, RestrictedCard cardToRemove) {
        super(GUIEventTypes.REMOVE_CARD_FROM_OTHER_HAND_EVENT);
        this.username = username;
        this.cardToRemove = cardToRemove;
    }

    /**
     *
     * @return The username from whose hand the card should be removed.
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return The restricted view of the card to be removed.
     */
    public RestrictedCard getCardToRemove() {
        return cardToRemove;
    }
}
