package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class AddCardToOtherHand extends Payload {
    private final String username;
    private final RestrictedCard newCard;

    public AddCardToOtherHand(String username, RestrictedCard newCard) {
        this.username = username;
        this.newCard = newCard;
    }
}
