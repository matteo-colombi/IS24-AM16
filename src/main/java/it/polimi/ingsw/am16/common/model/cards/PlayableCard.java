package it.polimi.ingsw.am16.common.model.cards;

/**
 * TODO write documentation
 */
public abstract class PlayableCard extends BoardCard {

    private final ResourceType type;

    /**
     * TODO write documentation
     * @param id
     * @param name
     * @param frontSide
     * @param backSide
     * @param type
     */
    public PlayableCard(int id, String name, CardSide frontSide, CardSide backSide, ResourceType type) {
        super(id, name, frontSide, backSide);
        this.type = type;
    }

    /**
     * TODO write documentation
     * @return
     */
    public ResourceType getType() {
        return type;
    }
}
