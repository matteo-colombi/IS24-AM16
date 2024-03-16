package it.polimi.ingsw.am16.common.model.cards;

/**
 * Class used to model the card's that can be given to the player to play during the game.
 */
public abstract class PlayableCard extends BoardCard {

    /**
     * Constructs a new playable card with the given id, name, sides and of the given resource type.
     * @param name The card's name.
     * @param frontSide The card's front side.
     * @param backSide The card's back side.
     * @param type The card's resource type.
     */
    public PlayableCard(String name, CardSide frontSide, CardSide backSide, ResourceType type) {
        super(name, frontSide, backSide, type);
    }
}
