package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.server.controller.GameController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CLIInputManager implements Runnable {

    private boolean running;
    private final CLI cliView;
    private final InputStream inputStream;

    //JUST FOR TESTING
    private int playerId;
    private GameController gameController;

    public CLIInputManager(CLI cliView, InputStream inputStream, GameController gameController) {
        this.cliView = cliView;
        this.inputStream = inputStream;

        //JUST FOR TESTING
        this.playerId = -1;
        this.gameController = gameController;
    }

    @Override
    public void run() {
        running = true;
        //Using BufferedReader instead of Scanner because it is thread safe
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            cliView.printCommandPrompt();
            while(running) {
                String input = reader.readLine();
                parseCommand(input);
            }
        } catch (IOException e) {
            System.err.println("An error occured.");
            e.printStackTrace();
        }
    }

    private void parseCommand(String input) {
        System.out.println(playerId + " " + input);

        String[] args = input.split(" ");

        String command = args[0];

        if(!cliView.allowedCommand(command)) {
            System.out.println("Unknown command: \"" + command + "\"");
            cliView.printCommandPrompt();
            return;
        }

        switch (command.toLowerCase()) {
            case "help" -> cliView.printHelp();
            case "create_game" -> {
                //TODO
            }
            case "join_game" -> {
                if (args.length < 3 || args[1] == null || args[2] == null || args[1].isEmpty() || args[2].isEmpty()) {
                    System.out.println("Invalid arguments. Usage: join_game [username] [gameId]");
                    cliView.printCommandPrompt();
                    break;
                }

                String username = args[1];
                String gameId = args[2];

                //THIS IS JUST FOR TESTING
                try {
                    this.playerId = gameController.createPlayer(username);
                } catch (UnexpectedActionException e) {
                    System.out.println("Couldn't join game: " + e.getMessage() + ".");
                    break;
                }

                gameController.joinPlayer(this.playerId, cliView);
            }
            case "players" -> {
                cliView.printPlayers();
            }
            case "draw_options" -> {
                cliView.printDrawOptions();
            }
            case "common_objectives" -> {
                cliView.printCommonObjectives();
            }
            case "starter" -> {
                if (args.length < 2 || args[1] == null || (!args[1].equals("front") && !args[1].equals("back"))) {
                    System.out.println("Invalid arguments. Usage: starter [front|back]");
                    cliView.printCommandPrompt();
                    break;
                }

                String side = args[1];
                SideType sideType;
                if (side.equals("front")) {
                    sideType = SideType.FRONT;
                } else {
                    sideType = SideType.BACK;
                }

                //FOR TESTING ONLY
                gameController.setStarterCard(playerId, sideType);
            }
            case "color", "colour" -> {
                if (args.length < 2 || args[1] == null) {
                    System.out.println("Invalid arguments. Usage: color [color]");
                    cliView.printCommandPrompt();
                    break;
                }

                String colorString = args[1];
                PlayerColor color = switch (colorString) {
                    case "red" -> PlayerColor.RED;
                    case "blue" -> PlayerColor.BLUE;
                    case "green" -> PlayerColor.GREEN;
                    case "yellow" -> PlayerColor.YELLOW;
                    default -> null;
                };

                if (color == null || !cliView.validColorChoice(color)) {
                    System.out.println("Invalid or already chosen color!");
                    cliView.printCommandPrompt();
                    return;
                }

                //JUST FOR TESTING
                gameController.setPlayerColor(playerId, color);
            }
            case "objective" -> {
                if (args.length < 2 || args[1] == null || (!args[1].equals("1") && !args[1].equals("2"))) {
                    System.out.println("Invalid arguments. Usage: objective [1|2]");
                    cliView.printCommandPrompt();
                    break;
                }

                int objectiveChoice = Integer.parseInt(args[1]);

                //JUST FOR TESTING
                gameController.setPlayerObjective(playerId, cliView.getPersonalObjectiveOption(objectiveChoice));

            }
            case "hand" -> {
                if (args.length == 1) {
                    cliView.printHand();
                } else {
                    if (args[1] == null || args[1].isEmpty()) {
                        System.out.println("Invalid arguments. Usage: hand [username]");
                        cliView.printCommandPrompt();
                        break;
                    }
                    String username = args[1];
                    cliView.printOtherHand(username);
                }
            }
            case "play_area" -> {
                if (args.length == 1) {
                    cliView.printPlayArea();
                } else {
                    if (args[1] == null || args[1].isEmpty()) {
                        System.out.println("Invalid arguments. Usage: play_area [username]");
                        cliView.printCommandPrompt();
                        break;
                    }
                    String username = args[1];
                    cliView.printPlayArea(username);
                }

            }
            case "play_card" -> {
                //TODO
            }
            case "draw_card" -> {
                //TODO
            }
            case "scroll_view" -> {
                if (args.length < 2 || args[1] == null || args[1].isEmpty() || (!args[1].equals("left") && !args[1].equals("right") && !args[1].equals("center"))) {
                    System.out.println("Invalid arguments. Usage: scroll_view [left|right|center] [(if left/right) offset]");
                    cliView.printCommandPrompt();
                    break;
                }
                if (args[1].equals("center")) {
                    cliView.scrollView("center", 0);
                } else {
                    try {
                        int offset = Integer.parseInt(args[2]);
                        cliView.scrollView(args[1], offset);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid arguments. Offset must be an integer.");
                    }
                }
            }
            case "points" -> {
                //TODO
            }
            case "winners" -> {
                //TODO
            }
            case "chat" -> {
                if (args.length == 1) {
                    cliView.printUnreadChat();
                } else {
                    //TODO (send chat message to everybody)
                }
            }
            case "chat_history" -> {
                cliView.printChatHistory();
            }
            case "chat_private" -> {
                //TODO (send private chat message)
            }
            case "exit" -> {
                if (cliView.getCliState() == CLI.CLIState.STARTUP) {
                    System.out.println("Good bye!");
                    running = false;
                } else {
                    //THIS IS ONLY FOR TESTING
                    System.out.println("Disconnecting from the game...");
                    gameController.disconnect(this.playerId);
                    this.gameController = null;
                    cliView.resetToStartup();
                    System.out.println("Disconnected.");
                    cliView.printCommandPrompt();
                }
            }
            case "rick" -> {
                if (args.length < 2 || args[1] == null || args[1].isEmpty()) {
                    System.out.println("Invalid arguments. Usage: rick [username]");
                    cliView.printCommandPrompt();
                    break;
                }

                String username = args[1];

                //FOR TESTING PURPOSES
                gameController.rick(username);
            }
        }
    }
}
