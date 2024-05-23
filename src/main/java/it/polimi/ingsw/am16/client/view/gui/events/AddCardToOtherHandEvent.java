package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import javafx.event.Event;

import java.io.Serial;

public class AddCardToOtherHandEvent extends Event {

    @Serial
    private static final long serialVersionUID = -1711343004840496242L;

    private final String username;
    private final RestrictedCard newCard;

    public AddCardToOtherHandEvent(String username, RestrictedCard newCard) {
        super(GUIEventTypes.ADD_CARD_TO_OTHER_HAND_EVENT);
        this.username = username;
        this.newCard = newCard;
    }

    public String getUsername() {
        return username;
    }

    public RestrictedCard getNewCard() {
        return newCard;
    }
}
