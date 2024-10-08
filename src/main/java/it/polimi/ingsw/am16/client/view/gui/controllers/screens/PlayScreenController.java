package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.controllers.elements.*;
import it.polimi.ingsw.am16.client.view.gui.events.ErrorEvent;
import it.polimi.ingsw.am16.client.view.gui.events.GUIEventTypes;
import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.client.view.gui.util.ErrorFactory;
import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import it.polimi.ingsw.am16.client.view.gui.util.guiErrors.GUIError;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.ErrorType;
import it.polimi.ingsw.am16.common.util.FilePaths;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;

import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Controller for the main play screen. Handles all the main game events and interactions.
 */
public class PlayScreenController {

    private static final Map<PlayerColor, String> haloClasses = Map.of(
            PlayerColor.RED, "halo-red",
            PlayerColor.BLUE, "halo-blue",
            PlayerColor.YELLOW, "halo-yellow",
            PlayerColor.GREEN, "halo-green"
    );

    @FXML
    private StackPane root;
    @FXML
    private StackPane centerContentPane;
    @FXML
    private Group infoTableSlot;
    @FXML
    private VBox pegContainer;
    @FXML
    public ScrollPane chatScrollPane;
    @FXML
    private VBox chatMessages;
    @FXML
    private TextField chatBox;
    @FXML
    private ToggleGroup chatFilterToggleGroup;
    @FXML
    private StackPane chatFilters;
    @FXML
    private Button chatFilterButton;
    @FXML
    private VBox chatFilterNames;
    @FXML
    public MediaView rick;
    @FXML
    private VBox objectivesCardGroup;
    @FXML
    private StackPane handSlot;
    @FXML
    private StackPane personalObjectiveSlot;
    @FXML
    private StackPane resourceDeckSlot;
    @FXML
    private StackPane goldDeckSlot;
    @FXML
    private HBox commonResourceCardsSlot;
    @FXML
    private HBox commonGoldCardsSlot;
    @FXML
    private StackPane pointsBoardSlot;
    @FXML
    private StackPane leaveButton;
    @FXML
    private StackPane rulesButton;
    @FXML
    private StackPane otherPlayersArea;
    @FXML
    private VBox playersBox;

    private ServerInterface serverInterface;

    private PointsBoardController pointsBoardController;

    private GUIState guiState;

    private Map<String, PlayerButtonController> playerButtons;

    private Map<String, OtherPlayerInfoController> otherHandControllers;
    private AtomicReference<OtherPlayerInfoController> currentlyShowingOtherHand;
    private PlayAreaGridController currentlyShowingPlayArea;

    private StarterPopupController starterPopupController;
    private ColorPopupController colorPopupController;
    private ObjectivePopupController objectivePopupController;

    private List<CardController> commonResourceCards;
    private List<CardController> commonGoldCards;

    private CardController resourceDeck;
    private CardController goldDeck;

    /**
     * Initializes the screen, registering all event handlers, adding buttons to switch play areas for all the players, preparing the points board, slots for common cards, for common objectives, for the player's personal objective and the hand.
     */
    @FXML
    public void initialize() {
        registerEvents();


        guiState = CodexGUI.getGUI().getGuiState();

        serverInterface = CodexGUI.getGUI().getServerInterface();

        pointsBoardController = ElementFactory.getPointsBoard();
        pointsBoardSlot.getChildren().add(pointsBoardController.getRoot());

        commonResourceCards = new ArrayList<>(2);
        Collections.addAll(commonResourceCards, null, null);
        commonGoldCards = new ArrayList<>(2);
        Collections.addAll(commonGoldCards, null, null);

        HandController hand = ElementFactory.getHandSlot();
        hand.setUsername(guiState.getUsername());
        guiState.setHand(hand);
        handSlot.getChildren().clear();
        handSlot.getChildren().add(hand.getRoot());

        currentlyShowingOtherHand = new AtomicReference<>();
        currentlyShowingOtherHand.set(null);

        receiveMessages(guiState.getChatMessages());

        setPlayers(guiState.getPlayerUsernames());
    }

    /**
     * Sets the players for this game. This is a definitive list that will not be changed in the future.
     * This method creates all the necessary elements to handle the players, including the buttons to switch play areas.
     *
     * @param usernames A list containing the username of each player in the game.
     */
    private void setPlayers(List<String> usernames) {
        playerButtons = new HashMap<>();
        otherHandControllers = new HashMap<>();
        playersBox.getChildren().clear();
        chatFilterNames.getChildren().clear();

        for (String username : usernames) {
            PlayerButtonController playerButtonController = ElementFactory.getPlayerButton();
            playerButtonController.setDisabled(true);
            playerButtons.put(username, playerButtonController);

            PegController pegController = ElementFactory.getPeg();
            pegController.setPegRadius(20);
            pegController.setPlaceholder(true);
            guiState.setPlayerPeg(username, pegController);

            if (!username.equals(guiState.getUsername())) {
                playersBox.getChildren().addLast(playerButtonController.getRoot());

                RadioButton playerChatFilter = new RadioButton(username);
                playerChatFilter.setId(username);
                playerChatFilter.setToggleGroup(chatFilterToggleGroup);
                chatFilterNames.getChildren().addLast(playerChatFilter);
            } else {
                playersBox.getChildren().addFirst(playerButtonController.getRoot());

                playerButtonController.setOnMouseClicked(e -> resetPlayer());
                playerButtonController.setCrown();
                pegContainer.getChildren().clear();
                pegContainer.getChildren().add(pegController.getRoot());
            }
        }

        playersBox.layout();
    }

