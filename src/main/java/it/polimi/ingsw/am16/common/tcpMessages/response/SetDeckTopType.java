package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.PlayableCardType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SetDeckTopType extends Payload {
    private final PlayableCardType whichDeck;
    private final ResourceType resourceType;

    @JsonCreator
    public SetDeckTopType(@JsonProperty("whichDeck") PlayableCardType whichDeck, @JsonProperty("resourceType") ResourceType resourceType) {
        this.whichDeck = whichDeck;
        this.resourceType = resourceType;
    }

    public PlayableCardType getWhichDeck() {
        return whichDeck;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }
}
