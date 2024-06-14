package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.controllers.elements.ErrorController;
import it.polimi.ingsw.am16.client.view.gui.events.ErrorEvent;
import it.polimi.ingsw.am16.client.view.gui.events.GUIEventTypes;
import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.client.view.gui.util.ErrorFactory;
import it.polimi.ingsw.am16.client.view.gui.util.guiErrors.GUIError;
import it.polimi.ingsw.am16.common.model.game.LobbyState;
import it.polimi.ingsw.am16.common.util.FilePaths;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class GamesScreenController {

    private static final Map<LobbyState, String> lobbyStateStrings = Map.of(
            LobbyState.JOINING, "Joining",
            LobbyState.REJOINING, "Rejoining",
            LobbyState.IN_GAME, "In game",
            LobbyState.ENDING, "Ending"
    );

    private ServerInterface serverInterface;

    @FXML
    private StackPane root;

    @FXML
    private VBox gamesList;

    @FXML
    private ImageView borderImage;

    @FXML
    private TextField gameIdField;

    private ErrorController errorController;
    private ErrorFactory errorFactory;

    @FXML
    public void initialize() {
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
        String username = CodexGUI.getGUI().getGuiState().getUsername();

        try {
            serverInterface.joinGame(gameId, username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setGamesList(List<String> games, Map<String, Integer> currentPlayers, Map<String, Integer> maxPlayers, Map<String, LobbyState> lobbyStates) {
        Platform.runLater(() -> {
            gamesList.getChildren().clear();
            for (String gameId : games) {
                gamesList.getChildren().add(createGameCard(gameId, currentPlayers.get(gameId), maxPlayers.get(gameId), lobbyStates.get(gameId)));
            }
        });
    }

    private StackPane createGameCard(String gameId, int currentPlayers, int maxPlayers, LobbyState lobbyState) {
        FXMLLoader cardLoader = new FXMLLoader(getClass().getResource(FilePaths.GUI_ELEMENTS + "/game-card.fxml"));
        try {
            StackPane card = cardLoader.load();
            Button joinButton = (Button) card.lookup("#cardJoinButton");
            Text idText = (Text) card.lookup("#idField");
            Text playersText = (Text) card.lookup("#playersField");
            Text lobbyStateText = (Text) card.lookup("#lobbyStateField");
            joinButton.setOnAction(actionEvent -> join(gameId));
            idText.setText(gameId);
            playersText.setText(String.format("%d/%d", currentPlayers, maxPlayers));
            lobbyStateText.setText(lobbyStateStrings.get(lobbyState));
            return card;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showError(ErrorEvent errorEvent) {
        errorController = ElementFactory.getErrorPopup();
        GUIError error = errorFactory.getError(errorEvent.getErrorType());
        error.configurePopup(errorController);
        errorController.setErrorText(errorEvent.getErrorMsg());
        //TODO display the popup
    }

    private void registerEvents() {
        root.addEventFilter(GUIEventTypes.ERROR_EVENT, errorEvent -> {
            showError(errorEvent);
            errorEvent.consume();
        });

        root.addEventFilter(GUIEventTypes.SET_GAMES_LIST_EVENT, gamesListEvent -> {
            setGamesList(gamesListEvent.getGameIds(), gamesListEvent.getCurrentPlayers(), gamesListEvent.getMaxPlayers(), gamesListEvent.getLobbyStates());
            gamesListEvent.consume();
        });
    }
}
