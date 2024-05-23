package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import javafx.event.Event;

import java.io.Serial;

public class RemoveCardFromOtherHandEvent extends Event {

    @Serial
    private static final long serialVersionUID = 5425980578824482580L;

    private final String username;
    private final RestrictedCard cardToRemove;

    public RemoveCardFromOtherHandEvent(String username, RestrictedCard cardToRemove) {
        super(GUIEventTypes.REMOVE_CARD_FROM_OTHER_HAND_EVENT);
        this.username = username;
        this.cardToRemove = cardToRemove;
    }

    public String getUsername() {
        return username;
    }

    public RestrictedCard getCardToRemove() {
        return cardToRemove;
    }
}