    /**
     * Sets the new state of the game. If the game is ending, switches to the end screen.
     *
     * @param state The new state of the game.
     */
    private void setGameState(GameState state) {
        if (state == GameState.ENDED) {
            CodexGUI.getGUI().switchToEndgameScreen();
        }
    }


    /**
     * Sets up and shows the error popup whenever an error occurs.
     *
     * @param errorEvent the fired error event.
     */
    public void showError(ErrorEvent errorEvent) {
        ErrorController errorController = ElementFactory.getErrorPopup();
        GUIError error = ErrorFactory.getError(errorEvent.getErrorType());
        error.configurePopup(errorController);
        errorController.setErrorText(errorEvent.getErrorMsg());
        error.show(root);
    }

    /**
     * Sets the color of the given player, creating a colored peg and adding it to the points board.
     * This method also adds the colored peg to the peg slot.
     *
     * @param username The username of the player whose color is being set.
     * @param color    The player's color.
     */
    private void setPlayerColor(String username, PlayerColor color) {
        guiState.setPlayerColor(username, color);
        guiState.getPlayerPeg(username).setPegColor(color);

        if (currentlyShowingPlayArea != null && currentlyShowingPlayArea.getOwnerUsername().equals(username) && !currentlyShowingPlayArea.getOwnerUsername().equals(guiState.getUsername())) {
            applyHalo(color);
        }

        if (username.equals(guiState.getUsername())) {
            if (colorPopupController != null)
                centerContentPane.getChildren().remove(colorPopupController.getRoot());
        }
        guiState.setGamePoints(username, 0);
        pointsBoardController.addPegInSlot(0, color);
        PlayerButtonController playerButtonController = playerButtons.get(username);
        playerButtonController.setColor(color);
    }

    /**
     * Sets the order in which players will play during this game. This method orders the player buttons in the correct order.
     *
     * @param startOrder The order in which players will play during this game.
     */
    private void setStartOrder(List<String> startOrder) {
        guiState.setTurnOrder(startOrder);

        for (int i = 0; i < startOrder.size(); i++) {
            PlayerButtonController playerButtonController = playerButtons.get(startOrder.get(i));
            playerButtonController.setImage(i + 1);
            playersBox.getChildren().remove(playerButtonController.getRoot());
            playersBox.getChildren().addLast(playerButtonController.getRoot());
        }

        if (startOrder.getFirst().equals(guiState.getUsername()) && currentlyShowingPlayArea.getOwnerUsername().equals(guiState.getUsername())) {
            addBlackPeg();
        }

        playersBox.requestLayout();
    }

    /**
     * Adds the black peg to the starting player next to their standard colored peg.
     */
    private void addBlackPeg() {
        PegController pegController = ElementFactory.getPeg();
        pegController.setPegColor((PlayerColor) null);
        pegController.setPegRadius(20);
        pegContainer.getChildren().addFirst(pegController.getRoot());
    }

    /**
     * Initiates the given player's turn. Adds a white halo the player's button.<br>
     * If it's this client's user's turn, a series of further actions is performed:
     * <ul>
     *     <li>plays a sound to alert the player that they have to play;</li>
     *     <li>enables drag and drop functionality on the cards.</li>
     * </ul>
     *
     * @param username The username of the player whose turn has just started.
     */
    private void turn(String username) {
        if (guiState.getActivePlayer() != null) {
            playerButtons.get(guiState.getActivePlayer()).setActive(false);
        }
        guiState.setActivePlayer(username);
        playerButtons.get(username).setActive(true);

        if (username.equals(guiState.getUsername())) {
            HandController handController = guiState.getHand();
            handController.setActive(true);
            if (!(guiState.getDontDraw() && guiState.getUsername().equals(guiState.getTurnOrder().getFirst()))) {
                String filename = null;
                try {
                    filename = Objects.requireNonNull(getClass().getResource(FilePaths.GUI_MEDIA + "/ding.mp3")).toURI().toString();
                } catch (URISyntaxException e) {
                    System.err.println("Error loading audio file");
                }
                AudioClip audioClip = new AudioClip(filename);
                audioClip.play();
            }
        }
    }

    /**
     * Updates the given player's info table with new values.
     *
     * @param username       The username of the player whose resource and object counts are being updated.
     * @param resourceCounts A map containing the amount of each resource currently visible on the player's play area.
     * @param objectCounts   A map containing the amount of each object currently visible on the player's play area.
     */
    private void updateInfoTable(String username, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        InfoTableController infoTableController = guiState.getInfoTable(username);
        infoTableController.updateInfoTable(resourceCounts, objectCounts);
        if (username.equals(guiState.getUsername())) {
            HandController handController = guiState.getHand();
            handController.updateCostSatisfied();
        }
    }

