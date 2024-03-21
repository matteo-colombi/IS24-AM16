package it.polimi.ingsw.am16.common.model.lobby;

import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.model.game.Game;
import it.polimi.ingsw.am16.common.model.game.GameModel;

/**
 * Class to handle a lobby, containing a game and the chat for that lobby.
 */
public class Lobby {
    private final String id;
    private final GameModel game;

    /**
     * Constructs a new lobby with the given id and number of players.
     * @param id The lobby's id.
     * @param numPlayers The number of players of this lobby.
     */
    public Lobby(String id, int numPlayers) {
        this.id = id;
        game = new Game(id, numPlayers);
    }

    /**
     * @return The lobby's id.
     */
    public String getLobbyId() {
        return id;
    }

    public GameModel getGame() {
        return game;
    }

    /**
     * Adds a player to the lobby.
     * @param username The new player's username.
     * @return The newly added player's id.
     * @throws UnexpectedActionException If the lobby is full, or if the game in this lobby has already started.
     */
    public int addPlayer(String username) throws UnexpectedActionException {
        return game.addPlayer(username);
    }

    /**
     * @return The maximum number of players allowed in this lobby.
     */
    public int getNumPlayers() {
        return game.getNumPlayers();
    }

    /**
     * @return The current number of players in this lobby.
     */
    public int getCurrentPlayerCount() {
        return game.getCurrentPlayerCount();
    }
}