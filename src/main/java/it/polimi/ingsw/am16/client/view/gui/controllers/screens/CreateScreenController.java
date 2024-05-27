package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.events.GUIEventTypes;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import java.rmi.RemoteException;

/**
 * Controller for the welcome screen.
 */
public class CreateScreenController {
    @FXML
    public RadioButton numPlayers2;
    @FXML
    public RadioButton numPlayers3;
    @FXML
    public RadioButton numPlayers4;
    @FXML
    private StackPane root;
    @FXML
    private ToggleGroup numPLayersToggleGroup;

    /**
     * The server interface.
     */
    private ServerInterface serverInterface;

    /**
     * Initializes the controller. The username is kept when returning to the welcome screen from the games screen.
     *
     */
    @FXML
    public void initialize() {
        registerEvents();

        this.serverInterface = CodexGUI.getGUI().getServerInterface();
    }

    @FXML
    public void back(ActionEvent ignored) {
        CodexGUI.getGUI().switchToWelcomeScreen();
    }

    @FXML
    public void create(ActionEvent ignored) {
        String username = CodexGUI.getGUI().getGuiState().getUsername();

        RadioButton selectedToggle = (RadioButton) numPLayersToggleGroup.getSelectedToggle();
        int numPlayers = Integer.parseInt(selectedToggle.getText());

        try {
            serverInterface.createGame(username, numPlayers);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCME
     *
     * @param errorMessage
     */
    public void showError(String errorMessage) {
        //TODO implement an event listener that calls this method
    }

    private void registerEvents() {
        root.addEventFilter(GUIEventTypes.ERROR_EVENT, errorEvent -> showError(errorEvent.getErrorMsg()));

        numPlayers2.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                create(null);
            }
        });

        numPlayers3.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                create(null);
            }
        });

        numPlayers4.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                create(null);
            }
        });
    }
}
