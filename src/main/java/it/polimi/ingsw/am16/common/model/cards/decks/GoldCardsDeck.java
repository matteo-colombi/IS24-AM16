package it.polimi.ingsw.am16.common.model.cards.decks;

import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.GoldCard;

/**
 * Class used to handle decks of gold cards.
 */
public class GoldCardsDeck extends Deck<GoldCard> {

    /**
     * Initializes the deck with all the gold cards present in the {@link CardRegistry}.
     */
    @Override
    public void initialize() {
        addCards(CardRegistry.getGoldCards());
    }
}
