package it.polimi.ingsw.am16.common.model.cards.decks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.StarterCard;

import java.util.List;

/**
 * Class used to handle decks of starter cards.
 */
public class StarterCardsDeck extends Deck<StarterCard> {

    /**
     * Constructs a new empty StarterCardsDeck.
     */
    public StarterCardsDeck() {
        super();
    }

    /**
     * Constructs a new StarterCardsDeck with the given cards.
     * @param cards The cards to insert in the new StarterCardsDeck.
     */
    @JsonCreator
    public StarterCardsDeck(@JsonProperty("cards") StarterCard[] cards) {
        super();
        setCards(List.of(cards));
    }

    /**
     * Initializes the deck with all the starter cards present in the {@link CardRegistry}.
     */
    @Override
    public void initialize() {
        addCards(CardRegistry.getStarterCards());
    }
}
