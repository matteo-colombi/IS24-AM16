package it.polimi.ingsw.am16.client;

import it.polimi.ingsw.am16.client.rmi.RMIClientImplementation;
import it.polimi.ingsw.am16.client.tcp.TCPClient;
import it.polimi.ingsw.am16.client.view.ViewInterface;
import it.polimi.ingsw.am16.client.view.cli.CLI;
import it.polimi.ingsw.am16.server.rmi.WelcomeRMIServer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    public static void start(String[] args) {

        ViewInterface view;

        switch (args[1].toLowerCase()) {
            case "--gui" -> {
                //TODO start gui if the view is a gui
                view = null;
            }
            case "--cli" -> {
                view = new CLI();
            }
            default -> {
                System.err.println("Invalid argument: \"" + args[1] + "\"");
                System.out.println("Please use --gui or --cli.");
                return;
            }
        }

        String[] hostAndPort = args[3].split(":");
        String host = hostAndPort[0];
        int port;
        try {
            port = Integer.parseInt(hostAndPort[1]);
            if (port < 1024 || port > 65535) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid port.");
            return;
        }

        switch (args[2].toLowerCase()) {
            case "--socket" -> {
                TCPClient tcpClient;
                try {
                    tcpClient = new TCPClient(host, port, view);
                } catch (UnknownHostException e) {
                    System.err.println("Unknown host: " + host);
                    System.exit(1);
                    return;
                } catch (IOException e) {
                    System.err.println("Couldn't establish a connection to the host.");
                    System.exit(1);
                    return;
                }
                tcpClient.run();
            }
            case "--rmi" -> {
                try {
                    WelcomeRMIServer welcomeRMIServer = (WelcomeRMIServer) Naming.lookup("rmi://" + host + ":" + port + "/CodexWelcomeServer");
                    RMIClientImplementation rmiClient = new RMIClientImplementation(welcomeRMIServer, view);
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
                System.err.println("Invalid argument: \"" + args[2] + "\"");
                System.out.println("Please use --socket or --rmi.");
            }
        }
    }
}
