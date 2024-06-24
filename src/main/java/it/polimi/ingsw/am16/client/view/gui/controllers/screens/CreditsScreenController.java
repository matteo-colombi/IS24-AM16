package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.common.util.FilePaths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.util.Objects;

/**
 * Controller for the credits screen.
 */
public class CreditsScreenController {

    @FXML
    private MediaView rick;
    @FXML
    private VBox links;
    @FXML
    private VBox rickText;

    @FXML
    public void click(ActionEvent ignored) {
        System.out.println("Clicked");
    }

    @FXML
    public void rick(ActionEvent ignored) {
        if (rick.getMediaPlayer() == null) {
            try {
                String filename = Objects.requireNonNull(getClass().getResource(FilePaths.GUI_MEDIA + "/rick.mp4")).toURI().toString();
                Media media = new Media(filename);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                rick.setMediaPlayer(mediaPlayer);
            } catch (Exception e) {
                System.err.println("Error while loading media: " + e.getMessage());
            }
        }

        rick.setVisible(true);
        rick.requestFocus();
        rick.getMediaPlayer().seek(rick.getMediaPlayer().getStartTime());
        rick.getMediaPlayer().play();
    }

    @FXML
    public void back(ActionEvent ignored) {
        if (rick.getMediaPlayer() != null && rick.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
            rick.getMediaPlayer().stop();
            rick.setVisible(false);
            links.setVisible(false);
            rickText.setVisible(true);
        } else {
            CodexGUI.getGUI().switchToWelcomeScreen();
        }
    }
}
