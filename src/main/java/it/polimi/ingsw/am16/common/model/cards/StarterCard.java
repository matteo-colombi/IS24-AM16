package it.polimi.ingsw.am16.common.model.cards;

/**
 * Class used to model starter cards.
 */
public class StarterCard extends BoardCard {
    /**
     * Constructs a new starter card with the given numerical id, name and sides.
     * @param name The card's name
     * @param frontSide The card's front side.
     * @param backSide The card's back side.
     */
    public StarterCard(String name, CardSide frontSide, CardSide backSide, ResourceType type) {
        super(name, frontSide, backSide, type);
    }
}
