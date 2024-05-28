package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import javafx.event.Event;

import java.io.Serial;

/**
 * Event fired in the GUI when a new card has to be added to the player's hand.
 */
public class AddCardToHandEvent extends Event {

    @Serial
    private static final long serialVersionUID = -5835085031097763281L;

    private final PlayableCard card;

    /**
     * @param card The card to be added to the player's hand.
     */
    public AddCardToHandEvent(PlayableCard card) {
        super(GUIEventTypes.ADD_CARD_TO_HAND_EVENT);
        this.card = card;
    }

    /**
     * @return The card to be added to the player's hand.
     */
    public PlayableCard getCard() {
        return card;
    }
}
