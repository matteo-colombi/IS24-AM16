package it.polimi.ingsw.am16.client;

import it.polimi.ingsw.am16.client.rmi.RMIClientImplementation;
import it.polimi.ingsw.am16.client.tcp.TCPClient;
import it.polimi.ingsw.am16.client.view.ViewInterface;
import it.polimi.ingsw.am16.client.view.cli.CLI;
import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.server.ServerInterface;
import it.polimi.ingsw.am16.server.rmi.WelcomeRMIServer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;


/**
 * Class to handle a client. It'll handle the creation of the view (CLI or GUI) and the server interface (TCP or RMI).
 */
public class Client {

    /**
     * Starts the client with the given arguments.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);

        String viewType;

        if (argsList.contains("--cli")) {
            viewType = "cli";
        } else {
            viewType = "gui";
        }

        ViewInterface view = null;

        switch (viewType) {
            case "gui" -> view = new CodexGUI();
            case "cli" -> view = new CLI();
        }

        view.startView(args);
    }

    /**
     * Creates a new connection with the server using the given protocol.
     * @param args The command line arguments passed to the application.
     * @param view The view of the client which will receive responses from the server.
     * @return The interface which the view can use to communicate with the server through the connection that was established.
     * @throws IllegalArgumentException Thrown if an invalid protocol name is given.
     */
    public static ServerInterface serverInterfaceFactory(List<String> args, ViewInterface view) throws IllegalArgumentException {
        String protocol;
        int protocolIndex = args.indexOf("--socket");
        if (protocolIndex == -1) {
            protocolIndex = args.indexOf("--rmi");
            protocol = "rmi";
        } else {
            protocol = "socket";
        }

        if (protocolIndex + 1 >= args.size()) {
            throw new IllegalArgumentException("Missing server address and port. Use -h for more information.");
        }

        String[] hostAndPort = args.get(protocolIndex + 1).split(":");

        String host = hostAndPort[0];
        int port;

        try {
            port = Integer.parseInt(hostAndPort[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Missing server address and port. Use -h for more information.");
        }

        ServerInterface serverInterface;

        switch (protocol) {
            case "socket" -> {
                TCPClient tcpClient;
                try {
                    tcpClient = new TCPClient(host, port, view);
                    new Thread(tcpClient).start();
                } catch (UnknownHostException e) {
                    throw new IllegalArgumentException("Unknown host: " + host);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Couldn't establish a connection to the host.");
                }
                serverInterface = tcpClient;
            }
            case "rmi" -> {
                try {
                    WelcomeRMIServer welcomeRMIServer = (WelcomeRMIServer) Naming.lookup("rmi://" + host + ":" + port + "/CodexWelcomeServer");
                    RMIClientImplementation rmiClient = new RMIClientImplementation(welcomeRMIServer, view);
                    serverInterface = rmiClient.getServerInterface();
                    rmiClient.start();
                } catch (RemoteException e) {
                    throw new IllegalArgumentException("Couldn't start RMI client: " + e.getMessage());
                } catch (NotBoundException e) {
                    throw new IllegalArgumentException("Couldn't find RMI server: " + e.getMessage());
                } catch (MalformedURLException e) {
                    throw new IllegalArgumentException("Invalid hostname: " + e.getMessage());
                }
            }
            default -> throw new IllegalArgumentException("Illegal protocol: " + protocol);
        }
        return serverInterface;
    }
}
