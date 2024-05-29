package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent form the server to inform the client that their player's hand has a new card.
 */
public class AddCardToHand extends Payload {
    private final PlayableCard card;

    /**
     *
     * @param card The new card.
     */
    @JsonCreator
    public AddCardToHand(@JsonProperty("card") PlayableCard card) {
        this.card = card;
    }

    /**
     *
     * @return The new card.
     */
    public PlayableCard getCard() {
        return card;
    }
}
