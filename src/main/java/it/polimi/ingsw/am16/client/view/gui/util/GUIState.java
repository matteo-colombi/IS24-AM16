package it.polimi.ingsw.am16.client.view.gui.util;

import it.polimi.ingsw.am16.client.view.gui.controllers.*;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.scene.input.DataFormat;

import java.util.*;

public class GUIState {

    public static final DataFormat droppedOnPos = new DataFormat("cardDroppedOnPos");
    public static final DataFormat draggedCard = new DataFormat("draggedCard");

    private ServerInterface serverInterface;

    private String username;
    private String gameId;

    private final Map<String, PlayAreaGridController> playAreas;
    private final Map<String, InfoTableController> infoTables;

    private final Map<Position, GridFillerController> gridFillers;

    private final List<PlayableCard> hand;

    private final List<String> playerUsernames;
    private final List<String> turnOrder;

    private final Map<String, Integer> gamePoints;
    private final Map<String, Integer> objectivePoints;

    private final Map<String, PlayerColor> playerColors;

    private final List<ChatMessage> chatMessages;

    public GUIState() {
        chatMessages = new ArrayList<>();
        playerUsernames = new ArrayList<>();
        turnOrder = new ArrayList<>();
        gridFillers = new HashMap<>();
        hand = new ArrayList<>();
        gamePoints = new HashMap<>();
        objectivePoints = new HashMap<>();
        playAreas = new HashMap<>();
        infoTables = new HashMap<>();
        playerColors = new HashMap<>();
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
        synchronized (gridFillers) {
            gridFillers.clear();
        }
        synchronized (this) {
            username = null;
            gameId = null;
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

    public GridFillerController getGridFillerInPos(Position position) {
        synchronized (gridFillers) {
            return gridFillers.get(position);
        }
    }

    public void putGridFillerInPos(Position position, GridFillerController gridFiller) {
        synchronized (gridFillers) {
            gridFillers.put(position, gridFiller);
        }
    }

    public void removeGridFillerInPos(Position position) {
        synchronized (gridFillers) {
            gridFillers.remove(position);
        }
    }

    public void setGridFillers(Map<Position, GridFillerController> gridFillers) {
        synchronized (this.gridFillers) {
            this.gridFillers.clear();
            this.gridFillers.putAll(gridFillers);
        }
    }

    public void setPlayerUsernames(List<String> playerUsernames) {
        synchronized (this.playerUsernames) {
            this.playerUsernames.clear();
            this.playerUsernames.addAll(playerUsernames);
        }
    }

    public void addPlayer(String username) {
        synchronized (playerUsernames) {
            playerUsernames.add(username);
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

    public void setHand(List<PlayableCard> hand) {
        synchronized (this.hand) {
            this.hand.clear();
            this.hand.addAll(hand);
        }
    }

    public PlayableCard getCardInHand(int index) {
        synchronized (hand) {
            return hand.get(index);
        }
    }

    public void addCardToHand(PlayableCard playableCard) {
        synchronized (hand) {
            hand.add(playableCard);
        }
    }

    public int removeCardFromHand(PlayableCard playableCard) {
        int index;
        synchronized (hand) {
            index = hand.indexOf(playableCard);
            hand.remove(playableCard);
        }
        return index;
    }

    public int getHandSize() {
        synchronized (hand) {
            return hand.size();
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
}
