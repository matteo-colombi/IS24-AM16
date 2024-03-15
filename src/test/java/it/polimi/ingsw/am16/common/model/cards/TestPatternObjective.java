package it.polimi.ingsw.am16.common.model.cards;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.model.PlayArea;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.util.Position;
import javafx.geometry.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPatternObjective {

    //TODO run this test once PlayArea is actually implemented.
    @Test
    public void testPatternObjective() {
        // Creating some objects used in the test.
        PatternObjective objective = new PatternObjective(
                0,
                "testObjective",
                2,
                new PatternObjective.CardPattern(
                        new ResourceType[]{ResourceType.ANIMAL, ResourceType.ANIMAL, ResourceType.ANIMAL},
                        new Position[]{new Position(-1, -1), new Position(-1, -1)}
                )
        );

        Player testPlayer = new Player(0, "testPlayer");
        StarterCard testStarterCard = new StarterCard(
                1,
                "testStarter",
                null,
                null
        );
        ResourceCard testAnimalCard = new ResourceCard(
                2,
                "testAnimalCard",
                null,
                null,
                ResourceType.ANIMAL
        );
        ResourceCard testInsectCard = new ResourceCard(
                3,
                "testInsectCard",
                null,
                null,
                ResourceType.INSECT
        );

        // The actual test.
        testPlayer.getPlayArea().getField().put(new Position(0, 0), testStarterCard);

        assertEquals(0, objective.evaluatePoints(testPlayer.getPlayArea()));

        testPlayer.getPlayArea().getField().put(new Position(1, 1), testAnimalCard);
        testPlayer.getPlayArea().getField().put(new Position(2, 2), testAnimalCard);
        testPlayer.getPlayArea().getField().put(new Position(3, 3), testAnimalCard);

        assertEquals(2, objective.evaluatePoints(testPlayer.getPlayArea()));

        testPlayer.getPlayArea().getField().put(new Position(4, 4), testAnimalCard);
        testPlayer.getPlayArea().getField().put(new Position(5, 5), testAnimalCard);

        assertEquals(2, objective.evaluatePoints(testPlayer.getPlayArea()));

        testPlayer.getPlayArea().getField().put(new Position(6, 6), testAnimalCard);

        assertEquals(4, objective.evaluatePoints(testPlayer.getPlayArea()));

        testPlayer.getPlayArea().getField().put(new Position(6, 6), testInsectCard);

        assertEquals(2, objective.evaluatePoints(testPlayer.getPlayArea()));

        //TODO expand this to try more cases

    }
}
