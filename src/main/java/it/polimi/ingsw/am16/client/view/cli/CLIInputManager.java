package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.server.controller.GameController;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CLIInputManager implements Runnable {

    private boolean running;
    private final CLI cliView;
    private final InputStream inputStream;

    //JUST FOR TESTING
    private int playerId;
    private String username;
    private GameController gameController;
    private final LobbyManager lobbyManager;

    public CLIInputManager(CLI cliView, InputStream inputStream, GameController gameController, LobbyManager lobbyManager) {
        this.cliView = cliView;
        this.inputStream = inputStream;

        //JUST FOR TESTING
        this.playerId = -1;
        this.gameController = gameController;
        this.lobbyManager = lobbyManager;
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
        System.out.println(playerId + " " + input); //FOR TESTING PURPOSES

        String[] args = input.split(" ");

        String command = args[0];

        if(!cliView.allowedCommand(command)) {
            System.out.println("Unknown command: \"" + command + "\"");
            cliView.printCommandPrompt();
            return;
        }

        switch (command.toLowerCase()) {
            case "help" -> {
                cliView.printHelp();
            }
            case "create_game" -> {
                if (args.length < 3 || args[1] == null || args[1].isEmpty() || args[2] == null || args[2].isEmpty()) {
                    System.out.println("Invalid arguments. Usage: create_game [username] [numPlayers]");
                    cliView.printCommandPrompt();
                    break;
                }

                String username = args[1];
                String numPlayersString = args[2];
                int numPlayers;
                try {
                    numPlayers = Integer.parseInt(numPlayersString);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid arguments. Number of players must be an integer.");
                    cliView.printCommandPrompt();
                    break;
                }

                //JUST FOR TESTING
                String gameId = lobbyManager.createGame(numPlayers);
                gameController = lobbyManager.getGame(gameId);

                this.username = username;
                try {
                    this.playerId = gameController.createPlayer(username);
                } catch (UnexpectedActionException e) {
                    System.out.println("Couldn't join game: " + e.getMessage() + ".");
                    break;
                }

                gameController.joinPlayer(this.playerId, cliView);

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
                this.username = username;
                try {
                    this.playerId = gameController.createPlayer(username);
                } catch (UnexpectedActionException e) {
                    System.out.println("Couldn't join game: " + e.getMessage() + ".");
                    break;
                }

                gameController.joinPlayer(this.playerId, cliView);
            }
            case "id" -> {
                cliView.printGameId();
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
                if (args.length == 1) {
                    cliView.printStarterCard();
                    break;
                } else if (args[1] == null || (!args[1].equals("front") && !args[1].equals("back"))) {
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
                if (args.length == 1) {
                    cliView.printObjectiveOptions();
                    break;
                } else if (args[1] == null || (!args[1].equals("1") && !args[1].equals("2"))) {
                    System.out.println("Invalid arguments. Usage: objective [1|2]");
                    cliView.printCommandPrompt();
                    break;
                }

                int objectiveChoice = Integer.parseInt(args[1]);

                //JUST FOR TESTING
                gameController.setPlayerObjective(playerId, cliView.getPersonalObjectiveOption(objectiveChoice));

            }
            case "objectives" -> {
                cliView.printAllObjectives();
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
                if (args.length < 4
                        || args[1] == null
                        || args[2] == null
                        || args[3] == null
                        || args[1].isEmpty()
                        || args[2].isEmpty()
                        || args[3].isEmpty()
                        || (!args[2].equals("front") && !args[2].equals("back"))) {
                    System.out.println("Invalid arguments. Usage: play_card [index] [front|back] [position: x;y]");
                    cliView.printCommandPrompt();
                    break;
                }

                String indexString = args[1];
                String side = args[2];
                String positionString = args[3];
                String[] coords = positionString.split("[;,]");
                if (coords.length != 2) {
                    System.out.println("Invalid arguments: position must be in the format \"x;y\"");
                    cliView.printCommandPrompt();
                    break;
                }
                String xString = coords[0];
                String yString = coords[1];

                int index;
                int x;
                int y;
                try {
                    index = Integer.parseInt(indexString);
                    x = Integer.parseInt(xString);
                    y = Integer.parseInt(yString);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid arguments. Index and coordinates must be integers.");
                    cliView.printCommandPrompt();
                    break;
                }

                List<PlayableCard> hand = cliView.getHand();

                if (index < 1 || index > hand.size()) {
                    System.out.printf("Invalid arguments. No card with index %d in hand.\n", index);
                    cliView.printCommandPrompt();
                    break;
                }

                //FOR TESTING PURPOSES
                gameController.placeCard(playerId, hand.get(index-1), SideType.valueOf(side.toUpperCase()), new Position(x, y));
            }
            case "draw_card" -> {
                if (args.length < 3
                        || args[1] == null
                        || args[2] == null
                        || args[1].isEmpty()
                        || args[2].isEmpty()
                        || (!args[1].equals("deck") && !args[1].equals("common"))
                        || (!args[2].equals("resource") && !args[2].equals("resources") && !args[2].equals("gold"))
                        || args[1].equals("common") && (args.length < 4 || args[3] == null || args[3].isEmpty() || (!args[3].equals("1") && !args[3].equals("2")))
                ) {
                    System.out.println("Invalid arguments. Usage: draw_card [deck|common] [resource|gold] [(if common) index]");
                    cliView.printCommandPrompt();
                    break;
                }

                //This is hideous, please don't judge
                DrawType drawType = switch (args[1]) {
                    case "deck" -> switch (args[2]) {
                        case "resource", "resources" -> DrawType.RESOURCE_DECK;
                        case "gold" -> DrawType.GOLD_DECK;
                        default -> null;
                    };
                    case "common" -> switch (args[2]) {
                        case "resource", "resources" -> switch (args[3]) {
                            case "1" -> DrawType.RESOURCE_1;
                            case "2" -> DrawType.RESOURCE_2;
                            default -> null;
                        };
                        case "gold" -> switch (args[3]) {
                            case "1" -> DrawType.GOLD_1;
                            case "2" -> DrawType.GOLD_2;
                            default -> null;
                        };
                        default -> null;
                    };
                    default -> null;
                };

                //JUST FOR TESTING
                gameController.drawCard(playerId, drawType);
            }
            case "scroll_view" -> {
                if (args.length < 2
                        || args[1] == null
                        || args[1].isEmpty()
                        || (!args[1].equals("left") && !args[1].equals("right") && !args[1].equals("center"))
                        || (args[1].equals("left") || args[1].equals("right")) && (args.length < 3 || args[2] == null || args[2].isEmpty())
                ) {
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

                cliView.printCommandPrompt();
            }
            case "points" -> {
                cliView.printPoints();
            }
            case "winners" -> {
                cliView.printWinners();
            }
            case "chat" -> {
                if (args.length == 1) {
                    cliView.printUnreadChat();
                } else {
                    String text = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

                    //JUST FOR TESTING
                    gameController.sendChatMessage(this.username, text);
                }
            }
            case "chat_history" -> {
                cliView.printChatHistory();
            }
            case "chat_private" -> {
                if (args.length < 3) {
                    System.out.println("Invalid arguments. Usage: chat_private [receiver username] [message]");
                    cliView.printCommandPrompt();
                    return;
                }

                String receiverUsername = args[1];
                if (!cliView.validUsername(receiverUsername)) {
                    System.out.println("Unknown user: \"" + receiverUsername + "\"");
                    return;
                }
                String text = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

                //JUST FOR TESTING
                gameController.sendChatMessage(this.username, text, Set.of(receiverUsername));
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
