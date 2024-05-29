package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the client to inform the server that the player has chosen on which side to play their starter card.
 */
public class ChooseStarterSide extends Payload {
    private final SideType side;

    /**
     *
     * @param side The side on which the player chose to place their starter card.
     */
    @JsonCreator
    public ChooseStarterSide(@JsonProperty("side") SideType side) {
        this.side = side;
    }

    /**
     *
     * @return The side on which the player chose to place their starter card.
     */
    public SideType getSide() {
        return side;
    }
}
