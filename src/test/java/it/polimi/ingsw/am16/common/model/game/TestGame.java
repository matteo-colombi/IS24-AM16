package it.polimi.ingsw.am16.common.model.game;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.NoStarterCardException;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.PlayerModel;
import it.polimi.ingsw.am16.common.model.players.hand.Hand;
import it.polimi.ingsw.am16.common.model.players.hand.HandModel;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.util.RNG;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestGame {

    private GameModel game;

    @Test
    public void testGame() throws UnexpectedActionException, UnknownObjectiveCardException, IllegalMoveException, NoStarterCardException {
        CardRegistry.initializeRegistry();
        RNG.setRNGSeed(51);

        game = new Game("69xD420", 2);
        game.addPlayer("xLorde");

        assertThrows(UnexpectedActionException.class, game::initializeGame);

        game.addPlayer("L2C");

        assertThrows(UnexpectedActionException.class, () -> game.addPlayer("teo"));
        assertThrows(UnexpectedActionException.class, game::startGame);

        game.initializeGame();

        PlayerModel[] players = game.getPlayers();
        PlayerModel xLorde = players[0];
        PlayerModel l2c = players[1];
        HandModel xLordeHand = xLorde.getHand();
        HandModel l2cHand = l2c.getHand();

        System.out.println("\nPlayer starter cards:");
        System.out.println("xLorde:");
        System.out.println(xLorde.getStarterCard());
        System.out.println("l2c:");
        System.out.println(l2c.getStarterCard());

        game.setPlayerStarterSide(0, SideType.BACK);
        game.setPlayerStarterSide(1, SideType.BACK);

        game.setPlayerColor(0, PlayerColor.BLUE);

        assertThrows(UnexpectedActionException.class, () -> game.setPlayerColor(1, PlayerColor.BLUE));

        game.setPlayerColor(1, PlayerColor.RED);

        game.initializeObjectives();

        System.out.println("\nPlayer objective options:");
        Arrays.stream(players).forEach(p -> System.out.println(p.getPersonalObjectiveOptions()));

        printCommonObjectives();

        game.setPlayerObjective(1, l2c.getPersonalObjectiveOptions().get(0));
        game.setPlayerObjective(0, xLorde.getPersonalObjectiveOptions().get(0));

        game.startGame();

        printCommonResourceCards();
        printCommonGoldCards();

        assertEquals(game.getStartingPlayer(), game.getActivePlayer());

        System.out.println(game.getStartingPlayer());

        printHand(xLorde);

        printHand(l2c);

        assertThrows(IllegalMoveException.class, () -> game.placeCard(1, l2cHand.getCard(2), SideType.BACK, new Position(1, 1)));
        assertThrows(IllegalMoveException.class, () -> game.placeCard(1, l2cHand.getCard(1), SideType.BACK, new Position(0, 1)));

        game.placeCard(0, xLordeHand.getCard(0), SideType.BACK, new Position(-1, 1));

        game.drawCard(0, DrawType.RESOURCE_DECK);

        printHand(xLorde);

        game.advanceTurn();
        assertEquals(1, game.getActivePlayer());

        game.placeCard(1, l2cHand.getCard(0), SideType.BACK, new Position(-1, 1));
        game.drawCard(1, DrawType.RESOURCE_2);

        assertEquals(1, l2c.getGamePoints());

        game.placeCard(0, xLordeHand.getCard(0), SideType.BACK, new Position(0, 2));

        game.drawCard(0, DrawType.GOLD_DECK);

        printHand(xLorde);

        game.advanceTurn();

        game.placeCard(1, l2cHand.getCard(0), SideType.BACK, new Position(1, 1));
        game.drawCard(1, DrawType.RESOURCE_2);

        printHand(l2c);

        game.advanceTurn();

        game.placeCard(0, xLordeHand.getCard(1), SideType.BACK, new Position(1, 1));
        game.drawCard(0, DrawType.RESOURCE_2);

        printHand(xLorde);

        game.advanceTurn();

        game.placeCard(1, l2cHand.getCard(2), SideType.BACK, new Position(0, 2));
        game.drawCard(1, DrawType.RESOURCE_DECK);

        printHand(l2c);

        game.advanceTurn();

        game.placeCard(0, xLordeHand.getCard(1), SideType.FRONT, new Position(1, -1));
        game.drawCard(0, DrawType.RESOURCE_DECK);

        printHand(xLorde);

        game.advanceTurn();

        game.placeCard(1, l2cHand.getCard(0), SideType.BACK, new Position(1, 3));
        game.drawCard(1, DrawType.GOLD_DECK);

        printHand(l2c);

        game.advanceTurn();

        game.placeCard(0, xLordeHand.getCard(1), SideType.BACK, new Position(2, 2));
        game.drawCard(0, DrawType.RESOURCE_DECK);

        printHand(xLorde);

        game.advanceTurn();

        game.placeCard(1, l2cHand.getCard(0), SideType.BACK, new Position(0, 4));
        game.drawCard(1, DrawType.GOLD_2);

        printHand(l2c);

        printCommonGoldCards();

        game.advanceTurn();

        game.placeCard(0, xLordeHand.getCard(2), SideType.BACK, new Position(2, -2));
        game.drawCard(0, DrawType.RESOURCE_2);

        printCommonResourceCards();

        game.advanceTurn();

        game.placeCard(1, l2cHand.getCard(0), SideType.BACK, new Position(-2, 0));
        game.drawCard(1, DrawType.RESOURCE_1);

        printCommonResourceCards();

        game.advanceTurn();

        game.placeCard(0, xLordeHand.getCard(2), SideType.BACK, new Position(1, -3));
        game.drawCard(0, DrawType.GOLD_1);

        printCommonGoldCards();

        game.advanceTurn();

        game.placeCard(1, l2cHand.getCard(2), SideType.BACK, new Position(-3, -1));
        game.drawCard(1, DrawType.RESOURCE_DECK);

        printHand(l2c);

        game.advanceTurn();

        game.placeCard(0, xLordeHand.getCard(1), SideType.BACK, new Position(3, -1));
        game.drawCard(0, DrawType.GOLD_DECK);

        printHand(xLorde);

        game.advanceTurn();

        game.placeCard(1, l2cHand.getCard(2), SideType.BACK, new Position(-1, 5));
        game.drawCard(1, DrawType.GOLD_DECK);

        printHand(l2c);

        game.advanceTurn();

        game.placeCard(0, xLordeHand.getCard(2), SideType.BACK, new Position(2, -4));
        game.drawCard(0, DrawType.GOLD_DECK);

        printHand(xLorde);

        game.advanceTurn();

        game.placeCard(1, l2cHand.getCard(2), SideType.BACK, new Position(-2, 2));
        game.drawCard(1, DrawType.RESOURCE_DECK);

        printHand(l2c);

        assertEquals(1, xLorde.getGamePoints());
        assertEquals(9, l2c.getGamePoints());

        game.advanceTurn();

        game.placeCard(0, xLordeHand.getCard(0), SideType.BACK, new Position(3, 1));
        game.drawCard(0, DrawType.GOLD_DECK);

        printHand(xLorde);

        game.advanceTurn();

        game.placeCard(1, l2cHand.getCard(1), SideType.BACK, new Position(-2, 4));
        game.drawCard(1, DrawType.GOLD_DECK);

        printHand(l2c);

        game.advanceTurn();

        game.placeCard(0, xLordeHand.getCard(0), SideType.BACK, new Position(2, 0));
        game.drawCard(0, DrawType.GOLD_DECK);

        printHand(xLorde);

        assertEquals(10, xLorde.getGamePoints());

        game.advanceTurn();

        game.placeCard(1, l2cHand.getCard(0), SideType.BACK, new Position(-1, 3));
        game.drawCard(1, DrawType.RESOURCE_DECK);

        printHand(l2c);

        assertEquals(19, l2c.getGamePoints());

        game.advanceTurn();

        game.placeCard(0, xLordeHand.getCard(2), SideType.BACK, new Position(-1, -1));
        game.drawCard(0, DrawType.GOLD_1);

        printCommonGoldCards();

        assertEquals(13, xLorde.getGamePoints());

        game.advanceTurn();

        game.placeCard(1, l2cHand.getCard(0), SideType.BACK, new Position(-3, 3));
        game.drawCard(1, DrawType.GOLD_DECK);

        printHand(l2c);

        game.advanceTurn();

        game.placeCard(0, xLordeHand.getCard(1), SideType.BACK, new Position(4, 2));
        game.drawCard(0, DrawType.GOLD_DECK);

        printHand(xLorde);

        game.triggerFinalRound();

        assertEquals(GameState.STARTED, game.getState());

        game.advanceTurn();

        game.placeCard(1, l2cHand.getCard(0), SideType.BACK, new Position(1, 5));
        game.drawCard(1, DrawType.GOLD_DECK);

        printHand(l2c);

        assertEquals(24, l2c.getGamePoints());

        game.triggerFinalRound();

        assertEquals(GameState.FINAL_ROUND, game.getState());


        game.advanceTurn();

        game.placeCard(0, xLordeHand.getCard(0), SideType.BACK, new Position(0, -2));

        assertEquals(21, xLorde.getGamePoints());

        game.advanceTurn();

        game.placeCard(1, l2cHand.getCard(1), SideType.BACK, new Position(2, 0));

        assertEquals(27, l2c.getGamePoints());

        game.endGame();

        assertEquals(21, xLorde.getGamePoints());
        assertEquals(7, xLorde.getObjectivePoints());
        assertEquals(28, xLorde.getTotalPoints());
        assertEquals(27, l2c.getGamePoints());
        assertEquals(10, l2c.getObjectivePoints());
        assertEquals(37, l2c.getTotalPoints());

        assertEquals(List.of(1), game.getWinnerIds());
    }

    private void printDeckTopTypes() {
        System.out.println("Deck top types: ");
        System.out.println("Resource deck: " + game.getResourceDeckTopType());
        System.out.println("Gold deck: " + game.getGoldDeckTopType());
        System.out.println();
    }

    private void printHand(PlayerModel playerModel) {
        String name = playerModel.getUsername();
        HandModel hand = playerModel.getHand();
        System.out.println(name + " hand:");
        for (int i = 0; i < hand.getSize(); i++) {
            System.out.println(hand.getCard(i));
        }
        System.out.println();
    }

    private void printCommonResourceCards() {
        System.out.println("Common resource cards: ");
        for(ResourceCard card : game.getCommonResourceCards()) {
            System.out.println(card);
        }
        System.out.println();
    }

    private void printCommonGoldCards() {
        System.out.println("Common gold cards: ");
        for(GoldCard card : game.getCommonGoldCards()) {
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

    private void printPersonalObjective(PlayerModel playerMode) {
        System.out.println(playerMode.getUsername() + " personal objective:");
        System.out.println(playerMode.getPersonalObjective());
        System.out.println();
    }
}
