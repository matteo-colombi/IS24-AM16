package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI to tell the view which cards the players can draw from.
 */
public class SetCommonCardsEvent extends Event {

    @Serial
    private static final long serialVersionUID = -2712413324210972786L;

    private final PlayableCard[] commonResourceCards;
    private final PlayableCard[] commonGoldCards;

    /**
     *
     * @param commonResourceCards The resource cards from which the players can draw from.
     * @param commonGoldCards The gold cards from which the players can draw from.
     */
    public SetCommonCardsEvent(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) {
        super(GUIEventTypes.SET_COMMON_CARDS_EVENT);
        this.commonResourceCards = commonResourceCards;
        this.commonGoldCards = commonGoldCards;
    }

    /**
     * @return The resource cards from which the players can draw from.
     */
    public PlayableCard[] getCommonResourceCards() {
        return commonResourceCards;
    }

    /**
     *
     * @return The gold cards from which the players can draw from.
     */
    public PlayableCard[] getCommonGoldCards() {
        return commonGoldCards;
    }
}
