package it.polimi.ingsw.am16.client.view.gui.util;

import it.polimi.ingsw.am16.client.view.gui.controllers.elements.*;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.util.FilePaths;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Map;

/**
 * Utility class that loads FXML files for GUI elements.
 */
public class ElementFactory {

    private ElementFactory() {}

    /**
     * Contains a resource card of each type. Used when just the back of the card is needed and thus the specific card is irrelevant.
     */
    private static final Map<ResourceType, ResourceCard> resourceBacks = Map.of(
            ResourceType.FUNGI, CardRegistry.getRegistry().getResourceCardFromName("resource_fungi_1"),
            ResourceType.PLANT, CardRegistry.getRegistry().getResourceCardFromName("resource_plant_1"),
            ResourceType.ANIMAL, CardRegistry.getRegistry().getResourceCardFromName("resource_animal_1"),
            ResourceType.INSECT, CardRegistry.getRegistry().getResourceCardFromName("resource_insect_1")
    );

    /**
     * Contains a gold card of each type. Used when just the back of the card is needed and thus the specific card is irrelevant.
     */
    private static final Map<ResourceType, GoldCard> goldBacks = Map.of(
            ResourceType.FUNGI, CardRegistry.getRegistry().getGoldCardFromName("gold_fungi_1"),
            ResourceType.PLANT, CardRegistry.getRegistry().getGoldCardFromName("gold_plant_1"),
            ResourceType.ANIMAL, CardRegistry.getRegistry().getGoldCardFromName("gold_animal_1"),
            ResourceType.INSECT, CardRegistry.getRegistry().getGoldCardFromName("gold_insect_1")
    );

    /**
     * A preloaded objective card. Used when just the back of the card is needed and thus the specific card is irrelevant.
     */
    private static final ObjectiveCard objectiveBack = CardRegistry.getRegistry().getObjectiveCardFromName("objective_pattern_1");

    /**
     * Loads a card GUI element.
     * @return The controller for the GUI card element.
     */
    public static CardController getCard() {
        FXMLLoader cardLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/card.fxml"));
        try {
            cardLoader.load();
            return cardLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a card GUI element and prepares it with the specified card type and resource type.
     * Used when just the back of the card needs to be shown.
     * @param playableCardType The {@link PlayableCardType} of this card.
     * @param resourceType The {@link ResourceType} of this card.
     * @return The controller for the newly loaded card.
     */
    public static CardController getCardBackOnly(PlayableCardType playableCardType, ResourceType resourceType) {
        FXMLLoader cardLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/card.fxml"));
        try {
            cardLoader.load();
            CardController cardController = cardLoader.getController();
            switch (playableCardType) {
                case RESOURCE -> cardController.setCardAndShowSide(resourceBacks.get(resourceType), SideType.BACK);
                case GOLD -> cardController.setCardAndShowSide(goldBacks.get(resourceType), SideType.BACK);
                default -> {
                    return null;
                }
            }
            return cardController;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a card GUI element and prepares it with the back of an objective card.
     * Used when just the back of the objective card needs to be shown.
     * @return The controller for the newly loaded card.
     */
    public static CardController getObjectiveBack() {
        FXMLLoader cardLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/card.fxml"));
        try {
            cardLoader.load();
            CardController cardController = cardLoader.getController();
            cardController.setCardAndShowSide(objectiveBack, SideType.BACK);
            return cardController;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a GUI peg element. Used to mark players on the points board, and to show a player's color.
     * @return The controller for the newly loaded peg element.
     */
    public static PegController getPeg() {
        FXMLLoader pegLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/peg.fxml"));
        try {
            pegLoader.load();
            return pegLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the points board element.
     * @return The controller for the points board.
     */
    public static PointsBoardController getPointsBoard() {
        FXMLLoader boardLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/points-board.fxml"));
        try {
            boardLoader.load();
            return boardLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a play area grid element, used to show cards played.
     * @return The controller for the play area grid.
     */
    public static PlayAreaGridController getPlayAreaGrid() {
        FXMLLoader playAreaGridLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/play-area.fxml"));
        try {
            playAreaGridLoader.load();
            return playAreaGridLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a grid filler element, used in the play area to mark areas where a card can be played. This element is usually transparent.
     * @return The controller for the grid filler element.
     */
    public static GridFillerController getGridFiller() {
        FXMLLoader gridFillerLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/grid-filler.fxml"));
        try {
            gridFillerLoader.load();
            return gridFillerLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads an info table element, used to show the amount of each resource/object visible on a player's play area.
     * @return The controller for the info table element.
     */
    public static InfoTableController getInfoTable() {
        FXMLLoader infoTableLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/info-table.fxml"));
        try {
            infoTableLoader.load();
            return infoTableLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a slot where a player's hand can be shown.
     * @return The controller for the newly loaded hand slot element.
     */
    public static HandController getHandSlot() {
        FXMLLoader handLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/hand.fxml"));
        try {
            handLoader.load();
            return handLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a player button element. The player button element is supposed to be used to switch between play areas.
     * @return The controller for the newly loaded player button.
     */
    public static PlayerButtonController getPlayerButton() {
        FXMLLoader playerButtonLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/player-button.fxml"));
        try {
            playerButtonLoader.load();
            return playerButtonLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a starter popup element, used to show the player the starter card they have been assigned and pick on which side they want to play it.
     * @return The controller for the newly loaded starter popup element.
     */
    public static StarterPopupController getStarterPopup() {
        FXMLLoader starterPopupLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/starter-popup.fxml"));
        try {
            starterPopupLoader.load();
            return starterPopupLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a color popup element, used to show the player the options from which they can choose their color.
     * @return The controller for the newly loaded color popup element.
     */
    public static ColorPopupController getColorPopup() {
        FXMLLoader colorPopupLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/color-popup.fxml"));
        try {
            colorPopupLoader.load();
            return colorPopupLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads an objective popup element, used to show the player the options from which they can choose their objective.
     * @return The controller for the newly loaded objective popup element.
     */
    public static ObjectivePopupController getObjectivePopup() {
        FXMLLoader objectivePopupController = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/objective-popup.fxml"));
        try {
            objectivePopupController.load();
            return objectivePopupController.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads an error popup element, used to display different types of errors to the player.
     * @return The controller for the newly loaded error popup element.
     */
    public static ErrorController getErrorPopup() {
        FXMLLoader errorPopupLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/error.fxml"));
        try {
            errorPopupLoader.load();
            return errorPopupLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads an element used to display basic information about other players, such as their name and a restricted view of their hand.
     * @return The controller for the newly loaded other player info element.
     */
    public static OtherPlayerInfoController getOtherPlayerInfo() {
        FXMLLoader otherHandLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/other-player-info.fxml"));
        try {
            otherHandLoader.load();
            return otherHandLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a placeholder hand, which is a static hand filled with placeholder (transparent) cards.
     * @return The newly loaded element.
     */
    public static Parent getPlaceholderHand() {
        FXMLLoader otherHandLoader = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/placeholder-hand.fxml"));
        try {
            return otherHandLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a popup to display the game's rulebook.
     * @return The controller for the newly loaded rules popup.
     */
    public static RulesPopupController getRulesPopup() {
        FXMLLoader rulesPopupController = new FXMLLoader(ElementFactory.class.getResource(FilePaths.GUI_ELEMENTS + "/rules-popup.fxml"));
        try {
            rulesPopupController.load();
            return rulesPopupController.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
