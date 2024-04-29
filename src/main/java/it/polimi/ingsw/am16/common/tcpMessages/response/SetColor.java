package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

public class SetColor extends Payload{
    private final String username;
    private final PlayerColor color;

    public SetColor(String username, PlayerColor color) {
        this.username = username;
        this.color = color;
    }
}
