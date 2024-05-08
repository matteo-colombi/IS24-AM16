package it.polimi.ingsw.am16.common.model.cards;

import java.io.Serial;
import java.io.Serializable;

/**
 * Class used to model all the cards in the game.
 */
public abstract class Card implements Serializable {

    @Serial
    private static final long serialVersionUID = -3155645170160408181L;

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

    /**
     * Checks equality between the given object and <code>this</code>.
     * @param o The object to be compared.
     * @return <code>true</code> if the given object is a {@link Card} and has the same name as <code>this</code>.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return name.equals(card.name);
    }

    /**
     * Calculates the card's hashcode based on the card's name.
     * @return the card's hashcode.
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
