package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.players.PlayArea;
import it.polimi.ingsw.am16.common.util.Position;

import org.junit.jupiter.api.Test;

public class TestCLIPlayArea {
    @Test
    void testCLIPlayArea() throws IllegalMoveException {
        CLIPlayArea cliPlayArea = new CLIPlayArea();
        cliPlayArea.addCard(CardRegistry.getRegistry().getStarterCardFromName("starter_1"), SideType.FRONT, new Position(0, 0));
        cliPlayArea.printPlayArea();
        cliPlayArea.addCard(CardRegistry.getRegistry().getResourceCardFromName("resource_fungi_3"), SideType.FRONT, new Position(1, 1));
        cliPlayArea.printPlayArea();
        cliPlayArea.addCard(CardRegistry.getRegistry().getResourceCardFromName("resource_animal_5"), SideType.FRONT, new Position(2, 0));
        cliPlayArea.printPlayArea();
        cliPlayArea.addCard(CardRegistry.getRegistry().getGoldCardFromName("gold_insect_2"), SideType.BACK, new Position(-1, -1));
        cliPlayArea.printPlayArea();
        cliPlayArea.addCard(CardRegistry.getRegistry().getResourceCardFromName("resource_plant_4"), SideType.FRONT, new Position(-2, -2));
        cliPlayArea.printPlayArea();

        PlayArea testPlayArea = new PlayArea();
        testPlayArea.setStarterCard(CardRegistry.getRegistry().getStarterCardFromName("starter_6"), SideType.BACK);
        testPlayArea.playCard(CardRegistry.getRegistry().getGoldCardFromName("gold_insect_5"), SideType.BACK, new Position(1, 1));
        testPlayArea.playCard(CardRegistry.getRegistry().getResourceCardFromName("resource_plant_4"), SideType.FRONT, new Position(-1, 1));
        testPlayArea.playCard(CardRegistry.getRegistry().getResourceCardFromName("resource_animal_3"), SideType.FRONT, new Position(2, 2));

        CLIPlayArea otherCliPlayArea = new CLIPlayArea(testPlayArea.getPlacementOrder(), testPlayArea.getField(), testPlayArea.getActiveSides());

        otherCliPlayArea.addCard(CardRegistry.getRegistry().getGoldCardFromName("gold_animal_6"), SideType.FRONT, new Position(3,1));

        otherCliPlayArea.printPlayArea();

        System.out.println(otherCliPlayArea.getPlaceablePositions());
    }
}
