package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.events.GUIEventTypes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class EndgameScreenController {
    @FXML
    public StackPane root;

    public void home(ActionEvent ignored) {
        //TODO implement
    }

    public void showError(String errorMessage) {
        //TODO implement
    }

    private void registerEvents() {
        root.addEventFilter(GUIEventTypes.ERROR_EVENT, errorEvent -> {
            showError(errorEvent.getErrorMsg());
            errorEvent.consume();
        });

        root.addEventFilter(GUIEventTypes.SET_GAMES_LIST_EVENT, gamesListEvent -> {
            gamesListEvent.consume();
        });
    }
}
