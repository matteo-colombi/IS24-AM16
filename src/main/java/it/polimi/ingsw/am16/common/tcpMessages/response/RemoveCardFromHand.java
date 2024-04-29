package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class RemoveCardFromHand extends Payload {
    private final PlayableCard card;

    public RemoveCardFromHand(PlayableCard card) {
        this.card = card;
    }
}
