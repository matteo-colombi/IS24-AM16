package it.polimi.ingsw.am16.server.rmi;

import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.server.ServerInterface;
import it.polimi.ingsw.am16.server.controller.GameController;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of the {@link ServerInterface} for RMI connections.
 */
public class RMIServerImplementation extends UnicastRemoteObject implements ServerInterface {

    @Serial
    private static final long serialVersionUID = -549123884082532918L;

    private final RemoteClientInterface clientInterface;
    private final LobbyManager lobbyManager;

    private final Timer pingTimer;
    private final AtomicBoolean ponged;

    private GameController gameController;
    private String username;

    public RMIServerImplementation(RemoteClientInterface clientInterface, LobbyManager lobbyManager) throws RemoteException {
        this.clientInterface = clientInterface;
        this.lobbyManager = lobbyManager;
        this.pingTimer = new Timer();
        this.ponged = new AtomicBoolean(true);
        this.gameController = null;
        this.username = null;

        pingRoutine();
    }

    /**
     * Pings the client to check if the connection is still alive. A client is disconnected
     * if it doesn't answer to the ping within 10 seconds.
     */
    private void pingRoutine() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (ponged.get()) {
                    ponged.set(false);
                    try {
                        clientInterface.ping();
                    } catch (RemoteException ignored) {
//                        System.err.println(ignored.getMessage());
                    }
                } else {
                    System.out.println("Client didn't answer the last ping.");
                    pingTimer.cancel();

                    if (gameController != null) {
                        gameController.disconnect(username);

                    }
                    username = null;
                    gameController = null;
                }
            }
        };

        pingTimer.schedule(task, 1000, 10000);
    }

    /**
     * DOCME
     *
     * @throws RemoteException
     */
    @Override
    public void getGames() throws RemoteException {
        Set<String> gameIds = lobbyManager.getGameIds();
        Map<String, Integer> currentPlayers = new HashMap<>();
        Map<String, Integer> maxPlayers = new HashMap<>();

        for (String gameId : gameIds) {
            GameController game = lobbyManager.getGame(gameId);

            currentPlayers.put(gameId, game.getCurrentPlayerCount());
            maxPlayers.put(gameId, game.getNumPlayers());
        }

        clientInterface.notifyGames(gameIds, currentPlayers, maxPlayers);
    }

    @Override
    public void createGame(String username, int numPlayers) throws RemoteException {
        if (gameController != null) {
            clientInterface.promptError("You are already in a game!");
            return;
        }

        String gameId;

        try {
            gameId = lobbyManager.createGame(numPlayers);
        } catch (IllegalArgumentException e) {
            clientInterface.promptError(e.getMessage());
            return;
        }

        gameController = lobbyManager.getGame(gameId);
        try {
            gameController.createPlayer(username);
        } catch (UnexpectedActionException e) {
            clientInterface.promptError("Couldn't join game: " + e.getMessage());
            gameController = null;
            return;
        }

        try {
            gameController.joinPlayer(username, clientInterface);
            this.username = username;
        } catch (UnexpectedActionException e) {
            System.err.println("Unexpected error: " + e.getMessage());
            this.username = null;
            gameController = null;
        }
    }

    /**
     * Lets the client join a game with the given id.
     *
     * @param gameId   The id of the game to join.
     * @param username The username of the player who wants to join the game.
     * @throws RemoteException If an error occurs during the RMI communication.
     */
    @Override
    public void joinGame(String gameId, String username) throws RemoteException {
        if (gameController != null) {
            clientInterface.promptError("You are already in a game.");
            return;
        }

        gameController = lobbyManager.getGame(gameId);
        if (gameController == null) {
            clientInterface.promptError("No game with the given id.");
            return;
        }

        if (!gameController.isRejoiningAfterCrash()) {
            try {
                gameController.createPlayer(username);
            } catch (UnexpectedActionException e) {
                clientInterface.promptError("Couldn't join game: " + e.getMessage());
                gameController = null;
                this.username = null;
                return;
            }
        }

        try {
            gameController.joinPlayer(username, clientInterface);
            this.username = username;
        } catch (UnexpectedActionException e) {
            clientInterface.promptError("User already rejoined the game.");
            gameController = null;
            this.username = null;
        }
    }

    /**
     * Sets the starter card for the player.
     *
     * @param side The side of the card to set as starter.
     * @throws RemoteException If an error occurs during the RMI communication.
     */
    @Override
    public void setStarterCard(SideType side) throws RemoteException {
        if (gameController != null) {
            gameController.setStarterCard(username, side);
        }
    }

    /**
     * Sets the in-game color for the player.
     *
     * @param color The color to set for the player.
     * @throws RemoteException If an error occurs during the RMI communication.
     */
    @Override
    public void setColor(PlayerColor color) throws RemoteException {
        if (gameController != null) {
            gameController.setPlayerColor(username, color);
        }
    }

    /**
     * Sets the personal objective card for the player.
     *
     * @param objectiveCard The objective card to set for the player.
     * @throws RemoteException If an error occurs during the RMI communication.
     */
    @Override
    public void setPersonalObjective(ObjectiveCard objectiveCard) throws RemoteException {
        if (gameController != null) {
            gameController.setPlayerObjective(username, objectiveCard);
        }
    }

    /**
     * Plays a card on the given side and position.
     *
     * @param playedCard The card to play.
     * @param side       The side to play the card on.
     * @param pos        The position to play the card at.
     * @throws RemoteException If an error occurs during the RMI communication.
     */
    @Override
    public void playCard(PlayableCard playedCard, SideType side, Position pos) throws RemoteException {
        if (gameController != null) {
            gameController.placeCard(username, playedCard, side, pos);
        }
    }

    /**
     * Draws a card from the given deck.
     *
     * @param drawType The deck to draw the card from.
     * @throws RemoteException If an error occurs during the RMI communication.
     */
    @Override
    public void drawCard(DrawType drawType) throws RemoteException {
        if (gameController != null) {
            gameController.drawCard(username, drawType);
        }
    }

    /**
     * Sends a chat message to all the players in the game.
     *
     * @param text The text of the message.
     * @throws RemoteException If an error occurs during the RMI communication.
     */
    @Override
    public void sendChatMessage(String text) throws RemoteException {
        if (gameController != null) {
            gameController.sendChatMessage(username, text);
        }
    }

    /**
     * Sends a chat message to the given player.
     *
     * @param text             The text of the message.
     * @param receiverUsername The username of the player to send the message to.
     * @throws RemoteException If an error occurs during the RMI communication.
     */
    @Override
    public void sendChatMessage(String text, String receiverUsername) throws RemoteException {
        if (gameController != null) {
            gameController.sendChatMessage(username, text, Set.of(receiverUsername));
        }
    }

    /**
     * Disconnects the client from the server.
     *
     * @throws RemoteException If an error occurs during the RMI communication.
     */
    @Override
    public void disconnect() throws RemoteException {
        if (gameController != null && username != null) {
            gameController.disconnect(username);
        }
        username = null;
        gameController = null;
        pingTimer.cancel();
        System.out.println("RMI client disconnected.");
    }

    /**
     * Also disconnects the client from the server.
     *
     * @throws RemoteException If an error occurs during the RMI communication.
     */
    @Override
    public void leaveGame() throws RemoteException {
        if (gameController != null && username != null) {
            gameController.disconnect(username);
        }
        username = null;
        gameController = null;
    }

    /**
     * Pings the client to check if the connection is still alive.
     *
     * @throws RemoteException If an error occurs during the RMI communication.
     */
    @Override
    public void pong() throws RemoteException {
        ponged.set(true);
    }
}
