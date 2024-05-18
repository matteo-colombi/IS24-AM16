package it.polimi.ingsw.am16.client.view.gui;

import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeScreenController implements Initializable {

    @FXML
    private TextField usernameField;

    private ServerInterface serverInterface;

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

    @FXML
    public void quit(ActionEvent ignored) {
        CodexGUI.getGUI().getStage().close();
    }

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

    @FXML
    public void create(ActionEvent ignored) {

    }

    private static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }
}
