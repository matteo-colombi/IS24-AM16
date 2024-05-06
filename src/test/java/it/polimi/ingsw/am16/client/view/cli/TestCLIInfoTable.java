package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.cards.ObjectType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

class TestCLIInfoTable {

    @Test
    void testCLIInfoTable() {
        Map<ResourceType, Integer> resources = new EnumMap<>(ResourceType.class);
        Map<ObjectType, Integer> objects = new EnumMap<>(ObjectType.class);

        resources.put(ResourceType.FUNGI, 5);
        resources.put(ResourceType.PLANT, 30);
        resources.put(ResourceType.ANIMAL, 40);
        resources.put(ResourceType.INSECT, 8);
        objects.put(ObjectType.INKWELL, 69);
        objects.put(ObjectType.MANUSCRIPT, 32);
        objects.put(ObjectType.QUILL, 4);

        CLIInfoTable testInfoTable = new CLIInfoTable(resources, objects);

        testInfoTable.getText().printText();

        resources.put(ResourceType.FUNGI, 41);
        resources.put(ResourceType.PLANT, 46);
        resources.put(ResourceType.ANIMAL, 24);
        resources.remove(ResourceType.INSECT);
        objects.put(ObjectType.INKWELL, 88);
        objects.put(ObjectType.MANUSCRIPT, 7);
        objects.put(ObjectType.QUILL, 99);

        testInfoTable.update(resources, objects);

        testInfoTable.getText().printText();
    }
}