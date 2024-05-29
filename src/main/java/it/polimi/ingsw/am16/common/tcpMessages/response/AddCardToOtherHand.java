package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent from the server to inform the client that another player has a new card in their hand.
 */
public class AddCardToOtherHand extends Payload {
    private final String username;
    private final RestrictedCard newCard;

    /**
     *
     * @param username The username of the card whose hand has been updated.
     * @param newCard A restricted view of the new card.
     */
    @JsonCreator
    public AddCardToOtherHand(@JsonProperty("username") String username, @JsonProperty("newCard") RestrictedCard newCard) {
        this.username = username;
        this.newCard = newCard;
    }

    /**
     *
     * @return The username of the card whose hand has been updated.
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return A restricted view of the new card.
     */
    public RestrictedCard getNewCard() {
        return newCard;
    }
}
