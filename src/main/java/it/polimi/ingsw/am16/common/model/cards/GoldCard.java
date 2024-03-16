package it.polimi.ingsw.am16.common.model.cards;

/**
 * Class used to model gold cards.
 */
public final class GoldCard extends PlayableCard {

    /**
     * Constructs a new gold card with the given id, name, sides and resource type.
     * @param name The card's name.
     * @param frontSide The card's front side.
     * @param backSide The card's back side.
     * @param type The card's resource type.
     */
    public GoldCard(String name, CardSide frontSide, CardSide backSide, ResourceType type) {
        super(name, frontSide, backSide, type);
    }
}
