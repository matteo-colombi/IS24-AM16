package it.polimi.ingsw.am16.common.model.cards;

public class StarterCard extends BoardCard{
    /**
     * TODO write documentation
     * This method constructs a starter card.
     * Only 1 starter card must be in play for each player
     *
     * @param id the identifier of the card
     * @param name
     * @param frontSide the front side of the starter card
     * @param backSide the back side of the starter card
     */
    public StarterCard(int id, String name, CardSide frontSide, CardSide backSide) {
        super(id, name, frontSide, backSide);
    }
}
