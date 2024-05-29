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

    @FXML
    public void initialize() {
        pegControllers = new HashMap<>();
        pleaseWaitBox.setVisible(true);
        chooseColorBox.setVisible(false);
        colorsBox.getChildren().clear();
        done = false;
    }

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

    @FXML
    public void submitColor() {
        if (selectedColor == null || done) return;

        try {
            CodexGUI.getGUI().getServerInterface().setColor(selectedColor);
        } catch (RemoteException e) {
            e.printStackTrace();
            return;
        }

        done = true;
    }

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

    public Parent getRoot() {
        return root;
    }
}
