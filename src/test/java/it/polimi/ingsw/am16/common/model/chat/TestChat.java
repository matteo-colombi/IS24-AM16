package it.polimi.ingsw.am16.common.model.chat;

import it.polimi.ingsw.am16.server.VirtualView;
import it.polimi.ingsw.am16.server.controller.ChatController;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TestChat {

    @Test
    void testChat() {
        ChatController manager = new ChatController(new VirtualView());

        Chat chatAldo = new Chat(1,"Aldo");
        Chat chatGiovanni = new Chat(2, "Giovanni42");
        Chat chatGiacomo = new Chat(3, "xXGiacomoXx");
        chatAldo.subscribe(manager);
        chatGiacomo.subscribe(manager);
        chatGiovanni.subscribe(manager);

        manager.sendMessage("Aldo", "Miiiiiiiiiiiiiiiiiii", Set.of("xXGiacomoXx"));

        manager.sendMessage("Giovanni42", "Ascoltate PoretCast raga");

        manager.sendMessage("Giovanni42", "Ma siete scemi?", Set.of("Aldo", "L2C", "Giovanni42"));

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
    }
}