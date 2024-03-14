package it.polimi.ingsw.am16.common.model.cards;

/**
 * Class used to model resource cards.
 */
public final class ResourceCard extends PlayableCard {

    /**
     * Constructs a new resource card with the given numerical id, name, sides and resource type.
     * @param id The card's numerical id.
     * @param name The card's name.
     * @param frontSide The card's front side.
     * @param backSide The card's back side.
     * @param type The card's resource type.
     */
    public ResourceCard(int id, String name, CardSide frontSide, CardSide backSide, ResourceType type) {
        super(id, name, frontSide, backSide, type);
    }
}
