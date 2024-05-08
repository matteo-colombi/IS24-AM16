package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.cards.ObjectType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;

import java.util.Map;

public class CLIInfoTable {

    private final static int AMOUNT_COLUMN = 19;

    private final Map<ResourceType, Integer> RESOURCE_ROWS = Map.of(
            ResourceType.FUNGI, 1,
            ResourceType.PLANT, 2,
            ResourceType.ANIMAL, 3,
            ResourceType.INSECT, 4
    );

    private final Map<ObjectType, Integer> OBJECT_ROWS = Map.of(
            ObjectType.INKWELL, 6,
            ObjectType.MANUSCRIPT, 7,
            ObjectType.QUILL, 8
    );

    private final CLIText infoTableAsset;

    public CLIInfoTable(Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        this.infoTableAsset = CLIAssetRegistry.getCLIAssetRegistry().getInfoTable();
        update(resourceCounts, objectCounts);
    }

    public void update(Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        for(ResourceType resourceType : ResourceType.values()) {
            CLIText newLabel = new CLIText(String.format("%02d", resourceCounts.getOrDefault(resourceType, 0)));
            infoTableAsset.mergeText(newLabel, RESOURCE_ROWS.get(resourceType), AMOUNT_COLUMN);
        }
        for(ObjectType objectType : ObjectType.values()) {
            CLIText newLabel = new CLIText(String.format("%02d", objectCounts.getOrDefault(objectType, 0)));
            infoTableAsset.mergeText(newLabel, OBJECT_ROWS.get(objectType), AMOUNT_COLUMN);
        }
    }

    public CLIText getText() {
        return infoTableAsset;
    }
}
