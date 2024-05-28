package it.polimi.ingsw.am16.client.view.gui.util;

import it.polimi.ingsw.am16.client.view.gui.controllers.elements.GridFillerController;
import it.polimi.ingsw.am16.client.view.gui.controllers.elements.HandController;
import it.polimi.ingsw.am16.client.view.gui.controllers.elements.InfoTableController;
import it.polimi.ingsw.am16.client.view.gui.controllers.elements.PlayAreaGridController;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.RestrictedCard;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.scene.input.DataFormat;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GUIState {

    public static final DataFormat droppedOnPos = new DataFormat("cardDroppedOnPos");
    public static final DataFormat draggedCard = new DataFormat("draggedCard");

    private ServerInterface serverInterface;

    private String username;
    private String gameId;
    private String activePlayer;
    private AtomicInteger numPlayers;

    private GameState gameState;

    private final Map<String, PlayAreaGridController> playAreas;
    private final Map<String, InfoTableController> infoTables;

    private final Map<String, HandController> otherHands;

    private HandController hand;

    private final List<String> playerUsernames;
    private final List<String> turnOrder;

    private final Map<String, Integer> gamePoints;
    private final Map<String, Integer> objectivePoints;

    private final Map<String, PlayerColor> playerColors;

    private final List<ChatMessage> chatMessages;

    private final AtomicBoolean dontDraw;

    public GUIState() {
        chatMessages = new ArrayList<>();
        playerUsernames = new ArrayList<>();
        turnOrder = new ArrayList<>();
        gamePoints = new HashMap<>();
        objectivePoints = new HashMap<>();
        playAreas = new HashMap<>();
        infoTables = new HashMap<>();
        otherHands = new HashMap<>();
        playerColors = new HashMap<>();
        dontDraw = new AtomicBoolean(false);
        numPlayers = new AtomicInteger(0);
    }

    public void clear() {
        synchronized (chatMessages) {
            chatMessages.clear();
        }
        synchronized (playerUsernames) {
            playerUsernames.clear();
        }
        synchronized (turnOrder) {
            turnOrder.clear();
        }
        synchronized (gamePoints) {
            gamePoints.clear();
        }
        synchronized (objectivePoints) {
            objectivePoints.clear();
        }
        synchronized (playAreas) {
            playAreas.clear();
        }
        synchronized (infoTables) {
            infoTables.clear();
        }
        synchronized (otherHands) {
            otherHands.clear();
        }
        synchronized (this) {
            username = null;
            gameId = null;
            numPlayers.set(0);
            activePlayer = null;
            gameState = null;
            hand = null;
            dontDraw.set(false);
        }
    }

    public synchronized String getGameId() {
        return gameId;
    }

    public synchronized void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public synchronized void setUsername(String username) {
        this.username = username;
    }

    public synchronized String getUsername() {
        return username;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers.set(numPlayers);
    }

    public int getNumPlayers() {
        return this.numPlayers.get();
    }

    public synchronized void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }

    public synchronized String getActivePlayer() {
        return activePlayer;
    }

    public synchronized GameState getGameState() {
        return gameState;
    }

    public synchronized void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public synchronized ServerInterface getServerInterface() {
        return serverInterface;
    }

    public synchronized void setServerInterface(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    public List<ChatMessage> getChatMessages() {
        synchronized (chatMessages) {
            return List.copyOf(this.chatMessages);
        }
    }

    public void addNewMessages(List<ChatMessage> messages) {
        synchronized (chatMessages) {
            chatMessages.addAll(messages);
        }
    }

    public void setPlayArea(String username, PlayAreaGridController playAreaGridController) {
        synchronized (playAreas) {
            playAreas.put(username, playAreaGridController);
        }
    }

    public PlayAreaGridController getPlayArea(String username) {
        synchronized (playAreas) {
            return playAreas.get(username);
        }
    }

    public void setInfoTable(String username, InfoTableController infoTableController) {
        synchronized (infoTables) {
            infoTables.put(username, infoTableController);
        }
    }

    public InfoTableController getInfoTable(String username) {
        synchronized (infoTables) {
            return infoTables.get(username);
        }
    }

    public void setOtherHand(String username, HandController hand) {
        synchronized (otherHands) {
            otherHands.put(username, hand);
        }
    }

    public HandController getOtherHand(String username) {
        synchronized (otherHands) {
            return otherHands.get(username);
        }
    }

    public void addPlayer(String username) {
        synchronized (playerUsernames) {
            playerUsernames.add(username);
        }
    }

    public void removePlayer(String whoDisconnected) {
        synchronized (playerUsernames) {
            playerUsernames.remove(whoDisconnected);
        }
    }

    public void setTurnOrder(List<String> turnOrder) {
        synchronized (this.turnOrder) {
            this.turnOrder.clear();
            this.turnOrder.addAll(turnOrder);
        }
    }

    public List<String> getPlayerUsernames() {
        synchronized (playerUsernames) {
            return List.copyOf(playerUsernames);
        }
    }

    public List<String> getTurnOrder() {
        synchronized (turnOrder) {
            return List.copyOf(turnOrder);
        }
    }

    public void setHand(HandController hand) {
        synchronized (this) {
            this.hand = hand;
        }
    }

    public HandController getHand() {
        synchronized (this) {
            return hand;
        }
    }

    public void setGamePoints(String username, int gamePoints) {
        synchronized (this.gamePoints) {
            this.gamePoints.put(username, gamePoints);
        }
    }

    public int getGamePoints(String username) {
        synchronized (gamePoints) {
            return gamePoints.getOrDefault(username, 0);
        }
    }

    public void setObjectivePoints(String username, int objectivePoints) {
        synchronized (this.objectivePoints) {
            this.objectivePoints.put(username, objectivePoints);
        }
    }

    public int getObjectivePoints(String username) {
        synchronized (objectivePoints) {
            return objectivePoints.getOrDefault(username, 0);
        }
    }

    public void setPlayerColor(String username, PlayerColor color) {
        synchronized (playerColors) {
            playerColors.put(username, color);
        }
    }

    public PlayerColor getPlayerColor(String username) {
        synchronized (playerColors) {
            return playerColors.get(username);
        }
    }

    public void setDontDraw(boolean dontDraw) {
        this.dontDraw.set(dontDraw);
    }

    public boolean getDontDraw() {
        return this.dontDraw.get();
    }
}
