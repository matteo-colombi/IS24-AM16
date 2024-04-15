package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.cards.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class TestCLIAssetRegistry {
    @Test
    void testCliAssetRegistry() throws IOException {
        CLIAssetRegistry assetRegistry = CLIAssetRegistry.getCLIAssetRegistry();
        for(ResourceCard card : CardRegistry.getRegistry().getResourceCards()) {
            System.out.println(card.getName());
            System.out.println("front");
            assetRegistry.getCard(card.getName()).getFront().printText();
            System.out.println("back");
            assetRegistry.getCard(card.getName()).getBack().printText();
        }

        for(GoldCard card : CardRegistry.getRegistry().getGoldCards()) {
            System.out.println(card.getName());
            System.out.println("front");
            assetRegistry.getCard(card.getName()).getFront().printText();
            System.out.println("back");
            assetRegistry.getCard(card.getName()).getBack().printText();
        }

        for(StarterCard card : CardRegistry.getRegistry().getStarterCards()) {
            System.out.println(card.getName());
            System.out.println("front");
            assetRegistry.getCard(card.getName()).getFront().printText();
            System.out.println("back");
            assetRegistry.getCard(card.getName()).getBack().printText();
        }

        for(ObjectiveCard card : CardRegistry.getRegistry().getObjectiveCards()) {
            System.out.println(card.getName());
            System.out.println("front");
            assetRegistry.getCard(card.getName()).getFront().printText();
        }
    }
}