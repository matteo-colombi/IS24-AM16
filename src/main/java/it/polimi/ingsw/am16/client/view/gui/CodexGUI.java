package it.polimi.ingsw.am16.client.view.gui;

import it.polimi.ingsw.am16.client.Client;
import it.polimi.ingsw.am16.client.view.ViewInterface;
import it.polimi.ingsw.am16.client.view.gui.events.*;
import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Popup;
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
 */
public class CodexGUI extends Application implements ViewInterface {

    /**
     * The instance of the GUI.
     */
    private static CodexGUI guiInstance;

    /**
     * The state of the GUI.
     */
    private GUIState guiState;

    /**
     * The stage of the GUI.
     */
    private Stage stage;

    /**
     * The server interface.
     */
    private ServerInterface serverInterface;

    /**
     * Gets the instance of the GUI.
     *
     * @return The instance of the GUI.
     */
    public static CodexGUI getGUI() {
        return guiInstance;
    }

    /**
     * Gets the server interface.
     *
     * @return The server interface.
     */
    public ServerInterface getServerInterface() {
        return serverInterface;
    }

    /**
     * Gets the GUI state.
     *
     * @return The GUI state.
     */
    public GUIState getGuiState() {
        return guiState;
    }

    /**
     * Gets the stage.
     *
     * @return The stage.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Starts the GUI, connecting it to the server and displaying the splash screen for 3 seconds before switching to the welcome screen.
     *
     * @param stage The arguments of the GUI.
     */
    @Override
    public void start(Stage stage) throws IOException {
        guiInstance = this;
        guiState = new GUIState();

        List<String> args = getParameters().getRaw();

        String[] hostAndPort = args.get(3).split(":");

        int port = -1;

        try {
            port = Integer.parseInt(hostAndPort[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid port: " + hostAndPort[1]);
            System.exit(1);
        }

        try {
            serverInterface = Client.serverInterfaceFactory(args.get(2), hostAndPort[0], port, this);
        } catch (IllegalArgumentException e) {
            return;
        }

        guiState.setServerInterface(serverInterface);

        this.stage = stage;

        FXMLLoader splashScreenLoader = new FXMLLoader(CodexGUI.class.getResource(FilePaths.GUI_SCREENS + "/splash-screen.fxml"));
        Scene scene = new Scene(splashScreenLoader.load());
        Image icon = new Image(Objects.requireNonNull(CodexGUI.class.getResourceAsStream(FilePaths.GUI_LOGO)));

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.setTitle("Codex Naturalis");
        stage.setScene(scene);
        stage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> {
            switchToWelcomeScreen();
        });
        delay.play();
    }

    /**
     * Switches to the welcome screen.
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

    @Override
    public void stop() {
        try{
            serverInterface.disconnect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the view. This includes the view's user input manager.
     */
    @Override
    public void startView(String[] args) {
        launch(args);
    }

    /**
     * Show the existing game IDs to the player.
     *
     * @param gameIds        The existing games' IDs.
     * @param currentPlayers The number of current players
     * @param maxPlayers     The maximum number of players
     */
    @Override
    public synchronized void printGames(Set<String> gameIds, Map<String, Integer> currentPlayers, Map<String, Integer> maxPlayers) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetGamesListEvent(gameIds.stream().toList(), currentPlayers, maxPlayers)));
    }

    /**
     * Tells the view that the player has joined a game with the given username.
     *
     * @param gameId   The id of the game which the player just joined.
     * @param username The username the player has joined the game with.
     */
    @Override
    public synchronized void joinGame(String gameId, String username) {
        guiState.setGameId(gameId);
        guiState.setUsername(username);

        switchToLobbyScreen();
    }

    /**
     * Adds a player to the game. Used to communicate the connection of a new player.
     *
     * @param username The new player's username.
     */
    @Override
    public synchronized void addPlayer(String username) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new AddPlayerEvent(username)));
    }

    /**
     * Tells the view all the usernames of the players present in the game.
     *
     * @param usernames The list of usernames of the players present in the game.
     */
    @Override
    public synchronized void setPlayers(List<String> usernames) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetPlayersEvent(usernames)));
    }

    /**
     * Sets the game state. To be called when the game's state changes.
     *
     * @param state The new game state.
     */
    @Override
    public synchronized void setGameState(GameState state) {
        guiState.setGameState(state);
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetGameStateEvent(state)));
    }

    /**
     * Sets the common cards for the game. Should be called whenever these change.
     *
     * @param commonResourceCards The common resource cards (may also contain gold cards if the resource card deck is empty). Should always be of length 2.
     * @param commonGoldCards     The common gold cards (may also contain resource cards if the gold card deck is empty). Should always be of length 2.
     */
    @Override
    public synchronized void setCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetCommonCardsEvent(commonResourceCards, commonGoldCards)));
    }

    /**
     * Sets the types of cards at the top of the respective deck.
     *
     * @param whichDeck    The deck which we are setting the top card of.
     * @param resourceType The resource type of the card on top of the given deck.
     */
    @Override
    public synchronized void setDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetDeckTopTypeEvent(whichDeck, resourceType)));
    }

    /**
     * Prompts the user to choose the side of the given starter card.
     *
     * @param starterCard The starter card of the player.
     */
    @Override
    public synchronized void promptStarterChoice(StarterCard starterCard) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new PromptStarterChoiceEvent(starterCard)));
    }

    /**
     * Tells the client that the color-choosing phase has begun.
     */
    @Override
    public synchronized void choosingColors() {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new ChoosingColorsEvent()));
    }

    /**
     * Prompts the client to choose their color.
     *
     * @param colorChoices The possible choices for the player's color.
     */
    @Override
    public synchronized void promptColorChoice(List<PlayerColor> colorChoices) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new PromptColorChoiceEvent(colorChoices)));
    }

    /**
     * Sets the player's color. If the player is still in the prompt because he didn't choose in time, the prompt is invalidated
     *
     * @param username The username whose color is being given.
     * @param color    The color assigned to the player.
     */
    @Override
    public synchronized void setColor(String username, PlayerColor color) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetColorEvent(username, color)));
    }

    /**
     * Tells the client that the cards for the game are being drawn.
     */
    @Override
    public synchronized void drawingCards() {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new DrawingCardsEvent()));
    }

    /**
     * Sets the player's hand.
     *
     * @param hand The player's hand.
     */
    @Override
    public synchronized void setHand(List<PlayableCard> hand) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetHandEvent(hand)));
    }

    /**
     * Adds the given card to this player's hand.
     *
     * @param card The card to be added.
     */
    @Override
    public synchronized void addCardToHand(PlayableCard card) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new AddCardToHandEvent(card)));
    }

    /**
     * Removed the given card from this player's hand.
     *
     * @param card The card to be removed.
     */
    @Override
    public synchronized void removeCardFromHand(PlayableCard card) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new RemoveCardFromHandEvent(card)));
    }

    /**
     * Sets the given player's restricted hand.
     *
     * @param username The username of the player whose hand is being given.
     * @param hand     The restricted hand.
     */
    @Override
    public synchronized void setOtherHand(String username, List<RestrictedCard> hand) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetOtherHandEvent(username, hand)));
    }

    /**
     * Adds the given restricted card to the given user's hand.
     *
     * @param username The user to add this card to.
     * @param newCard  The restricted card to be added.
     */
    @Override
    public synchronized void addCardToOtherHand(String username, RestrictedCard newCard) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new AddCardToOtherHandEvent(username, newCard)));
    }

    /**
     * Removes the given restricted card from the given user's hand.
     *
     * @param username     The user to remove this card from.
     * @param cardToRemove The restricted card to be removed.
     */
    @Override
    public synchronized void removeCardFromOtherHand(String username, RestrictedCard cardToRemove) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new RemoveCardFromOtherHandEvent(username, cardToRemove)));
    }

    /**
     * Sets the given player's play area.
     *
     * @param username           The player whose play area is being given.
     * @param cardPlacementOrder The order in which the cards were played in this play area.
     * @param field              The user's field.
     * @param activeSides        The map keeping track of which side every card is placed on.
     * @param legalPositions     The set of positions on which the player can place cards.
     * @param illegalPositions   The set of positions on which the player must not place cards.
     * @param resourceCounts     A map containing the amount of each resource that the player has.
     * @param objectCounts       A map containing the amount of each object that the player has.
     */
    @Override
    public synchronized void setPlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetPlayAreaEvent(username, cardPlacementOrder, field, activeSides, legalPositions, illegalPositions, resourceCounts, objectCounts)));
    }

    /**
     * Adds the given card to the given player's play area.
     *
     * @param username              The username of the player who played the card.
     * @param card                  The played card.
     * @param side                  The card the new card was played on.
     * @param pos                   The position where the new card was played.
     * @param addedLegalPositions   The set of new positions in which the player can play a card, following the move which was just made.
     * @param removedLegalPositions The set of positions in which the player can no longer play a card, following the move which was just made.
     * @param resourceCounts        A map containing the amount of each resource that the player has, following the move which was just made.
     * @param objectCounts          A map containing the amount of each object that the player has, following the move which was just made.
     */
    @Override
    public synchronized void playCard(String username, BoardCard card, SideType side, Position pos, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new PlayCardEvent(username, card, side, pos, addedLegalPositions, removedLegalPositions, resourceCounts, objectCounts)));
    }

    /**
     * Sets a player's number of game points.
     *
     * @param username   The username of the player whose points are being set.
     * @param gamePoints The given player's number of game points.
     */
    @Override
    public synchronized void setGamePoints(String username, int gamePoints) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetGamePointsEvent(username, gamePoints)));
    }

    /**
     * Sets a player's number of objective points.
     *
     * @param username        The username of the player whose points are being set.
     * @param objectivePoints The given player's number of objective points.
     */
    @Override
    public synchronized void setObjectivePoints(String username, int objectivePoints) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetObjectivePointsEvent(username, objectivePoints)));
    }

    /**
     * Sets the common objectives for the game.
     *
     * @param commonObjectives The common objectives. Should always contain 2 elements.
     */
    @Override
    public synchronized void setCommonObjectives(ObjectiveCard[] commonObjectives) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetCommonObjectivesEvent(commonObjectives)));
    }

    /**
     * Prompts the player to choose their objective from the ones given.
     *
     * @param possiblePersonalObjectives The possible objectives the player can choose from. Should always contain 2 cards.
     */
    @Override
    public synchronized void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new PromptObjectiveChoiceEvent(possiblePersonalObjectives)));
    }

    /**
     * Sets the player's personal objective.
     *
     * @param personalObjective The player's personal objective.
     */
    @Override
    public synchronized void setPersonalObjective(ObjectiveCard personalObjective) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetPersonalObjectiveEvent(personalObjective)));
    }

    /**
     * Sets the turn order for the game.
     *
     * @param usernames The turn order. Should always contain as many usernames as were added at the beginning of the game.
     */
    @Override
    public synchronized void setStartOrder(List<String> usernames) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetStartOrderEvent(usernames)));
    }

    /**
     * Tells the client that it is the given player's turn to play.
     *
     * @param username The player's username.
     */
    @Override
    public synchronized void turn(String username) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new TurnEvent(username)));
    }

    /**
     * Tells the client the winners of the game.
     *
     * @param winnerUsernames The winners of the game.
     */
    @Override
    public synchronized void setWinners(List<String> winnerUsernames) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SetWinnersEvent(winnerUsernames)));
    }

    /**
     * Adds all the messages given to the player's chat.
     *
     * @param messages The chat messages to add.
     */
    @Override
    public synchronized void addMessages(List<ChatMessage> messages) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new AddChatMessagesEvent(messages)));
    }

    /**
     * Adds the given message to the player's chat.
     *
     * @param message The new message.
     */
    @Override
    public synchronized void addMessage(ChatMessage message) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new AddChatMessagesEvent(List.of(message))));
    }

    /**
     * Tells the client that an error has occurred.
     *
     * @param errorMessage The message that should be displayed to the user.
     */
    @Override
    public synchronized void promptError(String errorMessage) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new ErrorEvent(errorMessage)));
    }

    /**
     * Forces the client to redraw the view.
     */
    @Override
    public synchronized void redrawView() {
        //TODO maybe remove
    }

    /**
     * Notifies the client that from now on they shouldn't draw cards anymore.
     */
    @Override
    public synchronized void notifyDontDraw() {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new NotifyDontDrawEvent()));
    }

    /**
     * Tells the client that another client has disconnected. This ends the game, if it had started. If the game hadn't started already, the player is simply removed.
     *
     * @param whoDisconnected The username of the player who disconnected.
     */
    @Override
    public synchronized void signalDisconnection(String whoDisconnected) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SignalDisconnectionEvent(whoDisconnected)));
    }

    /**
     * DOCME
     *
     * @param whoDisconnected
     */
    @Override
    public synchronized void signalGameSuspension(String whoDisconnected) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SignalGameSuspensionEvent(whoDisconnected)));
    }

    /**
     * Tells the client that a player has skipped their turn because of a deadlock.
     *
     * @param username The username of the player who skipped their turn.
     */
    @Override
    public synchronized void signalDeadlock(String username) {
        Platform.runLater(() -> stage.getScene().getRoot().fireEvent(new SignalDeadlockEvent(username)));
    }

    /**
     * DOCME
     */
    @Override
    public void signalConnectionLost() {
        //TODO make an actual popup :)

        Platform.runLater(() -> {
            Popup popup = new Popup();
            StackPane darkening = new StackPane();
            darkening.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
            StackPane testStackPane = new StackPane();
            testStackPane.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");
            Text testText = new Text("Connection lost to the server :(");
            testStackPane.getChildren().add(testText);
            popup.getContent().add(testStackPane);
            popup.setWidth(200);
            popup.setHeight(100);
            popup.setAutoHide(false);
            ((StackPane) stage.getScene().getRoot()).getChildren().addLast(darkening);
            stage.toFront();
            popup.show(stage.getScene().getRoot(), 0, 0);
            popup.centerOnScreen();

        });
    }

}
