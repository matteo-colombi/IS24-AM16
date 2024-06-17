package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.controllers.elements.ErrorController;
import it.polimi.ingsw.am16.client.view.gui.events.ErrorEvent;
import it.polimi.ingsw.am16.client.view.gui.events.GUIEventTypes;
import it.polimi.ingsw.am16.client.view.gui.util.ElementFactory;
import it.polimi.ingsw.am16.client.view.gui.util.ErrorFactory;
import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
import it.polimi.ingsw.am16.client.view.gui.util.guiErrors.GUIError;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.rmi.RemoteException;
import java.util.List;

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
    private RadioButton everyoneFilter;
    @FXML
    private Text motdText;

    private GUIState guiState;

    private ErrorController errorController;
    private ErrorFactory errorFactory;

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

    /**
     * Sets the game ID.
     *
     * @param gameId The game ID.
     */
    private void setGameId(String gameId) {
        gameIdField.setText(gameId);
    }


    private void setPlayers(List<String> usernames) {
        playersBox.getChildren().clear();
        for (String username : usernames) {
            addPlayer(username);
        }

        updateMotd();
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

        updateMotd();
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

        updateMotd();
    }

    private void updateMotd() {
        String motd;
        if (guiState.getNumPlayers() == guiState.getPlayerUsernames().size()) {
            motd = "Everyone is here! The game will start shortly...";
        } else {
            int missing = guiState.getNumPlayers()-guiState.getPlayerUsernames().size();
            motd = String.format("Almost ready! Just %d more player%s...", missing, missing != 1 ? "s" : "");
        }
        motdText.setText(motd);
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
        for (ChatMessage message : messages) {
            Text messageText = new Text();

            messageText.setText(message.toString());
            messageText.setWrappingWidth(chatMessages.getWidth() - chatMessages.getPadding().getRight());

            chatMessages.getChildren().addLast(messageText);
            chatScrollPane.setVvalue(1);
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

        chatBox.clear();
    }


    /**
     * This method sets up and shows the error popup whenever an error occurs
     * (and consequently, an error event is fired).
     *
     * @param errorEvent the fired error event
     */
    public void showError(ErrorEvent errorEvent) {
        errorController = ElementFactory.getErrorPopup();
        GUIError error = ErrorFactory.getError(errorEvent.getErrorType());
        error.configurePopup(errorController);
        errorController.setErrorText(errorEvent.getErrorMsg());
        error.show(root);
    }

    @FXML
    public void showChatFilter(ActionEvent ignored) {
        chatFilters.setVisible(!chatFilters.isVisible());
    }

    private void registerEvents() {
        root.addEventFilter(GUIEventTypes.SET_PLAYERS_EVENT, e -> setPlayers(e.getUsernames()));

        root.addEventFilter(GUIEventTypes.ADD_PLAYER_EVENT, e -> addPlayer(e.getUsername()));

        root.addEventFilter(GUIEventTypes.REJOIN_INFORMATION_START_EVENT, e -> {
            CodexGUI.getGUI().switchToPlayScreen();
        });

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

        leaveButton.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                leave();
                keyEvent.consume();
            }
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
