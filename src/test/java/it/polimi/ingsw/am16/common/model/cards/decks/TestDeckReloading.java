package it.polimi.ingsw.am16.common.model.cards.decks;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDeckReloading {

    /*
     * This test checks that decks get serialized and deserialized correctly.
     */

    @Test
    public void testDeckReloading() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CardRegistry.getRegistry();
        String directoryPath = "src/test/resources/json";
        File directory = new File(directoryPath);
        directory.mkdirs();
        String filePath = directoryPath + "/testDeckSave.json";
        File f = new File(filePath);
        f.createNewFile();

        ObjectiveCardsDeck objectiveDeck = DeckFactory.getObjectiveCardsDeck();
        mapper.writeValue(f, objectiveDeck);
        ObjectiveCardsDeck loadedObjectiveDeck = mapper.readValue(f, ObjectiveCardsDeck.class);
        assertEquals(objectiveDeck, loadedObjectiveDeck);

        StarterCardsDeck starterDeck = DeckFactory.getStarterCardsDeck();
        mapper.writeValue(f, starterDeck);
        StarterCardsDeck loadedStarterDeck = mapper.readValue(f, StarterCardsDeck.class);
        assertEquals(starterDeck, loadedStarterDeck);

        ResourceCardsDeck resourceDeck = DeckFactory.getResourceCardsDeck();
        mapper.writeValue(f, resourceDeck);
        ResourceCardsDeck loadedResourceDeck = mapper.readValue(f, ResourceCardsDeck.class);
        assertEquals(resourceDeck, loadedResourceDeck);

        GoldCardsDeck goldDeck = DeckFactory.getGoldCardsDeck();
        mapper.writeValue(f, goldDeck);
        GoldCardsDeck loadedGoldDeck = mapper.readValue(f, GoldCardsDeck.class);
        assertEquals(goldDeck, loadedGoldDeck);
    }
}
