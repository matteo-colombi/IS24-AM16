package it.polimi.ingsw.am16.client.view.gui.controllers;

import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerButtonController implements Initializable {

    @FXML
    private StackPane root;
    @FXML
    private Button button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setColor(PlayerColor color) {
        button.getStyleClass().clear();
        button.getStyleClass().add(color.name().toLowerCase());
    }

    public Parent getRoot() {
        return root;
    }
}
