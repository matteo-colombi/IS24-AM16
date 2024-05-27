package it.polimi.ingsw.am16.client.view.gui.util;

import it.polimi.ingsw.am16.client.view.gui.controllers.elements.*;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.util.FilePaths;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Map;

public class ElementFactory {

    private static final Map<ResourceType, ResourceCard> resourceBacks = Map.of(
            ResourceType.FUNGI, CardRegistry.getRegistry().getResourceCardFromName("resource_fungi_1"),
            ResourceType.PLANT, CardRegistry.getRegistry().getResourceCardFromName("resource_plant_1"),
            ResourceType.ANIMAL, CardRegistry.getRegistry().getResourceCardFromName("resource_animal_1"),
            ResourceType.INSECT, CardRegistry.getRegistry().getResourceCardFromName("resource_insect_1")
    );
    private static final Map<ResourceType, GoldCard> goldBacks = Map.of(
            ResourceType.FUNGI, CardRegistry.getRegistry().getGoldCardFromName("gold_fungi_1"),
            ResourceType.PLANT, CardRegistry.getRegistry().getGoldCardFromName("gold_plant_1"),
            ResourceType.ANIMAL, CardRegistry.getRegistry().getGoldCardFromName("gold_animal_1"),
            ResourceType.INSECT, CardRegistry.getRegistry().getGoldCardFromName("gold_insect_1")
    );

    public static CardController getCard() {
        FXMLLoader cardLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/card.fxml"));
        try {
            cardLoader.load();
            return cardLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CardController getCardBackOnly(PlayableCardType playableCardType, ResourceType resourceType) {
        FXMLLoader cardLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/card.fxml"));
        try {
            cardLoader.load();
            CardController cardController = cardLoader.getController();
            switch (playableCardType) {
                case RESOURCE -> {
                    cardController.setCardAndShowSide(resourceBacks.get(resourceType), SideType.BACK);
                }
                case GOLD -> {
                    cardController.setCardAndShowSide(goldBacks.get(resourceType), SideType.BACK);
                }
                default -> {return null;}
            }
            return cardController;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PegController getPeg() {
        FXMLLoader pegLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/peg.fxml"));
        try {
            pegLoader.load();
            return pegLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PointsBoardController getPointsBoard() {
        FXMLLoader boardLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/points-board.fxml"));
        try {
            boardLoader.load();
            return boardLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayAreaGridController getPlayAreaGrid() {
        FXMLLoader playAreaGridLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/play-area.fxml"));
        try {
            playAreaGridLoader.load();
            return playAreaGridLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static GridFillerController getGridFiller() {
        FXMLLoader gridFillerLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/grid-filler.fxml"));
        try {
            gridFillerLoader.load();
            return gridFillerLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static InfoTableController getInfoTable() {
        FXMLLoader infoTableLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/info-table.fxml"));
        try {
            infoTableLoader.load();
            return infoTableLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static HandController getHandSlot() {
        FXMLLoader handLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/hand.fxml"));
        try {
            handLoader.load();
            return handLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerButtonController getPlayerButton() {
        FXMLLoader playerButtonLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/player-button.fxml"));
        try {
            playerButtonLoader.load();
            return playerButtonLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static StarterPopupController getStarterPopup() {
        FXMLLoader starterPopupLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/starter-popup.fxml"));
        try {
            starterPopupLoader.load();
            return starterPopupLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ColorPopupController getColorPopup() {
        FXMLLoader colorPopupLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/color-popup.fxml"));
        try {
            colorPopupLoader.load();
            return colorPopupLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectivePopupController getObjectivePopup() {
        FXMLLoader objectivePopupController = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/objective-popup.fxml"));
        try {
            objectivePopupController.load();
            return objectivePopupController.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
