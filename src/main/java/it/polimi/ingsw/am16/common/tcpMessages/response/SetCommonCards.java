package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SetCommonCards extends Payload {
    private final PlayableCard[] commonResourceCards;
    private final PlayableCard[] commonGoldCards;

    @JsonCreator
    public SetCommonCards(@JsonProperty("commonResourceCards") PlayableCard[] commonResourceCards, @JsonProperty("commonGoldCards") PlayableCard[] commonGoldCards) {
        this.commonResourceCards = commonResourceCards;
        this.commonGoldCards = commonGoldCards;
    }

    public PlayableCard[] getCommonResourceCards() {
        return commonResourceCards;
    }

    public PlayableCard[] getCommonGoldCards() {
        return commonGoldCards;
    }
}
