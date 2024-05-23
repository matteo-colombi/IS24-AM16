package it.polimi.ingsw.am16.client.view.gui.controllers;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.events.GUIEventTypes;
import it.polimi.ingsw.am16.common.util.FilePaths;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class GamesScreenController implements Initializable {

    private ServerInterface serverInterface;

    @FXML
    private StackPane root;

    @FXML
    private VBox gamesList;

    @FXML
    private ImageView borderImage;

    @FXML
    private TextField gameIdField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borderImage.setMouseTransparent(true);
        this.serverInterface = CodexGUI.getGUI().getServerInterface();

        registerEvents();

        refresh();
    }

    @FXML
    public void refreshGamesList(ActionEvent ignored) {
        refresh();
    }

    @FXML
    public void back(ActionEvent ignored) {
        CodexGUI.getGUI().switchToWelcomeScreen();
    }

    @FXML
    public void manualJoin(ActionEvent ignored) {
        String gameId = gameIdField.getText();
        if (gameId.length() != 6) {
            gameIdField.selectAll();
            gameIdField.requestFocus();
            return;
        }

        join(gameId);
    }

    private void refresh() {
        try {
            serverInterface.getGames();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void join(String gameId) {
        try {
            serverInterface.joinGame(gameId, CodexGUI.getGUI().getGuiState().getUsername());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setGamesList(List<String> games, Map<String, Integer> currentPlayers, Map<String, Integer> maxPlayers) {
        Platform.runLater(() -> {
            gamesList.getChildren().clear();
            for(String gameId : games) {
                gamesList.getChildren().add(createGameCard(gameId, currentPlayers.get(gameId), maxPlayers.get(gameId)));
            }
        });
    }

    private StackPane createGameCard(String gameId, int currentPlayers, int maxPlayers) {
        FXMLLoader cardLoader = new FXMLLoader(getClass().getResource(FilePaths.GUI_ELEMENTS + "/game-card.fxml"));
        try {
            StackPane card = cardLoader.load();
            Button joinButton = (Button) card.lookup("#cardJoinButton");
            Text idText = (Text) card.lookup("#idField");
            Text playersText = (Text) card.lookup("#playersField");
            joinButton.setOnAction(actionEvent -> join(gameId));
            idText.setText(gameId);
            playersText.setText(String.format("%d/%d", currentPlayers, maxPlayers));
            return card;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            setGamesList(gamesListEvent.getGameIds(), gamesListEvent.getCurrentPlayers(), gamesListEvent.getMaxPlayers());
            gamesListEvent.consume();
        });
    }
}
