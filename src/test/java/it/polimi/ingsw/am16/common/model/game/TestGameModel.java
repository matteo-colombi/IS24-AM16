package it.polimi.ingsw.am16.common.model.game;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.NoStarterCardException;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.hand.HandModel;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.util.RNG;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameModel {

    /*
     *  This test simulates a complete game, from start to finish, checking that the model returns the correct
     *  value at different stages of the game.
     *  This test also checks that the model throws exceptions when it should.
     *  The RNG seed is set at the beginning to ensure the same game is played every time this test is run.
     */

    private GameModel game;

    @Test
    public void testGame() throws UnexpectedActionException, UnknownObjectiveCardException, IllegalMoveException, NoStarterCardException {
        CardRegistry.getRegistry();
        RNG.setRNGSeed(51);

        game = new Game("69xD420", 2);
        game.addPlayer("xLorde");

        assertThrows(UnexpectedActionException.class, game::initializeGame);

        game.addPlayer("teo");
        game.removePlayer("teo");
        game.addPlayer("teo");

        assertThrows(UnexpectedActionException.class, () -> game.addPlayer("teo"));
        assertThrows(UnexpectedActionException.class, () -> game.addPlayer("l2c"));
        assertThrows(UnexpectedActionException.class, game::startGame);

        assertFalse(game.isRejoiningAfterCrash());

        game.setConnected("xLorde", true);
        game.setConnected("teo", true);

        assertTrue(game.everybodyConnected());

        game.initializeGame();

        assertThrows(UnexpectedActionException.class, () -> game.addPlayer("l2c"));
        assertThrows(UnexpectedActionException.class, () -> game.initializeGame());

        Map<String, Player> players = game.getPlayers();
        Player xLorde = players.get("xLorde");
        Player teo = players.get("teo");
        Set<Player> playerSet = new HashSet<>(players.values());
        assertTrue(playerSet.contains(xLorde));
        assertTrue(playerSet.contains(teo));
        HandModel xLordeHand = xLorde.getHand();
        HandModel teoHand = teo.getHand();

        game.setPlayerStarterSide("xLorde", SideType.BACK);

        assertFalse(game.allPlayersChoseStarterSide());

        game.setPlayerStarterSide("teo", SideType.BACK);

        assertTrue(game.allPlayersChoseStarterSide());

        game.setPlayerColor("xLorde", PlayerColor.BLUE);

        assertFalse(game.allPlayersChoseColor());

        assertThrows(UnexpectedActionException.class, () -> game.setPlayerColor("teo", PlayerColor.BLUE));

        game.setPlayerColor("teo", PlayerColor.RED);

        assertTrue(game.allPlayersChoseColor());

        game.initializeObjectives();

        game.setPlayerObjective("teo", teo.getPersonalObjectiveOptions().get(0));

        assertFalse(game.allPlayersChoseObjective());

        assertThrows(UnexpectedActionException.class, () -> game.startGame());
        assertThrows(UnexpectedActionException.class, () -> game.placeCard("teo", CardRegistry.getRegistry().getResourceCardFromName("resource_fungi_3"), SideType.FRONT, new Position(1,1)));
        assertThrows(UnexpectedActionException.class, () -> game.drawCard("xLorde", DrawType.RESOURCE_2));
        assertThrows(UnexpectedActionException.class, () -> game.advanceTurn());
        assertThrows(UnexpectedActionException.class, () -> game.triggerFinalRound());
        assertThrows(UnexpectedActionException.class, () -> game.endGame());

        game.setPlayerObjective("xLorde", xLorde.getPersonalObjectiveOptions().get(0));

        assertTrue(game.allPlayersChoseObjective());

        //This test was written before playerIds were removed.
        //Hence, when player ids got removed, the logic to choose the starting player changed and this test
        //broke only because the starting player was now teo instead of xLorde.
        //These two nextInt calls make it so that xLorde is the starting player again.
        //The rest of the test is unchanged.
        RNG.getRNG().nextInt();
        RNG.getRNG().nextInt();

        game.startGame();
        game.distributeCards();

        assertThrows(UnexpectedActionException.class, () -> game.setPlayerStarterSide("xLorde", SideType.FRONT));
        assertThrows(UnexpectedActionException.class, () -> game.setPlayerColor("xLorde", PlayerColor.GREEN));
        assertThrows(UnexpectedActionException.class, () -> game.setPlayerObjective("username", xLorde.getPersonalObjective()));

        assertEquals(game.getStartingPlayer(), game.getActivePlayer());

        assertThrows(IllegalMoveException.class, () -> game.placeCard("teo", teoHand.getCards().get(2), SideType.FRONT, new Position(1, 1)));
        assertThrows(IllegalMoveException.class, () -> game.placeCard("teo", teoHand.getCards().get(1), SideType.FRONT, new Position(0, 1)));

        game.placeCard("xLorde", xLordeHand.getCards().get(0), SideType.FRONT, new Position(-1, 1));

        game.drawCard("xLorde", DrawType.RESOURCE_DECK);

        game.advanceTurn();
        assertEquals("teo", game.getActivePlayer());

        game.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(-1, 1));
        game.drawCard("teo", DrawType.RESOURCE_2);

        assertEquals(1, teo.getGamePoints());
        game.advanceTurn();

        game.pause();
        assertTrue(game.isRejoiningAfterCrash());
        game.setConnected("xLorde", true);
        game.setConnected("teo", true);
        game.initializeGame();
        assertFalse(game.isRejoiningAfterCrash());

        assertEquals("xLorde", game.getActivePlayer());

        game.placeCard("xLorde", xLordeHand.getCards().get(0), SideType.FRONT, new Position(0, 2));

        game.drawCard("xLorde", DrawType.GOLD_DECK);

        game.advanceTurn();

        game.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(1, 1));
        game.drawCard("teo", DrawType.RESOURCE_2);

        game.advanceTurn();

        game.placeCard("xLorde", xLordeHand.getCards().get(1), SideType.FRONT, new Position(1, 1));
        game.drawCard("xLorde", DrawType.RESOURCE_2);

        game.advanceTurn();

        game.placeCard("teo", teoHand.getCards().get(2), SideType.FRONT, new Position(0, 2));
        game.drawCard("teo", DrawType.RESOURCE_DECK);

        game.advanceTurn();

        game.placeCard("xLorde", xLordeHand.getCards().get(1), SideType.BACK, new Position(1, -1));
        game.drawCard("xLorde", DrawType.RESOURCE_DECK);

        game.advanceTurn();

        game.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(1, 3));
        game.drawCard("teo", DrawType.GOLD_DECK);

        game.advanceTurn();

        game.placeCard("xLorde", xLordeHand.getCards().get(1), SideType.FRONT, new Position(2, 2));
        game.drawCard("xLorde", DrawType.RESOURCE_DECK);

        game.advanceTurn();

        game.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(0, 4));
        game.drawCard("teo", DrawType.GOLD_2);

        game.advanceTurn();

        game.placeCard("xLorde", xLordeHand.getCards().get(2), SideType.FRONT, new Position(2, -2));
        game.drawCard("xLorde", DrawType.RESOURCE_2);

        game.advanceTurn();

        game.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(-2, 0));
        game.drawCard("teo", DrawType.RESOURCE_1);

        game.advanceTurn();

        game.placeCard("xLorde", xLordeHand.getCards().get(2), SideType.FRONT, new Position(1, -3));
        game.drawCard("xLorde", DrawType.GOLD_1);

        game.advanceTurn();

        game.placeCard("teo", teoHand.getCards().get(2), SideType.FRONT, new Position(-3, -1));
        game.drawCard("teo", DrawType.RESOURCE_DECK);

        game.advanceTurn();

        game.placeCard("xLorde", xLordeHand.getCards().get(1), SideType.FRONT, new Position(3, -1));
        game.drawCard("xLorde", DrawType.GOLD_DECK);

        game.advanceTurn();

        game.placeCard("teo", teoHand.getCards().get(2), SideType.FRONT, new Position(-1, 5));
        game.drawCard("teo", DrawType.GOLD_DECK);

        game.advanceTurn();

        game.placeCard("xLorde", xLordeHand.getCards().get(2), SideType.FRONT, new Position(2, -4));
        game.drawCard("xLorde", DrawType.GOLD_DECK);

        game.advanceTurn();

        game.placeCard("teo", teoHand.getCards().get(2), SideType.FRONT, new Position(-2, 2));
        game.drawCard("teo", DrawType.RESOURCE_DECK);

        assertEquals(1, xLorde.getGamePoints());
        assertEquals(9, teo.getGamePoints());

        game.advanceTurn();

        game.placeCard("xLorde", xLordeHand.getCards().get(0), SideType.FRONT, new Position(3, 1));
        game.drawCard("xLorde", DrawType.GOLD_DECK);

        game.advanceTurn();

        game.placeCard("teo", teoHand.getCards().get(1), SideType.FRONT, new Position(-2, 4));
        game.drawCard("teo", DrawType.GOLD_DECK);

        game.advanceTurn();

        game.placeCard("xLorde", xLordeHand.getCards().get(0), SideType.FRONT, new Position(2, 0));
        game.drawCard("xLorde", DrawType.GOLD_DECK);

        assertEquals(10, xLorde.getGamePoints());

        game.advanceTurn();

        game.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(-1, 3));
        game.drawCard("teo", DrawType.RESOURCE_DECK);

        assertEquals(19, teo.getGamePoints());

        game.advanceTurn();

        game.placeCard("xLorde", xLordeHand.getCards().get(2), SideType.FRONT, new Position(-1, -1));
        game.drawCard("xLorde", DrawType.GOLD_1);

        assertEquals(13, xLorde.getGamePoints());

        game.advanceTurn();

        game.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(-3, 3));
        game.drawCard("teo", DrawType.GOLD_DECK);

        game.advanceTurn();

        game.placeCard("xLorde", xLordeHand.getCards().get(1), SideType.FRONT, new Position(4, 2));
        game.drawCard("xLorde", DrawType.GOLD_DECK);

        game.triggerFinalRound();

        assertEquals(GameState.STARTED, game.getState());

        game.advanceTurn();

        game.placeCard("teo", teoHand.getCards().get(0), SideType.FRONT, new Position(1, 5));
        game.drawCard("teo", DrawType.GOLD_DECK);

        assertEquals(24, teo.getGamePoints());

        game.triggerFinalRound();

        assertEquals(GameState.FINAL_ROUND, game.getState());

        game.advanceTurn();

        game.placeCard("xLorde", xLordeHand.getCards().getFirst(), SideType.FRONT, new Position(0, -2));

        assertEquals(21, xLorde.getGamePoints());

        game.advanceTurn();

        game.placeCard("teo", teoHand.getCards().get(1), SideType.FRONT, new Position(2, 0));

        assertEquals(27, teo.getGamePoints());

        game.endGame();

        // Checking that everyone has the correct amount of points.

        assertEquals(21, xLorde.getGamePoints());
        assertEquals(7, xLorde.getObjectivePoints());
        assertEquals(28, xLorde.getTotalPoints());
        assertEquals(27, teo.getGamePoints());
        assertEquals(10, teo.getObjectivePoints());
        assertEquals(37, teo.getTotalPoints());

        assertEquals(List.of("teo"), game.getWinnerUsernames());
    }

    /*
     * Utility methods used while creating the test.
     */

    private void printDeckTopTypes() {
        System.out.println("Deck top types: ");
        System.out.println("Resource deck: " + game.getResourceDeckTopType());
        System.out.println("Gold deck: " + game.getGoldDeckTopType());
        System.out.println();
    }

    private void printHand(Player playerModel) {
        String name = playerModel.getUsername();
        HandModel hand = playerModel.getHand();
        System.out.println(name + " hand:");
        for (int i = 0; i < hand.getCards().size(); i++) {
            System.out.println(hand.getCards().get(i));
        }
        System.out.println();
    }

    private void printCommonResourceCards() {
        System.out.println("Common resource cards: ");
        for(PlayableCard card : game.getCommonResourceCards()) {
            System.out.println(card);
        }
        System.out.println();
    }

    private void printCommonGoldCards() {
        System.out.println("Common gold cards: ");
        for(PlayableCard card : game.getCommonGoldCards()) {
            System.out.println(card);
        }
        System.out.println();
    }

    private void printCommonObjectives() {
        System.out.println("Common objectives: ");
        for(ObjectiveCard card : game.getCommonObjectiveCards()) {
            System.out.println(card);
        }
        System.out.println();
    }

    private void printPersonalObjective(Player playerMode) {
        System.out.println(playerMode.getUsername() + " personal objective:");
        System.out.println(playerMode.getPersonalObjective());
        System.out.println();
    }
}
