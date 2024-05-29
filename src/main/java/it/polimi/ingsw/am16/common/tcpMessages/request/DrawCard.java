package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the client to the server to draw a card.
 */
public class DrawCard extends Payload {
    private final DrawType drawType;

    /**
     *
     * @param drawType What kind of draw the player wants to perform.
     */
    @JsonCreator
    public DrawCard(@JsonProperty("drawType") DrawType drawType) {
        this.drawType = drawType;
    }

    /**
     *
     * @return What kind of draw the player wants to perform.
     */
    public DrawType getDrawType() {
        return drawType;
    }
}
