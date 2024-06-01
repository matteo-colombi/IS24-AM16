package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.controllers.elements.*;
import it.polimi.ingsw.am16.client.view.gui.events.GUIEventTypes;
import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
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
import javafx.scene.text.Text;

import javax.swing.text.Element;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
    private VBox objectivesCardGroup;
    @FXML
    private HBox handSlot;
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
    private StackPane otherPlayersArea;
    @FXML
    private VBox playersBox;
    @FXML
    private StackPane more;

    private ServerInterface serverInterface;

    private PointsBoardController pointsBoardController;

    private GUIState guiState;

    private Map<String, PlayerButtonController> playerButtons;

    private Map<String, OtherHandController> otherHandControllers;
    private AtomicReference<OtherHandController> currentlyShowingOtherHand;
    private PlayAreaGridController currentlyShowingPlayArea;

    private StarterPopupController starterPopupController;
    private ColorPopupController colorPopupController;
    private ObjectivePopupController objectivePopupController;

    private List<CardController> commonResourceCards;
    private List<CardController> commonGoldCards;

    private CardController resourceDeck;
    private CardController goldDeck;

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
        handSlot.getChildren().add(hand.getRoot());

        currentlyShowingOtherHand = new AtomicReference<>();
        currentlyShowingOtherHand.set(null);

        receiveMessages(guiState.getChatMessages());

        setPlayers(guiState.getPlayerUsernames());
    }

    private void setPlayers(List<String> usernames) {
        playerButtons = new HashMap<>();
        otherHandControllers = new HashMap<>();
        playersBox.getChildren().clear();
        chatFilterNames.getChildren().clear();

        for (String username : usernames) {
            PlayerButtonController playerButtonController = ElementFactory.getPlayerButton();
            playerButtons.put(username, playerButtonController);
            playersBox.getChildren().add(playerButtonController.getRoot());

            if (!username.equals(guiState.getUsername())) {
                RadioButton playerChatFilter = new RadioButton(username);
                playerChatFilter.setId(username);
                playerChatFilter.setToggleGroup(chatFilterToggleGroup);
                chatFilterNames.getChildren().addLast(playerChatFilter);
            }
        }

        playersBox.requestLayout();
    }

    private void setGameState(GameState state) {
        //TODO
        if (state == GameState.ENDED) {
            CodexGUI.getGUI().switchToEndgameScreen();
        }
    }

    public void showError(String errorMessage) {
        //TODO
    }

    private void setPlayerColor(String username, PlayerColor color) {
        guiState.setPlayerColor(username, color);
        if (username.equals(guiState.getUsername())) {
            setPegColor(color);
            if (colorPopupController != null)
                centerContentPane.getChildren().remove(colorPopupController.getRoot());
        }
        guiState.setGamePoints(username, 0);
        pointsBoardController.addPegInSlot(0, color);
        PlayerButtonController playerButtonController = playerButtons.get(username);
        playerButtonController.setColor(color);
    }

    private void setPegColor(PlayerColor color) {
        PegController pegController = ElementFactory.getPeg();
        pegController.setPegColor(color);
        pegController.setPegRadius(20);
        pegContainer.getChildren().clear();
        pegContainer.getChildren().add(pegController.getRoot());
    }

    private void setStartOrder(List<String> startOrder) {
        guiState.setTurnOrder(startOrder);

        for (int i = 0; i < startOrder.size(); i++) {
            PlayerButtonController playerButtonController = playerButtons.get(startOrder.get(i));
            playerButtonController.setImage(i+1);
            playersBox.getChildren().remove(playerButtonController.getRoot());
            playersBox.getChildren().addLast(playerButtonController.getRoot());
        }

        if (startOrder.getFirst().equals(guiState.getUsername())) {
            setIsStartingPlayer();
        }

        playersBox.requestLayout();
    }

    private void setIsStartingPlayer() {
        PegController pegController = ElementFactory.getPeg();
        pegController.setPegColor((PlayerColor) null);
        pegController.setPegRadius(20);
        pegContainer.getChildren().addFirst(pegController.getRoot());
    }

    private void turn(String username) {
        if (guiState.getActivePlayer() != null) {
            playerButtons.get(guiState.getActivePlayer()).setActive(false);
        }
        guiState.setActivePlayer(username);
        playerButtons.get(username).setActive(true);

        if (username.equals(guiState.getUsername())) {
            HandController handController = guiState.getHand();
            handController.setActive(true);
        }
    }

    private void updateInfoTable(String username, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        InfoTableController infoTableController = guiState.getInfoTable(username);
        infoTableController.updateInfoTable(resourceCounts, objectCounts);
        if (username.equals(guiState.getUsername())) {
            HandController handController = guiState.getHand();
            handController.updateCostSatisfied();
        }
    }

    @FXML
    public void showChatFilter(ActionEvent ignored) {
        chatFilters.setVisible(!chatFilters.isVisible());
    }

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
            e.printStackTrace();
        }

        chatBox.clear();
    }

    private void setCommonObjectives(ObjectiveCard[] commonObjectives) {
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

    private void drawingCards() {
        //TODO
    }

    private void setHand(List<PlayableCard> hand) {
        HandController handController = guiState.getHand();
        for (PlayableCard playableCard : hand) {
            handController.addCard(playableCard);
        }
        handController.updateCostSatisfied();
    }

    private void addCardToHand(PlayableCard card) {
        enableDraw(false);

        HandController handController = guiState.getHand();
        handController.addCard(card);
        handController.updateCostSatisfied();
    }

    private void removeCardFromHand(PlayableCard card) {
        HandController handController = guiState.getHand();
        handController.removeCard(card);
    }

    private void setOtherHand(String username, List<RestrictedCard> hand) {
        HandController handController = ElementFactory.getHandSlot();
        for (RestrictedCard card : hand) {
            handController.addCard(card);
        }
        handController.setUsername(username);
        guiState.setOtherHand(username, handController);
        OtherHandController otherHandController = otherHandControllers.get(username);
        if (otherHandController != null) {
            otherHandController.setHandController(handController);
        }
    }

    private void addCardToOtherHand(String username, RestrictedCard card) {
        HandController handController = guiState.getOtherHand(username);
        handController.addCard(card);
    }

    private void removeCardFromOtherHand(String username, RestrictedCard card) {
        HandController handController = guiState.getOtherHand(username);
        handController.removeCard(card);
    }

    private void promptStarterChoice(StarterCard starterCard) {
        starterPopupController = ElementFactory.getStarterPopup();
        starterPopupController.setStarterCard(starterCard);
        centerContentPane.getChildren().addLast(starterPopupController.getRoot());
    }

    private void choosingColors() {
        colorPopupController = ElementFactory.getColorPopup();
        centerContentPane.getChildren().addLast(colorPopupController.getRoot());
    }

    private void promptColorChoice(List<PlayerColor> colorChoices) {
        colorPopupController.setColors(colorChoices);
    }

    private void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) {
        objectivePopupController = ElementFactory.getObjectivePopup();
        objectivePopupController.setObjectives(possiblePersonalObjectives);
        centerContentPane.getChildren().addLast(objectivePopupController.getRoot());
    }

    private void setPersonalObjective(ObjectiveCard personalObjective) {
        if (objectivePopupController != null)
            centerContentPane.getChildren().remove(objectivePopupController.getRoot());
        CardController cardController = ElementFactory.getCard();
        cardController.setCard(personalObjective);
        cardController.showSide(SideType.FRONT);
        cardController.setTurnable();
        personalObjectiveSlot.getChildren().clear();
        personalObjectiveSlot.getChildren().add(cardController.getRoot());

        guiState.setPersonalObjective(cardController);
    }

    private void setPlayArea(String username, List<Position> placementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        PlayAreaGridController playAreaGridController = ElementFactory.getPlayAreaGrid();
        Set<Position> emptySet = Set.of();
        for(Position pos : placementOrder) {
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
            playerButtons.get(username).setOnMouseClicked(e -> resetPlayer());
            if (starterPopupController != null) {
                centerContentPane.getChildren().remove(starterPopupController.getRoot());
            }
        } else {
            createOtherHandHover(username);
        }

        updateInfoTable(username, resourceCounts, objectCounts);
    }

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

    private void setDeckTopType(PlayableCardType whichDeck, ResourceType deckTopType) {
        CardController cardBack = ElementFactory.getCardBackOnly(whichDeck, deckTopType);

        if (cardBack == null) throw new RuntimeException("Unknown Card Type: " + whichDeck);

        cardBack.setShadowColor(deckTopType);

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

    private void setCommonCards(PlayableCard[] resourceCards, PlayableCard[] goldCards) {
        for (int i = 0; i < resourceCards.length; i++) {
            CardController cardController = ElementFactory.getCard();
            PlayableCard card = resourceCards[i];
            cardController.setCardAndShowSide(card, SideType.FRONT);
            cardController.setShadowColor(card.getType());
            switch (i) {
                case 0 -> cardController.setDrawType(DrawType.RESOURCE_1);
                case 1 -> cardController.setDrawType(DrawType.RESOURCE_2);
            }
            commonResourceCards.set(i, cardController);
            commonResourceCardsSlot.getChildren().set(i, cardController.getRoot());
        }
        for (int i = 0; i < goldCards.length; i++) {
            CardController cardController = ElementFactory.getCard();
            PlayableCard card = goldCards[i];
            cardController.setCardAndShowSide(card, SideType.FRONT);
            cardController.setShadowColor(card.getType());
            switch (i) {
                case 0 -> cardController.setDrawType(DrawType.GOLD_1);
                case 1 -> cardController.setDrawType(DrawType.GOLD_2);
            }
            commonGoldCards.set(i, cardController);
            commonGoldCardsSlot.getChildren().set(i, cardController.getRoot());
        }
    }

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

    private void notifyDontDraw() {
        guiState.setDontDraw(true);
        //TODO
    }

    private void setWinners(List<String> winnerUsernames) {
        //TODO
    }

    private void signalDeadLock(String username) {
        //TODO
    }

    private void signalGameSuspension(String whoDisconnected) {
        //TODO
    }

    private void signalGameDeletion(String whoDisconnected) {
        //TODO
    }

    private void receiveMessages(List<ChatMessage> messages) {
        guiState.addNewMessages(messages);
        for (ChatMessage message : messages) {
            Text messageText = new Text();

            messageText.setText(message.toString());
            messageText.setWrappingWidth(chatMessages.getWidth() - chatMessages.getPadding().getRight());

            chatMessages.getChildren().addLast(messageText);
            chatScrollPane.setVvalue(1);
        }
    }

    private void createOtherHandHover(String username) {
        PlayerButtonController playerButton = playerButtons.get(username);
        OtherHandController otherHandController = ElementFactory.getOtherHand();
        otherHandController.setUsername(username);
        otherHandControllers.put(username, otherHandController);

        otherHandController.getRoot().setVisible(false);

        otherPlayersArea.getChildren().addLast(otherHandController.getRoot());
        otherHandController.getRoot().setTranslateX(-75);

        playerButton.getRoot().boundsInParentProperty().addListener((observable, oldValue, newValue) -> {
            otherHandController.getRoot().setTranslateY(-(playersBox.getHeight()/2 - newValue.getCenterY()));
        });

        playerButton.setHovers(otherHandController, currentlyShowingOtherHand);

        otherHandController.setPlayAreaButtonAction(event -> {
            OtherHandController current = currentlyShowingOtherHand.get();
            if (current != null) {
                current.getRoot().setVisible(false);
                currentlyShowingOtherHand.set(null);
            }
            switchToOtherPlayer(username);
        });

    }

    private void switchToOtherPlayer(String username) {
        for(String u : guiState.getPlayerUsernames()) {
            playerButtons.get(u).setDisabled(u.equals(username));

            if (!u.equals(username) && !u.equals(guiState.getUsername())) {
                otherHandControllers.get(u).setHandController(guiState.getOtherHand(u));
            }
        }

        applyHalo(guiState.getPlayerColor(username));

        switchToPlayArea(username);
        switchToHand(username);

        CardController cardController = ElementFactory.getObjectiveBack();
        personalObjectiveSlot.getChildren().set(0, cardController.getRoot());

        pegContainer.getChildren().clear();
        PegController peg = ElementFactory.getPeg();
        peg.setPegRadius(20);
        peg.setPegColor(guiState.getPlayerColor(username));
        pegContainer.getChildren().add(peg.getRoot());
        List<String> turnOrder = guiState.getTurnOrder();
        if (!turnOrder.isEmpty() && turnOrder.getFirst().equals(username)) {
            setIsStartingPlayer();
        }
    }

    private void resetPlayer() {
        for (String u : guiState.getPlayerUsernames()) {
            playerButtons.get(u).setDisabled(u.equals(guiState.getUsername()));

            if (!u.equals(guiState.getUsername())) {
                otherHandControllers.get(u).setHandController(guiState.getOtherHand(u));
            }
        }

        removeHalo();

        switchToPlayArea(guiState.getUsername());
        switchToHand(guiState.getUsername());

        if (guiState.getPersonalObjective() != null)
            personalObjectiveSlot.getChildren().set(0, guiState.getPersonalObjective().getRoot());

        pegContainer.getChildren().clear();
        PegController peg = ElementFactory.getPeg();
        peg.setPegRadius(20);
        peg.setPegColor(guiState.getPlayerColor(guiState.getUsername()));
        pegContainer.getChildren().add(peg.getRoot());
        List<String> turnOrder = guiState.getTurnOrder();
        if (!turnOrder.isEmpty() && turnOrder.getFirst().equals(guiState.getUsername())) {
            setIsStartingPlayer();
        }
    }

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

    private void applyHalo(PlayerColor color) {
        removeHalo();
        root.getStyleClass().add(haloClasses.get(color));
    }

    private void removeHalo() {
        List<String> rootClasses = root.getStyleClass();
        haloClasses.forEach((key, value) -> rootClasses.remove(value));
    }

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

    private void leave() {
        try {
            serverInterface.leaveGame();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        CodexGUI.getGUI().switchToWelcomeScreen();
    }

    private void registerEvents() {
        root.addEventFilter(GUIEventTypes.ERROR_EVENT, e -> showError(e.getErrorMsg()));

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

        root.addEventFilter(GUIEventTypes.SET_WINNERS_EVENT, e -> setWinners(e.getWinnerUsernames()));

        root.addEventFilter(GUIEventTypes.SET_START_ORDER_EVENT, e -> setStartOrder(e.getUsernames()));

        root.addEventFilter(GUIEventTypes.PROMPT_OBJECTIVE_CHOICE_EVENT, e -> promptObjectiveChoice(e.getPossiblePersonalObjectives()));

        root.addEventFilter(GUIEventTypes.PROMPT_STARTER_CHOICE_EVENT, e -> promptStarterChoice(e.getStarterCard()));

        root.addEventFilter(GUIEventTypes.PROMPT_COLOR_CHOICE_EVENT, e -> promptColorChoice(e.getColorChoices()));

        root.addEventFilter(GUIEventTypes.CHOOSING_COLORS_EVENT, e -> choosingColors());

        root.addEventFilter(GUIEventTypes.DRAWING_CARDS_EVENT, e -> drawingCards());

        root.addEventFilter(GUIEventTypes.SET_GAME_STATE_EVENT, e -> setGameState(e.getState()));

        root.addEventFilter(GUIEventTypes.TURN_EVENT, e -> turn(e.getUsername()));

        root.addEventFilter(GUIEventTypes.SET_OTHER_HAND_EVENT, e -> setOtherHand(e.getUsername(), e.getHand()));

        root.addEventFilter(GUIEventTypes.ADD_CARD_TO_OTHER_HAND_EVENT, e -> addCardToOtherHand(e.getUsername(), e.getNewCard()));

        root.addEventFilter(GUIEventTypes.REMOVE_CARD_FROM_OTHER_HAND_EVENT, e -> removeCardFromOtherHand(e.getUsername(), e.getCardToRemove()));

        leaveButton.setOnMouseClicked(e -> {
            leave();
            e.consume();
        });

        //FIXME remove the comment. This is nonsense
        /*
        leaveButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                leave();
                keyEvent.consume();
            }
        });
         */

        CodexGUI.getGUI().getStage().getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
            if (evt.getPickResult().getIntersectedNode() != chatFilterButton && !inHierarchy(evt.getPickResult().getIntersectedNode(), chatFilters)) {
                chatFilters.setVisible(false);
            }

            if (currentlyShowingOtherHand.get() != null) {
                OtherHandController current = currentlyShowingOtherHand.get();
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

    /**
     * Shows or hides the "more" section.
     * @param ignored The action event (which is ignored).
     */
    @FXML
    public void showMore(ActionEvent ignored) {
        more.setVisible(!more.isVisible());
    }

    /**
     * Shows the rules screen.
     * @param ignored The action event (which is ignored).
     */
    @FXML
    public void showRules(ActionEvent ignored) {
        CodexGUI.getGUI().switchToRulesScreen();
    }
}
