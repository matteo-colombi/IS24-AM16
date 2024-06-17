package it.polimi.ingsw.am16.common.model.cards;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.NoStarterCardException;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.util.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPatternObjective {

    private CardRegistry registry;
    private List<ObjectiveCard> objectiveCards;
    private StarterCard starterCard;
    private Player testPlayer;

    @Test
    public void testPatternObjective() throws IllegalMoveException, NoStarterCardException {
        // Creating some objects used in the test.
        initialize();

        ObjectiveCard objective = objectiveCards.getFirst();
        assertEquals("objective_pattern_1", objective.getName());

        assertEquals(0, objective.evaluatePoints(testPlayer.getPlayArea()));

        PlayableCard fungiCard = registry.getResourceCards().getFirst();
        PlayableCard insectCard = registry.getResourceCards().get(30);
        assertEquals(ResourceType.FUNGI, fungiCard.getType());
        assertEquals(ResourceType.INSECT, insectCard.getType());

        testPlayer.giveStarterCard(starterCard);
        testPlayer.chooseStarterCardSide(SideType.FRONT);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(1,1));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(2,2));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(3,3));

        assertEquals(2, objective.evaluatePoints(testPlayer.getPlayArea()));

        testPlayer = new Player("testPlayer");

        testPlayer.giveStarterCard(starterCard);
        testPlayer.chooseStarterCardSide(SideType.FRONT);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(-1,-1));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(-2,-2));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(-3,-3));

        assertEquals(2, objective.evaluatePoints(testPlayer.getPlayArea()));

        testPlayer = new Player("testPlayer");

        testPlayer.giveStarterCard(starterCard);
        testPlayer.chooseStarterCardSide(SideType.FRONT);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(-1,1));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(-2,0));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(0,2));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(1,3));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(2,4));

        assertEquals(2, objective.evaluatePoints(testPlayer.getPlayArea()));

        testPlayer.giveCard(fungiCard);
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(3,5));

        assertEquals(4, objective.evaluatePoints(testPlayer.getPlayArea()));

        testPlayer = new Player("testPlayer");

        testPlayer.giveStarterCard(starterCard);
        testPlayer.chooseStarterCardSide(SideType.FRONT);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(1,1));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(-1,-1));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(-2,-2));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(2,2));

        assertEquals(0, objective.evaluatePoints(testPlayer.getPlayArea()));

        testPlayer.giveCard(fungiCard);
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(3,3));

        assertEquals(2, objective.evaluatePoints(testPlayer.getPlayArea()));

        testPlayer = new Player("testPlayer");

        testPlayer.giveStarterCard(starterCard);
        testPlayer.chooseStarterCardSide(SideType.FRONT);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(1,1));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(2,2));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(3,3));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(0,2));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(1,3));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(2,4));

        assertEquals(4, objective.evaluatePoints(testPlayer.getPlayArea()));

        testPlayer = new Player("testPlayer");

        testPlayer.giveStarterCard(starterCard);
        testPlayer.chooseStarterCardSide(SideType.FRONT);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(-1,1));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(-2,2));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(-3,3));

        assertEquals(0, objective.evaluatePoints(testPlayer.getPlayArea()));

        testPlayer = new Player("testPlayer");

        testPlayer.giveStarterCard(starterCard);
        testPlayer.chooseStarterCardSide(SideType.FRONT);
        testPlayer.giveCard(fungiCard);
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(-1,1));
        testPlayer.giveCard(insectCard);
        testPlayer.playCard(insectCard, SideType.BACK, new Position(-2,2));
        testPlayer.giveCard(fungiCard);
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(-3,3));

        assertEquals(0, objective.evaluatePoints(testPlayer.getPlayArea()));

        testPlayer = new Player("testPlayer");

        testPlayer.giveStarterCard(starterCard);
        testPlayer.chooseStarterCardSide(SideType.FRONT);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(fungiCard);
        testPlayer.giveCard(insectCard);
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(1,1));
        testPlayer.playCard(fungiCard, SideType.BACK, new Position(2,2));
        testPlayer.playCard(insectCard, SideType.BACK, new Position(3,3));

        assertEquals(0, objective.evaluatePoints(testPlayer.getPlayArea()));

        testPlayer = new Player("testPlayer");

        testPlayer.giveStarterCard(starterCard);
        testPlayer.chooseStarterCardSide(SideType.FRONT);
        testPlayer.giveCard(insectCard);
        testPlayer.giveCard(insectCard);
        testPlayer.giveCard(insectCard);
        testPlayer.playCard(insectCard, SideType.BACK, new Position(1,1));
        testPlayer.playCard(insectCard, SideType.BACK, new Position(2,2));
        testPlayer.playCard(insectCard, SideType.BACK, new Position(3,3));

        assertEquals(0, objective.evaluatePoints(testPlayer.getPlayArea()));
    }

    public void initialize() {
        registry = CardRegistry.getRegistry();

        objectiveCards = registry.getObjectiveCards();

        starterCard = registry.getStarterCards().getFirst();
        assertEquals("starter_1", starterCard.getName());

        testPlayer = new Player("testPlayer");
    }
}
