package it.polimi.ingsw.am16.common.model.cards;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class TestCardRegistry {

    @Test
    public void testCardRegistry() {
        // This test does not guarantee that all the cards in the registry are actually correct.
        // All it does is check that the names and amounts are correct.

        assertTrue(CardRegistry.initializeRegistry());

        /*
            Check that the total amount of cards is correct.
         */
        int totalCards = 0;
        assertEquals(40, CardRegistry.getResourceCards().size());
        totalCards += CardRegistry.getResourceCards().size();

        assertEquals(40, CardRegistry.getGoldCards().size());
        totalCards += CardRegistry.getGoldCards().size();

        assertEquals(16, CardRegistry.getObjectiveCards().size());
        totalCards += CardRegistry.getObjectiveCards().size();

        assertEquals(6, CardRegistry.getStarterCards().size());
        totalCards += CardRegistry.getStarterCards().size();

        assertEquals(102, totalCards);

        /*
            Check that all the card names are correct. Check all types are correct.
         */
        int i = 1;
        for(StarterCard card : CardRegistry.getStarterCards()) {
            assertEquals("starter_" + i, card.getName());
            assertNull(card.getType());
            i++;
        }
        i = 1;
        for(ObjectiveCard objectiveCard : CardRegistry.getObjectiveCards()) {
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
        List<ResourceCard> resourceCards = CardRegistry.getResourceCards();
        List<GoldCard> goldCards = CardRegistry.getGoldCards();

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