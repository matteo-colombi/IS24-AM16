package it.polimi.ingsw.am16.common.model.cards;

/**
 * //TODO write documentation
 */
public abstract class Card {
    private final int id;
    private final String name;

    /**
     * //TODO write documentation
     * @param id
     * @param name
     */
    public Card(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * //TODO write documentation
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * //TODO write documentation
     * @return
     */
    public String getName() {
        return name;
    }
}
