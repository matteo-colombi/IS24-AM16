package it.polimi.ingsw.am16.server.controller;

import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.client.TestRemoteViewImplementation;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.util.RNG;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TestGameController {
    @Test
    void testGameController() throws UnexpectedActionException {
        RNG.setRNGSeed(420);
        LobbyManager lobbyManager = new LobbyManager();
        String gameId = lobbyManager.createGame(2);
        GameController controller = lobbyManager.getGame(gameId);

        RemoteClientInterface xLordeInterface = new TestRemoteViewImplementation("xLorde");
        RemoteClientInterface l2cInterface = new TestRemoteViewImplementation("l2c");

        controller.joinPlayer("xLorde", xLordeInterface);
        controller.joinPlayer("l2c", l2cInterface);

        controller.setStarterCard("xLorde", SideType.BACK);
        controller.setStarterCard("l2c", SideType.FRONT);

        controller.setPlayerColor("l2c", PlayerColor.BLUE);
        controller.setPlayerColor("xLorde", PlayerColor.BLUE);
        controller.setPlayerColor("l2c", PlayerColor.BLUE);

        controller.setPlayerObjective("l2c", CardRegistry.getRegistry().getObjectiveCardFromName("objective_pattern_7"));
        controller.setPlayerObjective("l2c", CardRegistry.getRegistry().getObjectiveCardFromName("objective_pattern_5"));

        controller.setPlayerObjective("xLorde", CardRegistry.getRegistry().getObjectiveCardFromName("objective_resources_3"));

        controller.placeCard("l2c", CardRegistry.getRegistry().getResourceCardFromName("resource_fungi_3"), SideType.BACK, new Position(1, 1));
        controller.drawCard("l2c", DrawType.GOLD_DECK);

        controller.sendChatMessage("xLorde", "Ciao leo!!!", Set.of("l2c"));
        controller.sendChatMessage("l2c", "Ciao a tutti!");
    }
}