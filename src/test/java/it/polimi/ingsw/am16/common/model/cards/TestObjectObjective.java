package it.polimi.ingsw.am16.common.model.cards;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.NoStarterCardException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.util.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestObjectObjective {

    @Test
    public void testObjectObjective() throws NoStarterCardException, IllegalMoveException, UnknownObjectiveCardException {
        CardRegistry registry = CardRegistry.getRegistry();

        ObjectiveCard obj1 = registry.getObjectiveCards().get(12);
        ObjectiveCard obj2 = registry.getObjectiveCards().get(13);
        ObjectiveCard obj3 = registry.getObjectiveCards().get(14);
        ObjectiveCard obj4 = registry.getObjectiveCards().get(15);

        StarterCard starter = registry.getStarterCards().getFirst();

        Player pObj1 = new Player(0, "testPlayer");
        Player pObj2 = new Player(0, "testPlayer");
        Player pObj3 = new Player(0, "testPlayer");
        Player pObj4 = new Player(0, "testPlayer");

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


        PlayableCard withQuill = registry.getResourceCards().get(34);
        PlayableCard withManuscript = registry.getResourceCards().get(35);
        PlayableCard withInkwell = registry.getResourceCards().get(36);

        assertEquals(0, pObj1.getObjectivePoints());

        //TEST the objective with all three cards.
        pObj1.giveCard(withQuill);
        pObj1.giveCard(withManuscript);
        pObj1.giveCard(withInkwell);
        pObj1.giveCard(withInkwell);
        pObj1.playCard(withQuill, SideType.FRONT, new Position(1, -1));
        pObj1.playCard(withManuscript, SideType.FRONT, new Position(2, -2));
        pObj1.playCard(withInkwell, SideType.FRONT, new Position(3, -3));
        pObj1.playCard(withInkwell, SideType.FRONT, new Position(4, -2));

        pObj1.evaluatePersonalObjective();

        assertEquals(3, pObj1.getObjectivePoints());

        pObj1.giveCard(withQuill);
        pObj1.giveCard(withManuscript);
        pObj1.playCard(withQuill, SideType.FRONT, new Position(5, -1));
        pObj1.playCard(withManuscript, SideType.FRONT, new Position(6, -2));

        pObj1.evaluatePersonalObjective();

        assertEquals(9, pObj1.getObjectivePoints());

        //TEST the manuscript objective
        pObj2.giveCard(withQuill);
        pObj2.giveCard(withQuill);
        pObj2.playCard(withQuill, SideType.FRONT, new Position(1, -1));
        pObj2.playCard(withQuill, SideType.FRONT, new Position(2, -2));

        pObj2.evaluatePersonalObjective();

        assertEquals(0, pObj2.getObjectivePoints());

        pObj2.giveCard(withManuscript);
        pObj2.giveCard(withManuscript);
        pObj2.playCard(withManuscript, SideType.FRONT, new Position(3, -3));
        pObj2.playCard(withManuscript, SideType.FRONT, new Position(4, -4));

        pObj2.evaluatePersonalObjective();

        assertEquals(2, pObj2.getObjectivePoints());

        pObj2.giveCard(withManuscript);
        pObj2.playCard(withManuscript, SideType.FRONT, new Position(5, -5));

        pObj2.evaluatePersonalObjective();

        assertEquals(4, pObj2.getObjectivePoints());

        pObj2.giveCard(withManuscript);
        pObj2.playCard(withManuscript, SideType.FRONT, new Position(6, -6));

        pObj2.evaluatePersonalObjective();

        assertEquals(8, pObj2.getObjectivePoints());

        // TEST the inkwell objective
        pObj3.giveCard(withQuill);
        pObj3.giveCard(withQuill);
        pObj3.playCard(withQuill, SideType.FRONT, new Position(1, -1));
        pObj3.playCard(withQuill, SideType.FRONT, new Position(2, -2));

        pObj3.evaluatePersonalObjective();

        assertEquals(0, pObj3.getObjectivePoints());

        pObj3.giveCard(withInkwell);
        pObj3.giveCard(withInkwell);
        pObj3.playCard(withInkwell, SideType.FRONT, new Position(3, -3));
        pObj3.playCard(withInkwell, SideType.FRONT, new Position(4, -2));

        pObj3.evaluatePersonalObjective();

        assertEquals(2, pObj3.getObjectivePoints());

        pObj3.giveCard(withInkwell);
        pObj3.playCard(withInkwell, SideType.FRONT, new Position(5, -1));

        pObj3.evaluatePersonalObjective();

        assertEquals(4, pObj3.getObjectivePoints());

        pObj3.giveCard(withInkwell);
        pObj3.playCard(withInkwell, SideType.FRONT, new Position(6, 0));

        pObj3.evaluatePersonalObjective();

        assertEquals(8, pObj3.getObjectivePoints());

        // TEST the quill objective
        pObj4.giveCard(withManuscript);
        pObj4.giveCard(withManuscript);
        pObj4.playCard(withManuscript, SideType.FRONT, new Position(1, 1));
        pObj4.playCard(withManuscript, SideType.FRONT, new Position(2, 2));

        pObj4.evaluatePersonalObjective();

        assertEquals(0, pObj4.getObjectivePoints());

        pObj4.giveCard(withQuill);
        pObj4.giveCard(withQuill);
        pObj4.playCard(withQuill, SideType.FRONT, new Position(3, 1));
        pObj4.playCard(withQuill, SideType.FRONT, new Position(4, 0));

        pObj4.evaluatePersonalObjective();

        assertEquals(2, pObj4.getObjectivePoints());

        pObj4.giveCard(withQuill);
        pObj4.playCard(withQuill, SideType.FRONT, new Position(5, -1));

        pObj4.evaluatePersonalObjective();

        assertEquals(4, pObj4.getObjectivePoints());

        pObj4.giveCard(withQuill);
        pObj4.playCard(withQuill, SideType.FRONT, new Position(6, -2));

        pObj4.evaluatePersonalObjective();

        assertEquals(8, pObj4.getObjectivePoints());
    }
}
