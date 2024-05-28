package it.polimi.ingsw.am16;

import it.polimi.ingsw.am16.client.Client;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.server.Server;

import java.util.Arrays;
import java.util.List;

public class CodexStart {

    /**
     * Main method to start the client or the server.
     * @param args The arguments to start the client or the server.
     */
    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);

        if (argsList.contains("-h") || argsList.contains("--help")) {
            printFullHelp();
            return;
        }

        if (argsList.isEmpty()) {
            printClientUsage();
            return;
        }

        String type;

        if (argsList.contains("--server") || argsList.contains("-s")) {
            type = "server";
        } else {
            type = "client";
            if (!(argsList.contains("--socket") || argsList.contains("--rmi"))) {
                printClientUsage();
                return;
            }
        }

        CardRegistry.getRegistry();

        switch (type) {
            case "client" -> Client.main(args);
            case "server" -> {
                if (argsList.size() < 4) {
                    printServerUsage();
                    return;
                }
                Server.main(args);
            }
        }
    }

    /**
     * Prints a help message to show the syntax of the command to start the client.
     */
    private static void printClientUsage() {
        System.err.println("Invalid arguments.");
        System.out.println("Arguments to start a client should contain at least:");
        System.out.println("<--socket | --rmi> serverAddress:port");
        System.out.println("Use --help or -h for more information.");
    }

    /**
     * Prints a help message to show the syntax of the command to start the server.
     */
    private static void printServerUsage() {
        System.err.println("Invalid arguments.");
        System.out.println("Arguments to start a server should be:");
        System.out.println("<-s | --server> serverAddress tcpPort rmiPort");
        System.out.println("Optional argument:");
        System.out.println("\t--seed theSeed\t sets the RNG seed for the server. The seed should be a number (positive or negative).");
    }

    /**
     * Prints a help message to show the syntax of command line arguments and their effect.
     */
    private static void printFullHelp() {
        System.out.println("Here is a list of available arguments. Please note that any unrecognized arguments will be ignored.");
        System.out.println("To start a client:");
        System.out.println("\t--socket serverAddress:port\t starts a socket client (not compatible with --rmi).");
        System.out.println("\t--rmi serverAddress:por\t starts an RMI client (not compatible with --socket).");
        System.out.println("\t--cli \t starts a command line client");
        System.out.println("\t--gui \t starts a graphical user interface client");
        System.out.println("The only mandatory argument is either --socket or --rmi. If --cli and --gui are missing, a graphical user interface client will start by default.");
        System.out.println("To start a server, use:");
        System.out.println("\t<-s | --server> serverAddress tcpPort rmiPort\t starts a server");
        System.out.println("For servers, an optional argument is also present:");
        System.out.println("\t--seed theSeed\t sets the RNG seed for the server. The seed should be a number (positive or negative).");
    }
}

