package it.polimi.ingsw.am16.common.model.cards;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestCardRegistry {

    @Test
    public void testCardRegistry() {
        // This test does not guarantee that all the cards in the registry are actually correct.
        // All it does is check that the names, amounts and types are correct.

        CardRegistry registry = CardRegistry.getRegistry();
        
        /*
            Check that the total amount of cards is correct.
         */
        int totalCards = 0;
        assertEquals(40, registry.getResourceCards().size());
        totalCards += registry.getResourceCards().size();

        assertEquals(40, registry.getGoldCards().size());
        totalCards += registry.getGoldCards().size();

        assertEquals(16, registry.getObjectiveCards().size());
        totalCards += registry.getObjectiveCards().size();

        assertEquals(6, registry.getStarterCards().size());
        totalCards += registry.getStarterCards().size();

        assertEquals(102, totalCards);

        /*
            Check that all the card names are correct. Check all types are correct.
         */
        int i = 1;
        for(StarterCard card : registry.getStarterCards()) {
            assertEquals("starter_" + i, card.getName());
            assertNull(card.getType());
            i++;
        }
        i = 1;
        for(ObjectiveCard objectiveCard : registry.getObjectiveCards()) {
            if (i >= 1 && i <= 8) {
                assertEquals("objective_pattern_" + i, objectiveCard.getName());
            } else if (i >= 9 && i <= 12) {
                assertEquals("objective_resources_" + (i-8), objectiveCard.getName());
            } else if (i >= 13 && i <= 16) {
                assertEquals("objective_object_" + (i-12), objectiveCard.getName());
            } else {
                fail();
            }
            i++;
        }
        List<ResourceCard> resourceCards = registry.getResourceCards();
        List<GoldCard> goldCards = registry.getGoldCards();

        for(i = 1; i <= resourceCards.size(); i++) {
            if (i >= 1 && i <= 10) {
                assertEquals("resource_fungi_" + i, resourceCards.get(i-1).getName());
                assertEquals("gold_fungi_" + i, goldCards.get(i-1).getName());
                assertEquals(ResourceType.FUNGI, resourceCards.get(i-1).getType());
                assertEquals(ResourceType.FUNGI, goldCards.get(i-1).getType());
            } else if (i >= 11 && i <= 20) {
                assertEquals("resource_plant_" + (i-10), resourceCards.get(i-1).getName());
                assertEquals("gold_plant_" + (i-10), goldCards.get(i-1).getName());
                assertEquals(ResourceType.PLANT, resourceCards.get(i-1).getType());
                assertEquals(ResourceType.PLANT, goldCards.get(i-1).getType());
            } else if (i >= 21 && i <= 30) {
                assertEquals("resource_animal_" + (i-20), resourceCards.get(i-1).getName());
                assertEquals("gold_animal_" + (i-20), goldCards.get(i-1).getName());
                assertEquals(ResourceType.ANIMAL, resourceCards.get(i-1).getType());
                assertEquals(ResourceType.ANIMAL, goldCards.get(i-1).getType());
            } else if (i >= 31 && i <= 40) {
                assertEquals("resource_insect_" + (i-30), resourceCards.get(i-1).getName());
                assertEquals("gold_insect_" + (i-30), goldCards.get(i-1).getName());
                assertEquals(ResourceType.INSECT, resourceCards.get(i-1).getType());
                assertEquals(ResourceType.INSECT, goldCards.get(i-1).getType());
            } else {
                fail();
            }
        }
    }
}