package it.polimi.ingsw.am16.common.model.cards.decks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.ResourceCard;

import java.util.List;

/**
 * Class used to handle decks of resource cards.
 */
public class ResourceCardsDeck extends Deck<ResourceCard> {

    /**
     * DOCME
     */
    public ResourceCardsDeck() {
        super();
    }

    /**
     * DOCME
     * @param cards
     */
    @JsonCreator
    public ResourceCardsDeck(@JsonProperty("cards") ResourceCard[] cards) {
        super();
        setCards(List.of(cards));
    }

    /**
     * Initializes the deck with all the resource cards present in the {@link CardRegistry}.
     */
    @Override
    public void initialize() {
        addCards(CardRegistry.getResourceCards());
    }
}
