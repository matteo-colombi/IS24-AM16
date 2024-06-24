package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import javafx.event.Event;

import java.io.Serial;
import java.util.List;

/**
 * Event that is fired in the GUI when the another player's restricted view of the hand of cards is being communicated by the server.
 */
public class SetOtherHandEvent extends Event {

    @Serial
    private static final long serialVersionUID = 3556876021105529123L;

    private final String username;
    private final List<RestrictedCard> hand;

    /**
     *
     * @param username The username of the player whose hand is being given.
     * @param hand The restricted view of the specified player's hand.
     */
    public SetOtherHandEvent(String username, List<RestrictedCard> hand) {
        super(GUIEventTypes.SET_OTHER_HAND_EVENT);
        this.username = username;
        this.hand = hand;
    }

    /**
     * @return The username of the player whose hand is being given.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return The restricted view of the specified player's hand.
     */
    public List<RestrictedCard> getHand() {
        return hand;
    }
}
