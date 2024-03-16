package it.polimi.ingsw.am16.common.model.cards;

/**
 * Class used to model all the cards in the game.
 */
public abstract class Card {
    private final String name;

    /**
     * Construct a card with the given numerical id and name.
     * @param name the card's name.
     */
    public Card(String name) {
        this.name = name;
    }
    /**
     * @return The card's name.
     */
    public String getName() {
        return name;
    }
}
