package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.cards.*;
import org.junit.jupiter.api.Test;

class TestCLICardAssetRegistry {
    @Test
    void testCliAssetRegistry() {
        CLIAssetRegistry assetRegistry = CLIAssetRegistry.getCLIAssetRegistry();
        for(ResourceCard card : CardRegistry.getRegistry().getResourceCards()) {
            System.out.println(card.getName());
            System.out.println("front");
            assetRegistry.getCard(card.getName()).front().printText(false);
            System.out.println("back");
            assetRegistry.getCard(card.getName()).back().printText(false);
        }

        for(GoldCard card : CardRegistry.getRegistry().getGoldCards()) {
            System.out.println(card.getName());
            System.out.println("front");
            assetRegistry.getCard(card.getName()).front().printText(false);
            System.out.println("back");
            assetRegistry.getCard(card.getName()).back().printText(false);
        }

        for(StarterCard card : CardRegistry.getRegistry().getStarterCards()) {
            System.out.println(card.getName());
            System.out.println("front");
            assetRegistry.getCard(card.getName()).front().printText(false);
            System.out.println("back");
            assetRegistry.getCard(card.getName()).back().printText(false);
        }

        for(ObjectiveCard card : CardRegistry.getRegistry().getObjectiveCards()) {
            System.out.println(card.getName());
            System.out.println("front");
            assetRegistry.getCard(card.getName()).front().printText(false);
        }
    }
}