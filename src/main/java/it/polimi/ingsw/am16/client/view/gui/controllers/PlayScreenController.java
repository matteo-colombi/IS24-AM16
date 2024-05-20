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
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
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
    private VBox infoTable;
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

    private ServerInterface serverInterface;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GUIState guiState = CodexGUI.getGUI().getGuiState();
        guiState.setPlayScreenController(this);
        guiState.setCurrentController(this);
        guiState.setChatListener(this);

        CodexGUI.getGUI().getStage().getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
            if (evt.getPickResult().getIntersectedNode() != chatFilterButton && !inHierarchy(evt.getPickResult().getIntersectedNode(), chatFilters)) {
                chatFilters.setVisible(false);
            }
        });

        this.serverInterface = CodexGUI.getGUI().getServerInterface();

        chatBox.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                sendChatMessage();
            }
        });

        receiveMessages(guiState.getChatMessages());

        setCommonObjectives(new ObjectiveCard[]{CardRegistry.getRegistry().getObjectiveCardFromName("objective_pattern_5"), CardRegistry.getRegistry().getObjectiveCardFromName("objective_object_1")});
        setHand(List.of(CardRegistry.getRegistry().getResourceCardFromName("resource_animal_9"), CardRegistry.getRegistry().getGoldCardFromName("gold_insect_10")));
        setPersonalObjective(CardRegistry.getRegistry().getObjectiveCardFromName("objective_resources_3"));
        setPegColor(PlayerColor.RED);
        setIsStartingPlayer();

        PlayAreaGridController playAreaGridController = ElementFactory.getPlayAreaGrid();
        CardController testCard = ElementFactory.getCard();
        testCard.setCardAndShowSide(CardRegistry.getRegistry().getStarterCardFromName("starter_6"), SideType.FRONT);
        playAreaGridController.setCenterCard(testCard);
        CardController testCard2 = ElementFactory.getCard();
        testCard2.setCardAndShowSide(CardRegistry.getRegistry().getResourceCardFromName("resource_plant_2"), SideType.FRONT);
        playAreaGridController.putCard(testCard2, new Position(1,1));
        CardController testCard3 = ElementFactory.getCard();
        testCard3.setCardAndShowSide(CardRegistry.getRegistry().getGoldCardFromName("gold_insect_3"), SideType.FRONT);
        playAreaGridController.putCard(testCard3, new Position(-1,-1));
        CardController testCard4 = ElementFactory.getCard();
        testCard4.setCardAndShowSide(CardRegistry.getRegistry().getGoldCardFromName("gold_insect_3"), SideType.FRONT);
        playAreaGridController.putCard(testCard4, new Position(0,-2));
        CardController testCard5 = ElementFactory.getCard();
        testCard5.setCardAndShowSide(CardRegistry.getRegistry().getGoldCardFromName("gold_insect_3"), SideType.FRONT);
        playAreaGridController.putCard(testCard5, new Position(-1,-3));
        CardController testCard6 = ElementFactory.getCard();
        testCard6.setCardAndShowSide(CardRegistry.getRegistry().getGoldCardFromName("gold_animal_1"), SideType.FRONT);
        playAreaGridController.putCard(testCard6, new Position(1,-3));


        setCenterContent(playAreaGridController.getRoot());
    }

    @Override
    public void showError(String errorMessage) {
        //TODO
    }

    public void setPegColor(PlayerColor color) {
        PegController pegController  = ElementFactory.getPeg();
        pegController.setPegColor(color);
        Platform.runLater(() -> {
            pegContainer.getChildren().clear();
            pegContainer.getChildren().add(pegController.getRoot());
        });
    }

    public void setIsStartingPlayer() {
        PegController pegController = ElementFactory.getPeg();
        pegController.setPegColor(null);
        Platform.runLater(() -> {
            pegContainer.getChildren().addFirst(pegController.getRoot());
        });
    }

    public void updateInfoTable(Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        Platform.runLater(() -> {
            for(Map.Entry<ResourceType, Integer> entry : resourceCounts.entrySet()) {
                ((Text) infoTable.lookup("#" + entry.getKey().name().toLowerCase() + "Amount")).setText(String.format("%02d", entry.getValue()));
            }

            for(Map.Entry<ObjectType, Integer> entry : objectCounts.entrySet()) {
                ((Text) infoTable.lookup("#" + entry.getKey().name().toLowerCase() + "Amount")).setText(String.format("%02d", entry.getValue()));
            }
        });
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
        for(int i = 0; i<hand.size(); i++) {
            CardController cardController = ElementFactory.getCard();
            cardController.setCard(hand.get(i));
            cardController.showSide(SideType.FRONT);
            cardController.setDraggable(true);
            final int finalI = i;
            Platform.runLater(() -> handSlot.getChildren().set(finalI, cardController.getRoot()));
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
