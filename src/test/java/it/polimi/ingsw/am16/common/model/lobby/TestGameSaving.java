package it.polimi.ingsw.am16.common.model.lobby;

import it.polimi.ingsw.am16.common.exceptions.NoStarterCardException;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.Game;
import it.polimi.ingsw.am16.common.model.game.GameModel;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.FilePaths;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.util.RNG;
import it.polimi.ingsw.am16.server.controller.GameController;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGameSaving {

    @Test
    public void testLobby() throws UnexpectedActionException, NoStarterCardException, UnknownObjectiveCardException, IOException, InterruptedException {
        CardRegistry.getRegistry();
        RNG.setRNGSeed(0);

        Game game = new Game("ABCD", 2);

        game.addPlayer("leonardo");
        game.addPlayer("andrea");
        game.setConnected("leonardo", true);
        game.setConnected("andrea", true);
        game.initializeGame();
        game.setPlayerStarterSide("leonardo", SideType.FRONT);
        game.setPlayerStarterSide("andrea", SideType.FRONT);
        game.setPlayerColor("leonardo", PlayerColor.BLUE);
        game.setPlayerColor("andrea", PlayerColor.RED);
        game.initializeObjectives();
        game.setPlayerObjective("leonardo", game.getPlayers().get("leonardo").getPersonalObjectiveOptions().getFirst());

        String directoryPath = "src/test/resources/json/testSaves";
        String filePath = directoryPath + "/ABCD.json";
        File directory = new File(directoryPath);
        directory.mkdirs();
        JsonMapper.getObjectMapper().writeValue(new File(filePath), game);

        Game reloadedGame = JsonMapper.getObjectMapper().readValue(new File(filePath), Game.class);

        assertEquals(game.getId(), reloadedGame.getId());
        assertEquals(game.getNumPlayers(), reloadedGame.getNumPlayers());
        assertEquals(0, reloadedGame.getCurrentPlayerCount());
        assertEquals(game.getActivePlayer(), reloadedGame.getActivePlayer());
        assertEquals(game.getStartingPlayer(), reloadedGame.getStartingPlayer());
        assertEquals(game.getWinnerUsernames(), reloadedGame.getWinnerUsernames());
        assertEquals(game.getAvailableColors(), reloadedGame.getAvailableColors());
        assertEquals(game.getPlayers(), reloadedGame.getPlayers());
        assertArrayEquals(game.getCommonObjectiveCards(), reloadedGame.getCommonObjectiveCards());
        assertArrayEquals(game.getCommonGoldCards(), reloadedGame.getCommonGoldCards());
        assertArrayEquals(game.getCommonResourceCards(), reloadedGame.getCommonResourceCards());
        assertEquals(game.getState(), reloadedGame.getState());
        assertEquals(game.getResourceDeckTopType(), reloadedGame.getResourceDeckTopType());
        assertEquals(game.getGoldDeckTopType(), reloadedGame.getGoldDeckTopType());

        assertEquals(game.getObjectiveCardsDeck(), reloadedGame.getObjectiveCardsDeck());
        assertEquals(game.getStarterCardsDeck(), reloadedGame.getStarterCardsDeck());
        assertEquals(game.getGoldCardsDeck(), reloadedGame.getGoldCardsDeck());
        assertEquals(game.getResourceCardsDeck(), reloadedGame.getResourceCardsDeck());
    }
}
