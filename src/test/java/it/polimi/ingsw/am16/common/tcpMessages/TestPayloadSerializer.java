package it.polimi.ingsw.am16.common.tcpMessages;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.tcpMessages.response.AddCardToHand;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class TestPayloadSerializer {
    @Test
    void testPayloadSerializer() throws IOException {
        File f = new File("src/test/resources/json/testPayloadSerializer/test.json");
        ObjectMapper mapper = JsonMapper.getObjectMapper();

        Payload testPayload = new AddCardToHand(CardRegistry.getRegistry().getGoldCardFromName("gold_animal_5"));

        mapper.writeValue(f, testPayload);

        Payload reloadedPayload = mapper.readValue(f, Payload.class);

        System.out.println(((AddCardToHand) reloadedPayload).getCard());
    }
}
