package it.polimi.ingsw.am16.server.rmi;

import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.server.ServerInterface;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * DOCME
 */
public class WelcomeRMIServerImplementation extends UnicastRemoteObject implements WelcomeRMIServer {

    @Serial
    private static final long serialVersionUID = -2933588802156833189L;

    private final LobbyManager lobbyManager;

    public WelcomeRMIServerImplementation(LobbyManager lobbyManager) throws RemoteException {
        this.lobbyManager = lobbyManager;
    }

    @Override
    public ServerInterface getClientHandler(RemoteClientInterface clientInterface) throws RemoteException {
        System.out.println("New RMI client connected.");
        return new RMIServerImplementation(clientInterface, lobbyManager);
    }
}
