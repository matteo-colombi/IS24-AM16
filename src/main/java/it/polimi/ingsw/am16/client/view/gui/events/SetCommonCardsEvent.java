package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import javafx.event.Event;

import java.io.Serial;

public class SetCommonCardsEvent extends Event {

    @Serial
    private static final long serialVersionUID = -2712413324210972786L;

    private final PlayableCard[] commonResourceCards;
    private final PlayableCard[] commonGoldCards;

    public SetCommonCardsEvent(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) {
        super(GUIEventTypes.SET_COMMON_CARDS_EVENT);
        this.commonResourceCards = commonResourceCards;
        this.commonGoldCards = commonGoldCards;
    }

    public PlayableCard[] getCommonResourceCards() {
        return commonResourceCards;
    }

    public PlayableCard[] getCommonGoldCards() {
        return commonGoldCards;
    }
}
