package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server when the common cards from which players can draw from have changed.
 */
public class SetCommonCards extends Payload {
    private final PlayableCard[] commonResourceCards;
    private final PlayableCard[] commonGoldCards;

    /**
     * @param commonResourceCards The common resource cards from which players can draw from.
     * @param commonGoldCards The common gold cards from which players can draw from.
     */
    @JsonCreator
    public SetCommonCards(@JsonProperty("commonResourceCards") PlayableCard[] commonResourceCards, @JsonProperty("commonGoldCards") PlayableCard[] commonGoldCards) {
        this.commonResourceCards = commonResourceCards;
        this.commonGoldCards = commonGoldCards;
    }

    /**
     * @return The common resource cards from which players can draw from.
     */
    public PlayableCard[] getCommonResourceCards() {
        return commonResourceCards;
    }

    /**
     * @return The common gold cards from which players can draw from.
     */
    public PlayableCard[] getCommonGoldCards() {
        return commonGoldCards;
    }
}
