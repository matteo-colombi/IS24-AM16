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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * Controller for the screen that displays the currently active games on the server.
 */
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

    /**
     * Initializes this screen, registering events for the screen's buttons and refreshing the list.
     */
    @FXML
    public void initialize() {
        borderImage.setMouseTransparent(true);
        this.serverInterface = CodexGUI.getGUI().getServerInterface();

        registerEvents();

        refresh();
    }

    /**
     * Handles the click event on the refresh button, refreshing the games list.
     */
    @FXML
    public void refreshGamesList(ActionEvent ignored) {
        refresh();
    }

    /**
     * Handles the click event on the back button, switching back to the welcome screen.
     */
    @FXML
    public void back(ActionEvent ignored) {
        CodexGUI.getGUI().switchToWelcomeScreen();
    }

    /**
     * Handles the click event on the manual join button, extracting the game id from the text field.
     */
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

    /**
     * Refreshes the game list.
     */
    private void refresh() {
        try {
            serverInterface.getGames();
        } catch (RemoteException e) {
            System.err.println("Error while fetching games list: " + e.getMessage());
        }
    }

    /**
     * Sends a join request to the given game id to the server.
     * @param gameId The id of the game to join.
     */
    private void join(String gameId) {
        String username = CodexGUI.getGUI().getGuiState().getUsername();

        try {
            serverInterface.joinGame(gameId, username);
        } catch (RemoteException e) {
            System.err.println("Error while joining game: " + e.getMessage());
        }
    }

    /**
     * Adds all the given games to the list, displaying basic information about them.
     * @param games The list of active games on the server.
     * @param currentPlayers The number of players currently in each game.
     * @param maxPlayers The maximum number of players allowed in each game.
     * @param lobbyStates The state of each lobby.
     */
    public synchronized void setGamesList(List<String> games, Map<String, Integer> currentPlayers, Map<String, Integer> maxPlayers, Map<String, LobbyState> lobbyStates) {
        Platform.runLater(() -> {
            gamesList.getChildren().clear();
            for (String gameId : games) {
                gamesList.getChildren().add(createGameCard(gameId, currentPlayers.get(gameId), maxPlayers.get(gameId), lobbyStates.get(gameId)));
            }
        });
    }

    /**
     * Creates a list element for the game list.
     * @param gameId The id of the game.
     * @param currentPlayers The current number of players in the game.
     * @param maxPlayers The maximum number of players for this game.
     * @param lobbyState The state of this game's lobby.
     * @return The created game list element's root.
     */
    private Pane createGameCard(String gameId, int currentPlayers, int maxPlayers, LobbyState lobbyState) {
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


    /**
     * This method sets up and shows the error popup whenever an error occurs.
     *
     * @param errorEvent the fired error event
     */
    public void showError(ErrorEvent errorEvent) {
        ErrorController errorController = ElementFactory.getErrorPopup();
        GUIError error = ErrorFactory.getError(errorEvent.getErrorType());
        error.configurePopup(errorController);
        errorController.setErrorText(errorEvent.getErrorMsg());
        error.show(root);
    }

    /**
     * Registers event handles for the events handled by this screen.
     */
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
