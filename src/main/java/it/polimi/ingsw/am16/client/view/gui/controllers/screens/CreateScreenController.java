package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.controllers.elements.ErrorController;
import it.polimi.ingsw.am16.client.view.gui.events.ErrorEvent;
import it.polimi.ingsw.am16.client.view.gui.events.GUIEventTypes;
import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.client.view.gui.util.ErrorFactory;
import it.polimi.ingsw.am16.client.view.gui.util.guiErrors.GUIError;
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

    private ErrorController errorController;
    private ErrorFactory errorFactory;

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
        errorFactory = new ErrorFactory();
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
     * @param errorEvent
     */
    public void showError(ErrorEvent errorEvent) {
        errorController = ElementFactory.getErrorPopup();
        GUIError error = errorFactory.getError(errorEvent.getErrorType());
        error.configurePopup(errorController);
        errorController.setErrorText(errorEvent.getErrorMsg());
        //TODO display the popup
    }

    private void registerEvents() {
        root.addEventFilter(GUIEventTypes.ERROR_EVENT, errorEvent -> showError(errorEvent));

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