    /**
     * Shows the chat filter box, allowing players to send both private and public messages.
     */
    @FXML
    public void showChatFilter(ActionEvent ignored) {
        chatFilters.setVisible(!chatFilters.isVisible());
    }

    /**
     * Sends a chat message.
     */
    private void sendChatMessage() {
        String text = chatBox.getText();
        if (text.isEmpty())
            return;

        RadioButton selectedFilter = (RadioButton) chatFilterToggleGroup.getSelectedToggle();
        String username = selectedFilter.getText();

        try {
            if (username.equals("Everyone")) {
                serverInterface.sendChatMessage(text);
            } else {
                serverInterface.sendChatMessage(text, username);
            }
        } catch (RemoteException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }

        chatBox.clear();
    }

    /**
     * Sets the common objectives for this game, showing them in the correct slot.
     *
     * @param commonObjectives The common objectives for this game.
     */
    private void setCommonObjectives(ObjectiveCard[] commonObjectives) {
        guiState.setCommonObjectives(List.of(commonObjectives));

        List<Parent> cardPanes = new ArrayList<>();
        for (ObjectiveCard objectiveCard : commonObjectives) {
            CardController cardController = ElementFactory.getCard();
            cardController.setCardAndShowSide(objectiveCard, SideType.FRONT);
            cardPanes.add(cardController.getRoot());
        }
        Platform.runLater(() -> {
            objectivesCardGroup.getChildren().clear();
            objectivesCardGroup.getChildren().addAll(cardPanes);
        });
    }

    /**
     * Sets this client's player's hand.
     *
     * @param hand The cards in the player's hand.
     */
    private void setHand(List<PlayableCard> hand) {
        HandController handController = guiState.getHand();
        for (PlayableCard playableCard : hand) {
            handController.addCard(playableCard);
        }
        handController.updateCostSatisfied();
    }

    /**
     * Adds a card to this client's player's hand.
     *
     * @param card The card to be added.
     */
    private void addCardToHand(PlayableCard card) {
        enableDraw(false);

        HandController handController = guiState.getHand();
        handController.addCard(card);
        handController.updateCostSatisfied();
    }

    /**
     * Removes the given card from this client's player's hand.
     *
     * @param card The card to remove.
     */
    private void removeCardFromHand(PlayableCard card) {
        HandController handController = guiState.getHand();
        handController.removeCard(card);
    }

    /**
     * Sets another player's restricted view of their hand.
     *
     * @param username The username of the player whose restricted hand is being set.
     * @param hand     The player's restricted hand.
     */
    private void setOtherHand(String username, List<RestrictedCard> hand) {
        HandController handController = ElementFactory.getHandSlot();
        for (RestrictedCard card : hand) {
            handController.addCard(card);
        }
        handController.setUsername(username);
        guiState.setOtherHand(username, handController);
        OtherPlayerInfoController otherPlayerInfoController = otherHandControllers.get(username);
        if (currentlyShowingPlayArea != null && currentlyShowingPlayArea.getOwnerUsername().equals(username)) {
            handSlot.getChildren().set(0, handController.getRoot());
        } else {
            if (otherPlayerInfoController != null) {
                otherPlayerInfoController.setHandController(handController);
            }
        }
    }

    /**
     * Adds a restricted card to another player's hand.
     *
     * @param username The username of the player whose hand this card should be added to.
     * @param card     The card to be added.
     */
    private void addCardToOtherHand(String username, RestrictedCard card) {
        HandController handController = guiState.getOtherHand(username);
        handController.addCard(card);
    }

    /**
     * Removes a restricted card from another player's hand.
     *
     * @param username The username of the player whose hand this card should be removed from.
     * @param card     The card to be removed.
     */
    private void removeCardFromOtherHand(String username, RestrictedCard card) {
        HandController handController = guiState.getOtherHand(username);
        handController.removeCard(card);
    }

    /**
     * Opens a popup to allow the player to choose the side on which to play their starter card.
     *
     * @param starterCard The starter card that was assigned to the player.
     */
    private void promptStarterChoice(StarterCard starterCard) {
        starterPopupController = ElementFactory.getStarterPopup();
        starterPopupController.setStarterCard(starterCard);
        centerContentPane.getChildren().addLast(starterPopupController.getRoot());
    }

    /**
     * Opens a popup to allow the player to choose their color. By default, this popup tells the player to wait.
     */
    private void choosingColors() {
        colorPopupController = ElementFactory.getColorPopup();
        centerContentPane.getChildren().addLast(colorPopupController.getRoot());
    }

    /**
     * Shows the color options on the previously opened color popup. If {@link PlayScreenController#choosingColors} methods wasn't called previously, this method will call it automatically.
     *
     * @param colorChoices The options from which the player can choose their color.
     */
    private void promptColorChoice(List<PlayerColor> colorChoices) {
        if (colorPopupController == null) choosingColors();
        colorPopupController.setColors(colorChoices);
    }

