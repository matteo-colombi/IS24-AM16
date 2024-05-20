package it.polimi.ingsw.am16.client.view.gui.controllers;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

/**
 * Controller for the welcome screen.
 */
public class WelcomeScreenController implements ScreenController, Initializable {

    /**
     * The text field for the username.
     */
    @FXML
    private TextField usernameField;

    /**
     * The field where the user can enter the number of players to create a new game.
     */
    @FXML
    private TextField numPlayersField;

    /**
     * The server interface.
     */
    private ServerInterface serverInterface;

    /**
     * Initializes the controller. The username is kept when returning to the welcome screen from the games screen.
     * @param url The URL.
     * @param resourceBundle The resource bundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CodexGUI.getGUI().getGuiState().setWelcomeScreenController(this);
        CodexGUI.getGUI().getGuiState().setCurrentController(this);
        addTextLimiter(usernameField, 10);
        usernameField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                join(null);
            }
        });

        makeNumOnly(numPlayersField);
        addTextLimiter(numPlayersField, 1);
        numPlayersField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                create(null);
            }
        });

        String username = CodexGUI.getGUI().getGuiState().getUsername();
        if (username != null) {
            usernameField.setText(username);
            usernameField.positionCaret(username.length());
        }
        this.serverInterface = CodexGUI.getGUI().getServerInterface();
    }

    /**
     * Quits the application.
     * @param ignored The action event (which is ignored).
     */
    @FXML
    public void quit(ActionEvent ignored) {
        CodexGUI.getGUI().getStage().close();
    }

    /**
     * Joins a game.
     * @param ignored The action event (which is ignored).
     */
    @FXML
    public void join(ActionEvent ignored) {
        String username = usernameField.getText();
        if (username.isEmpty() || username.length() > 10) {
            usernameField.selectAll();
            usernameField.requestFocus();
            return;
        }

        CodexGUI.getGUI().getGuiState().setUsername(username);

        CodexGUI.getGUI().switchToGamesScreen();
    }

    /**
     * Creates a new game.
     * @param ignored The action event (which is ignored).
     */
    @FXML
    public void create(ActionEvent ignored) {
        String username = usernameField.getText();
        if (username.isEmpty() || username.length() > 10) {
            usernameField.selectAll();
            usernameField.requestFocus();
            return;
        }

        boolean invalid = numPlayersField.getText().isEmpty();

        int numPlayers = 0;
        try {
            numPlayers = Integer.parseInt(numPlayersField.getText());
        } catch (NumberFormatException e) {
            invalid = true;
        }

        if (invalid) {
            numPlayersField.selectAll();
            numPlayersField.requestFocus();
            return;
        }

        CodexGUI.getGUI().getGuiState().setUsername(username);
        try {
            serverInterface.createGame(username, numPlayers);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes it so that the given text field can only accept the numbers 2, 3 and 4.
     * @param tf The text field to which this constraint should be applied.
     */
    private static void makeNumOnly(final TextField tf) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
                if (!newValue.matches("[234]")) {
                    tf.clear();
                }
            }
        );
    }

    /**
     * Adds a text limiter to a text field.
     * @param tf The text field to which this constraint should be applied.
     * @param maxLength The maximum length.
     */
    private static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }

    /**
     * DOCME
     * @param errorMessage
     */
    @Override
    public void showError(String errorMessage) {
        //TODO implement
    }
}
