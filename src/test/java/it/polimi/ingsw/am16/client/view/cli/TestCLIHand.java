package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestCLIHand {
    @Test
    void testCLIHand() {
        CLI cli = new CLI();
        cli.joinGame("test","test");
        List<PlayableCard> hand = new ArrayList<>();
        hand.add(CardRegistry.getRegistry().getGoldCardFromName("gold_fungi_3"));
        hand.add(CardRegistry.getRegistry().getGoldCardFromName("gold_animal_4"));
        hand.add(CardRegistry.getRegistry().getResourceCardFromName("resource_insect_10"));

        cli.setHand(hand);

        List<RestrictedCard> restrictedHand = new ArrayList<>();
        restrictedHand.add(hand.get(0).getRestrictedVersion());
        restrictedHand.add(hand.get(1).getRestrictedVersion());
        restrictedHand.add(hand.get(2).getRestrictedVersion());

        cli.setOtherHand("test", restrictedHand);
        cli.printOtherHand("test");
    }
}
