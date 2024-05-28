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

    /**
     * Constructs a new WelcomeRMIServerImplementation object.
     * @param lobbyManager The lobby manager that keeps information about all currently active games on this server.
     * @throws RemoteException Thrown if an exception occurs while creating the remote object stubs.
     */
    public WelcomeRMIServerImplementation(LobbyManager lobbyManager) throws RemoteException {
        this.lobbyManager = lobbyManager;
    }

    @Override
    public ServerInterface getClientHandler(RemoteClientInterface clientInterface) throws RemoteException {
        System.out.println("New RMI client connected.");
        return new RMIServerImplementation(clientInterface, lobbyManager);
    }
}
