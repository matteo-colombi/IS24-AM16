package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class RemoveCardFromOtherHand extends Payload {
    private final String username;
    private final RestrictedCard cardToRemove;

    public RemoveCardFromOtherHand(String username, RestrictedCard cardToRemove) {
        this.username = username;
        this.cardToRemove = cardToRemove;
    }
}
