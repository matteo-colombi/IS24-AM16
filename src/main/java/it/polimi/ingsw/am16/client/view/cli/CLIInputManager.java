package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.server.controller.GameController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLIInputManager implements Runnable {

    private boolean running;
    private CLI cliView;
    private GameController gameController;

    public CLIInputManager(CLI cliView, GameController gameController) {
        this.cliView = cliView;
        this.gameController = gameController;
    }

    @Override
    public void run() {
        running = true;
        //Using BufferedReader instead of Scanner because it is thread safe
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            cliView.printCommandPrompt();
            while(running) {
                String input = reader.readLine();
                if (input.equalsIgnoreCase("exit")) {
                    running = false;
                    break;
                }
                parseCommand(input);
            }
        } catch (IOException e) {
            System.err.println("An error occured.");
            e.printStackTrace();
        }
    }

    private void parseCommand(String input) {
        input = input.toLowerCase();

        String[] args = input.split(" ");

        String command = args[0];

        switch (command) {
            case "help" -> cliView.printHelp();
            case "join_game" -> {
                if (args.length < 2 || args[1] == null || args[2] == null || args[1].isEmpty() || args[2].isEmpty()) {
                    System.out.println("Invalid arguments. Usage: join_game [username] [gameId]");
                    break;
                }

                String username = args[1];
                String gameId = args[2];

                //THIS IS JUST FOR TESTING
                int playerId;
                try {
                    playerId = gameController.createPlayer(username);
                } catch (UnexpectedActionException e) {
                    System.out.println("Couldn't join game: " + e.getMessage() + ".");
                    break;
                }

                gameController.joinPlayer(playerId, cliView);
            }
            default -> {
                System.out.println("Unknown command: \"" + command + "\"");
                cliView.printCommandPrompt();
            }
        }
    }
}
