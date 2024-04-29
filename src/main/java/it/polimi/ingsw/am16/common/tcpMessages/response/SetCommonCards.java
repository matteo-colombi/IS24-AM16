package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SetCommonCards extends Payload {
    private final PlayableCard[] commonResourceCards;
    private final PlayableCard[] commonGoldCards;

    public SetCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) {
        this.commonResourceCards = commonResourceCards;
        this.commonGoldCards = commonGoldCards;
    }
}
