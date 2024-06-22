package it.polimi.ingsw.am16.common.model.cards;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.NoStarterCardException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.util.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestResourceObjective {

    /*
     * This test checks that resource objectives' points are counted correctly.
     */

    @Test
    public void testResourceObjective() throws NoStarterCardException, UnknownObjectiveCardException, IllegalMoveException {
        CardRegistry registry = CardRegistry.getRegistry();

        ObjectiveCard obj1 = registry.getObjectiveCards().get(8);
        ObjectiveCard obj2 = registry.getObjectiveCards().get(9);
        ObjectiveCard obj3 = registry.getObjectiveCards().get(10);
        ObjectiveCard obj4 = registry.getObjectiveCards().get(11);

        StarterCard starter = registry.getStarterCards().getFirst();

        Player pObj1 = new Player("testPlayer");
        Player pObj2 = new Player("testPlayer");
        Player pObj3 = new Player("testPlayer");
        Player pObj4 = new Player("testPlayer");

        pObj1.giveStarterCard(starter);
        pObj1.chooseStarterCardSide(SideType.FRONT);
        pObj1.giveObjectiveOptions(obj1, obj1);
        pObj1.setObjectiveCard(obj1);
        pObj2.giveStarterCard(starter);
        pObj2.chooseStarterCardSide(SideType.FRONT);
        pObj2.giveObjectiveOptions(obj2, obj2);
        pObj2.setObjectiveCard(obj2);
        pObj3.giveStarterCard(starter);
        pObj3.chooseStarterCardSide(SideType.FRONT);
        pObj3.giveObjectiveOptions(obj3, obj3);
        pObj3.setObjectiveCard(obj3);
        pObj4.giveStarterCard(starter);
        pObj4.chooseStarterCardSide(SideType.FRONT);
        pObj4.giveObjectiveOptions(obj4, obj4);
        pObj4.setObjectiveCard(obj4);


        PlayableCard withFungi = registry.getResourceCards().getFirst();
        PlayableCard withPlant = registry.getResourceCards().get(10);
        PlayableCard withAnimal = registry.getResourceCards().get(20);
        PlayableCard withInsect = registry.getResourceCards().get(30);

        assertEquals(0, pObj1.getObjectivePoints());

        //TEST the fungi one.
        pObj1.giveCard(withFungi);
        pObj1.playCard(withFungi, SideType.BACK, new Position(1, 1));

        pObj1.evaluatePersonalObjective();

        assertEquals(0, pObj1.getObjectivePoints());

        pObj1.giveCard(withFungi);
        pObj1.playCard(withFungi, SideType.BACK, new Position(2, 2));

        pObj1.evaluatePersonalObjective();

        assertEquals(2, pObj1.getObjectivePoints());

        pObj1.giveCard(withFungi);
        pObj1.giveCard(withFungi);
        pObj1.giveCard(withFungi);
        pObj1.playCard(withFungi, SideType.BACK, new Position(3, 3));
        pObj1.playCard(withFungi, SideType.BACK, new Position(4, 4));
        pObj1.playCard(withFungi, SideType.BACK, new Position(5, 5));

        pObj1.evaluatePersonalObjective();

        assertEquals(6, pObj1.getObjectivePoints());

        //TEST the plant one.
        pObj2.giveCard(withPlant);
        pObj2.giveCard(withPlant);
        pObj2.playCard(withPlant, SideType.BACK, new Position(1, 1));
        pObj2.playCard(withPlant, SideType.BACK, new Position(2, 2));

        pObj2.evaluatePersonalObjective();

        assertEquals(0, pObj2.getObjectivePoints());

        pObj2.giveCard(withPlant);
        pObj2.playCard(withPlant, SideType.BACK, new Position(3, 3));

        pObj2.evaluatePersonalObjective();

        assertEquals(2, pObj2.getObjectivePoints());

        pObj2.giveCard(withPlant);
        pObj2.giveCard(withPlant);
        pObj2.giveCard(withPlant);
        pObj2.playCard(withPlant, SideType.BACK, new Position(4, 4));
        pObj2.playCard(withPlant, SideType.BACK, new Position(5, 5));
        pObj2.playCard(withPlant, SideType.BACK, new Position(6, 6));

        pObj2.evaluatePersonalObjective();

        assertEquals(6, pObj2.getObjectivePoints());

        //TEST the animal one.
        pObj3.giveCard(withAnimal);
        pObj3.playCard(withAnimal, SideType.BACK, new Position(1, 1));

        pObj3.evaluatePersonalObjective();

        assertEquals(0, pObj3.getObjectivePoints());

        pObj3.giveCard(withAnimal);
        pObj3.playCard(withAnimal, SideType.BACK, new Position(2, 2));

        pObj3.evaluatePersonalObjective();

        assertEquals(2, pObj3.getObjectivePoints());

        pObj3.giveCard(withAnimal);
        pObj3.giveCard(withAnimal);
        pObj3.giveCard(withAnimal);
        pObj3.playCard(withAnimal, SideType.BACK, new Position(3, 3));
        pObj3.playCard(withAnimal, SideType.BACK, new Position(4, 4));
        pObj3.playCard(withAnimal, SideType.BACK, new Position(5, 5));

        pObj3.evaluatePersonalObjective();

        assertEquals(6, pObj3.getObjectivePoints());

        //TEST the insect one.
        pObj4.giveCard(withInsect);
        pObj4.playCard(withInsect, SideType.BACK, new Position(1, 1));

        pObj4.evaluatePersonalObjective();

        assertEquals(0, pObj4.getObjectivePoints());

        pObj4.giveCard(withInsect);
        pObj4.playCard(withInsect, SideType.BACK, new Position(2, 2));

        pObj4.evaluatePersonalObjective();

        assertEquals(2, pObj4.getObjectivePoints());

        pObj4.giveCard(withInsect);
        pObj4.giveCard(withInsect);
        pObj4.giveCard(withInsect);
        pObj4.playCard(withInsect, SideType.BACK, new Position(3, 3));
        pObj4.playCard(withInsect, SideType.BACK, new Position(4, 4));
        pObj4.playCard(withInsect, SideType.BACK, new Position(5, 5));

        pObj4.evaluatePersonalObjective();

        assertEquals(6, pObj4.getObjectivePoints());
    }
}
