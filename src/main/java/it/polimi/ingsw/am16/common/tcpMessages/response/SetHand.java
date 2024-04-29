package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

import java.util.List;

public class SetHand extends Payload {
    private final List<PlayableCard> hand;

    public SetHand(List<PlayableCard> hand) {
        this.hand = hand;
    }
}
