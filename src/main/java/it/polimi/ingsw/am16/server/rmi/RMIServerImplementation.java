package it.polimi.ingsw.am16.server.rmi;

import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.model.game.LobbyState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.ErrorType;
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

    /**
     * Constructs a new object with which the client can communicate through the RMI protocol.
     * @param clientInterface The interface of the client with which the server can respond and send data to the client.
     * @param lobbyManager The lobby manager that contains information about all active games on this server.
     * @throws RemoteException Thrown if an error occurs while creating the remote object.
     */
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
                    } catch (RemoteException ignored) {}
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

    @Override
    public void getGames() throws RemoteException {
        Set<String> gameIds = lobbyManager.getGameIds();
        Map<String, Integer> currentPlayers = new HashMap<>();
        Map<String, Integer> maxPlayers = new HashMap<>();
        Map<String, LobbyState> lobbyStates = new HashMap<>();

        for (String gameId : gameIds) {
            GameController game = lobbyManager.getGame(gameId);

            currentPlayers.put(gameId, game.getCurrentPlayerCount());
            maxPlayers.put(gameId, game.getNumPlayers());
            lobbyStates.put(gameId, game.getLobbyState());
        }

        clientInterface.notifyGames(gameIds, currentPlayers, maxPlayers, lobbyStates);
    }

    @Override
    public void createGame(String username, int numPlayers) throws RemoteException {
        if (gameController != null) {
            clientInterface.promptError("You are already in a game!", ErrorType.JOIN_ERROR);
            return;
        }

        String gameId;

        try {
            gameId = lobbyManager.createGame(numPlayers);
        } catch (IllegalArgumentException e) {
            clientInterface.promptError(e.getMessage(), ErrorType.JOIN_ERROR);
            return;
        }

        gameController = lobbyManager.getGame(gameId);
        try {
            gameController.createPlayer(username);
        } catch (UnexpectedActionException e) {
            clientInterface.promptError("Couldn't join game: " + e.getMessage(), ErrorType.JOIN_ERROR);
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

    @Override
    public void joinGame(String gameId, String username) throws RemoteException {
        if (gameController != null) {
            clientInterface.promptError("You are already in a game.", ErrorType.JOIN_ERROR);
            return;
        }

        gameController = lobbyManager.getGame(gameId);
        if (gameController == null) {
            clientInterface.promptError("No game with the given id.", ErrorType.JOIN_ERROR);
            return;
        }

        if (!gameController.isRejoiningAfterCrash()) {
            try {
                gameController.createPlayer(username);
            } catch (UnexpectedActionException e) {
                clientInterface.promptError("Couldn't join game: " + e.getMessage(), ErrorType.JOIN_ERROR);
                gameController = null;
                this.username = null;
                return;
            }
        }

        try {
            gameController.joinPlayer(username, clientInterface);
            this.username = username;
        } catch (UnexpectedActionException e) {
            clientInterface.promptError("User already rejoined the game.", ErrorType.JOIN_ERROR);
            gameController = null;
            this.username = null;
        }
    }

    @Override
    public void setStarterCard(SideType side) throws RemoteException {
        if (gameController != null) {
            gameController.setStarterCard(username, side);
        }
    }

    @Override
    public void setColor(PlayerColor color) throws RemoteException {
        if (gameController != null) {
            gameController.setPlayerColor(username, color);
        }
    }

    @Override
    public void setPersonalObjective(ObjectiveCard objectiveCard) throws RemoteException {
        if (gameController != null) {
            gameController.setPlayerObjective(username, objectiveCard);
        }
    }

    @Override
    public void playCard(PlayableCard playedCard, SideType side, Position pos) throws RemoteException {
        if (gameController != null) {
            gameController.placeCard(username, playedCard, side, pos);
        }
    }

    @Override
    public void drawCard(DrawType drawType) throws RemoteException {
        if (gameController != null) {
            gameController.drawCard(username, drawType);
        }
    }

    @Override
    public void sendChatMessage(String text) throws RemoteException {
        if (gameController != null) {
            gameController.sendChatMessage(username, text);
        }
    }

    @Override
    public void sendChatMessage(String text, String receiverUsername) throws RemoteException {
        if (gameController != null) {
            gameController.sendChatMessage(username, text, Set.of(receiverUsername));
        }
    }

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

    @Override
    public void leaveGame() throws RemoteException {
        leaveGame(false);
    }

    @Override
    public void leaveGame(boolean forced) throws RemoteException {
        if (!forced) {
            if (gameController != null && username != null) {
                gameController.disconnect(username);
            }
        }
        username = null;
        gameController = null;
    }

    @Override
    public void pong() throws RemoteException {
        ponged.set(true);
    }
}
