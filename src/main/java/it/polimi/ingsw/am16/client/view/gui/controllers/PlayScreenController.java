package it.polimi.ingsw.am16.client.view.gui.controllers;

import it.polimi.ingsw.am16.client.view.gui.ChatListener;
import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

public class PlayScreenController implements ChatListener, ScreenController, Initializable {
    @FXML
    private StackPane centerContentPane;
    @FXML
    private Group infoTableSlot;
    @FXML
    private VBox pegContainer;
    @FXML
    private Button chatFilterButton;
    @FXML
    private StackPane chatFilters;
    @FXML
    private TextField chatBox;
    @FXML
    private ToggleGroup chatFilterToggleGroup;
    @FXML
    private VBox chatMessages;
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

    private ServerInterface serverInterface;

    private PointsBoardController pointsBoardController;

    private GUIState guiState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guiState = CodexGUI.getGUI().getGuiState();
        guiState.setPlayScreenController(this);
        guiState.setCurrentController(this);
        guiState.setChatListener(this);

        CodexGUI.getGUI().getStage().getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
            if (evt.getPickResult().getIntersectedNode() != chatFilterButton && !inHierarchy(evt.getPickResult().getIntersectedNode(), chatFilters)) {
                chatFilters.setVisible(false);
            }
        });

        this.serverInterface = CodexGUI.getGUI().getServerInterface();

        pointsBoardController = ElementFactory.getPointsBoard();
        pointsBoardSlot.getChildren().add(pointsBoardController.getRoot());

        chatBox.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                sendChatMessage();
            }
        });

        receiveMessages(guiState.getChatMessages());

        setCommonObjectives(new ObjectiveCard[]{CardRegistry.getRegistry().getObjectiveCardFromName("objective_pattern_5"), CardRegistry.getRegistry().getObjectiveCardFromName("objective_object_1")});
        setHand(List.of(CardRegistry.getRegistry().getResourceCardFromName("resource_animal_9"), CardRegistry.getRegistry().getGoldCardFromName("gold_insect_10")));
        setPersonalObjective(CardRegistry.getRegistry().getObjectiveCardFromName("objective_resources_3"));
        setIsStartingPlayer();

        setDeckTopType(PlayableCardType.RESOURCE, ResourceType.ANIMAL);
        setDeckTopType(PlayableCardType.GOLD,ResourceType.INSECT);

        setCommonCards(new PlayableCard[]{CardRegistry.getRegistry().getResourceCardFromName("resource_fungi_4"), CardRegistry.getRegistry().getResourceCardFromName("resource_insect_1")}, new PlayableCard[]{CardRegistry.getRegistry().getGoldCardFromName("gold_plant_3"), CardRegistry.getRegistry().getGoldCardFromName("gold_animal_10")});

        addCardToHand(CardRegistry.getRegistry().getGoldCardFromName("gold_animal_6"));
        addCardToHand(CardRegistry.getRegistry().getGoldCardFromName("gold_animal_6"));
        addCardToHand(CardRegistry.getRegistry().getGoldCardFromName("gold_animal_6"));
        addCardToHand(CardRegistry.getRegistry().getGoldCardFromName("gold_animal_6"));

        guiState.setUsername("teo");
        guiState.addPlayer("andre");
        guiState.addPlayer("xLorde");

        setPlayerColor("teo", PlayerColor.BLUE);
        setPlayerColor("andre", PlayerColor.YELLOW);
        setPlayerColor("xLorde", PlayerColor.GREEN);

        setGamePoints("xLorde", 35);

        setPlayArea("teo",
                List.of(),
                Map.of(new Position(0, 0), CardRegistry.getRegistry().getStarterCardFromName("starter_3")),
                Map.of(CardRegistry.getRegistry().getStarterCardFromName("starter_3"), SideType.BACK),
                Set.of(new Position(1, 1), new Position(1, -1), new Position(-1, -1), new Position(-1, 1)),
                Set.of(new Position(1, 1)),
                Map.of(),
                Map.of()
        );

        playCard("teo",
                CardRegistry.getRegistry().getGoldCardFromName("gold_fungi_3"),
                SideType.FRONT,
                new Position(1, 1),
                Set.of(new Position(0, 2)), Set.of(new Position(-1, -1)),
                Map.of(), Map.of()
        );

        updateInfoTable("teo", Map.of(ResourceType.ANIMAL, 5), Map.of());
    }

    @Override
    public void showError(String errorMessage) {
        //TODO
    }

    public void setPegColor(PlayerColor color) {
        PegController pegController  = ElementFactory.getPeg();
        pegController.setPegColor(color);
        pegController.setPegRadius(20);
        Platform.runLater(() -> {
            pegContainer.getChildren().clear();
            pegContainer.getChildren().add(pegController.getRoot());
        });
    }

    public void setIsStartingPlayer() {
        PegController pegController = ElementFactory.getPeg();
        pegController.setPegColor((PlayerColor) null);
        pegController.setPegRadius(20);
        Platform.runLater(() -> {
            pegContainer.getChildren().addFirst(pegController.getRoot());
        });
    }

    public void updateInfoTable(String username, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        InfoTableController infoTableController = guiState.getInfoTable(username);
        infoTableController.updateInfoTable(resourceCounts, objectCounts);
    }

    @FXML
    public void showChatFilter(ActionEvent ignored) {
        chatFilters.setVisible(!chatFilters.isVisible());
    }

    public void sendChatMessage() {
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

    @Override
    public void receiveMessage(ChatMessage message) {
        Text newText = new Text();
        newText.setText(message.toString());
        Platform.runLater(() -> chatMessages.getChildren().addLast(newText));
    }

    @Override
    public void receiveMessages(List<ChatMessage> messages) {
        for(ChatMessage message : messages) {
            receiveMessage(message);
        }
    }

    public void setCommonObjectives(ObjectiveCard[] commonObjectives) {
        List<Parent> cardPanes = new ArrayList<>();
        for(ObjectiveCard objectiveCard : commonObjectives) {
            CardController cardController = ElementFactory.getCard();
            cardController.setCardAndShowSide(objectiveCard, SideType.FRONT);
            cardPanes.add(cardController.getRoot());
        }
        Platform.runLater(() -> {
            objectivesCardGroup.getChildren().clear();
            objectivesCardGroup.getChildren().addAll(cardPanes);
        });
    }

    public void setHand(List<PlayableCard> hand) {
        guiState.setHand(hand);
        for(int i = 0; i<hand.size(); i++) {
            CardController cardController = ElementFactory.getCard();
            cardController.setCard(hand.get(i));
            cardController.showSide(SideType.FRONT);
            final int finalI = i;
            Platform.runLater(() -> handSlot.getChildren().set(finalI, cardController.getRoot()));
        }
    }

    public void addCardToHand(PlayableCard card) {
        if (guiState.getHandSize() >= 3) return;
        guiState.addCardToHand(card);
        CardController cardController = ElementFactory.getCard();
        cardController.setCardAndShowSide(card, SideType.FRONT);
        cardController.setShadowColor(card.getType());
        cardController.setDraggable(true);
        cardController.setInteractable(true);
        Platform.runLater(() -> handSlot.getChildren().set(guiState.getHandSize()-1, cardController.getRoot()));

    }

    public void removeCardFromHand(PlayableCard card) {
        int index = guiState.removeCardFromHand(card);
        if (index != -1) {
            CardController placeholder = ElementFactory.getCard();
            Platform.runLater(() -> handSlot.getChildren().set(index, placeholder.getRoot()));
        }
    }

    public void setPersonalObjective(ObjectiveCard personalObjective) {
        CardController cardController = ElementFactory.getCard();
        cardController.setCardAndShowSide(personalObjective, SideType.FRONT);
        Platform.runLater(() -> {
            personalObjectiveSlot.getChildren().clear();
            personalObjectiveSlot.getChildren().add(cardController.getRoot());
        });
    }

    public void setCenterContent(Node node) {
        Platform.runLater(() -> {
            centerContentPane.getChildren().clear();
            centerContentPane.getChildren().add(node);
        });
    }

    public void setPlayArea(String username, List<Position> ignored, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        PlayAreaGridController playAreaGridController = ElementFactory.getPlayAreaGrid();
        BoardCard starterCard = field.get(new Position(0, 0));
        SideType starterSide = activeSides.get(starterCard);
        CardController cardController = ElementFactory.getCard();
        cardController.setCardAndShowSide(starterCard, starterSide);
        playAreaGridController.setCenterCard(cardController, legalPositions, illegalPositions);

        guiState.setPlayArea(username, playAreaGridController);

        InfoTableController infoTableController = ElementFactory.getInfoTable();
        guiState.setInfoTable(username, infoTableController);

        if (username.equals(guiState.getUsername())) {
            centerContentPane.getChildren().add(playAreaGridController.getRoot());
            infoTableSlot.getChildren().add(infoTableController.getRoot());
        }

        updateInfoTable(username, resourceCounts, objectCounts);
    }

    public void playCard(String username, BoardCard card, SideType side, Position pos, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        PlayAreaGridController playAreaGridController = guiState.getPlayArea(username);

        CardController cardController = ElementFactory.getCard();
        cardController.setCardAndShowSide(card, side);

        playAreaGridController.putCard(cardController, pos, addedLegalPositions, removedLegalPositions);

        updateInfoTable(username, resourceCounts, objectCounts);
    }

    public void setDeckTopType(PlayableCardType whichDeck, ResourceType deckTopType) {
        CardController cardBack = ElementFactory.getCardBackOnly(whichDeck, deckTopType);

        if (cardBack == null) throw new RuntimeException("Unknown Card Type: " + whichDeck);

        cardBack.setShadowColor(deckTopType);
        Parent cardPane = cardBack.getRoot();

        switch (whichDeck) {
            case RESOURCE -> resourceDeckSlot.getChildren().set(0, cardPane);
            case GOLD -> goldDeckSlot.getChildren().set(0, cardPane);
        }
    }

    public void setCommonCards(PlayableCard[] resourceCards, PlayableCard[] goldCards) {
        for (int i = 0; i<resourceCards.length; i++) {
            CardController cardController = ElementFactory.getCard();
            if (resourceCards[i] != null) {
                PlayableCard card = resourceCards[i];
                cardController.setCardAndShowSide(card, SideType.FRONT);
                cardController.setShadowColor(card.getType());
            }
            int finalI = i;
            Platform.runLater(() -> commonResourceCardsSlot.getChildren().set(finalI, cardController.getRoot()));
        }
        for (int i = 0; i<goldCards.length; i++) {
            CardController cardController = ElementFactory.getCard();
            if (goldCards[i] != null) {
                PlayableCard card = goldCards[i];
                cardController.setCardAndShowSide(card, SideType.FRONT);
                cardController.setShadowColor(card.getType());
            }
            int finalI = i;
            Platform.runLater(() -> commonGoldCardsSlot.getChildren().set(finalI, cardController.getRoot()));
        }
    }

    public void setPlayerColor(String username, PlayerColor color) {
        guiState.setPlayerColor(username, color);
        if (username.equals(guiState.getUsername())) {
            setPegColor(color);
        }
        guiState.setGamePoints(username, 0);
        pointsBoardController.addPegInSlot(0, color);
    }

    public void setGamePoints(String username, int gamePoints) {
        int tempGamePoints = gamePoints;
        if (tempGamePoints > 29) {
            tempGamePoints = 29;
        }

        PlayerColor color = guiState.getPlayerColor(username);
        pointsBoardController.removePegInSlot(guiState.getGamePoints(username), color);
        guiState.setGamePoints(username, gamePoints);
        pointsBoardController.addPegInSlot(tempGamePoints, color);
    }

    public void setObjectivePoints(String username, int objectivePoints) {
        guiState.setObjectivePoints(username, objectivePoints);
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
}
