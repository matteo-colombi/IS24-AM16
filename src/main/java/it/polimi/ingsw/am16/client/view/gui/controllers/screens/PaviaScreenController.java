package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.net.URISyntaxException;

public class PaviaScreenController {

    @FXML
    private MediaView paviaBurns;
    @FXML
    public void initialize() {
        if(paviaBurns.getMediaPlayer() == null) {
            try {
                String filename = getClass().getResource("/assets/gui/pavia.mp4").toURI().toString();
                Media media = new Media(filename);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                paviaBurns.setMediaPlayer(mediaPlayer);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        paviaBurns.getMediaPlayer().play();
    }

    public void play() {
        paviaBurns.getMediaPlayer().play();
    }

    public void pause() {
        paviaBurns.getMediaPlayer().pause();
    }

    public void reset() {
        if(paviaBurns.getMediaPlayer().getStatus() != MediaPlayer.Status.READY) {
            paviaBurns.getMediaPlayer().seek(Duration.seconds(0.0));
        }
    }

    public void back() {
        if(paviaBurns.getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
            paviaBurns.getMediaPlayer().stop();
        }
        CodexGUI.getGUI().switchToWelcomeScreen();
    }


}