    /**
     * Opens a popup from which the player can choose their personal objective.
     *
     * @param possiblePersonalObjectives The options from which the player can pick their personal objective.
     */
    private void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) {
        objectivePopupController = ElementFactory.getObjectivePopup();
        objectivePopupController.setObjectives(possiblePersonalObjectives);
        centerContentPane.getChildren().addLast(objectivePopupController.getRoot());
    }

    /**
     * Sets the player's personal objective and closes the choice popup.
     *
     * @param personalObjective The player's personal objective.
     */
    private void setPersonalObjective(ObjectiveCard personalObjective) {
        if (objectivePopupController != null)
            centerContentPane.getChildren().remove(objectivePopupController.getRoot());
        else
            objectivePopupController = ElementFactory.getObjectivePopup();
        CardController cardController = ElementFactory.getCard();
        cardController.setCard(personalObjective);
        cardController.showSide(SideType.FRONT);
        cardController.setTurnable();
        if (currentlyShowingPlayArea.getOwnerUsername().equals(guiState.getUsername())) {
            personalObjectiveSlot.getChildren().clear();
            personalObjectiveSlot.getChildren().add(cardController.getRoot());
        }

        guiState.setPersonalObjective(cardController);
    }

    /**
     * Sets the given player's starter card and creates their play area, and initializes the info table.<br>
     * This method also closes the starter card choice popup if the given username if this client's player's username.
     *
     * @param username         The username of the player whose new play area is being given.
     * @param placementOrder   The order in which card were placed in this play area.
     * @param field            A map containing the position of each card in the play area.
     * @param activeSides      The side on which each card was played in this play area.
     * @param legalPositions   The set of positions where a card can be placed.
     * @param illegalPositions The set of positions where a card cannot be placed.
     * @param resourceCounts   A map containing the amount of each resource currently visible on the player's play area.
     * @param objectCounts     A map containing the amount of each object currently visible on the player's play area.
     */
    private void setPlayArea(String username, List<Position> placementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        PlayAreaGridController playAreaGridController = ElementFactory.getPlayAreaGrid();
        playAreaGridController.setOwnerUsername(username);
        Set<Position> emptySet = Set.of();
        for (Position pos : placementOrder) {
            BoardCard card = field.get(pos);
            SideType side = activeSides.get(card);
            CardController cardController = ElementFactory.getCard();
            cardController.setCardAndShowSide(card, side);
            if (pos.equals(new Position(0, 0))) {
                playAreaGridController.setCenterCard(cardController, emptySet, emptySet);
            } else {
                playAreaGridController.putCard(cardController, pos, emptySet, emptySet);
            }
        }
        Set<Position> onlyLegal = new HashSet<>(legalPositions);
        onlyLegal.removeAll(illegalPositions);
        playAreaGridController.putFillersInGrid(onlyLegal);

        guiState.setPlayArea(username, playAreaGridController);

        InfoTableController infoTableController = ElementFactory.getInfoTable();
        guiState.setInfoTable(username, infoTableController);

        if (username.equals(guiState.getUsername())) {
            resetPlayer();
            if (starterPopupController != null) {
                centerContentPane.getChildren().remove(starterPopupController.getRoot());
            }
        } else {
            setPlayerButtonActions(username);
        }

        updateInfoTable(username, resourceCounts, objectCounts);
    }

    /**
     * Plays a card on the given player's play area and adds them to their grid.
     *
     * @param username              The username of the player who played the card.
     * @param card                  The card that was played.
     * @param side                  The side on which the card was played.
     * @param pos                   The position where the card was played.
     * @param addedLegalPositions   The set of new positions where a card can be placed.
     * @param removedLegalPositions The set of positions where a card can no longer be placed.
     * @param resourceCounts        A map containing the new amount of each resource currently visible on the player's play area.
     * @param objectCounts          A map containing the amount of each object currently visible on the player's play area.
     */
    private void playCard(String username, BoardCard card, SideType side, Position pos, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        PlayAreaGridController playAreaGridController = guiState.getPlayArea(username);

        CardController cardController = ElementFactory.getCard();
        cardController.setCardAndShowSide(card, side);

        playAreaGridController.putCard(cardController, pos, addedLegalPositions, removedLegalPositions);

        updateInfoTable(username, resourceCounts, objectCounts);

        guiState.getHand().setActive(false);
        if (!guiState.getDontDraw() && username.equals(guiState.getUsername())) {
            enableDraw(true);
        }
    }

    /**
     * Sets and shows the type of card currently on top of the given deck.
     *
     * @param whichDeck   The type of deck.
     * @param deckTopType The type of card on top of the specified deck.
     */
    private void setDeckTopType(PlayableCardType whichDeck, ResourceType deckTopType) {
        CardController cardBack;
        if (deckTopType != null) {
            cardBack = ElementFactory.getCardBackOnly(whichDeck, deckTopType);

            if (cardBack == null) throw new RuntimeException("Unknown Card Type: " + whichDeck);

            cardBack.setShadowColor(deckTopType);
        } else {
            cardBack = ElementFactory.getCard();
        }

        Parent cardPane = cardBack.getRoot();

        switch (whichDeck) {
            case RESOURCE -> {
                cardBack.setDrawType(DrawType.RESOURCE_DECK);
                resourceDeck = cardBack;
                resourceDeckSlot.getChildren().set(0, cardPane);
            }
            case GOLD -> {
                cardBack.setDrawType(DrawType.GOLD_DECK);
                goldDeck = cardBack;
                goldDeckSlot.getChildren().set(0, cardPane);
            }
        }
    }

    /**
     * Sets and shows the common cards from which the players can draw.
     *
     * @param resourceCards The resource cards from which the players can draw from.
     * @param goldCards     The gold cards from which the players can draw from.
     */
    private void setCommonCards(PlayableCard[] resourceCards, PlayableCard[] goldCards) {
        for (int i = 0; i < resourceCards.length; i++) {
            PlayableCard card = resourceCards[i];
            CardController cardController = ElementFactory.getCard();
            if (card != null) {
                cardController.setCardAndShowSide(card, SideType.FRONT);
                cardController.setShadowColor(card.getType());
                switch (i) {
                    case 0 -> cardController.setDrawType(DrawType.RESOURCE_1);
                    case 1 -> cardController.setDrawType(DrawType.RESOURCE_2);
                }
            }
            commonResourceCards.set(i, cardController);
            commonResourceCardsSlot.getChildren().set(i, cardController.getRoot());
        }
        for (int i = 0; i < goldCards.length; i++) {
            PlayableCard card = goldCards[i];
            CardController cardController = ElementFactory.getCard();
            if (card != null) {
                cardController.setCardAndShowSide(card, SideType.FRONT);
                cardController.setShadowColor(card.getType());
                switch (i) {
                    case 0 -> cardController.setDrawType(DrawType.GOLD_1);
                    case 1 -> cardController.setDrawType(DrawType.GOLD_2);
                }
            }
            commonGoldCards.set(i, cardController);
            commonGoldCardsSlot.getChildren().set(i, cardController.getRoot());
        }
    }

    /**
     * Sets the amount of game points earned by the given player and updates the points table.
     *
     * @param username   The username of the player whose game points are being given.
     * @param gamePoints The amount of game points earned by the player.
     */
    private void setGamePoints(String username, int gamePoints) {
        int tempGamePoints = gamePoints;
        if (tempGamePoints > 29) {
            tempGamePoints = 29;
        }

        PlayerColor color = guiState.getPlayerColor(username);
        pointsBoardController.removePegInSlot(guiState.getGamePoints(username), color);
        guiState.setGamePoints(username, gamePoints);
        pointsBoardController.addPegInSlot(tempGamePoints, color);
    }

    private void setObjectivePoints(String username, int objectivePoints) {
        guiState.setObjectivePoints(username, objectivePoints);
    }

    /**
     * Disables draws for subsequent turns.
     * Plays a sound to alert the player that the last round has begun.
     */
    private void notifyDontDraw() {
        guiState.setDontDraw(true);
        String filename = null;
        try {
            filename = Objects.requireNonNull(getClass().getResource(FilePaths.GUI_MEDIA + "/final_round.mp3")).toURI().toString();
        } catch (URISyntaxException e) {
            System.err.println("Error loading audio file");
        }
        AudioClip audioClip = new AudioClip(filename);
        audioClip.play();
    }

    /**
     * Tells the user that the given player is in a deadlock situation and their turn is skipped.
     *
     * @param username The username of the player who is in a deadlock situation.
     */
    private void signalDeadLock(String username) {
        ErrorController errorController = ElementFactory.getErrorPopup();
        GUIError error = ErrorFactory.getError(ErrorType.GENERIC_ERROR);
        error.configurePopup(errorController);
        if (username.equals(guiState.getUsername())) {
            errorController.setErrorText("You have deadlocked yourself.\nYour turn is skipped.");
        } else {
            errorController.setErrorText(username + " has deadlocked themselves.\nTheir turn is skipped.");
        }
        error.show(root);
    }

    /**
     * Tells the player that the given player has disconnected and the game is being suspended as a result.
     *
     * @param whoDisconnected The username of the player who disconnected.
     */
    private void signalGameSuspension(String whoDisconnected) {
        ErrorController errorController = ElementFactory.getErrorPopup();
        GUIError error = ErrorFactory.getError(ErrorType.OTHER_PLAYER_DISCONNECTED);
        error.configurePopup(errorController);
        errorController.setErrorText(whoDisconnected + " disconnected. Rejoin later.");
        error.show(root);
    }

    /**
     * Tells the player that the given player has disconnected and the game is being deleted as a result.
     *
     * @param whoDisconnected The username of the player who disconnected.
     */
    private void signalGameDeletion(String whoDisconnected) {
        ErrorController errorController = ElementFactory.getErrorPopup();
        GUIError error = ErrorFactory.getError(ErrorType.OTHER_PLAYER_DISCONNECTED);
        error.configurePopup(errorController);
        errorController.setErrorText(whoDisconnected + " disconnected.\nThe game was deleted.");
        error.show(root);
    }

    /**
     * Adds the given messages to the chat.
     *
     * @param messages The newly received messages.
     */
    private void receiveMessages(List<ChatMessage> messages) {
        guiState.addNewMessages(messages);
        for (ChatMessage message : messages) {
            if (message.text().equals("rick")) {
                if (rick.getMediaPlayer() == null) {
                    try {
                        String filename = Objects.requireNonNull(getClass().getResource(FilePaths.GUI_MEDIA + "/rick.mp4")).toURI().toString();
                        Media media = new Media(filename);
                        MediaPlayer mediaPlayer = new MediaPlayer(media);
                        rick.setMediaPlayer(mediaPlayer);
                    } catch (Exception e) {
                        System.err.println("Error loading media: " + e.getMessage());
                    }
                }

                rick.setVisible(true);
                rick.requestFocus();
                rick.getMediaPlayer().seek(rick.getMediaPlayer().getStartTime());
                rick.getMediaPlayer().setOnEndOfMedia(() -> rick.setVisible(false));
                rick.getMediaPlayer().setOnError(() -> rick.setVisible(false));
                rick.getMediaPlayer().play();
            } else {
                Text messageText = new Text();

                messageText.setText(message.toString());
                messageText.setWrappingWidth(chatMessages.getWidth() - chatMessages.getPadding().getRight());

                chatMessages.getChildren().addLast(messageText);
                chatScrollPane.setVvalue(1);
            }
        }
    }

    /**
     * Adds event listeners to the player buttons to show other player's basic info, like their hand, and to allow the switch to see their play area.
     *
     * @param username The username of the player whose player button should be updated with event listeners.
     */
    private void setPlayerButtonActions(String username) {
        PlayerButtonController playerButton = playerButtons.get(username);
        OtherPlayerInfoController otherPlayerInfoController = ElementFactory.getOtherPlayerInfo();
        otherPlayerInfoController.setUsername(username);
        otherHandControllers.put(username, otherPlayerInfoController);

        otherPlayerInfoController.getRoot().setVisible(false);

        otherPlayersArea.getChildren().addLast(otherPlayerInfoController.getRoot());
        otherPlayerInfoController.getRoot().setTranslateX(-85);
        otherPlayerInfoController.getRoot().setTranslateY(-(playersBox.getHeight() / 2 - playerButton.getRoot().getBoundsInParent().getCenterY()));

        playerButton.getRoot().boundsInParentProperty().addListener((observable, oldValue, newValue) -> otherPlayerInfoController.getRoot().setTranslateY(-(playersBox.getHeight() / 2 - newValue.getCenterY())));

        playerButton.setActions(otherPlayerInfoController, currentlyShowingOtherHand);
        playerButton.setDisabled(false);

        otherPlayerInfoController.setPlayAreaButtonAction(event -> {
            OtherPlayerInfoController current = currentlyShowingOtherHand.get();
            if (current != null) {
                current.getRoot().setVisible(false);
                currentlyShowingOtherHand.set(null);
            }
            switchToOtherPlayer(username);
        });

    }

    /**
     * Switches the currently visible screen to the given player's screen.
     * This method changes the visible play area, info table, hand and personal objective. This method also adds a colored halo around the screen.
     *
     * @param username The username of the player whose screen should be shown.
     */
    private void switchToOtherPlayer(String username) {
        for (String u : guiState.getPlayerUsernames()) {
            if (!u.equals(username) && !u.equals(guiState.getUsername())) {
                playerButtons.get(u).setDisabled(guiState.getPlayArea(u) == null);
                OtherPlayerInfoController otherPlayerInfoController = otherHandControllers.get(u);
                HandController otherHand = guiState.getOtherHand(u);
                if (otherPlayerInfoController != null && otherHand != null) {
                    otherPlayerInfoController.setHandController(otherHand);
                }
            }
        }

        playerButtons.get(username).setDisabled(true);
        playerButtons.get(guiState.getUsername()).setDisabled(false);

        applyHalo(guiState.getPlayerColor(username));

        switchToPlayArea(username);
        switchToHand(username);

        if (objectivePopupController != null) {
            CardController cardController = ElementFactory.getObjectiveBack();
            personalObjectiveSlot.getChildren().set(0, cardController.getRoot());
        }

        pegContainer.getChildren().clear();
        PegController peg = guiState.getPlayerPeg(username);
        pegContainer.getChildren().add(peg.getRoot());
        List<String> turnOrder = guiState.getTurnOrder();
        if (!turnOrder.isEmpty() && turnOrder.getFirst().equals(username)) {
            addBlackPeg();
        }
    }

    /**
     * Resets the screen to this client's player's and removes any halos around the screen.
     */
    private void resetPlayer() {
        for (String u : guiState.getPlayerUsernames()) {
            if (!u.equals(guiState.getUsername())) {
                playerButtons.get(u).setDisabled(guiState.getPlayArea(u) == null);

                OtherPlayerInfoController otherPlayerInfoController = otherHandControllers.get(u);
                HandController otherHand = guiState.getOtherHand(u);
                if (otherPlayerInfoController != null && otherHand != null) {
                    otherHandControllers.get(u).setHandController(guiState.getOtherHand(u));
                }
            }
        }

        playerButtons.get(guiState.getUsername()).setDisabled(true);

        removeHalo();

        switchToPlayArea(guiState.getUsername());
        switchToHand(guiState.getUsername());

        if (guiState.getPersonalObjective() != null) {
            personalObjectiveSlot.getChildren().set(0, guiState.getPersonalObjective().getRoot());
        } else {
            CardController placeholder = ElementFactory.getCard();
            personalObjectiveSlot.getChildren().set(0, placeholder.getRoot());
        }

        pegContainer.getChildren().clear();
        PegController peg = guiState.getPlayerPeg(guiState.getUsername());
        pegContainer.getChildren().add(peg.getRoot());
        List<String> turnOrder = guiState.getTurnOrder();
        if (!turnOrder.isEmpty() && turnOrder.getFirst().equals(guiState.getUsername())) {
            addBlackPeg();
        }
    }

    /**
     * Switches the currently visible play area to the given player's.
     *
     * @param username The username of the player whose play area should be shown.
     */
    private void switchToPlayArea(String username) {
        int playAreaIndex;
        if (currentlyShowingPlayArea == null) {
            playAreaIndex = -1;
        } else {
            playAreaIndex = centerContentPane.getChildren().indexOf(currentlyShowingPlayArea.getRoot());
        }
        currentlyShowingPlayArea = guiState.getPlayArea(username);

        if (playAreaIndex != -1) {
            centerContentPane.getChildren().remove(playAreaIndex);
        }

        if (currentlyShowingPlayArea != null) {
            centerContentPane.getChildren().addFirst(currentlyShowingPlayArea.getRoot());
        }

        InfoTableController infoTableController = guiState.getInfoTable(username);
        infoTableSlot.getChildren().clear();
        if (infoTableController != null) {
            infoTableSlot.getChildren().add(infoTableController.getRoot());
        }
    }

    /**
     * Switches the visible hand to the requested player's.
     *
     * @param username The username of the player whose hand should be shown.
     */
    private void switchToHand(String username) {
        handSlot.getChildren().clear();
        if (username.equals(guiState.getUsername())) {
            handSlot.getChildren().add(guiState.getHand().getRoot());
        } else {
            if (guiState.getOtherHand(username) != null) {
                handSlot.getChildren().add(guiState.getOtherHand(username).getRoot());
            } else {
                handSlot.getChildren().add(ElementFactory.getPlaceholderHand());
            }
        }
    }

    /**
     * Applies a colored halo around the screen.
     *
     * @param color The color of the halo to apply. If <code>null</code>, a black halo will be applied.
     */
    private void applyHalo(PlayerColor color) {
        removeHalo();
        if (color != null) {
            root.getStyleClass().add(haloClasses.get(color));
        } else {
            root.getStyleClass().add("halo-unknown");
        }
    }

    /**
     * Removes any halos around the screen.
     */
    private void removeHalo() {
        List<String> rootClasses = root.getStyleClass();
        haloClasses.forEach((key, value) -> rootClasses.remove(value));
        rootClasses.remove("halo-unknown");
    }

    /**
     * Enables or disables the common cards and decks so that the player can draw a card.
     *
     * @param enabled Whether the common cards and decks should be enabled.
     */
    private void enableDraw(boolean enabled) {
        for (CardController drawOption : commonResourceCards) {
            drawOption.setInteractable(enabled);
            drawOption.setDrawable(enabled);
        }
        for (CardController drawOption : commonGoldCards) {
            drawOption.setInteractable(enabled);
            drawOption.setDrawable(enabled);
        }
        resourceDeck.setInteractable(enabled);
        resourceDeck.setDrawable(enabled);
        goldDeck.setInteractable(enabled);
        goldDeck.setDrawable(enabled);
    }

    /**
     * Leaves the game.
     */
    private void leave() {
        try {
            serverInterface.leaveGame();
        } catch (RemoteException e) {
            System.err.println("Error communicating with the server: " + e.getMessage());
        }

        CodexGUI.getGUI().switchToWelcomeScreen();
    }

    /**
     * Shows the rules popup.
     */
    @FXML
    public void showRules() {
        RulesPopupController rulesPopupController = ElementFactory.getRulesPopup();
        root.getChildren().addLast(rulesPopupController.getRoot());
    }

    /**
     * Registers all the main event handlers for this screen, including buttons and events fired by incoming messages from the server.
     */
    private void registerEvents() {
        root.addEventFilter(GUIEventTypes.ERROR_EVENT, this::showError);

        root.addEventFilter(GUIEventTypes.SET_COLOR_EVENT, e -> setPlayerColor(e.getUsername(), e.getColor()));

        root.addEventFilter(GUIEventTypes.SET_COMMON_CARDS_EVENT, e -> setCommonCards(e.getCommonResourceCards(), e.getCommonGoldCards()));

        root.addEventFilter(GUIEventTypes.SET_COMMON_OBJECTIVES_EVENT, e -> setCommonObjectives(e.getCommonObjectives()));

        root.addEventFilter(GUIEventTypes.SET_PERSONAL_OBJECTIVE_EVENT, e -> setPersonalObjective(e.getPersonalObjective()));

        root.addEventFilter(GUIEventTypes.SET_DECK_TOP_TYPE_EVENT, e -> setDeckTopType(e.getWhichDeck(), e.getResourceType()));

        root.addEventFilter(GUIEventTypes.SET_PLAY_AREA_EVENT, e -> setPlayArea(e.getUsername(), e.getCardPlacementOrder(), e.getField(), e.getActiveSides(), e.getLegalPositions(), e.getIllegalPositions(), e.getResourceCounts(), e.getObjectCounts()));

        root.addEventFilter(GUIEventTypes.PLAY_CARD_EVENT, e -> playCard(e.getUsername(), e.getCard(), e.getSide(), e.getPos(), e.getAddedLegalPositions(), e.getRemovedLegalPositions(), e.getResourceCounts(), e.getObjectCounts()));

        root.addEventFilter(GUIEventTypes.SET_HAND_EVENT, e -> setHand(e.getHand()));

        root.addEventFilter(GUIEventTypes.ADD_CARD_TO_HAND_EVENT, e -> addCardToHand(e.getCard()));

        root.addEventFilter(GUIEventTypes.REMOVE_CARD_FROM_HAND_EVENT, e -> removeCardFromHand(e.getCard()));

        root.addEventFilter(GUIEventTypes.SET_GAME_POINTS_EVENT, e -> setGamePoints(e.getUsername(), e.getGamePoints()));

        root.addEventFilter(GUIEventTypes.SET_OBJECTIVE_POINTS_EVENT, e -> setObjectivePoints(e.getUsername(), e.getObjectivePoints()));

        root.addEventFilter(GUIEventTypes.ADD_CHAT_MESSAGES_EVENT, e -> receiveMessages(e.getMessages()));

        root.addEventFilter(GUIEventTypes.SIGNAL_DEADLOCK_EVENT, e -> signalDeadLock(e.getUsername()));

        root.addEventFilter(GUIEventTypes.SIGNAL_GAME_SUSPENSION_EVENT, e -> signalGameSuspension(e.getWhoDisconnected()));

        root.addEventFilter(GUIEventTypes.SIGNAL_GAME_DELETION_EVENT, e -> signalGameDeletion(e.getWhoDisconnected()));

        root.addEventFilter(GUIEventTypes.NOTIFY_DONT_DRAW_EVENT, e -> notifyDontDraw());

        root.addEventFilter(GUIEventTypes.SET_START_ORDER_EVENT, e -> setStartOrder(e.getUsernames()));

        root.addEventFilter(GUIEventTypes.PROMPT_OBJECTIVE_CHOICE_EVENT, e -> promptObjectiveChoice(e.getPossiblePersonalObjectives()));

        root.addEventFilter(GUIEventTypes.PROMPT_STARTER_CHOICE_EVENT, e -> promptStarterChoice(e.getStarterCard()));

        root.addEventFilter(GUIEventTypes.PROMPT_COLOR_CHOICE_EVENT, e -> promptColorChoice(e.getColorChoices()));

        root.addEventFilter(GUIEventTypes.CHOOSING_COLORS_EVENT, e -> choosingColors());

        root.addEventFilter(GUIEventTypes.SET_GAME_STATE_EVENT, e -> setGameState(e.getState()));

        root.addEventFilter(GUIEventTypes.TURN_EVENT, e -> turn(e.getUsername()));

        root.addEventFilter(GUIEventTypes.SET_OTHER_HAND_EVENT, e -> setOtherHand(e.getUsername(), e.getHand()));

        root.addEventFilter(GUIEventTypes.ADD_CARD_TO_OTHER_HAND_EVENT, e -> addCardToOtherHand(e.getUsername(), e.getNewCard()));

        root.addEventFilter(GUIEventTypes.REMOVE_CARD_FROM_OTHER_HAND_EVENT, e -> removeCardFromOtherHand(e.getUsername(), e.getCardToRemove()));

        leaveButton.setOnMouseClicked(e -> {
            leave();
            e.consume();
        });

        rulesButton.setOnMouseClicked(e -> {
            showRules();
            e.consume();
        });

        rulesButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                showRules();
                keyEvent.consume();
            }
        });

        CodexGUI.getGUI().getStage().getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
            if (evt.getPickResult().getIntersectedNode() != chatFilterButton && !inHierarchy(evt.getPickResult().getIntersectedNode(), chatFilters)) {
                chatFilters.setVisible(false);
            }

            if (currentlyShowingOtherHand.get() != null) {
                OtherPlayerInfoController current = currentlyShowingOtherHand.get();
                PlayerButtonController playerButton = playerButtons.get(current.getUsername());

                if (evt.getPickResult().getIntersectedNode() != current.getRoot() && !inHierarchy(evt.getPickResult().getIntersectedNode(), current.getRoot())) {
                    current.getRoot().setVisible(false);
                    currentlyShowingOtherHand.set(null);
                }

                if (inHierarchy(evt.getPickResult().getIntersectedNode(), playerButton.getRoot())) {
                    evt.consume();
                }
            }
        });

        chatBox.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                sendChatMessage();
            }
        });
    }

    /**
     * Checks whether the given node contains another node between its descendants.
     *
     * @param node                      The "father" node.
     * @param potentialHierarchyElement A potential descendant of the father node.
     * @return Whether the given node is a descendant of the father node.
     */
    private static boolean inHierarchy(Node node, Node potentialHierarchyElement) {
        if (potentialHierarchyElement == null) {
            return true;
        }
        while (node != null) {
            if (node == potentialHierarchyElement) {
                return true;
            }
            node = node.getParent();
        }
        return false;
    }
}
