package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller class for the popup that allows the player to select their color.
 */
public class ColorPopupController {

    @FXML
    private StackPane root;
    @FXML
    private VBox pleaseWaitBox;
    @FXML
    private VBox chooseColorBox;
    @FXML
    private HBox colorsBox;

    private PlayerColor selectedColor;

    private Map<PlayerColor, PegController> pegControllers;

    private boolean done;

    /**
     * Initializes this controller with default values. By default, the popup shows a placeholder text.
     */
    @FXML
    public void initialize() {
        pegControllers = new HashMap<>();
        pleaseWaitBox.setVisible(true);
        chooseColorBox.setVisible(false);
        colorsBox.getChildren().clear();
        done = false;
    }

    /**
     * Sets the colors available for the player to choose and displays them.
     * @param availableColors The list of colors from which the player can choose.
     */
    public void setColors(List<PlayerColor> availableColors) {
        for(PlayerColor color : availableColors) {
            PegController pegController = ElementFactory.getPeg();
            pegController.setPegColor(color);
            pegController.setPegRadius(20);
            pegController.setInteractable(true);
            pegController.getRoot().setOnMouseClicked(mouseEvent -> selectColor(color));
            pegControllers.put(color, pegController);
            colorsBox.getChildren().add(pegController.getRoot());
        }
        pleaseWaitBox.setVisible(false);
        chooseColorBox.setVisible(true);
    }

    /**
     * Handles the submission to the server of the player's color choice.
     */
    @FXML
    public void submitColor() {
        if (selectedColor == null || done) return;

        try {
            CodexGUI.getGUI().getServerInterface().setColor(selectedColor);
        } catch (RemoteException e) {
            System.err.println("Error communicating with the server: " + e.getMessage());
            return;
        }

        done = true;
    }

    /**
     * Selects the peg of the specified color.
     * @param color The color that the player selected.
     */
    private void selectColor(PlayerColor color) {
        this.selectedColor = color;
        for(PlayerColor playerColor : pegControllers.keySet()) {
            if (playerColor == color) {
                pegControllers.get(playerColor).setSelected(true);
                pegControllers.get(playerColor).setInteractable(false);
            } else {
                pegControllers.get(playerColor).setSelected(false);
                pegControllers.get(playerColor).setInteractable(true);
            }
        }
    }

    /**
     * @return The root node of this GUI element.
     */
    public Parent getRoot() {
        return root;
    }
}
