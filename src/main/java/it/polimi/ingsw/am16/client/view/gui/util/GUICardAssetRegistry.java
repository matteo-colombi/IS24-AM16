package it.polimi.ingsw.am16.client.view.gui.util;

import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.util.FilePaths;

public class GUICardAssetRegistry {
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
        }
        throw new RuntimeException("Unknown card name: " + cardName);
    }
}
