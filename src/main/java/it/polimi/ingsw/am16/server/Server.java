package it.polimi.ingsw.am16.server;

import it.polimi.ingsw.am16.common.util.FilePaths;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;
import it.polimi.ingsw.am16.server.rmi.WelcomeRMIServer;
import it.polimi.ingsw.am16.server.rmi.WelcomeRMIServerImplementation;
import it.polimi.ingsw.am16.server.tcp.WelcomeTCPServer;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    /**
     * Starts the server.
     * @param args The command line arguments.
     */
    public static void start(String[] args) {
        int tcpPort;
        int rmiPort;

        try {
            tcpPort = Integer.parseInt(args[1]);
            rmiPort = Integer.parseInt(args[2]);
            if (tcpPort < 1024 || rmiPort < 1024 || tcpPort > 65535 || rmiPort > 65535) {
                System.err.println("Ports under 1024 or over 65535 cannot be used.");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number. Ports should be positive integers between 1024 and 65535");
            return;
        }

        if (tcpPort == rmiPort) {
            System.err.println("Please choose two different ports for Socket and RMI.");
            return;
        }

        LobbyManager lobbyManager = new LobbyManager();
        try {
            lobbyManager.loadGames(FilePaths.SAVE_DIRECTORY);
        } catch (IOException e) {
            System.err.println("Couldn't load game saves: " + e.getMessage());
        }

        WelcomeTCPServer welcomeTCPServer = new WelcomeTCPServer(tcpPort, lobbyManager);
        Thread welcomeTCPServerThread = new Thread(welcomeTCPServer);
        welcomeTCPServerThread.start();

        try {
            System.setProperty("java.rmi.server.hostname", "192.168.1.10");

            WelcomeRMIServer welcomeRMIServer = new WelcomeRMIServerImplementation(lobbyManager);
            Registry registry = LocateRegistry.createRegistry(rmiPort);
            registry.rebind("CodexWelcomeServer", welcomeRMIServer);

            System.out.println("RMI server ready");
        } catch (RemoteException e) {
            System.err.println(e.getMessage() + ". RMI server couldn't start.");
        }
    }
}
