package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.cards.StarterCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class PromptStarterChoice extends Payload {
    private final StarterCard starterCard;

    public PromptStarterChoice(StarterCard starterCard) {
        this.starterCard = starterCard;
    }
}
