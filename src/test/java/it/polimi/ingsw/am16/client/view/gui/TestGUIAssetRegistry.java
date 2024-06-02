package it.polimi.ingsw.am16.client.view.gui;

import it.polimi.ingsw.am16.client.view.gui.util.GUIAssetRegistry;
import it.polimi.ingsw.am16.common.model.cards.*;
import org.junit.jupiter.api.Test;

public class TestGUIAssetRegistry {
    @Test
    void testGUICardAssetRegistry() {
        CardRegistry.getRegistry();

        for(ResourceCard card : CardRegistry.getRegistry().getResourceCards()) {
            System.out.println(card.getName() + " -> " + GUIAssetRegistry.getAssetName(card.getName(), SideType.FRONT));
            System.out.println(card.getName() + " -> " + GUIAssetRegistry.getAssetName(card.getName(), SideType.BACK));
        }

        for(GoldCard card : CardRegistry.getRegistry().getGoldCards()) {
            System.out.println(card.getName() + " -> " + GUIAssetRegistry.getAssetName(card.getName(), SideType.FRONT));
            System.out.println(card.getName() + " -> " + GUIAssetRegistry.getAssetName(card.getName(), SideType.BACK));
        }

        for(ObjectiveCard card : CardRegistry.getRegistry().getObjectiveCards()) {
            System.out.println(card.getName() + " -> " + GUIAssetRegistry.getAssetName(card.getName(), SideType.FRONT));
            System.out.println(card.getName() + " -> " + GUIAssetRegistry.getAssetName(card.getName(), SideType.BACK));
        }

        for(StarterCard card : CardRegistry.getRegistry().getStarterCards()) {
            System.out.println(card.getName() + " -> " + GUIAssetRegistry.getAssetName(card.getName(), SideType.FRONT));
            System.out.println(card.getName() + " -> " + GUIAssetRegistry.getAssetName(card.getName(), SideType.BACK));
        }
    }
}
