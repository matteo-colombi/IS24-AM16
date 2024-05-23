package it.polimi.ingsw.am16.client.view.gui.controllers;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the welcome screen.
 */
public class CreateScreenController implements ScreenController, Initializable {

    @FXML
    public VBox playersSelector;

    /**
     * Initializes the controller. The username is kept when returning to the welcome screen from the games screen.
     *
     * @param url            The URL.
     * @param resourceBundle The resource bundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CodexGUI.getGUI().getGuiState().setCreateScreenController(this);
        CodexGUI.getGUI().getGuiState().setCurrentController(this);


    }

    @FXML
    public void back(ActionEvent ignored) {
        CodexGUI.getGUI().switchToWelcomeScreen();
    }

    @Override
    public void showError(String errorMessage) {

    }
}
