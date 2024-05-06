package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class ChooseColor extends Payload {
    private final PlayerColor color;

    @JsonCreator
    public ChooseColor(@JsonProperty("color") PlayerColor color) {
        this.color = color;
    }

    public PlayerColor getColor() {
        return color;
    }
}
