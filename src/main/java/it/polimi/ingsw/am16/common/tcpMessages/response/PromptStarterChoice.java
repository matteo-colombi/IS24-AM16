package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.StarterCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class PromptStarterChoice extends Payload {
    private final StarterCard starterCard;

    @JsonCreator
    public PromptStarterChoice(@JsonProperty("starterCard") StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    public StarterCard getStarterCard() {
        return starterCard;
    }
}
