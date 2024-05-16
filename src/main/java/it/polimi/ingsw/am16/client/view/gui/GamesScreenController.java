package it.polimi.ingsw.am16.client.view.gui;

import it.polimi.ingsw.am16.common.util.FilePaths;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
    private VBox gamesList;

    @FXML
    private TextField gameIdField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CodexGUI.getGUI().getGuiState().setGamesScreenController(this);
        this.serverInterface = CodexGUI.getGUI().getServerInterface();
        refresh();
    }

    @FXML
    public void refreshGamesList(ActionEvent ignored) {
        refresh();
    }

    @FXML
    public void back(ActionEvent ignored) {
        FXMLLoader welcomeScreenLoader = new FXMLLoader(getClass().getResource(FilePaths.GUI_SCREENS + "/welcome-screen.fxml"));
        try {
            Parent welcomeScreen = welcomeScreenLoader.load();
            CodexGUI.getGUI().getStage().getScene().setRoot(welcomeScreen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public void refresh() {
        try {
            serverInterface.getGames();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void join(String gameId) {
        try {
            serverInterface.joinGame(gameId, CodexGUI.getGUI().getGuiState().getUsername());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setGamesList(List<String> games, Map<String, Integer> currentPlayers, Map<String, Integer> maxPlayers) {
        Platform.runLater(() -> {
            gamesList.getChildren().clear();
            for(String gameId : games) {
                gamesList.getChildren().add(createGameCard(gameId, currentPlayers.get(gameId), maxPlayers.get(gameId)));
            }
        });
    }

    public StackPane createGameCard(String gameId, int currentPlayers, int maxPlayers) {
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
}
