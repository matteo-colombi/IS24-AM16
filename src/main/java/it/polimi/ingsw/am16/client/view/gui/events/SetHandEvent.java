package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import javafx.event.Event;

import java.io.Serial;
import java.util.List;

public class SetHandEvent extends Event {

    @Serial
    private static final long serialVersionUID = -2509371787909762614L;

    private final List<PlayableCard> hand;

    public SetHandEvent(List<PlayableCard> hand) {
        super(GUIEventTypes.SET_HAND_EVENT);
        this.hand = hand;
    }

    public List<PlayableCard> getHand() {
        return hand;
    }
}
