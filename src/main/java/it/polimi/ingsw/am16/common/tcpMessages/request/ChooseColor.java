package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the client to inform the server that the player has chosen their color.
 */
public class ChooseColor extends Payload {
    private final PlayerColor color;

    /**
     *
     * @param color The chosen color.
     */
    @JsonCreator
    public ChooseColor(@JsonProperty("color") PlayerColor color) {
        this.color = color;
    }

    /**
     *
     * @return The chosen color.
     */
    public PlayerColor getColor() {
        return color;
    }
}
