package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class RemoveCardFromHand extends Payload {
    private final PlayableCard card;

    @JsonCreator
    public RemoveCardFromHand(@JsonProperty("card") PlayableCard card) {
        this.card = card;
    }

    public PlayableCard getCard() {
        return card;
    }
}
