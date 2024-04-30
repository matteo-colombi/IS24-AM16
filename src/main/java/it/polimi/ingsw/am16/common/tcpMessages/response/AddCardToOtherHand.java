package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class AddCardToOtherHand extends Payload {
    private final String username;
    private final RestrictedCard newCard;

    @JsonCreator
    public AddCardToOtherHand(@JsonProperty("username") String username, @JsonProperty("newCard") RestrictedCard newCard) {
        this.username = username;
        this.newCard = newCard;
    }

    public String getUsername() {
        return username;
    }

    public RestrictedCard getNewCard() {
        return newCard;
    }
}
