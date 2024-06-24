package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to the client to tell them that a player has been assigned their color.
 */
public class SetColor extends Payload{
    private final String username;
    private final PlayerColor color;

    /**
     * @param username The username of the player whose color is being given.
     * @param color The color of the specified player.
     */
    @JsonCreator
    public SetColor(@JsonProperty("username") String username, @JsonProperty("color") PlayerColor color) {
        this.username = username;
        this.color = color;
    }

    /**
     * @return The username of the player whose color is being given.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return The color of the specified player.
     */
    public PlayerColor getColor() {
        return color;
    }
}
