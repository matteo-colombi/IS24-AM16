package it.polimi.ingsw.am16.server.rmi;

import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.server.ServerInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface used to handle the creation of new instances of {@link RMIServerImplementation}.
 */
public interface WelcomeRMIServer extends Remote {

    /**
     * Creates a new instance of {@link RMIServerImplementation} for the given client.
     * @param clientInterface The client that connected to the server.
     * @return A new instance of {@link RMIServerImplementation}.
     * @throws RemoteException If an error occurs while creating the new instance.
     */
    ServerInterface getClientHandler(RemoteClientInterface clientInterface) throws RemoteException;

}
