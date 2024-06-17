package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class OtherPlayerInfoController {
    @FXML
    private StackPane root;
    @FXML
    private Text usernameField;
    @FXML
    private StackPane handSlot;
    @FXML
    private Button playAreaButton;

    private String username;

    @FXML
    public void initialize() {
        usernameField.setText("");
    }

    public void setUsername(String username) {
        this.username = username;
        usernameField.setText(username);
    }

    public void setHandController(HandController handController) {
        handSlot.getChildren().clear();
        handSlot.getChildren().add(handController.getRoot());
    }

    public void setPlayAreaButtonAction(EventHandler<ActionEvent> eventHandler) {
        playAreaButton.setOnAction(eventHandler);
    }

    public String getUsername() {
        return username;
    }

    public Parent getRoot() {
        return root;
    }
}
