package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class RemoveCardFromOtherHand extends Payload {
    private final String username;
    private final RestrictedCard cardToRemove;

    @JsonCreator
    public RemoveCardFromOtherHand(@JsonProperty("username") String username, @JsonProperty("cardToRemove") RestrictedCard cardToRemove) {
        this.username = username;
        this.cardToRemove = cardToRemove;
    }

    public String getUsername() {
        return username;
    }

    public RestrictedCard getCardToRemove() {
        return cardToRemove;
    }
}
