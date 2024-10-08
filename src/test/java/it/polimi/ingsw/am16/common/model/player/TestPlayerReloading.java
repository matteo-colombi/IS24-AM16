package it.polimi.ingsw.am16.common.model.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.NoStarterCardException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.server.VirtualView;
import it.polimi.ingsw.am16.server.controller.ChatController;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPlayerReloading {

    /*
     * This test checks that players are serialized and deserialized correctly.
     */

    @Test
    void testPlayerReloading() throws NoStarterCardException, UnknownObjectiveCardException, IllegalMoveException, IOException {
        CardRegistry registry = CardRegistry.getRegistry();
        ObjectMapper mapper = JsonMapper.getObjectMapper();
        String directoryPath = "src/test/resources/json";
        File directory = new File(directoryPath);
        directory.mkdirs();
        String filePath = directoryPath + "/testPlayerReloading.json";
        File f = new File(filePath);

        Player player = new Player("matteo");

        player.giveStarterCard(registry.getStarterCardFromName("starter_3"));
        player.setColor(PlayerColor.RED);
        player.chooseStarterCardSide(SideType.BACK);

        player.giveObjectiveOptions(registry.getObjectiveCardFromName("objective_resources_2"), registry.getObjectiveCardFromName("objective_pattern_5"));
        player.setObjectiveCard(registry.getObjectiveCardFromName("objective_pattern_5"));

        player.giveCard(registry.getResourceCardFromName("resource_fungi_6"));
        player.giveCard(registry.getGoldCardFromName("gold_animal_2"));
        player.giveCard(registry.getGoldCardFromName("gold_insect_4"));
        player.giveCard(registry.getResourceCardFromName("resource_animal_10"));

        player.playCard(registry.getResourceCardFromName("resource_animal_10"), SideType.FRONT, new Position(1, 1));
        player.playCard(registry.getGoldCardFromName("gold_insect_4"), SideType.BACK, new Position(-1, 1));

        ChatController chatController = new ChatController(new VirtualView());

        Player fakePlayer = new Player("L2C");
        fakePlayer.getChat().subscribe(chatController);
        player.getChat().subscribe(chatController);

        chatController.sendMessage("L2C", "I am a message from fakePlayer");

        chatController.sendMessage("L2C", "This is a chat message!!!", Set.of("L2C", "xLorde"), true);
        chatController.sendMessage("L2C", "This is another chat message!");

        mapper.writeValue(f, player);

        Player reloadedPlayer = mapper.readValue(f, Player.class);

        assertEquals(player.getUsername(), reloadedPlayer.getUsername());
        assertEquals(player.getGamePoints(), reloadedPlayer.getGamePoints());
        assertEquals(player.getObjectivePoints(), reloadedPlayer.getObjectivePoints());
        assertEquals(player.getPlayerColor(), reloadedPlayer.getPlayerColor());
        assertEquals(player.getStarterCard(), reloadedPlayer.getStarterCard());
        assertEquals(player.getPersonalObjectiveOptions(), reloadedPlayer.getPersonalObjectiveOptions());
        assertEquals(player.getPersonalObjective(), reloadedPlayer.getPersonalObjective());
        assertEquals(player.getHand().getCards(), reloadedPlayer.getHand().getCards());
        assertEquals(player.getHand().getRestrictedVersion(), reloadedPlayer.getHand().getRestrictedVersion());
        assertEquals(player.getHand().getRestrictedVersion().toString(), reloadedPlayer.getHand().getRestrictedVersion().toString());
        assertEquals(player.getHand().toString(), reloadedPlayer.getHand().toString());
        assertEquals(player.getChosePersonalObjective(), reloadedPlayer.getChosePersonalObjective());
        assertEquals(player.getChoseColor(), reloadedPlayer.getChoseColor());
        assertEquals(player.getChoseStarterCardSide(), reloadedPlayer.getChoseStarterCardSide());
        assertEquals(player.getPlayArea().getField(), reloadedPlayer.getPlayArea().getField());
        assertEquals(player.getPlayArea().getObjectCounts(), reloadedPlayer.getPlayArea().getObjectCounts());
        assertEquals(player.getPlayArea().getPlacementOrder(), reloadedPlayer.getPlayArea().getPlacementOrder());
        assertEquals(player.getPlayArea().getActiveSides(), reloadedPlayer.getPlayArea().getActiveSides());
        assertEquals(player.getPlayArea().getCardCount(), reloadedPlayer.getPlayArea().getCardCount());
        assertEquals(player.getPlayArea().getResourceAndObjectCounts(), reloadedPlayer.getPlayArea().getResourceAndObjectCounts());
        assertEquals(player.getPlayArea().getMinX(), reloadedPlayer.getPlayArea().getMinX());
        assertEquals(player.getPlayArea().getMaxX(), reloadedPlayer.getPlayArea().getMaxX());
        assertEquals(player.getPlayArea().getMinY(), reloadedPlayer.getPlayArea().getMinY());
        assertEquals(player.getPlayArea().getMaxY(), reloadedPlayer.getPlayArea().getMaxY());
        assertEquals(player.getChat().getUsername(), reloadedPlayer.getChat().getUsername());
        assertEquals(player.getChat().getMessages(), reloadedPlayer.getChat().getMessages());
    }
}
