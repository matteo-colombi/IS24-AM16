package it.polimi.ingsw.am16.common.tcpMessages;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.tcpMessages.response.SetCommonCards;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class TestPayloadSerializer {
    @Test
    void testPayloadSerializer() throws IOException {
        String directoryPath = "src/test/resources/json/testPayloadSerializer";
        File directory = new File(directoryPath);
        directory.mkdirs();
        String filePath = directoryPath + "/test.json";
        File f = new File(filePath);
        ObjectMapper mapper = JsonMapper.getObjectMapper();

        PlayableCard[] commonResource = new PlayableCard[]{
                CardRegistry.getRegistry().getResourceCardFromName("resource_fungi_3"),
                CardRegistry.getRegistry().getResourceCardFromName("resource_insect_4")
        };

        PlayableCard[] commonGold = new PlayableCard[]{
                CardRegistry.getRegistry().getGoldCardFromName("gold_animal_3"),
                CardRegistry.getRegistry().getResourceCardFromName("gold_plant_4")
        };



        Payload testPayload = new SetCommonCards(commonResource, commonGold);

        mapper.writeValue(f, testPayload);

        Payload reloadedPayload = mapper.readValue(f, Payload.class);

        System.out.println(Arrays.toString(((SetCommonCards) reloadedPayload).getCommonGoldCards()));
        System.out.println(Arrays.toString(((SetCommonCards) reloadedPayload).getCommonGoldCards()));

    }
}
