package it.polimi.ingsw.am16.common.model.lobby;

import it.polimi.ingsw.am16.common.exceptions.NoStarterCardException;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.Game;
import it.polimi.ingsw.am16.common.model.game.GameModel;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.RNG;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestLobby {

    @Test
    public void testLobby() throws UnexpectedActionException, NoStarterCardException, UnknownObjectiveCardException, IOException {
        CardRegistry.initializeRegistry();
        RNG.setRNGSeed(0);
        LobbyManager lobbyManager = new LobbyManager();
        String newId = lobbyManager.createGame(2);
        GameModel game = lobbyManager.getGame(newId);
        game.addPlayer("leonardo");
        game.addPlayer("andrea");
        game.initializeGame();
        game.setPlayerStarterSide(0, SideType.FRONT);
        game.setPlayerStarterSide(1, SideType.FRONT);
        game.setPlayerColor(0, PlayerColor.BLUE);
        game.setPlayerColor(1, PlayerColor.RED);
        game.initializeObjectives();
        game.setPlayerObjective(0, game.getPlayers()[0].getPersonalObjectiveOptions().getFirst());
        lobbyManager.saveGames("src/test/resources/testSaves");
    }
}
