package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.model.cards.BoardCard;
import it.polimi.ingsw.am16.common.model.cards.Card;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.players.PlayArea;
import it.polimi.ingsw.am16.common.util.Position;

import javafx.geometry.Side;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;


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

        Map<BoardCard, SideType> converted = testPlayArea.getActiveSides().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getSideType()));

        CLIPlayArea otherCliPlayArea = new CLIPlayArea(testPlayArea.getPlacementOrder(), testPlayArea.getField(), converted);

        otherCliPlayArea.printPlayArea();

        otherCliPlayArea.addCard(CardRegistry.getRegistry().getGoldCardFromName("gold_plant_8"), SideType.FRONT, new Position(5, 0));

        otherCliPlayArea.printPlayArea();
    }
}
