package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.events.GUIEventTypes;
import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

public class LobbyScreenController {
    @FXML
    private StackPane root;
    @FXML
    private Text gameIdField;
    @FXML
    private VBox playersBox;
    @FXML
    private StackPane leaveButton;
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
    private RadioButton everyoneFilter;

    private GUIState guiState;

    private ServerInterface serverInterface;

    @FXML
    public void initialize() {
        registerEvents();

        serverInterface = CodexGUI.getGUI().getServerInterface();
        guiState = CodexGUI.getGUI().getGuiState();
        playersBox.getChildren().clear();
        chatFilterNames.getChildren().clear();

        setGameId(guiState.getGameId());
    }

    private void setGameId(String gameId) {
        gameIdField.setText(gameId);
    }

    private void setPlayers(List<String> usernames) {
        playersBox.getChildren().clear();
        for(String username : usernames) {
            addPlayer(username);
        }
    }

    private void addPlayer(String username) {
        guiState.addPlayer(username);
        Text usernameText = new Text(username);
        usernameText.setId(username);
        playersBox.getChildren().addLast(usernameText);
        if (!username.equals(guiState.getUsername())) {
            RadioButton playerChatFilter = new RadioButton(username);
            playerChatFilter.setId(username);
            playerChatFilter.setToggleGroup(chatFilterToggleGroup);
            chatFilterNames.getChildren().addLast(playerChatFilter);
        }
    }

    private void removePlayer(String whoDisconnected) {
        guiState.removePlayer(whoDisconnected);
        Text usernameText = (Text) playersBox.lookup("#" + whoDisconnected);
        playersBox.getChildren().remove(usernameText);
        RadioButton playerChatFilter = (RadioButton) chatFilterNames.lookup("#" + whoDisconnected);
        if (playerChatFilter.isSelected()) {
            everyoneFilter.setSelected(true);
        }
        chatFilterNames.getChildren().remove(playerChatFilter);
    }

    private void leave() {
        try {
            serverInterface.leaveGame();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        CodexGUI.getGUI().switchToWelcomeScreen();
    }

    private void receiveMessages(List<ChatMessage> messages) {
        guiState.addNewMessages(messages);
        for(ChatMessage message : messages) {
            Text newText = new Text();
            newText.setText(message.toString());
            Platform.runLater(() -> chatMessages.getChildren().addLast(newText));
        }
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

//        chatFilterToggleGroup.selectToggle(chatFilterToggleGroup.getToggles().getFirst());
        chatBox.clear();
    }

    @FXML
    public void showChatFilter(ActionEvent ignored) {
        chatFilters.setVisible(!chatFilters.isVisible());
    }

    private void registerEvents() {
        root.addEventFilter(GUIEventTypes.SET_PLAYERS_EVENT, e -> setPlayers(e.getUsernames()));

        root.addEventFilter(GUIEventTypes.ADD_PLAYER_EVENT, e -> addPlayer(e.getUsername()));

        root.addEventFilter(GUIEventTypes.SIGNAL_DISCONNECTION_EVENT, e -> removePlayer(e.getWhoDisconnected()));

        root.addEventFilter(GUIEventTypes.ADD_CHAT_MESSAGES_EVENT, e -> receiveMessages(e.getMessages()));

        root.addEventFilter(GUIEventTypes.SET_GAME_STATE_EVENT, e -> {
            if (e.getState() == GameState.INIT) {
                CodexGUI.getGUI().switchToPlayScreen();
            }
        });

        leaveButton.setOnMouseClicked(e -> {
            leave();
            e.consume();
        });

        CodexGUI.getGUI().getStage().getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, evt -> {
            if (evt.getPickResult().getIntersectedNode() != chatFilterButton && !inHierarchy(evt.getPickResult().getIntersectedNode(), chatFilters)) {
                chatFilters.setVisible(false);
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
}
