package it.polimi.ingsw.am16.common.model.cards.decks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;

import java.util.List;

/**
 * Class used to handle decks of objective cards.
 */
public class ObjectiveCardsDeck extends Deck<ObjectiveCard> {

    /**
     * Constructs a new empty ObjectiveCardsDeck.
     */
    public ObjectiveCardsDeck() {
        super();
    }

    /**
     * Constructs a new ObjectiveCardsDeck with the given cards.
     * @param cards The cards to insert in the new ObjectiveCardsDeck.
     */
    @JsonCreator
    public ObjectiveCardsDeck(@JsonProperty("cards") ObjectiveCard[] cards) {
        super();
        setCards(List.of(cards));
    }

    /**
     * Initializes the deck with all the objective cards present in the {@link CardRegistry}.
     */
    @Override
    public void initialize() {
        addCards(CardRegistry.getRegistry().getObjectiveCards());
    }
}
