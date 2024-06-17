package it.polimi.ingsw.am16.client.view.gui.util;

import it.polimi.ingsw.am16.client.view.gui.controllers.elements.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.server.ServerInterface;
import javafx.scene.input.DataFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Bean-like class that stores important information about the state of a GUI.
 * This class is thread safe.
 */
public class GUIState {

    public static final DataFormat droppedOnPos = new DataFormat("cardDroppedOnPos");
    public static final DataFormat draggedCard = new DataFormat("draggedCard");

    private ServerInterface serverInterface;

    private String username;
    private String gameId;
    private String activePlayer;
    private final AtomicInteger numPlayers;

    private final Map<String, PlayAreaGridController> playAreas;
    private final Map<String, InfoTableController> infoTables;

    private CardController personalObjective;

    private final Map<String, HandController> otherHands;

    private HandController hand;

    private final List<String> playerUsernames;
    private final List<String> turnOrder;

    private final Map<String, Integer> gamePoints;
    private final Map<String, Integer> objectivePoints;

    private final Map<String, PlayerColor> playerColors;
    private final Map<String, PegController> playerPegs;

    private final List<ChatMessage> chatMessages;

    private final AtomicBoolean dontDraw;

    /**
     * Creates and initializes a new GUIState.
     */
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
        playerPegs = new HashMap<>();
        dontDraw = new AtomicBoolean(false);
        numPlayers = new AtomicInteger(0);
    }

    /**
     * Clears this GUIState of all its contents. Essentially equivalent to creating a new GUIState.
     */
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
        synchronized (playerColors) {
            playerColors.clear();
        }
        synchronized (playerPegs) {
            playerPegs.clear();
        }
        synchronized (this) {
            username = null;
            gameId = null;
            numPlayers.set(0);
            activePlayer = null;
            hand = null;
            dontDraw.set(false);
            personalObjective = null;
        }
    }

    /**
     * @return The current game id stored in this GUIState.
     */
    public synchronized String getGameId() {
        return gameId;
    }

    /**
     * Stores the current game's id.
     * @param gameId The current game's id.
     */
    public synchronized void setGameId(String gameId) {
        this.gameId = gameId;
    }

    /**
     * Stores the player's current username.
     * @param username The player's username.
     */
    public synchronized void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The player's current username.
     */
    public synchronized String getUsername() {
        return username;
    }

    /**
     * Stores the maximum number of players allowed in the current game.
     * @param numPlayers The maximum number of players allowed in the current game.
     */
    public void setNumPlayers(int numPlayers) {
        this.numPlayers.set(numPlayers);
    }

    /**
     * @return The maximum number of players allowed in the current game.
     */
    public int getNumPlayers() {
        return this.numPlayers.get();
    }

    /**
     * Stores the username of the player who is currently playing their turn.
     * @param activePlayer The username of the player who is currently playing their turn.
     */
    public synchronized void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     * @return The username of the player who is currently playing their turn.
     */
    public synchronized String getActivePlayer() {
        return activePlayer;
    }

    /**
     * @return The interface used by this client to communicate with the server.
     */
    public synchronized ServerInterface getServerInterface() {
        return serverInterface;
    }

    /**
     * Stores the interface used by this client to communicate with the server.
     * @param serverInterface The interface to communicate with the server.
     */
    public synchronized void setServerInterface(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    /**
     * @return A copy of the list containing all chat messages received by this client.
     */
    public List<ChatMessage> getChatMessages() {
        synchronized (chatMessages) {
            return List.copyOf(this.chatMessages);
        }
    }

    /**
     * Stores newly received chat messages ands adds them to the ones received previously.
     * @param messages The newly received messages.
     */
    public void addNewMessages(List<ChatMessage> messages) {
        synchronized (chatMessages) {
            chatMessages.addAll(messages);
        }
    }

    /**
     * Stores each player's controller for the play area.
     * @param username The username of the player whose play area is being stored.
     * @param playAreaGridController The user's play area controller.
     */
    public void setPlayArea(String username, PlayAreaGridController playAreaGridController) {
        synchronized (playAreas) {
            playAreas.put(username, playAreaGridController);
        }
    }

    /**
     * Retrieves the controller for the desired player's play area.
     * @param username The player whose play area is must be returned.
     * @return The controller for the desired player's play area.
     */
    public PlayAreaGridController getPlayArea(String username) {
        synchronized (playAreas) {
            return playAreas.get(username);
        }
    }

    /**
     * Stores each player's controller for the info table.
     * @param username The username of the player whose info table is being stored.
     * @param infoTableController The user's info table controller.
     */
    public void setInfoTable(String username, InfoTableController infoTableController) {
        synchronized (infoTables) {
            infoTables.put(username, infoTableController);
        }
    }

    /**
     * Retrieves the controller for the desired player's info table.
     * @param username The player whose info table is must be returned.
     * @return The controller for the desired player's info table.
     */
    public InfoTableController getInfoTable(String username) {
        synchronized (infoTables) {
            return infoTables.get(username);
        }
    }

    /**
     * Stores the player's personal objective.
     * @param personalObjective The player's personal objective.
     */
    public synchronized void setPersonalObjective(CardController personalObjective) {
        this.personalObjective = personalObjective;
    }

    /**
     * @return The player's personal objective.
     */
    public synchronized CardController getPersonalObjective() {
        return personalObjective;
    }

    /**
     * Stores the controller for another player's hand.
     * @param username The player whose hand is being stored.
     * @param hand The controller for the player's hand.
     */
    public void setOtherHand(String username, HandController hand) {
        synchronized (otherHands) {
            otherHands.put(username, hand);
        }
    }

    /**
     * Retrieves the controller for another player's hand.
     * @param username The username of the player whose hand must be returned.
     * @return The controller for the specified player's hand.
     */
    public HandController getOtherHand(String username) {
        synchronized (otherHands) {
            return otherHands.get(username);
        }
    }

    /**
     * Stores the username of a player who just joined the game.
     * @param username The new player's username.
     */
    public void addPlayer(String username) {
        synchronized (playerUsernames) {
            playerUsernames.add(username);
        }
    }

    /**
     * Removes a player's username from the list of current players.
     * @param whoDisconnected The username of the player who disconnected.
     */
    public void removePlayer(String whoDisconnected) {
        synchronized (playerUsernames) {
            playerUsernames.remove(whoDisconnected);
        }
    }

    /**
     * @return A list containing all the usernames of the players in the current game.
     */
    public List<String> getPlayerUsernames() {
        synchronized (playerUsernames) {
            return List.copyOf(playerUsernames);
        }
    }

    /**
     * Sets the order in which players will play in this game.
     * @param turnOrder The order in which players will play in this game. Must contain only usernames in the list returned by {@link GUIState#getPlayerUsernames()}.
     */
    public void setTurnOrder(List<String> turnOrder) {
        synchronized (this.turnOrder) {
            this.turnOrder.clear();
            this.turnOrder.addAll(turnOrder);
        }
    }

    /**
     * @return A list of the current players in this game, ordered in the order in which they will play during the game.
     */
    public List<String> getTurnOrder() {
        synchronized (turnOrder) {
            return List.copyOf(turnOrder);
        }
    }

    /**
     * Stores the controller for the player's hand.
     * @param hand The controller for the player's hand.
     */
    public void setHand(HandController hand) {
        synchronized (this) {
            this.hand = hand;
        }
    }

    /**
     * @return The controller for the player's hand.
     */
    public HandController getHand() {
        synchronized (this) {
            return hand;
        }
    }

    /**
     * Stores the amount of game points of the specified player.
     * @param username The username of the player whose points are being stored.
     * @param gamePoints The amount of game points of the specified player.
     */
    public void setGamePoints(String username, int gamePoints) {
        synchronized (this.gamePoints) {
            this.gamePoints.put(username, gamePoints);
        }
    }

    /**
     * Retrieves the number of game points of the specified player.
     * @param username The username of the player whose amount of game points is being requested.
     * @return The requested amount of game points.
     */
    public int getGamePoints(String username) {
        synchronized (gamePoints) {
            return gamePoints.getOrDefault(username, 0);
        }
    }

    /**
     * Stores the amount of objective points of the specified player.
     * @param username The username of the player whose points are being stored.
     * @param objectivePoints The amount of objective points of the specified player.
     */
    public void setObjectivePoints(String username, int objectivePoints) {
        synchronized (this.objectivePoints) {
            this.objectivePoints.put(username, objectivePoints);
        }
    }

    /**
     * Retrieves the number of objective points of the specified player.
     * @param username The username of the player whose amount of objective points is being requested.
     * @return The requested amount of objective points.
     */
    public int getObjectivePoints(String username) {
        synchronized (objectivePoints) {
            return objectivePoints.getOrDefault(username, 0);
        }
    }

    /**
     * Stores a player's assigned color.
     * @param username The username of the player.
     * @param color The color assigned to the player.
     */
    public void setPlayerColor(String username, PlayerColor color) {
        synchronized (playerColors) {
            playerColors.put(username, color);
        }
    }

    /**
     * Retrieves a player's assigned color.
     * @param username The username of the player.
     * @return The color assigned to the player.
     */
    public PlayerColor getPlayerColor(String username) {
        synchronized (playerColors) {
            return playerColors.get(username);
        }
    }

    /**
     * Stores the controller for a player's colored peg.
     * @param username The username of the player.
     * @param pegController The controller for the player's colored peg.
     */
    public void setPlayerPeg(String username, PegController pegController) {
        synchronized (playerPegs) {
            playerPegs.put(username, pegController);
        }
    }

    /**
     * Retrieves the controller for a player's colored peg.
     * @param username The username of the player.
     * @return The controller for the player's colored peg.
     */
    public PegController getPlayerPeg(String username) {
        synchronized (playerPegs) {
            return playerPegs.get(username);
        }
    }

    /**
     * Sets whether players should not draw cards.
     * @param dontDraw Whether players should not draw cards.
     */
    public void setDontDraw(boolean dontDraw) {
        this.dontDraw.set(dontDraw);
    }

    /**
     * @return Whether players should not draw cards.
     */
    public boolean getDontDraw() {
        return this.dontDraw.get();
    }
}
