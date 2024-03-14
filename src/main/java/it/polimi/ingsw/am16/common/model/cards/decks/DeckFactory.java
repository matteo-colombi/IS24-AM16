package it.polimi.ingsw.am16.common.model.cards.decks;

/**
 * Utility class to obtain new, already shuffled decks.
 */
public class DeckFactory {

    /**
     * Instantiates, initializes and shuffles a new {@link ObjectiveCardsDeck}.
     * @return The created deck.
     */
    public static ObjectiveCardsDeck getObjectiveCardsDeck() {
        ObjectiveCardsDeck deck = new ObjectiveCardsDeck();
        deck.initialize();
        deck.shuffle();
        return deck;
    }

    /**
     * Instantiates, initializes and shuffles a new {@link StarterCardsDeck}.
     * @return The created deck.
     */
    public static StarterCardsDeck getStarterCardsDeck() {
        StarterCardsDeck deck = new StarterCardsDeck();
        deck.initialize();
        deck.shuffle();
        return deck;
    }

    /**
     * Instantiates, initializes and shuffles a new {@link ResourceCardsDeck}.
     * @return The created deck.
     */
    public static ResourceCardsDeck getResourceCardsDeck() {
        ResourceCardsDeck deck = new ResourceCardsDeck();
        deck.initialize();
        deck.shuffle();
        return deck;
    }

    /**
     * Instantiates, initializes and shuffles a new {@link GoldCardsDeck}.
     * @return The created deck.
     */
    public static GoldCardsDeck getGoldCardsDeck() {
        GoldCardsDeck deck = new GoldCardsDeck();
        deck.initialize();
        deck.shuffle();
        return deck;
    }
}
