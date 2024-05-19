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


/**
 * Class to handle a client. It'll handle the creation of the view (CLI or GUI) and the server interface (TCP or RMI).
 */
public class Client {
    public static void main(String[] args) {

        ViewInterface view;

        switch (args[1].toLowerCase()) {
            case "--gui" -> view = new CodexGUI();
            case "--cli" -> view = new CLI();
            default -> {
                System.err.println("Invalid argument: \"" + args[1] + "\"");
                System.out.println("Please use --gui or --cli.");
                return;
            }
        }

        view.startView(args);
    }

    public static ServerInterface serverInterfaceFactory(String protocol, String host, int port, ViewInterface view) throws IllegalArgumentException {
        ServerInterface serverInterface = null;

        switch (protocol.toLowerCase()) {
            case "--socket" -> {
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
            case "--rmi" -> {
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
            default -> {
                System.err.println("Invalid argument: \"" + protocol + "\"");
                System.out.println("Please use --socket or --rmi.");
                throw new IllegalArgumentException();
            }
        }
        return serverInterface;
    }
}
