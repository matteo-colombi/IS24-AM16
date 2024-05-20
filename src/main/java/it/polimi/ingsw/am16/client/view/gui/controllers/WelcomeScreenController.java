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

public class WelcomeScreenController implements ScreenController, Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField numPlayersField;

    private ServerInterface serverInterface;

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

    private static void makeNumOnly(final TextField tf) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
                if (!newValue.matches("[234]")) {
                    tf.clear();
                }
            }
        );
    }

    private static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener((ov, oldValue, newValue) -> {
            if (tf.getText().length() > maxLength) {
                String s = tf.getText().substring(0, maxLength);
                tf.setText(s);
            }
        });
    }

    @Override
    public void showError(String errorMessage) {
        //TODO implement
    }
}
