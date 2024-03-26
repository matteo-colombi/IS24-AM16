package it.polimi.ingsw.am16.common.model.cards.decks;

import it.polimi.ingsw.am16.common.model.cards.*;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDecks {

    @Test
    public void testDecks() {
        CardRegistry.getRegistry();
        testResourceDeck(DeckFactory.getResourceCardsDeck());
        testGoldDeck(DeckFactory.getGoldCardsDeck());
        testObjectiveDeck(DeckFactory.getObjectiveCardsDeck());
        testStarterDeck(DeckFactory.getStarterCardsDeck());
    }

    private void testResourceDeck(ResourceCardsDeck deck) {
        int cardNum = 0;
        Map<ResourceType, Integer> typeMap = new EnumMap<>(ResourceType.class);
        while(!deck.isEmpty()) {
            ResourceType type = deck.peekTop().getType();
            ResourceCard card = deck.drawCard();
            assertEquals(type, card.getType());
            typeMap.putIfAbsent(type, 0);
            typeMap.merge(type, 1, Integer::sum);
            cardNum++;
        }
        assertEquals(40, cardNum);
        for(int n : typeMap.values()) {
            assertEquals(10, n);
        }
    }

    private void testGoldDeck(GoldCardsDeck deck) {
        int cardNum = 0;
        Map<ResourceType, Integer> typeMap = new EnumMap<>(ResourceType.class);
        while(!deck.isEmpty()) {
            ResourceType type = deck.peekTop().getType();
            GoldCard card = deck.drawCard();
            assertEquals(type, card.getType());
            typeMap.putIfAbsent(type, 0);
            typeMap.merge(type, 1, Integer::sum);
            cardNum++;
        }
        assertEquals(40, cardNum);
        for(int n : typeMap.values()) {
            assertEquals(10, n);
        }
    }

    private void testObjectiveDeck(ObjectiveCardsDeck deck) {
        Pattern p = Pattern.compile("objective_(\\p{Alpha}+)_(\\d+)");
        int cardNum = 0;
        Map<String, Integer> typeMap = new HashMap<>();
        while(!deck.isEmpty()) {
            ObjectiveCard peekCard = deck.peekTop();
            ObjectiveCard card = deck.drawCard();
            assertEquals(peekCard, card);
            Matcher m = p.matcher(card.getName());
            if(m.matches()) {
                String type = m.group(1);
                typeMap.putIfAbsent(type, 0);
                typeMap.merge(type, 1, Integer::sum);
            }
            cardNum++;
        }
        assertEquals(16, cardNum);
        assertEquals(8, typeMap.get("pattern"));
        assertEquals(4, typeMap.get("resources"));
        assertEquals(4, typeMap.get("object"));
    }

    private void testStarterDeck(StarterCardsDeck deck) {
        int cardNum = 0;
        while(!deck.isEmpty()) {
            StarterCard peekCard = deck.peekTop();
            StarterCard card = deck.drawCard();
            assertEquals(peekCard, card);
            cardNum++;
        }
        assertEquals(6, cardNum);
    }
}
