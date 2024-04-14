package it.polimi.ingsw.am16.server.controller;

import it.polimi.ingsw.am16.client.OtherTestRemoteViewImplementation;
import it.polimi.ingsw.am16.client.RemoteViewInterface;
import it.polimi.ingsw.am16.client.TestRemoteViewImplementation;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.util.RNG;
import org.junit.jupiter.api.Test;

class TestGameController {
    @Test
    void testGameController() {
        RNG.setRNGSeed(420);

        String gameId = LobbyManager.createGame(2);
        GameController controller = LobbyManager.getGame(gameId);

        RemoteViewInterface xLordeInterface = new TestRemoteViewImplementation("xLorde");
        RemoteViewInterface l2cInterface = new OtherTestRemoteViewImplementation("l2c");

        int xLordeId = controller.createPlayer("xLorde");
        int l2cId = controller.createPlayer("l2c");

        controller.joinPlayer(xLordeId, xLordeInterface);
        controller.joinPlayer(l2cId, l2cInterface);

        controller.setStarterCard(xLordeId, SideType.BACK);
        controller.setStarterCard(l2cId, SideType.FRONT);

        controller.setPlayerColor(l2cId, PlayerColor.BLUE);
        controller.setPlayerColor(xLordeId, PlayerColor.BLUE);
        controller.setPlayerColor(l2cId, PlayerColor.BLUE);

        controller.setPlayerObjective(l2cId, CardRegistry.getRegistry().getObjectiveCardFromName("objective_pattern_7"));
        controller.setPlayerObjective(l2cId, CardRegistry.getRegistry().getObjectiveCardFromName("objective_pattern_5"));

        controller.setPlayerObjective(xLordeId, CardRegistry.getRegistry().getObjectiveCardFromName("objective_resources_3"));

        controller.placeCard(l2cId, CardRegistry.getRegistry().getResourceCardFromName("resource_fungi_3"), SideType.BACK, new Position(1, 1));
        controller.drawCard(l2cId, DrawType.GOLD_DECK);
    }
}