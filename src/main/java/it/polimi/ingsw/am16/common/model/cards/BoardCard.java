package it.polimi.ingsw.am16.common.model.cards;

/**
 * TODO write documentation
 */
public abstract class BoardCard extends Card {

    private final CardSide frontSide;
    private final CardSide backSide;

    /**
     * TODO write documentation
     * @param id
     * @param name
     * @param frontSide
     * @param backSide
     */
    public BoardCard(int id, String name, CardSide frontSide, CardSide backSide) {
        super(id, name);
        this.frontSide = frontSide;
        this.backSide = backSide;
    }

    /**
     * TODO write documentation
     * @return
     */
    public CardSide getFrontSide() {
        return frontSide;
    }

    /**
     * TODO write documentation
     * @return
     */
    public CardSide getBackSide() {
        return backSide;
    }
}
