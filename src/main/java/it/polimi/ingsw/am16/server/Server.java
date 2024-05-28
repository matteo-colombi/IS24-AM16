package it.polimi.ingsw.am16.server;

import it.polimi.ingsw.am16.common.util.FilePaths;
import it.polimi.ingsw.am16.common.util.RNG;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;
import it.polimi.ingsw.am16.server.rmi.WelcomeRMIServer;
import it.polimi.ingsw.am16.server.rmi.WelcomeRMIServerImplementation;
import it.polimi.ingsw.am16.server.tcp.WelcomeTCPServer;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;

public class Server {
    /**
     * Starts the server.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);
        int serverIndex = argsList.indexOf("--server");
        if (serverIndex == -1) {
            serverIndex = argsList.indexOf("-s");
        }

        if (serverIndex+3 >= argsList.size()) {
            System.out.println("Invalid arguments. Use -h for more information.");
            return;
        }

        String host = argsList.get(serverIndex+1);
        int tcpPort;
        int rmiPort;

        try {
            tcpPort = Integer.parseInt(argsList.get(serverIndex+2));
            rmiPort = Integer.parseInt(argsList.get(serverIndex+3));
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

        if (argsList.contains("--seed")) {
            int seedIndex = argsList.indexOf("--seed");
            if (seedIndex+1 >= argsList.size()) {
                System.out.println("Invalid arguments. Use -h for more information.");
                return;
            }
            try {
                long seed = Long.parseLong(argsList.get(seedIndex+1));
                RNG.setRNGSeed(seed);
            } catch (NumberFormatException e) {
                System.out.println("Invalid arguments. Seed should be a number.");
                return;
            }
        }

        LobbyManager lobbyManager = new LobbyManager();
        try {
            lobbyManager.loadGames(FilePaths.SAVE_DIRECTORY);
        } catch (IOException e) {
            System.err.println("Couldn't load game saves: " + e.getMessage());
        }

        System.out.println("Starting server on host " + host);

        WelcomeTCPServer welcomeTCPServer = new WelcomeTCPServer(tcpPort, lobbyManager);
        Thread welcomeTCPServerThread = new Thread(welcomeTCPServer);
        welcomeTCPServerThread.start();

        try {
            System.setProperty("java.rmi.server.hostname", host);
//            System.setProperty("java.rmi.server.logCalls", "true");

            WelcomeRMIServer welcomeRMIServer = new WelcomeRMIServerImplementation(lobbyManager);
            Registry registry = LocateRegistry.createRegistry(rmiPort);
            registry.rebind("CodexWelcomeServer", welcomeRMIServer);

            System.out.println("RMI server ready: registry listening on port " + rmiPort);
        } catch (RemoteException e) {
            System.err.println(e.getMessage() + ". RMI server couldn't start.");
        }
    }
}
