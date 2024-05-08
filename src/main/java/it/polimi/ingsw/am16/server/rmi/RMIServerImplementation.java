package it.polimi.ingsw.am16.server.rmi;

import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.server.ServerInterface;
import it.polimi.ingsw.am16.server.controller.GameController;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class RMIServerImplementation extends UnicastRemoteObject implements ServerInterface {

    private final RemoteClientInterface clientInterface;
    private final LobbyManager lobbyManager;

    private final Timer pingTimer;
    private final AtomicBoolean ponged;

    private GameController gameController;
    private int playerId;

    public RMIServerImplementation(RemoteClientInterface clientInterface, LobbyManager lobbyManager) throws RemoteException {
        this.clientInterface = clientInterface;
        this.lobbyManager = lobbyManager;
        this.pingTimer = new Timer();
        this.ponged = new AtomicBoolean(true);
        this.gameController = null;
        this.playerId = -1;

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
    public void createGame(String username, int numPlayers) throws RemoteException {

    }

    @Override
    public void joinGame(String gameId, String username) throws RemoteException {

    }

    @Override
    public void setStarterCard(SideType side) throws RemoteException {

    }

    @Override
    public void setColor(PlayerColor color) throws RemoteException {

    }

    @Override
    public void setPersonalObjective(ObjectiveCard objectiveCard) throws RemoteException {

    }

    @Override
    public void playCard(PlayableCard playedCard, SideType side, Position pos) throws RemoteException {

    }

    @Override
    public void drawCard(DrawType drawType) throws RemoteException {

    }

    @Override
    public void sendChatMessage(String text) throws RemoteException {

    }

    @Override
    public void sendChatMessage(String text, String receiverUsername) throws RemoteException {

    }

    @Override
    public void disconnect() throws RemoteException {

    }

    @Override
    public void leaveGame() throws RemoteException {

    }

    @Override
    public void pong() throws RemoteException {
        ponged.set(true);
    }
}
