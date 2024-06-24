package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import javafx.event.Event;

import java.io.Serial;

/**
 * Event fired in the GUI when a new card has to be added to the another player's hand.
 */
public class AddCardToOtherHandEvent extends Event {

    @Serial
    private static final long serialVersionUID = -1711343004840496242L;

    private final String username;
    private final RestrictedCard newCard;

    /**
     * @param username The username of the player whose hand this card should be added to.
     * @param newCard The restricted view of the card that needs to be added to the specified player's hand.
     */
    public AddCardToOtherHandEvent(String username, RestrictedCard newCard) {
        super(GUIEventTypes.ADD_CARD_TO_OTHER_HAND_EVENT);
        this.username = username;
        this.newCard = newCard;
    }

    /**
     * @return The username of the player whose hand this card should be added to.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return The restricted view of the card that needs to be added to the specified player's hand.
     */
    public RestrictedCard getNewCard() {
        return newCard;
    }
}
