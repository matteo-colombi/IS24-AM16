package it.polimi.ingsw.am16.client.view.gui.controllers.screens;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.events.GUIEventTypes;
import it.polimi.ingsw.am16.client.view.gui.util.GUIState;
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

/**
 * Controller class for the lobby screen.
 */
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

    private ServerInterface serverInterface;

    /**
     * Initializes the lobby screen, registering events on the screen's buttons and to listen to incoming information from the server.
     */
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
     * Sets the game ID to display on the screen.
     * @param gameId The game ID.
     */
    private void setGameId(String gameId) {
        gameIdField.setText(gameId);
    }

    /**
     * Sets the currently connected players so that their usernames can be shown to the player.
     * @param usernames The list of currently connected players.
     */
    private void setPlayers(List<String> usernames) {
        playersBox.getChildren().clear();
        for (String username : usernames) {
            addPlayer(username);
        }

        updateMotd();
    }

    /**
     * Adds a new player to the lobby and shows their username to the player.
     * @param username The username of the new player.
     */
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

    /**
     * Removes the player with the given username and removes their name from the screen.
     * @param whoDisconnected The username of the player who disconnected from the lobby.
     */
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

    /**
     * Updates the "Message Of The Day" in the lobby, showing the number of players missing for the game to start.
     * If the number of expected players is reached, shows a message that tells the player that the game is starting shortly instead.
     */
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

    /**
     * Leaves the lobby, communicating this to the server.
     * Switches back to the welcome screen.
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
     * Adds the new messages to the chat.
     * @param messages The received messages.
     */
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

    /**
     * Sends a new chat message to the server.
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
            System.err.println("Error sending chat message: " + e.getMessage());
        }

        chatBox.clear();
    }

    /**
     * Shows the chat filter box, with which users can decide whether to send public or private chat messages.
     */
    @FXML
    public void showChatFilter(ActionEvent ignored) {
        chatFilters.setVisible(!chatFilters.isVisible());
    }

    /**
     * Registers event handlers for this screen's buttons, or events fired by incoming information from the server.
     */
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

    /**
     * Checks whether the given node contains another node between its descendants.
     * @param node The "father" node.
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
