package it.polimi.ingsw.am16.common.model.lobby;

import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.model.game.Game;
import it.polimi.ingsw.am16.common.model.game.GameModel;

//DOCME
public class Lobby {
    private final GameModel game;
    private final String id;

    public Lobby(String id, int numPlayers) {
        this.id = id;
        game = new Game(id, numPlayers);
    }

    public String getLobbyId() {
        return id;
    }

    public void addPlayer(String username) throws UnexpectedActionException {
        game.addPlayer(username);
    }

    public int getNumPlayers() {
        return game.getNumPlayers();
    }

    public int getCurrentPlayerCount() {
        return game.getCurrentPlayerCount();
    }
}