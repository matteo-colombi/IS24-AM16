package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when a card has to be removed from the player's hand.
 */
public class RemoveCardFromHandEvent extends Event {

    @Serial
    private static final long serialVersionUID = -865532934190222448L;

    private final PlayableCard card;

    /**
     *
     * @param card The card to be removed.
     */
    public RemoveCardFromHandEvent(PlayableCard card) {
        super(GUIEventTypes.REMOVE_CARD_FROM_HAND_EVENT);
        this.card = card;
    }

    /**
     *
     * @return The card to be removed.
     */
    public PlayableCard getCard() {
        return card;
    }
}
