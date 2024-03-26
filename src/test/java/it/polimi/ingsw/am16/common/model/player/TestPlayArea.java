package it.polimi.ingsw.am16.common.model.player;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.players.PlayArea;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.util.Position;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestPlayArea {
    @Test
    public void testPlayArea() throws IllegalMoveException, UnknownObjectiveCardException {
        CardRegistry registry = CardRegistry.getRegistry();

        Player p = new Player(0, "cornerable");
        PlayArea playArea = p.getPlayArea();

        ResourceCard plantResource = registry.getResourceCards().get(10);

        // check that you can't place isolated cards (no starting card yet)
        assertThrows(IllegalMoveException.class, () -> playArea.playCard(plantResource, SideType.BACK, new Position(0, 0)));
        assertEquals(0, playArea.getMinX());
        assertEquals(0, playArea.getMaxX());
        assertEquals(0, playArea.getMinY());
        assertEquals(0, playArea.getMaxY());

        StarterCard starter = registry.getStarterCards().get(4);
        playArea.setStarterCard(starter, SideType.BACK);
        assertEquals(0, playArea.getMinX());
        assertEquals(0, playArea.getMaxX());
        assertEquals(0, playArea.getMinY());
        assertEquals(0, playArea.getMaxY());

        // check that the starter card was placed correctly
        assertEquals(starter, playArea.getField().get(new Position(0, 0)));

        // check that the map returned by playArea.getField() is unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> playArea.getField().put(new Position(1, 1), plantResource));

        // check that you can't place a card on a blocked corner
        assertThrows(IllegalMoveException.class, () -> playArea.playCard(plantResource, SideType.FRONT, new Position(1, -1)));

        // check that you can't place a card for which you don't have the resources
        final GoldCard finalGoldCard = registry.getGoldCards().get(9);
        assertThrows(IllegalMoveException.class, () -> playArea.playCard(finalGoldCard, SideType.FRONT, new Position(1, 1)));

        playArea.playCard(plantResource, SideType.FRONT, new Position(-1, 1));

        Map<ResourceType, Integer> resourceCounts = playArea.getResourceCounts();
        Map<ObjectType, Integer> objectCounts = playArea.getObjectCounts();

        // check that the resources get counted correctly
        assertEquals(3, resourceCounts.get(ResourceType.PLANT));
        assertEquals(1, resourceCounts.get(ResourceType.ANIMAL));
        assertEquals(1, resourceCounts.get(ResourceType.INSECT));

        GoldCard goldCard = registry.getGoldCards().get(10);

        ResourceCard animalCard = registry.getResourceCards().get(26);
        playArea.playCard(animalCard, SideType.FRONT, new Position(0, 2));

        objectCounts = playArea.getObjectCounts();

        // check that objects are counted correctly
        assertEquals(1, objectCounts.get(ObjectType.QUILL));

        p.playCard(goldCard, SideType.FRONT, new Position(-2, 2));

        resourceCounts = playArea.getResourceCounts();
        objectCounts = playArea.getObjectCounts();

        // check that the placed card actually gave points
        assertEquals(2, p.getGamePoints());
        assertEquals(0, p.getObjectivePoints());

        // double check all reasource and object counts
        assertEquals(1, resourceCounts.get(ResourceType.FUNGI));
        assertEquals(2, resourceCounts.get(ResourceType.PLANT));
        assertEquals(2, resourceCounts.get(ResourceType.ANIMAL));
        assertEquals(1, resourceCounts.get(ResourceType.INSECT));
        assertEquals(2, objectCounts.get(ObjectType.QUILL));
        assertEquals(0, objectCounts.get(ObjectType.MANUSCRIPT));
        assertEquals(0, objectCounts.get(ObjectType.INKWELL));

        ResourceCard fungiResource = registry.getResourceCards().getFirst();

        // check that you can't place an isolated card (with starter card this time)
        assertThrows(IllegalMoveException.class, () -> playArea.playCard(fungiResource, SideType.FRONT, new Position(5, -33)));

        animalCard = registry.getResourceCards().get(21);
        p.playCard(animalCard, SideType.FRONT, new Position(-1, 3));

        goldCard = registry.getGoldCards().get(25);
        p.playCard(goldCard, SideType.FRONT, new Position(1, 1));

        // check that points were awarded correctly
        assertEquals(6, p.getGamePoints());
        assertEquals(0, p.getObjectivePoints());

        // check that you can't place a card off the "accepted" grid
        final ResourceCard finalAnimalCard = animalCard;
        assertThrows(IllegalMoveException.class, () -> playArea.playCard(finalAnimalCard, SideType.BACK, new Position(1, 2)));

        animalCard = registry.getResourceCards().get(20);
        p.playCard(animalCard, SideType.BACK, new Position(0, 4));

        GoldCard omegaAnimalCard = registry.getGoldCards().get(29);
        p.playCard(omegaAnimalCard, SideType.FRONT, new Position(1, 5));

        // check that points were awarded correctly
        assertEquals(11, p.getGamePoints());

        resourceCounts = playArea.getResourceCounts();
        objectCounts = playArea.getObjectCounts();

        // double check all counts
        assertEquals(0, resourceCounts.get(ResourceType.FUNGI));
        assertEquals(2, resourceCounts.get(ResourceType.PLANT));
        assertEquals(5, resourceCounts.get(ResourceType.ANIMAL));
        assertEquals(1, resourceCounts.get(ResourceType.INSECT));
        assertEquals(1, objectCounts.get(ObjectType.QUILL));
        assertEquals(0, objectCounts.get(ObjectType.MANUSCRIPT));
        assertEquals(0, objectCounts.get(ObjectType.INKWELL));

        // check bounds
        assertEquals(-2, playArea.getMinX());
        assertEquals(1, playArea.getMaxX());
        assertEquals(0, playArea.getMinY());
        assertEquals(5, playArea.getMaxY());

        // hurray!
    }
}
