package it.polimi.ingsw.am16.server.rmi;

import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.server.ServerInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * DOCME
 */
public interface WelcomeRMIServer extends Remote {

    ServerInterface getClientHandler(RemoteClientInterface clientInterface) throws RemoteException;

}
