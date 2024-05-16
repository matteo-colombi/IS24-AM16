package it.polimi.ingsw.am16;

import it.polimi.ingsw.am16.client.Client;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.server.Server;

public class CodexStart {


    /**
     * Main method to start the client or the server.
     * @param args The arguments to start the client or the server.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        CardRegistry.getRegistry();

        switch (args[0]) {
            case "--client" -> {
                if (args.length != 4) {
                    printUsage();
                    return;
                }
                Client.start(args);
            }
            case "--server" -> {
                if (args.length != 4) {
                    printUsage();
                    return;
                }
                Server.start(args);
            }
            default -> printUsage();
        }
    }

    /**
     * Prints a help message to show the syntax of the command to start the client.
     */
    private static void printUsage() {
        System.err.println("Invalid arguments.");
        System.out.println("Arguments should be:");
        System.out.println("--client [--gui or --cli] [--socket or --rmi] serverAddress:port");
        System.out.println("or, to start a server");
        System.out.println("--server serverHost socketPort rmiPort");
    }
}

