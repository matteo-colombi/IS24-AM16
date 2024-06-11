package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.events.GUIEventTypes;
import it.polimi.ingsw.am16.common.util.FilePaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Controller for the welcome screen.
 */
public class WelcomeScreenController {
    @FXML
    private StackPane root;

    @FXML
    private StackPane showMoreButton;

    @FXML
    private StackPane more;

    /**
     * The text field for the username.
     */
    @FXML
    private TextField usernameField;

    @FXML
    private MediaView createSound;

    @FXML
    private MediaView joinSound;


    /**
     * Initializes the controller. The username is kept when returning to the welcome screen from the games screen.
     */
    @FXML
    public void initialize() {
        registerEvents();

        addTextLimiter(usernameField, 10);

        String username = CodexGUI.getGUI().getGuiState().getUsername();
        if (username != null) {
            usernameField.setText(username);
            usernameField.positionCaret(username.length());
        }

        more.setVisible(false);
    }

    /**
     * Creates a new game.
     *
     * @param ignored The action event (which is ignored).
     */
    @FXML
    public void create(ActionEvent ignored) throws URISyntaxException {
        String username = usernameField.getText();
        if (username.isEmpty() || username.length() > 10) {
            usernameField.selectAll();
            usernameField.requestFocus();
            return;
        }

        if (createSound.getMediaPlayer() == null) {
            try {
                String filename = Objects.requireNonNull(getClass().getResource(FilePaths.GUI_MEDIA + "/QUANDO.mp4")).toURI().toString();
                Media media = new Media(filename);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                createSound.setMediaPlayer(mediaPlayer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        createSound.getMediaPlayer().seek(createSound.getMediaPlayer().getStartTime());
        createSound.getMediaPlayer().play();

        CodexGUI.getGUI().getGuiState().setUsername(username);
        CodexGUI.getGUI().switchToCreateScreen();
    }

    /**
     * Joins a game.
     *
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

        if (joinSound.getMediaPlayer() == null) {
            try {
                String filename = Objects.requireNonNull(getClass().getResource(FilePaths.GUI_MEDIA + "/SINCERELY.mp4")).toURI().toString();
                Media media = new Media(filename);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                joinSound.setMediaPlayer(mediaPlayer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        joinSound.getMediaPlayer().seek(joinSound.getMediaPlayer().getStartTime());
        joinSound.getMediaPlayer().play();

        CodexGUI.getGUI().getGuiState().setUsername(username);
        CodexGUI.getGUI().switchToGamesScreen();
    }

    /**
     * Quits the application.
     *
     * @param ignored The action event (which is ignored).
     */
    @FXML
    public void quit(ActionEvent ignored) {
        CodexGUI.getGUI().getStage().close();
    }

    /**
     * Adds a text limiter to a text field.
     *
     * @param tf        The text field to which this constraint should be applied.
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
     * Shows or hides the "more" section.
     */
    public void showMore() {
        more.setVisible(!more.isVisible());
    }

    /**
     * Shows the rules screen.
     *
     * @param ignored The action event (which is ignored).
     */
    @FXML
    public void showRules(ActionEvent ignored) {
        CodexGUI.getGUI().getGuiState().setUsername(usernameField.getText());
        CodexGUI.getGUI().switchToRulesScreen();
    }

    /**
     * Shows the credits screen.
     *
     * @param ignored The action event (which is ignored).
     */
    @FXML
    public void showCredits(ActionEvent ignored) {
        CodexGUI.getGUI().getGuiState().setUsername(usernameField.getText());
        CodexGUI.getGUI().switchToCreditsScreen();
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

        usernameField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                join(null);
            }
        });
    }
}
