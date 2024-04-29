package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.cards.PlayableCardType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SetDeckTopType extends Payload {
    private final PlayableCardType whichDeck;
    private final ResourceType resourceType;

    public SetDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) {
        this.whichDeck = whichDeck;
        this.resourceType = resourceType;
    }
}
