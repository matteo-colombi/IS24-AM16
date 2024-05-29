package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to inform the client that a player has disconnected, causing the game to be deleted.
 */
public class SignalGameDeletion extends Payload {

    private final String whoDisconnected;

    /**
     * @param whoDisconnected The username of the player who disconnected, causing the game to be deleted.
     */
    public SignalGameDeletion(String whoDisconnected) {
        this.whoDisconnected = whoDisconnected;
    }

    /**
     * @return The username of the player who disconnected, causing the game to be deleted.
     */
    public String getWhoDisconnected() {
        return whoDisconnected;
    }
}
