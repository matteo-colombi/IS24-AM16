package it.polimi.ingsw.am16.client.view.gui.util;

import it.polimi.ingsw.am16.client.view.gui.ChatListener;
import it.polimi.ingsw.am16.client.view.gui.controllers.*;
import it.polimi.ingsw.am16.common.model.cards.Card;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.scene.input.DataFormat;
import org.controlsfx.control.spreadsheet.Grid;

import java.util.*;

public class GUIState {

    public static final DataFormat droppedOnPos = new DataFormat("cardDroppedOnPos");
    public static final DataFormat draggedCard = new DataFormat("draggedCard");

    private ServerInterface serverInterface;

    private String username;
    private String gameId;

    private final Map<Position, Card> field;

    private final Map<Position, GridFillerController> gridFillers;

    private final Set<Position> placeablePositions;

    private final List<PlayableCard> hand;

    private final List<String> playerUsernames;
    private final List<String> turnOrder;

    private final Map<String, Integer> gamePoints;
    private final Map<String, Integer> objectivePoints;

    private final Map<String, PlayerColor> playerColors;

    private ScreenController currentController;
    private WelcomeScreenController welcomeScreenController;
    private CreateScreenController createScreenController;
    private GamesScreenController gamesScreenController;
    private PlayScreenController playScreenController;

    private ChatListener chatListener;

    private final List<ChatMessage> chatMessages;

    public GUIState() {
        chatMessages = new ArrayList<>();
        playerUsernames = new ArrayList<>();
        turnOrder = new ArrayList<>();
        gridFillers = new HashMap<>();
        hand = new ArrayList<>();
        gamePoints = new HashMap<>();
        objectivePoints = new HashMap<>();
        field = new HashMap<>();
        placeablePositions = new HashSet<>();
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
        synchronized (field) {
            field.clear();
        }
        synchronized (gridFillers) {
            gridFillers.clear();
        }
        synchronized (placeablePositions) {
            placeablePositions.clear();
        }
        synchronized (this) {
            username = null;
            gameId = null;
            currentController = null;
            gamesScreenController = null;
            welcomeScreenController = null;
            playScreenController = null;
            chatListener = null;
        }
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

    public synchronized CreateScreenController getCreateScreenController() {
        return createScreenController;
    }

    public synchronized void setCreateScreenController(CreateScreenController createScreenController) {
        this.createScreenController = createScreenController;
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

    public void receiveNewMessage(ChatMessage message) {
        synchronized (chatMessages) {
            chatMessages.add(message);
            if (chatListener != null) {
                chatListener.receiveMessage(message);
            }
        }
    }

    public void receiveNewMessages(List<ChatMessage> messages) {
        synchronized (chatMessages) {
            chatMessages.addAll(messages);
            if (chatListener != null) {
                chatListener.receiveMessages(messages);
            }
        }
    }

    public void setChatListener(ChatListener l) {
        synchronized (chatMessages) {
            chatListener = l;
        }
    }

    public Card getField(Position position) {
        synchronized (field) {
            return field.get(position);
        }
    }

    public void setField(Map<Position, Card> field) {
        synchronized (this.field) {
            this.field.clear();
            this.field.putAll(field);
        }
    }

    public void putCardInField(Position position, Card card) {
        synchronized (field) {
            field.put(position, card);
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

    public Set<Position> getPlaceablePositions() {
        synchronized (placeablePositions) {
            return Set.copyOf(placeablePositions);
        }
    }

    public void setPlaceablePositions(Set<Position> placeablePositions) {
        synchronized (this.placeablePositions) {
            this.placeablePositions.clear();
            this.placeablePositions.addAll(placeablePositions);
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
