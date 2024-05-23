package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import javafx.event.Event;

import java.io.Serial;

public class RemoveCardFromHandEvent extends Event {

    @Serial
    private static final long serialVersionUID = -865532934190222448L;

    private final PlayableCard card;

    public RemoveCardFromHandEvent(PlayableCard card) {
        super(GUIEventTypes.REMOVE_CARD_FROM_HAND_EVENT);
        this.card = card;
    }

    public PlayableCard getCard() {
        return card;
    }
}
