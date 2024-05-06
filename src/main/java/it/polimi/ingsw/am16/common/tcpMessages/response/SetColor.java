package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SetColor extends Payload{
    private final String username;
    private final PlayerColor color;

    @JsonCreator
    public SetColor(@JsonProperty("username") String username, @JsonProperty("color") PlayerColor color) {
        this.username = username;
        this.color = color;
    }

    public String getUsername() {
        return username;
    }

    public PlayerColor getColor() {
        return color;
    }
}
