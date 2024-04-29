package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

import java.util.List;

public class SetOtherHand extends Payload {
    private final String username;
    private final List<RestrictedCard> hand;

    public SetOtherHand(String username, List<RestrictedCard> hand) {
        this.username = username;
        this.hand = hand;
    }
}
