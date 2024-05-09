package it.polimi.ingsw.am16.server.rmi;

import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.server.ServerInterface;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Implementation of the RMI server that handles the creation of new instances of {@link RMIServerImplementation}.
 */
public class WelcomeRMIServerImplementation extends UnicastRemoteObject implements WelcomeRMIServer {

    @Serial
    private static final long serialVersionUID = -2933588802156833189L;

    private final LobbyManager lobbyManager;

    public WelcomeRMIServerImplementation(LobbyManager lobbyManager) throws RemoteException {
        this.lobbyManager = lobbyManager;
    }

    /**
     * Creates a new instance of {@link RMIServerImplementation} for the given client.
     * @param clientInterface The client that connected to the server.
     * @return A new instance of {@link RMIServerImplementation}.
     * @throws RemoteException If an error occurs while creating the new instance.
     */
    @Override
    public ServerInterface getClientHandler(RemoteClientInterface clientInterface) throws RemoteException {
        System.out.println("New RMI client connected.");
        return new RMIServerImplementation(clientInterface, lobbyManager);
    }
}
