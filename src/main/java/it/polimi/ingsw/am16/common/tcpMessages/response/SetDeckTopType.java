package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.PlayableCardType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to inform the client about the types of cards that are on top of the decks.
 */
public class SetDeckTopType extends Payload {
    private final PlayableCardType whichDeck;
    private final ResourceType resourceType;

    /**
     * @param whichDeck The deck whose top card type is being given.
     * @param resourceType The type of the card on top of the specified deck.
     */
    @JsonCreator
    public SetDeckTopType(@JsonProperty("whichDeck") PlayableCardType whichDeck, @JsonProperty("resourceType") ResourceType resourceType) {
        this.whichDeck = whichDeck;
        this.resourceType = resourceType;
    }

    /**
     * @return The deck whose top card type is being given.
     */
    public PlayableCardType getWhichDeck() {
        return whichDeck;
    }

    /**
     * @return The type of the card on top of the specified deck.
     */
    public ResourceType getResourceType() {
        return resourceType;
    }
}
