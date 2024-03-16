package it.polimi.ingsw.am16.common.model.cards;

/**
 * Class used to model all the cards in the game.
 */
public abstract class Card {
    private final String name;

    /**
     * Construct a card with the given name.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return name.equals(card.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
