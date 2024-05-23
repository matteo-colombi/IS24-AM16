package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import javafx.event.Event;

import java.io.Serial;
import java.util.List;

public class SetOtherHandEvent extends Event {

    @Serial
    private static final long serialVersionUID = 3556876021105529123L;

    private final String username;
    private final List<RestrictedCard> hand;

    public SetOtherHandEvent(String username, List<RestrictedCard> hand) {
        super(GUIEventTypes.SET_OTHER_HAND_EVENT);
        this.username = username;
        this.hand = hand;
    }

    public String getUsername() {
        return username;
    }

    public List<RestrictedCard> getHand() {
        return hand;
    }
}
