package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.common.util.FilePaths;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.util.Objects;

public class PaviaScreenController {
    @FXML
    private MediaView paviaBurns;
    @FXML
    public HBox controlButtons;

    @FXML
    public void initialize() {
        registerEvents();

        if (paviaBurns.getMediaPlayer() == null) {
            try {
                String filename = Objects.requireNonNull(getClass().getResource(FilePaths.GUI_MEDIA + "/pavia.mp4")).toURI().toString();
                Media media = new Media(filename);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                paviaBurns.setMediaPlayer(mediaPlayer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        paviaBurns.getMediaPlayer().seek(paviaBurns.getMediaPlayer().getStartTime());
        paviaBurns.getMediaPlayer().play();
    }

    public void play() {
        paviaBurns.getMediaPlayer().play();
    }

    public void pause() {
        paviaBurns.getMediaPlayer().pause();
    }

    public void reset() {
        if (paviaBurns.getMediaPlayer().getStatus() != MediaPlayer.Status.READY) {
            paviaBurns.getMediaPlayer().seek(Duration.seconds(0.0));
        }
    }

    public void back() {
        if (paviaBurns.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
            paviaBurns.getMediaPlayer().stop();
        }

        CodexGUI.getGUI().switchToWelcomeScreen();
    }

    private void registerEvents() {
        paviaBurns.setOnMouseEntered(mouseEvent -> {
            controlButtons.setVisible(true);
            mouseEvent.consume();
        });

        paviaBurns.setOnMouseExited(mouseEvent -> {
            controlButtons.setVisible(false);
            mouseEvent.consume();
        });

        controlButtons.setOnMouseEntered(mouseEvent -> {
            controlButtons.setVisible(true);
            mouseEvent.consume();
        });
    }
}
