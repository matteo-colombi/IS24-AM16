package it.polimi.ingsw.am16.client.view.gui;

import it.polimi.ingsw.am16.client.Client;
import it.polimi.ingsw.am16.client.view.ViewInterface;
import it.polimi.ingsw.am16.client.view.gui.controllers.elements.ErrorController;
import it.polimi.ingsw.am16.client.view.gui.events.*;
import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.client.view.gui.util.ErrorFactory;
import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import it.polimi.ingsw.am16.client.view.gui.util.guiErrors.GUIError;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.game.LobbyState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.ErrorType;
import it.polimi.ingsw.am16.common.util.FilePaths;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The main class of the GUI. This class is responsible for starting the GUI and switching between the different screens.
 * It also handles incoming calls from the server.
 */
public class CodexGUI extends Application implements ViewInterface {

    private static CodexGUI guiInstance;
    private GUIState guiState;
    private Stage stage;
    private ServerInterface serverInterface;

    /**
     * @return The only instance of the GUI.
     */
    public static CodexGUI getGUI() {
        return guiInstance;
    }

    /**
     * @return The server interface to communicate with the server.
     */
    public ServerInterface getServerInterface() {
        return serverInterface;
    }

    /**
     * @return This GUI's state.
     *
     * @see GUIState
     */
    public GUIState getGuiState() {
        return guiState;
    }

