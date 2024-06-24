package it.polimi.ingsw.am16.client.view.gui.util;

import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.util.FilePaths;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to retrieve asset names and paths.
 */
public class GUIAssetRegistry {

    private GUIAssetRegistry() {}

    /**
     * Constructs a card's asset path based on its name and the desired side.
     * @param cardName The card's name.
     * @param side The desired side.
     * @return The constructed asset path.
     */
    public static String getAssetName(String cardName, SideType side) {
        switch (side) {
            case FRONT -> {
                return FilePaths.GUI_CARD_FRONTS + "/" + cardName + "_front.png";
            }
            case BACK -> {
                if (cardName.contains("starter")) {
                    return FilePaths.GUI_CARD_BACKS + "/" + cardName + "_back.png";
                } else if (cardName.contains("objective")) {
                    return FilePaths.GUI_CARD_BACKS + "/objective_back.png";
                } else {
                    cardName = cardName.replaceAll("_\\d+", "");
                    return FilePaths.GUI_CARD_BACKS + "/" + cardName + "_back.png";
                }
            }
            default -> throw new IllegalArgumentException("Invalid card name or side: " + cardName + ", " + side);
        }
    }

    /**
     * @return The list of paths for all the pages of the rulebook.
     */
    public static List<String> getGUIRulebookPaths() {
        List<String> paths = new ArrayList<>();

        final int rulebookPageNum = 12;

        for(int i = 1; i<=rulebookPageNum; i++) {
            paths.add(String.format("%s/rulebook-%d.png", FilePaths.GUI_RULEBOOK, i));
        }

        return paths;
    }
}
