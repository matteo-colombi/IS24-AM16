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
     * @param protocol The protocol, either "socket" or "rmi", which will be used to communicate with the server and vice versa.
     * @param host The hostname of the server.
     * @param port The port which the server is expected to be listening on.
     * @param view The view of the client which will receive responses from the server.
     * @return The interface which the view can use to communicate with the server through the connection that was established.
     * @throws IllegalArgumentException Thrown if an invalid protocol name is given.
     */
    public static ServerInterface serverInterfaceFactory(String protocol, String host, int port, ViewInterface view) throws IllegalArgumentException {
        ServerInterface serverInterface = null;

        switch (protocol) {
            case "socket" -> {
                TCPClient tcpClient = null;
                try {
                    tcpClient = new TCPClient(host, port, view);
                    new Thread(tcpClient).start();
                } catch (UnknownHostException e) {
                    System.err.println("Unknown host: " + host);
                    System.exit(1);
                } catch (IOException e) {
                    System.err.println("Couldn't establish a connection to the host.");
                    System.exit(1);
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
                    System.err.println("Couldn't start RMI client: " + e.getMessage());
                    System.exit(1);
                } catch (NotBoundException e) {
                    System.err.println("Couldn't find RMI server: " + e.getMessage());
                    System.exit(1);
                } catch (MalformedURLException e) {
                    System.err.println("Invalid hostname: " + e.getMessage());
                    System.exit(1);
                }
            }
            default -> throw new IllegalArgumentException("Illegal protocol: " + protocol);
        }
        return serverInterface;
    }
}
