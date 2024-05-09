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

public class RMIServerImplementation extends UnicastRemoteObject implements ServerInterface {

    @Serial
    private static final long serialVersionUID = -549123884082532918L;

    private final RemoteClientInterface clientInterface;
    private final LobbyManager lobbyManager;

    private final Timer pingTimer;
    private final AtomicBoolean ponged;

    private GameController gameController;
    private int playerId;
    private String username;

    public RMIServerImplementation(RemoteClientInterface clientInterface, LobbyManager lobbyManager) throws RemoteException {
        this.clientInterface = clientInterface;
        this.lobbyManager = lobbyManager;
        this.pingTimer = new Timer();
        this.ponged = new AtomicBoolean(true);
        this.gameController = null;
        this.playerId = -1;
        this.username = null;

        pingRoutine();
    }

    private void pingRoutine() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (ponged.get()) {
                    ponged.set(false);
                    try {
                        clientInterface.ping();
                    } catch (RemoteException ignored) {

                    }
                } else {
                    System.out.println("Client didn't answer the last ping.");
                    pingTimer.cancel();

                    if (gameController != null) {
                        gameController.disconnect(playerId);

                    }
                    playerId = -1;
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

        for (String gameId : gameIds) {
            GameController game = lobbyManager.getGame(gameId);

            currentPlayers.put(gameId, game.getCurrentPlayerCount());
            maxPlayers.put(gameId, game.getNumPlayers());
        }

        //FIXME throws the Exception
        clientInterface.getGames(gameIds, currentPlayers, maxPlayers);
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
            playerId = gameController.createPlayer(username);
        } catch (UnexpectedActionException e) {
            clientInterface.promptError("Couldn't join game: " + e.getMessage());
            gameController = null;
            playerId = -1;
            return;
        }

        try {
            gameController.joinPlayer(playerId, clientInterface);
            this.username = username;
        } catch (UnexpectedActionException e) {
            System.err.println("Unexpected error: " + e.getMessage());
            this.username = null;
            gameController = null;
            playerId = -1;
            e.printStackTrace();
        }
    }

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

        if (gameController.isRejoiningAfterCrash()) {
            try {
                playerId = gameController.getPlayerId(username);
            } catch (IllegalArgumentException e) {
                clientInterface.promptError("Couldn't join game: " + e.getMessage());
                playerId = -1;
                gameController = null;
                return;
            }
        } else {
            try {
                playerId = gameController.createPlayer(username);
            } catch (UnexpectedActionException e) {
                clientInterface.promptError("Couldn't join game: " + e.getMessage());
                playerId = -1;
                gameController = null;
                this.username = null;
                return;
            }
        }

        try {
            gameController.joinPlayer(playerId, clientInterface);
            this.username = username;
        } catch (UnexpectedActionException e) {
            clientInterface.promptError("User already rejoined the game.");
            gameController = null;
            playerId = -1;
            this.username = null;
        }
    }

    @Override
    public void setStarterCard(SideType side) throws RemoteException {
        if (gameController != null) {
            gameController.setStarterCard(playerId, side);
        }
    }

    @Override
    public void setColor(PlayerColor color) throws RemoteException {
        if (gameController != null) {
            gameController.setPlayerColor(playerId, color);
        }
    }

    @Override
    public void setPersonalObjective(ObjectiveCard objectiveCard) throws RemoteException {
        if (gameController != null) {
            gameController.setPlayerObjective(playerId, objectiveCard);
        }
    }

    @Override
    public void playCard(PlayableCard playedCard, SideType side, Position pos) throws RemoteException {
        if (gameController != null) {
            gameController.placeCard(playerId, playedCard, side, pos);
        }
    }

    @Override
    public void drawCard(DrawType drawType) throws RemoteException {
        if (gameController != null) {
            gameController.drawCard(playerId, drawType);
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
        if (gameController != null && playerId != -1) {
            gameController.disconnect(playerId);
        }
        playerId = -1;
        gameController = null;
    }

    @Override
    public void leaveGame() throws RemoteException {
        if (gameController != null && playerId != -1) {
            gameController.disconnect(playerId);
        }
        playerId = -1;
        gameController = null;
    }

    @Override
    public void pong() throws RemoteException {
        ponged.set(true);
    }
}
