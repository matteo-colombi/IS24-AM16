package it.polimi.ingsw.am16.client.view.gui;

import it.polimi.ingsw.am16.common.util.FilePaths;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class WelcomeScreenController implements Initializable {

    @FXML
    private TextField usernameField;

    private ServerInterface serverInterface;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CodexGUI.getGUI().getGuiState().setWelcomeScreenController(this);
        addTextLimiter(usernameField, 10);
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

        Stage stage = CodexGUI.getGUI().getStage();

        FXMLLoader gamesScreenLoader = new FXMLLoader(getClass().getResource(FilePaths.GUI_SCREENS + "/games-screen.fxml"));
        try {
            Parent gamesScreen = gamesScreenLoader.load();
            stage.getScene().setRoot(gamesScreen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void create(ActionEvent ignored) {

    }

    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }
}
