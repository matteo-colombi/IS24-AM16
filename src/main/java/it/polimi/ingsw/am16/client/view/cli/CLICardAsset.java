package it.polimi.ingsw.am16.client.view.cli;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.SideType;

/**
 * Record that holds the front and back for a card.
 * @param front The card's front asset.
 * @param back The card's back asset.
 */
public record CLICardAsset(@JsonProperty("front") CLIText front, @JsonProperty("back") CLIText back) {

    /**
     * Returns the asset for the requested side.
     * @param sideType The side type.
     * @return The requested side.
     */
    public CLIText getSide(SideType sideType) {
        if (sideType == SideType.FRONT)
            return front;
        return back;
    }

    /**
     * @return A string representation of the card asset. Used for debugging.
     */
    @Override
    public String toString() {
        return "CLIAsset{" +
                "front=" + front +
                ", back=" + back +
                '}';
    }
}
