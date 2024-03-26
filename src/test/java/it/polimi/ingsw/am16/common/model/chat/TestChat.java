package it.polimi.ingsw.am16.common.model.chat;

import org.junit.jupiter.api.Test;

import java.util.Set;

class TestChat {

    @Test
    void testChat() {
        ChatManager manager = new ChatManager();

        Chat chatAldo = new Chat("Aldo");
        Chat chatGiovanni = new Chat("Giovanni42");
        Chat chatGiacomo = new Chat("xXGiacomoXx");
        chatAldo.subscribe(manager);
        chatGiacomo.subscribe(manager);
        chatGiovanni.subscribe(manager);

        chatAldo.sendMessage("Miiiiiiiiiiiiiiiiiii", Set.of("xXGiacomoXx"));

        chatGiovanni.sendMessage("Ascoltate PoretCast raga");

        chatGiovanni.sendMessage("Ma siete scemi?", Set.of("Aldo", "L2C", "Giovanni42"));

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