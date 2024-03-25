package it.polimi.ingsw.am16.common.model.cards.decks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.GoldCard;

import java.util.List;

/**
 * Class used to handle decks of gold cards.
 */
public class GoldCardsDeck extends Deck<GoldCard> {

    /**
     * DOCME
     */
    public GoldCardsDeck() {
        super();
    }

    /**
     * DOCME
     * @param cards
     */
    @JsonCreator
    public GoldCardsDeck(@JsonProperty("cards") GoldCard[] cards) {
        super();
        setCards(List.of(cards));
    }

    /**
     * Initializes the deck with all the gold cards present in the {@link CardRegistry}.
     */
    @Override
    public void initialize() {
        addCards(CardRegistry.getGoldCards());
    }
}
