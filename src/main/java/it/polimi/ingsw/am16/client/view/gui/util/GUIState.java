package it.polimi.ingsw.am16.client.view.gui.util;

import it.polimi.ingsw.am16.client.view.gui.ChatListener;
import it.polimi.ingsw.am16.client.view.gui.controllers.GamesScreenController;
import it.polimi.ingsw.am16.client.view.gui.controllers.PlayScreenController;
import it.polimi.ingsw.am16.client.view.gui.controllers.ScreenController;
import it.polimi.ingsw.am16.client.view.gui.controllers.WelcomeScreenController;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.scene.input.DataFormat;

import java.util.ArrayList;
import java.util.List;

public class GUIState {

    public static final DataFormat droppedOnPos = new DataFormat("cardDroppedOnPos");
    public static final DataFormat draggedCard = new DataFormat("draggedCard");

    private ServerInterface serverInterface;

    private String username;
    private String gameId;

    private ScreenController currentController;
    private WelcomeScreenController welcomeScreenController;
    private GamesScreenController gamesScreenController;
    private PlayScreenController playScreenController;

    private ChatListener chatListener;

    private List<ChatMessage> chatMessages;

    public GUIState() {
        chatMessages = new ArrayList<>();
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public synchronized void receiveNewMessage(ChatMessage message) {
        chatMessages.add(message);
        if (chatListener != null) {
            chatListener.receiveMessage(message);
        }
    }

    public synchronized void receiveNewMessages(List<ChatMessage> messages) {
        chatMessages.addAll(messages);
        if (chatListener != null) {
            chatListener.receiveMessages(messages);
        }
    }

    public synchronized void setChatListener(ChatListener l) {
        chatListener = l;
    }

    public synchronized PlayScreenController getPlayScreenController() {
        return playScreenController;
    }

    public synchronized void setPlayScreenController(PlayScreenController playScreenController) {
        this.playScreenController = playScreenController;
    }

    public synchronized String getGameId() {
        return gameId;
    }

    public synchronized void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public synchronized ScreenController getCurrentController() {
        return currentController;
    }

    public synchronized void setCurrentController(ScreenController currentController) {
        this.currentController = currentController;
    }

    public synchronized WelcomeScreenController getWelcomeScreenController() {
        return welcomeScreenController;
    }

    public synchronized void setWelcomeScreenController(WelcomeScreenController welcomeScreenController) {
        this.welcomeScreenController = welcomeScreenController;
    }

    public synchronized GamesScreenController getGamesScreenController() {
        return gamesScreenController;
    }

    public synchronized void setGamesScreenController(GamesScreenController gamesScreenController) {
        this.gamesScreenController = gamesScreenController;
    }

    public synchronized void setUsername(String username) {
        this.username = username;
    }

    public synchronized String getUsername() {
        return username;
    }

    public ServerInterface getServerInterface() {
        return serverInterface;
    }

    public void setServerInterface(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
    }
}