    /**
     * @return The stage of the application.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Starts the GUI, connecting it to the server and displaying the splash screen for 2 seconds before switching to the welcome screen. <br>
     * {@inheritDoc}
     */
    @Override
    public void start(Stage stage) throws IOException {
        guiInstance = this;
        guiState = new GUIState();

        List<String> args = getParameters().getRaw();

        try {
            serverInterface = Client.serverInterfaceFactory(args, this);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            Platform.exit();
        }

        guiState.setServerInterface(serverInterface);

        this.stage = stage;

        FXMLLoader splashScreenLoader = new FXMLLoader(CodexGUI.class.getResource(FilePaths.GUI_SCREENS + "/splash-screen.fxml"));
        Scene scene = new Scene(splashScreenLoader.load());
        Image icon = new Image(Objects.requireNonNull(CodexGUI.class.getResourceAsStream(FilePaths.GUI_LOGO)));

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        stage.setResizable(true);
        stage.getIcons().add(icon);
        stage.setTitle("Codex Naturalis");
        stage.setScene(scene);
        stage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> switchToWelcomeScreen());
        delay.play();
    }

    /**
     * Switches to the welcome screen.
     *
     * @see it.polimi.ingsw.am16.client.view.gui.controllers.screens.WelcomeScreenController
     */
    public void switchToWelcomeScreen() {
        FXMLLoader welcomeScreenLoader = new FXMLLoader(CodexGUI.class.getResource(FilePaths.GUI_SCREENS + "/welcome-screen.fxml"));
        try {
            Parent welcomeScreen = welcomeScreenLoader.load();
            stage.getScene().setRoot(welcomeScreen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        guiState.clear();
    }

    /**
     * Switches to the games screen.
     *
     * @see it.polimi.ingsw.am16.client.view.gui.controllers.screens.CreateScreenController
     */
    public void switchToCreateScreen() {
        FXMLLoader createScreenLoader = new FXMLLoader(getClass().getResource(FilePaths.GUI_SCREENS + "/create-screen.fxml"));
        try {
            Parent createScreen = createScreenLoader.load();
            stage.getScene().setRoot(createScreen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Switches to the games screen.
     *
     * @see it.polimi.ingsw.am16.client.view.gui.controllers.screens.GamesScreenController
     */
    public void switchToGamesScreen() {
        FXMLLoader gamesScreenLoader = new FXMLLoader(getClass().getResource(FilePaths.GUI_SCREENS + "/games-screen.fxml"));
        try {
            Parent gamesScreen = gamesScreenLoader.load();
            stage.getScene().setRoot(gamesScreen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Switches to the lobby screen.
     *
     * @see it.polimi.ingsw.am16.client.view.gui.controllers.screens.LobbyScreenController
     */
    public void switchToLobbyScreen() {
        FXMLLoader lobbyScreenLoader = new FXMLLoader(getClass().getResource(FilePaths.GUI_SCREENS + "/lobby-screen.fxml"));
        try {
            Parent lobbyScreen = lobbyScreenLoader.load();
            stage.getScene().setRoot(lobbyScreen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Switches to the game screen.
     *
     * @see it.polimi.ingsw.am16.client.view.gui.controllers.screens.PlayScreenController
     */
    public void switchToPlayScreen() {
        FXMLLoader playScreenLoader = new FXMLLoader(getClass().getResource(FilePaths.GUI_SCREENS + "/play-screen.fxml"));
        try {
            Parent playScreen = playScreenLoader.load();
            stage.getScene().setRoot(playScreen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Switches to the endgame screen.
     *
     * @see it.polimi.ingsw.am16.client.view.gui.controllers.screens.EndgameScreenController
     */
    public void switchToEndgameScreen() {
        FXMLLoader endgameScreenLoader = new FXMLLoader(getClass().getResource(FilePaths.GUI_SCREENS + "/endgame-screen.fxml"));
        try {
            Parent endgameScreen = endgameScreenLoader.load();
            stage.getScene().setRoot(endgameScreen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Switches to the rules screen.
     *
     * @see it.polimi.ingsw.am16.client.view.gui.controllers.screens.RulesScreenController
     */
    public void switchToRulesScreen() {
        FXMLLoader rulesScreenLoader = new FXMLLoader(getClass().getResource(FilePaths.GUI_SCREENS + "/rules-screen.fxml"));
        try {
            Parent rulesScreen = rulesScreenLoader.load();
            stage.getScene().setRoot(rulesScreen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Switches to the credits screen.
     *
     * @see it.polimi.ingsw.am16.client.view.gui.controllers.screens.CreditsScreenController
     */
    public void switchToCreditsScreen() {
        FXMLLoader creditsScreenLoader = new FXMLLoader(getClass().getResource(FilePaths.GUI_SCREENS + "/credits-screen.fxml"));
        try {
            Parent creditsScreen = creditsScreenLoader.load();
            stage.getScene().setRoot(creditsScreen);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        if (serverInterface != null) {
            try {
                serverInterface.disconnect();
            } catch (RemoteException e) {
                System.err.println("Couldn't communicate to the server that the client is stopping: " + e.getMessage());
            }
        }
    }

    @Override
    public void startView(String[] args) {
        launch(args);
        System.exit(0);
    }

    @Override
    public synchronized void printGames(Set<String> gameIds, Map<String, Integer> currentPlayers, Map<String, Integer> maxPlayers, Map<String, LobbyState> lobbyStates) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetGamesListEvent(gameIds.stream().toList(), currentPlayers, maxPlayers, lobbyStates)));
    }

    @Override
    public synchronized void joinGame(String gameId, String username, int numPlayers) {
        guiState.setGameId(gameId);
        guiState.setUsername(username);
        guiState.setNumPlayers(numPlayers);

        Platform.runLater(this::switchToLobbyScreen);
    }

    @Override
    public void rejoinInformationStart() {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new RejoinInformationStartEvent()));
    }

    @Override
    public void rejoinInformationEnd() {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new RejoinInformationEndEvent()));
    }

    @Override
    public synchronized void addPlayer(String username) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new AddPlayerEvent(username)));
    }

    @Override
    public synchronized void setPlayers(List<String> usernames) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetPlayersEvent(usernames)));
    }

    @Override
    public synchronized void setGameState(GameState state) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetGameStateEvent(state)));
    }

    @Override
    public synchronized void setCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetCommonCardsEvent(commonResourceCards, commonGoldCards)));
    }

    @Override
    public synchronized void setDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetDeckTopTypeEvent(whichDeck, resourceType)));
    }

    @Override
    public synchronized void promptStarterChoice(StarterCard starterCard) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new PromptStarterChoiceEvent(starterCard)));
    }

    @Override
    public synchronized void choosingColors() {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new ChoosingColorsEvent()));
    }

    @Override
    public synchronized void promptColorChoice(List<PlayerColor> colorChoices) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new PromptColorChoiceEvent(colorChoices)));
    }

    @Override
    public synchronized void setColor(String username, PlayerColor color) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetColorEvent(username, color)));
    }

    @Override
    public synchronized void drawingCards() {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new DrawingCardsEvent()));
    }

    @Override
    public synchronized void setHand(List<PlayableCard> hand) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetHandEvent(hand)));
    }

    @Override
    public synchronized void addCardToHand(PlayableCard card) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new AddCardToHandEvent(card)));
    }

    @Override
    public synchronized void removeCardFromHand(PlayableCard card) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new RemoveCardFromHandEvent(card)));
    }

    @Override
    public synchronized void setOtherHand(String username, List<RestrictedCard> hand) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetOtherHandEvent(username, hand)));
    }

    @Override
    public synchronized void addCardToOtherHand(String username, RestrictedCard newCard) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new AddCardToOtherHandEvent(username, newCard)));
    }

    @Override
    public synchronized void removeCardFromOtherHand(String username, RestrictedCard cardToRemove) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new RemoveCardFromOtherHandEvent(username, cardToRemove)));
    }

    @Override
    public synchronized void setPlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetPlayAreaEvent(username, cardPlacementOrder, field, activeSides, legalPositions, illegalPositions, resourceCounts, objectCounts)));
    }

    @Override
    public synchronized void playCard(String username, BoardCard card, SideType side, Position pos, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new PlayCardEvent(username, card, side, pos, addedLegalPositions, removedLegalPositions, resourceCounts, objectCounts)));
    }

    @Override
    public synchronized void setGamePoints(String username, int gamePoints) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetGamePointsEvent(username, gamePoints)));
    }

    @Override
    public synchronized void setObjectivePoints(String username, int objectivePoints) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetObjectivePointsEvent(username, objectivePoints)));
    }

    @Override
    public synchronized void setCommonObjectives(ObjectiveCard[] commonObjectives) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetCommonObjectivesEvent(commonObjectives)));
    }

    @Override
    public synchronized void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new PromptObjectiveChoiceEvent(possiblePersonalObjectives)));
    }

    @Override
    public synchronized void setPersonalObjective(ObjectiveCard personalObjective) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetPersonalObjectiveEvent(personalObjective)));
    }

    @Override
    public synchronized void setStartOrder(List<String> usernames) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetStartOrderEvent(usernames)));
    }

    @Override
    public synchronized void turn(String username) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new TurnEvent(username)));
    }

    @Override
    public synchronized void setWinners(List<String> winnerUsernames, Map<String, ObjectiveCard> personalObjectives) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetEndGameInfoEvent(winnerUsernames, personalObjectives)));
    }

    @Override
    public synchronized void addMessages(List<ChatMessage> messages) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new AddChatMessagesEvent(messages)));
    }

    @Override
    public synchronized void addMessage(ChatMessage message) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new AddChatMessagesEvent(List.of(message))));
    }

    @Override
    public synchronized void promptError(String errorMessage, ErrorType errorType) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new ErrorEvent(errorMessage, errorType)));
    }

    @Override
    public synchronized void notifyDontDraw() {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new NotifyDontDrawEvent()));
    }

    @Override
    public synchronized void signalDisconnection(String whoDisconnected) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SignalDisconnectionEvent(whoDisconnected)));
    }

    @Override
    public synchronized void signalGameSuspension(String whoDisconnected) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SignalGameSuspensionEvent(whoDisconnected)));
    }

    @Override
    public void signalGameDeletion(String whoDisconnected) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SignalGameDeletionEvent(whoDisconnected)));
    }

    @Override
    public synchronized void signalDeadlock(String username) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SignalDeadlockEvent(username)));
    }

    @Override
    public void signalConnectionLost() {
        ErrorController errorController = ElementFactory.getErrorPopup();
        GUIError error = ErrorFactory.getError(ErrorType.CONNECTION_DEAD);
        error.configurePopup(errorController);
        errorController.setErrorText("Connection lost with the server.");
        error.show((Pane) stage.getScene().getRoot());
    }
}
