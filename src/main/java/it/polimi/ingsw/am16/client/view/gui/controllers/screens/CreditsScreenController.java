package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreditsScreenController implements Initializable {

    @FXML
    private MediaView rick;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(rick.getMediaPlayer() == null) {
            try {
                String filename = getClass().getResource("/assets/gui/rk.mp4").toURI().toString();
                Media media = new Media(filename);
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                rick.setMediaPlayer(mediaPlayer);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        rick.getMediaPlayer().seek(rick.getMediaPlayer().getStartTime());
        rick.getMediaPlayer().play();
    }
}
