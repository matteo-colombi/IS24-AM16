package it.polimi.ingsw.am16.client.view.gui.util;

import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.util.FilePaths;

import java.util.ArrayList;
import java.util.List;

public class GUIAssetRegistry {

    private GUIAssetRegistry() {}

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

    public static List<String> getGUIRulebookPaths() {
        List<String> paths = new ArrayList<>();

        final int rulebookPageNum = 12;

        for(int i = 1; i<=rulebookPageNum; i++) {
            paths.add(String.format("%s/rulebook-%d.png", FilePaths.GUI_RULEBOOK, i));
        }

        return paths;
    }
}
