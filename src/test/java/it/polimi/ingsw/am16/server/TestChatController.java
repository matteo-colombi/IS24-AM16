package it.polimi.ingsw.am16.server;

import it.polimi.ingsw.am16.common.model.chat.Chat;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.server.controller.ChatController;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestChatController {

    /*
     * This test checks that chat messages are getting routed appropriately.
     */

    @Test
    void testChat() {
        ChatController manager = new ChatController(new VirtualView());

        Chat chatAldo = new Chat("Aldo");
        Chat chatGiovanni = new Chat("Giovanni42");
        Chat chatGiacomo = new Chat("xXGiacomoXx");
        chatAldo.subscribe(manager);
        chatGiacomo.subscribe(manager);
        chatGiovanni.subscribe(manager);

        manager.sendMessage("Aldo", "Miiiiiiiiiiiiiiiiiii", Set.of("xXGiacomoXx"), true);

        manager.sendMessage("Giovanni42", "Ascoltate PoretCast raga");

        manager.sendMessage("Giovanni42", "Ma siete scemi?", Set.of("Aldo", "L2C", "Giovanni42"), true);

        chatGiacomo.unsubscribe();

        manager.broadcast("AO smettetela di fare gli scemi");

        System.out.println("Giovanni's chat:");
        for(ChatMessage m : chatGiovanni.getMessages()) {
            System.out.println(m);
        }

        System.out.println("Aldo's chat:");
        for(ChatMessage m : chatAldo.getMessages()) {
            System.out.println(m);
        }

        System.out.println("Giacomo's chat:");
        for(ChatMessage m : chatGiacomo.getMessages()) {
            System.out.println(m);
        }

        Set<ChatMessage> chatMessageSet = new HashSet<>();
        chatMessageSet.addAll(chatGiacomo.getMessages());
        chatMessageSet.addAll(chatAldo.getMessages());
        chatMessageSet.addAll(chatGiovanni.getMessages());

        assertEquals(chatMessageSet.size(), 4);
    }
}