package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

/**
 * Controller for the welcome screen.
 */
public class WelcomeScreenController {
    @FXML
    private StackPane showMoreButton;

    @FXML
    private StackPane more;

    @FXML
    private TextField usernameField;

    /**
     * Initializes the controller, prefilling the username field if a username is available and registering events handled by this screen.
     */
    @FXML
    public void initialize() {
        registerEvents();

        String username = CodexGUI.getGUI().getGuiState().getUsername();
        if (username != null) {
            usernameField.setText(username);
            usernameField.positionCaret(username.length());
        }

        more.setVisible(false);
    }

    /**
     * Switches the screen to the game creation screen, if the player inserted a valid username.
     */
    @FXML
    public void create(ActionEvent ignored) {
        String username = usernameField.getText();
        if (username.isEmpty()) {
            usernameField.selectAll();
            usernameField.requestFocus();
            return;
        }

        CodexGUI.getGUI().getGuiState().setUsername(username);
        CodexGUI.getGUI().switchToCreateScreen();
    }

    /**
     * Switches to the game joining screen, if the player inserted a valid username.
     */
    @FXML
    public void join(ActionEvent ignored) {
        String username = usernameField.getText();
        if (username.isEmpty()) {
            usernameField.selectAll();
            usernameField.requestFocus();
            return;
        }

        CodexGUI.getGUI().getGuiState().setUsername(username);
        CodexGUI.getGUI().switchToGamesScreen();
    }

    /**
     * Quits the application.
     */
    @FXML
    public void quit(ActionEvent ignored) {
        Platform.exit();
    }

    /**
     * Shows or hides the "more" section.
     */
    public void showMore() {
        more.setVisible(!more.isVisible());
    }

    /**
     * Switches to the rules screen.
     */
    @FXML
    public void showRules(ActionEvent ignored) {
        CodexGUI.getGUI().getGuiState().setUsername(usernameField.getText());
        CodexGUI.getGUI().switchToRulesScreen();
    }

    /**
     * Switches to the credits screen.
     */
    @FXML
    public void showCredits(ActionEvent ignored) {
        CodexGUI.getGUI().getGuiState().setUsername(usernameField.getText());
        CodexGUI.getGUI().switchToCreditsScreen();
    }

    private void registerEvents() {
        showMoreButton.setOnMouseClicked(e -> {
            showMore();
            e.consume();
        });

        showMoreButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                showMore();
                keyEvent.consume();
            }
        });

        usernameField.textProperty().addListener((ov, oldValue, newValue) -> {
            if (usernameField.getText().length() > 10) {
                String s = usernameField.getText().substring(0, 10);
                usernameField.setText(s);
            }
        });

        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                usernameField.setText(oldValue);
            }
        });

        usernameField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                join(null);
            }
        });
    }
}
