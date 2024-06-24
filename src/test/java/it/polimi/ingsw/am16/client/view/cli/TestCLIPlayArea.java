package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.util.Position;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

public class TestCLIPlayArea {
    @Test
    void testCLIPlayArea() {
        CLIPlayArea cliPlayArea = new CLIPlayArea();
        cliPlayArea.addCard(CardRegistry.getRegistry().getStarterCardFromName("starter_1"), SideType.FRONT, new Position(0, 0), Set.of(), Set.of(), Map.of(), Map.of());
        cliPlayArea.printPlayArea();
        cliPlayArea.addCard(CardRegistry.getRegistry().getResourceCardFromName("resource_fungi_3"), SideType.FRONT, new Position(1, 1), Set.of(), Set.of(), Map.of(), Map.of());
        cliPlayArea.printPlayArea();
        cliPlayArea.addCard(CardRegistry.getRegistry().getResourceCardFromName("resource_animal_5"), SideType.FRONT, new Position(2, 0), Set.of(), Set.of(), Map.of(), Map.of());
        cliPlayArea.printPlayArea();
        cliPlayArea.addCard(CardRegistry.getRegistry().getGoldCardFromName("gold_insect_2"), SideType.BACK, new Position(-1, -1), Set.of(), Set.of(), Map.of(), Map.of());
        cliPlayArea.printPlayArea();
        cliPlayArea.addCard(CardRegistry.getRegistry().getResourceCardFromName("resource_plant_4"), SideType.FRONT, new Position(-2, -2), Set.of(), Set.of(), Map.of(), Map.of());
        cliPlayArea.printPlayArea();

        cliPlayArea.addCard(CardRegistry.getRegistry().getGoldCardFromName("gold_plant_4"), SideType.BACK, new Position(3, -1), Set.of(), Set.of(), Map.of(), Map.of());
        cliPlayArea.addCard(CardRegistry.getRegistry().getGoldCardFromName("gold_fungi_5"), SideType.BACK, new Position(4, 0), Set.of(), Set.of(), Map.of(), Map.of());
        cliPlayArea.addCard(CardRegistry.getRegistry().getGoldCardFromName("gold_animal_6"), SideType.BACK, new Position(5, 1), Set.of(), Set.of(), Map.of(), Map.of());
        cliPlayArea.addCard(CardRegistry.getRegistry().getGoldCardFromName("gold_insect_7"), SideType.BACK, new Position(6, 0), Set.of(), Set.of(), Map.of(), Map.of());

        cliPlayArea.printPlayArea();

        cliPlayArea.moveView(1);

        cliPlayArea.printPlayArea();

        cliPlayArea.moveView(1);

        cliPlayArea.printPlayArea();

        cliPlayArea.moveView(1);

        cliPlayArea.printPlayArea();

        cliPlayArea.moveView(-1);

        cliPlayArea.printPlayArea();

        cliPlayArea.moveView(-1);

        cliPlayArea.printPlayArea();
    }
}
