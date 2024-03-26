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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLobbyReloading {

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

        lobbyManager = new LobbyManager();

        lobbyManager.loadGames("src/test/resources/testSaves");
        System.out.println(lobbyManager.getGameIds());
        GameModel reloadedGame = lobbyManager.getGame("YQNL04");

        assertEquals(game.getId(), reloadedGame.getId());
        assertEquals(game.getNumPlayers(), reloadedGame.getNumPlayers());
        assertEquals(game.getCurrentPlayerCount(), reloadedGame.getCurrentPlayerCount());
        assertEquals(game.getActivePlayer(), reloadedGame.getActivePlayer());
        assertEquals(game.getStartingPlayer(), reloadedGame.getStartingPlayer());
        assertEquals(game.getWinnerIds(), reloadedGame.getWinnerIds());
        assertEquals(game.getAvailableColors(), reloadedGame.getAvailableColors());
        assertArrayEquals(game.getPlayers(), reloadedGame.getPlayers());
        assertArrayEquals(game.getCommonObjectiveCards(), reloadedGame.getCommonObjectiveCards());
        assertArrayEquals(game.getCommonGoldCards(), reloadedGame.getCommonGoldCards());
        assertArrayEquals(game.getCommonResourceCards(), reloadedGame.getCommonResourceCards());
        assertEquals(game.getState(), reloadedGame.getState());
        assertEquals(game.getResourceDeckTopType(), reloadedGame.getResourceDeckTopType());
        assertEquals(game.getGoldDeckTopType(), reloadedGame.getGoldDeckTopType());

        Game castedGame = (Game) game;
        Game reloadedCastedGame = (Game) reloadedGame;
        assertEquals(castedGame.getObjectiveCardsDeck(), reloadedCastedGame.getObjectiveCardsDeck());
        assertEquals(castedGame.getStarterCardsDeck(), reloadedCastedGame.getStarterCardsDeck());
        assertEquals(castedGame.getGoldCardsDeck(), reloadedCastedGame.getGoldCardsDeck());
        assertEquals(castedGame.getResourceCardsDeck(), reloadedCastedGame.getResourceCardsDeck());
    }
}
