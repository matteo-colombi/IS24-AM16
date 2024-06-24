package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent from the server to inform the client that another player no longer has a card in their hand.
 */
public class RemoveCardFromOtherHand extends Payload {
    private final String username;
    private final RestrictedCard cardToRemove;

    /**
     *
     * @param username The username of the player from whose hand the card has been removed.
     * @param cardToRemove A restricted view of the card to be removed.
     */
    @JsonCreator
    public RemoveCardFromOtherHand(@JsonProperty("username") String username, @JsonProperty("cardToRemove") RestrictedCard cardToRemove) {
        this.username = username;
        this.cardToRemove = cardToRemove;
    }

    /**
     * @return The username of the player from whose hand the card has been removed.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return A restricted view of the card to be removed.
     */
    public RestrictedCard getCardToRemove() {
        return cardToRemove;
    }
}
