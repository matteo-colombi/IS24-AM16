package it.polimi.ingsw.am16.server;

import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.client.TestRemoteViewImplementation;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.model.game.Game;
import it.polimi.ingsw.am16.common.model.game.GameModel;
import it.polimi.ingsw.am16.common.model.game.LobbyState;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.hand.HandModel;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.util.RNG;
import it.polimi.ingsw.am16.server.controller.GameController;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameController {

    @Test
    void testGameController() throws UnexpectedActionException {
        CardRegistry.getRegistry();
        RNG.setRNGSeed(51);
        LobbyManager lobbyManager = new LobbyManager();
        GameModel game = new Game("69xD420", 2);
        GameController controller = new GameController(lobbyManager, game);

        assertEquals(2, controller.getNumPlayers());

        RemoteClientInterface user1Interface = new TestRemoteViewImplementation();
        RemoteClientInterface user2Interface = new TestRemoteViewImplementation();

        controller.createPlayer("xLorde");
        controller.joinPlayer("xLorde", user1Interface);
        assertEquals(1, controller.getCurrentPlayerCount());
        controller.disconnect("xLorde");
        assertEquals(0, controller.getCurrentPlayerCount());
        assertEquals(LobbyState.JOINING, controller.getLobbyState());
        controller.createPlayer("xLorde");
        controller.joinPlayer("xLorde", user1Interface);
        controller.createPlayer("teo");
        controller.joinPlayer("teo", user2Interface);

        /*
         *  This is done just for convenience to have a reference to the player's hands
         *  and not have to type out long card names
         */
        Map<String, Player> players = game.getPlayers();
        Player xLorde = players.get("xLorde");
        Player teo = players.get("teo");
        HandModel xLordeHand = xLorde.getHand();
        HandModel teoHand = teo.getHand();

        assertEquals(LobbyState.IN_GAME, controller.getLobbyState());

        controller.sendChatMessage("xLorde", "Hi", Set.of("teo"));
        controller.sendChatMessage("teo", "hello");
        controller.sendChatMessage("xLorde", "Test", Set.of("nobody"));

        assertEquals(3, xLorde.getChat().getMessages().size());
        assertEquals(2, teo.getChat().getMessages().size());

        controller.setStarterCard("xLorde", SideType.BACK);
        controller.setStarterCard("teo", SideType.BACK);

        controller.setPlayerColor("teo", PlayerColor.RED);
        controller.setPlayerColor("xLorde", PlayerColor.BLUE);

        //This is done in order to get the starting player to be the same as in TestGameModel
        //This way, we can create the same exact game here
        RNG.getRNG().nextInt();

        controller.setPlayerObjective("teo", teo.getPersonalObjectiveOptions().getFirst());
        controller.setPlayerObjective("xLorde", xLorde.getPersonalObjectiveOptions().getFirst());

        controller.placeCard("xLorde", xLordeHand.getCards().get(0), SideType.FRONT, new Position(-1, 1));
        controller.drawCard("xLorde", DrawType.RESOURCE_DECK);

        controller.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(-1, 1));
        controller.drawCard("teo", DrawType.RESOURCE_2);

        controller.disconnect("teo");

        assertEquals(LobbyState.REJOINING, controller.getLobbyState());
        assertTrue(controller.isRejoiningAfterCrash());

        controller.joinPlayer("teo", user2Interface);
        controller.joinPlayer("xLorde", user1Interface);

        assertEquals(LobbyState.IN_GAME, controller.getLobbyState());
        assertFalse(controller.isRejoiningAfterCrash());

        controller.placeCard("xLorde", xLordeHand.getCards().get(0), SideType.FRONT, new Position(0, 2));
        controller.drawCard("xLorde", DrawType.GOLD_DECK);

        controller.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(1, 1));
        controller.drawCard("teo", DrawType.RESOURCE_2);

        controller.placeCard("xLorde", xLordeHand.getCards().get(1), SideType.FRONT, new Position(1, 1));
        controller.drawCard("xLorde", DrawType.RESOURCE_2);

        controller.placeCard("teo", teoHand.getCards().get(2), SideType.FRONT, new Position(0, 2));
        controller.drawCard("teo", DrawType.RESOURCE_DECK);

        controller.placeCard("xLorde", xLordeHand.getCards().get(1), SideType.BACK, new Position(1, -1));
        controller.drawCard("xLorde", DrawType.RESOURCE_DECK);

        controller.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(1, 3));
        controller.drawCard("teo", DrawType.GOLD_DECK);

        controller.placeCard("xLorde", xLordeHand.getCards().get(1), SideType.FRONT, new Position(2, 2));
        controller.drawCard("xLorde", DrawType.RESOURCE_DECK);

        controller.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(0, 4));
        controller.drawCard("teo", DrawType.GOLD_2);

        controller.placeCard("xLorde", xLordeHand.getCards().get(2), SideType.FRONT, new Position(2, -2));
        controller.drawCard("xLorde", DrawType.RESOURCE_2);

        controller.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(-2, 0));
        controller.drawCard("teo", DrawType.RESOURCE_1);

        controller.placeCard("xLorde", xLordeHand.getCards().get(2), SideType.FRONT, new Position(1, -3));
        controller.drawCard("xLorde", DrawType.GOLD_1);

        controller.placeCard("teo", teoHand.getCards().get(2), SideType.FRONT, new Position(-3, -1));
        controller.drawCard("teo", DrawType.RESOURCE_DECK);

        controller.placeCard("xLorde", xLordeHand.getCards().get(1), SideType.FRONT, new Position(3, -1));
        controller.drawCard("xLorde", DrawType.GOLD_DECK);

        controller.placeCard("teo", teoHand.getCards().get(2), SideType.FRONT, new Position(-1, 5));
        controller.drawCard("teo", DrawType.GOLD_DECK);

        controller.placeCard("xLorde", xLordeHand.getCards().get(2), SideType.FRONT, new Position(2, -4));
        controller.drawCard("xLorde", DrawType.GOLD_DECK);

        controller.placeCard("teo", teoHand.getCards().get(2), SideType.FRONT, new Position(-2, 2));
        controller.drawCard("teo", DrawType.RESOURCE_DECK);

        controller.placeCard("xLorde", xLordeHand.getCards().get(0), SideType.FRONT, new Position(3, 1));
        controller.drawCard("xLorde", DrawType.GOLD_DECK);

        controller.placeCard("teo", teoHand.getCards().get(1), SideType.FRONT, new Position(-2, 4));
        controller.drawCard("teo", DrawType.GOLD_DECK);

        controller.placeCard("xLorde", xLordeHand.getCards().get(0), SideType.FRONT, new Position(2, 0));
        controller.drawCard("xLorde", DrawType.GOLD_DECK);

        controller.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(-1, 3));
        controller.drawCard("teo", DrawType.RESOURCE_DECK);

        controller.placeCard("xLorde", xLordeHand.getCards().get(2), SideType.FRONT, new Position(-1, -1));
        controller.drawCard("xLorde", DrawType.GOLD_1);

        controller.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(-3, 3));
        controller.drawCard("teo", DrawType.GOLD_DECK);

        controller.placeCard("xLorde", xLordeHand.getCards().get(1), SideType.FRONT, new Position(4, 2));
        controller.drawCard("xLorde", DrawType.GOLD_DECK);

        controller.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(1, 5));
        controller.drawCard("teo", DrawType.GOLD_DECK);

        assertEquals(LobbyState.ENDING, controller.getLobbyState());

        controller.placeCard("xLorde", xLordeHand.getCards().getFirst(), SideType.FRONT, new Position(0, -2));

        controller.placeCard("teo", teoHand.getCards().get(1), SideType.FRONT, new Position(2, 0));

        // Checking that everyone has the correct amount of points.

        assertEquals(21, xLorde.getGamePoints());
        assertEquals(7, xLorde.getObjectivePoints());
        assertEquals(28, xLorde.getTotalPoints());
        assertEquals(27, teo.getGamePoints());
        assertEquals(10, teo.getObjectivePoints());
        assertEquals(37, teo.getTotalPoints());

        assertEquals(List.of("teo"), game.getWinnerUsernames());

    }
}
