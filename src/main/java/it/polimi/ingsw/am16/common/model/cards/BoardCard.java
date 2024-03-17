package it.polimi.ingsw.am16.common.model.cards;

/**
 * Class used to model the cards that can be placed on the player's board.
 */
public abstract class BoardCard extends Card {

    private final CardSide frontSide;
    private final CardSide backSide;
    private final ResourceType type;

    /**
     * Constructs a new card with the given name and sides.
     * @param name The card's name.
     * @param frontSide The card's front side.
     * @param backSide The card's back side.
     * @param type The card's type. Set to <code>null</code> for starter cards.
     */
    public BoardCard(String name, CardSide frontSide, CardSide backSide, ResourceType type) {
        super(name);
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.type = type;
    }

    /**
     * @return The card's front side.
     */
    public CardSide getFrontSide() {
        return frontSide;
    }

    /**
     * @return The card's back side.
     */
    public CardSide getBackSide() {
        return backSide;
    }

    /**
     * Returns the card's type.
     * @return The card's type. Returns <code>null</code> if this is a starter card.
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * Returns the corresponding side of this card.
     * @param sideType The side to be returned.
     * @return the card's side corresponding to the <code>sideType</code> given.
     */
    public CardSide getCardSideBySideType(SideType sideType) {
        if (sideType == SideType.FRONT)
            return getFrontSide();

        return getBackSide();
    }
}
