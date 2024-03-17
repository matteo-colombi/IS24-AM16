package it.polimi.ingsw.am16.common.model.game;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.PlayerModel;
import it.polimi.ingsw.am16.common.model.players.hand.Hand;
import it.polimi.ingsw.am16.common.model.players.hand.HandModel;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.util.RNG;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TestGame {

    @Test
    public void testGame() throws UnexpectedActionException, UnknownObjectiveCardException, IllegalMoveException {
        CardRegistry.initializeRegistry();
        RNG.setRNGSeed(0);

        GameModel game = new Game("69xD420", 2);
        game.addPlayer("xLorde");
        game.addPlayer("L2C");
        try{
            game.addPlayer("teo");
            fail("Game did not launch exception when already full");
        } catch (UnexpectedActionException e) {
            assertTrue(true);
        }
        try {
            game.startGame();
            fail("Game did not launch exception when trying to start ahead of time");
        } catch (UnexpectedActionException e) {
            assertTrue(true);
        }
        game.initializeGame();

        PlayerModel[] players = game.getPlayers();
        PlayerModel xLorde = players[0];
        PlayerModel l2c = players[1];
        HandModel xLordeHand = xLorde.getHand();
        HandModel l2cHand = l2c.getHand();

        System.out.println("\nPlayer starter cards:");
        Arrays.stream(game.getPlayers()).forEach(p -> System.out.println(p.getStarterCard()));

        game.setPlayerStarterSide(0, SideType.FRONT);
        game.setPlayerStarterSide(1, SideType.BACK);

        game.setPlayerColor(0, PlayerColor.BLUE);
        try{
            game.setPlayerColor(1, PlayerColor.BLUE);
            fail();
        } catch (UnexpectedActionException e) {
            assertTrue(true);
        }
        game.setPlayerColor(1, PlayerColor.GREEN);

        game.initializeObjectives();

        System.out.println("\nPlayer objective options:");
        Arrays.stream(players).forEach(p -> System.out.println(p.getPersonalObjectiveOptions()));

        System.out.println("\nCommon objectives:");
        System.out.println(Arrays.toString(game.getCommonObjectiveCards()));

        game.setPlayerObjective(1, l2c.getPersonalObjectiveOptions().get(0));
        game.setPlayerObjective(0, xLorde.getPersonalObjectiveOptions().get(1));

        game.startGame();

        System.out.println("\nCommon cards:");
        System.out.println(Arrays.toString(game.getCommonResourceCards()));
        System.out.println(Arrays.toString(game.getCommonGoldCards()));

        assertEquals(game.getStartingPlayer(), game.getActivePlayer());

        System.out.println("xLorde hand:");
        System.out.println(xLordeHand);

        System.out.println("l2c hand:");
        System.out.println(l2cHand);
        try {
            game.placeCard(1, l2cHand.getCard(2), SideType.BACK, new Position(1, 1));
            fail("That move was illegal...");
        } catch (IllegalMoveException e) {
            assertTrue(true);
        }
        //FIXME try this again once that is fixed.
        /*
        try {
            game.placeCard(1, l2cHand.getCard(1), SideType.BACK, new Position(0, 1));
            fail("That too was illegal...");
        } catch (IllegalMoveException e) {
            assertTrue(true);
        }
         */
        game.placeCard(1, l2cHand.getCard(1), SideType.BACK, new Position(1, 1));
        game.drawCard(1, DrawType.RESOURCE_2);

        System.out.println("l2c points:");
        System.out.println(l2c.getGamePoints());

        System.out.println("\nl2cHand:");
        System.out.println(l2cHand);

        System.out.println("\nGame common cards:");
        System.out.println(Arrays.toString(game.getCommonResourceCards()));
        System.out.println(Arrays.toString(game.getCommonGoldCards()));

        game.advanceTurn();
        assertEquals(0, game.getActivePlayer());

        game.placeCard(0, xLordeHand.getCard(1), SideType.BACK, new Position(1, 1));
        game.drawCard(0, DrawType.RESOURCE_DECK);

        System.out.println("\nxLorde hand:");
        System.out.println(xLordeHand);

        game.advanceTurn();
        assertEquals(1, game.getActivePlayer());

        game.placeCard(1, l2cHand.getCard(2), SideType.BACK, new Position(1, -1));
        game.drawCard(1, DrawType.GOLD_DECK);

        System.out.println("\nl2c hand:");
        System.out.println(l2cHand);

        game.advanceTurn();

        game.placeCard(0, xLordeHand.getCard(2), SideType.BACK, new Position(-1, -1));
        game.drawCard(0, DrawType.GOLD_DECK);

        System.out.println("\nxLorde hand:");
        System.out.println(xLordeHand);

        game.advanceTurn();

        game.placeCard(1, l2cHand.getCard(1), SideType.BACK, new Position(-1, -1));
        game.drawCard(1, DrawType.GOLD_DECK);

        System.out.println("l2c points:");
        System.out.println(l2c.getGamePoints());

        System.out.println("\nl2c hand:");
        System.out.println(l2cHand);

        game.advanceTurn();

        game.placeCard(0, xLordeHand.getCard(1), SideType.BACK, new Position(0, 2));
        game.drawCard(0, DrawType.GOLD_DECK);

    }

}
