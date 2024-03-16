package it.polimi.ingsw.am16.common.model.cards.decks;

import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.ResourceCard;

/**
 * Class used to handle decks of resource cards.
 */
public class ResourceCardsDeck extends Deck<ResourceCard> {

    /**
     * Initializes the deck with all the resource cards present in the {@link CardRegistry}.
     */
    @Override
    public void initialize() {
        addCards(CardRegistry.getResourceCards());
    }
}
