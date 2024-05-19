package it.polimi.ingsw.am16.client.view.gui;

import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the welcome screen.
 */
public class WelcomeScreenController implements Initializable {

    /**
     * The text field for the username.
     */
    @FXML
    private TextField usernameField;

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
        addTextLimiter(usernameField, 10);
        String username = CodexGUI.getGUI().getGuiState().getUsername();
        if (username != null) {
            usernameField.setText(username);
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

    }

    /**
     * Adds a text limiter to a text field.
     * @param tf The text field.
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
}
