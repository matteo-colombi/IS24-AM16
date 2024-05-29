package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent from the server to inform the client that their player's hand no longer has a card.
 */
public class RemoveCardFromHand extends Payload {
    private final PlayableCard card;

    /**
     * @param card The card that was removed from the player's hand.
     */
    @JsonCreator
    public RemoveCardFromHand(@JsonProperty("card") PlayableCard card) {
        this.card = card;
    }

    /**
     * @return The card that was removed from the player's hand.
     */
    public PlayableCard getCard() {
        return card;
    }
}
