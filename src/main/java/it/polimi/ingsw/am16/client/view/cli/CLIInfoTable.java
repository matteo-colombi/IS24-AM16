package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.cards.ObjectType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;

import java.util.Map;

/**
 * Utility class to manage a table to display information about the number of each resource and object currently visible on a player's play field.
 */
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

    /**
     * Creates a new info table and initializes it with the given amounts of each resource and object.
     * @param resourceCounts A map containing the amount of each resource currently visible on the player's play field.
     * @param objectCounts A map containing the amount of each object currently visible on the player's play field.
     */
    public CLIInfoTable(Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        this.infoTableAsset = CLIAssetRegistry.getCLIAssetRegistry().getInfoTable();
        update(resourceCounts, objectCounts);
    }

    /**
     * Updates this info table with the new amounts for each resource and object.
     * @param resourceCounts A map containing the amount of each resource currently visible on the player's play field.
     * @param objectCounts A map containing the amount of each object currently visible on the player's play field.
     */
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

    /**
     * @return A print-ready {@link CLIText} representation of this info table.
     */
    public CLIText getText() {
        return infoTableAsset;
    }
}
