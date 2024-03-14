package it.polimi.ingsw.am16.common.model.cards;

/**
 * Class used to model all the cards in the game.
 */
public abstract class Card {
    private final int id;
    private final String name;

    /**
     * Construct a card with the given numerical id and name.
     * @param id the card's numerical id.
     * @param name the card's name.
     */
    public Card(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return The card's numerical id.
     */
    public int getId() {
        return id;
    }

    /**
     * @return The card's name.
     */
    public String getName() {
        return name;
    }
}
