package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * Controller for a GUI element to show basic info about another player, such as their username and a restricted view of their hand.
 */
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

    /**
     * Initializes this element, removing the placeholder username.
     */
    @FXML
    public void initialize() {
        usernameField.setText("");
    }

    /**
     * Sets the username of the player whose info is being shown.
     * @param username The username of the player.
     */
    public void setUsername(String username) {
        this.username = username;
        usernameField.setText(username);
    }

    /**
     * Sets the controller for the restricted view of the hand of the other player.
     * @param handController The controller for the restricted view of the other player's hand.
     */
    public void setHandController(HandController handController) {
        handSlot.getChildren().clear();
        handSlot.getChildren().add(handController.getRoot());
    }

    /**
     * Sets the action that happens when the user clicks on the button on this popup.
     * @param eventHandler The event handler for the action.
     */
    public void setPlayAreaButtonAction(EventHandler<ActionEvent> eventHandler) {
        playAreaButton.setOnAction(eventHandler);
    }

    /**
     * @return The username whose info is shown on this element.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return The root node of this element.
     */
    public Parent getRoot() {
        return root;
    }
}
