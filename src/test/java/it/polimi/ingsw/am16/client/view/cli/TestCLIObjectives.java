package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import org.junit.jupiter.api.Test;

public class TestCLIObjectives {
    @Test
    void testCLIObjectives() {
        ObjectiveCard[] objectiveCards = new ObjectiveCard[2];
        objectiveCards[0] = CardRegistry.getRegistry().getObjectiveCardFromName("objective_pattern_6");
        objectiveCards[1] = CardRegistry.getRegistry().getObjectiveCardFromName("objective_resources_2");

        CLI cli = new CLI();
        cli.setCommonObjectives(objectiveCards);
    }
}
