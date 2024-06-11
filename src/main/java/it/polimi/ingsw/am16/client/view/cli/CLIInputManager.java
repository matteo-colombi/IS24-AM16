package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.server.ServerInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

/**
 * DOCME
 * N.B. the only parts of this class that are thread safe are the allowedCommands methods
 * Everything else should never be called by multiple threads
 */
public class CLIInputManager implements Runnable {

    private boolean running;
    private final CLI cliView;
    private final InputStream inputStream;
    private ServerInterface serverInterface;
    private final Set<CLICommand> allowedCommands;

    public CLIInputManager(CLI cliView, InputStream inputStream) {
        this.cliView = cliView;
        this.inputStream = inputStream;
        this.allowedCommands = new ConcurrentSkipListSet<>();
    }

    public void setServerInterface(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    @Override
    public void run() {
        running = true;
        //Using BufferedReader instead of Scanner because it is thread safe
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            while (running) {
                String input = reader.readLine();
                parseCommand(input);
            }
        } catch (IOException e) {
            System.err.println("An error occured.");
            e.printStackTrace();
        }

        System.exit(0);
    }

    private void parseCommand(String input) {
        String[] args;
        if (input == null) {
            args = new String[]{""};
        } else {
            args = input.split(" ");
        }

        String inputCommand = args[0].toLowerCase();

        //FIXME remove, just for testing
        if (inputCommand.equals("test")) {
            Map<String, ObjectiveCard> personalObjectives = Map.of(
                    "teo", CardRegistry.getRegistry().getObjectiveCardFromName("objective_resources_3"),
                    "xLorde", CardRegistry.getRegistry().getObjectiveCardFromName("objective_object_1"),
                    "andre", CardRegistry.getRegistry().getObjectiveCardFromName("objective_pattern_3"),
                    "l2c", CardRegistry.getRegistry().getObjectiveCardFromName("objective_pattern_8")
            );
            cliView.printPersonalObjectives(personalObjectives);
        }

        Set<CLICommand> matchingCommands = commandMatch(inputCommand);

        if (matchingCommands.size() > 1) {
            System.out.println("Ambiguous command: \"" + inputCommand + "\"");
            System.out.println("Possible matches:");
            matchingCommands.stream().sorted().forEach(c -> System.out.println("\t- " + c));
            cliView.printCommandPrompt();
            return;
        } else if (matchingCommands.isEmpty()) {
            System.out.println("Command not allowed: \"" + inputCommand + "\"");
            cliView.printCommandPrompt();
            return;
        }

        CLICommand command = matchingCommands.iterator().next();

        switch (command) {
            case HELP -> {
                cliView.printHelp();
            }
            case GET_GAMES -> {
                try {
                    serverInterface.getGames();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            case CREATE_GAME -> {
                if (args.length < 3 || args[1] == null || args[1].isEmpty() || args[2] == null || args[2].isEmpty()) {
                    System.out.println("Invalid arguments. Usage: " + CLICommand.CREATE_GAME.getUsage());
                    cliView.printCommandPrompt();
                    break;
                }

                String username = args[1];
                if (username.length() > 10) {
                    System.out.println("Invalid argument. Username must be 10 characters or less.");
                    cliView.printCommandPrompt();
                    break;
                }

                String numPlayersString = args[2];
                int numPlayers;
                try {
                    numPlayers = Integer.parseInt(numPlayersString);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid arguments. Number of players must be an integer.");
                    cliView.printCommandPrompt();
                    break;
                }

                try {
                    serverInterface.createGame(username, numPlayers);
                } catch (RemoteException e) {
                    //TODO handle it
                    e.printStackTrace();
                }
            }
            case JOIN_GAME -> {
                if (args.length < 3 || args[1] == null || args[2] == null || args[1].isEmpty() || args[2].isEmpty()) {
                    System.out.println("Invalid arguments. Usage: " + CLICommand.JOIN_GAME.getUsage());
                    cliView.printCommandPrompt();
                    break;
                }

                String username = args[1];
                if (username.length() > 10) {
                    System.out.println("Invalid argument. Username must be 10 characters or less.");
                    cliView.printCommandPrompt();
                    break;
                }
                String gameId = args[2].toUpperCase();

                try {
                    serverInterface.joinGame(gameId, username);
                } catch (RemoteException e) {
                    //TODO handle it
                    e.printStackTrace();
                }
            }
            case ID -> {
                cliView.printGameId();
            }
            case PLAYERS -> {
                cliView.printPlayers();
            }
            case DRAW_OPTIONS -> {
                cliView.printDrawOptions();
            }
            case COMMON_OBJECTIVES -> {
                cliView.printCommonObjectives();
            }
            case STARTER -> {
                if (args.length == 1) {
                    cliView.printStarterCard();
                    break;
                } else if (args[1] == null || (!args[1].equals("front") && !args[1].equals("back"))) {
                    System.out.println("Invalid arguments. Usage: " + CLICommand.STARTER.getUsage());
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

                try {
                    serverInterface.setStarterCard(sideType);
                } catch (RemoteException e) {
                    //TODO handle it
                    e.printStackTrace();
                }
            }
            case COLOR -> {
                if (args.length == 1) {
                    cliView.printColorOptions();
                    return;
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

                try {
                    serverInterface.setColor(color);
                } catch (RemoteException e) {
                    //TODO handle it
                    e.printStackTrace();
                }
            }
            case OBJECTIVE -> {
                if (args.length == 1) {
                    cliView.printObjectiveOptions();
                    break;
                } else if (args[1] == null || (!args[1].equals("1") && !args[1].equals("2"))) {
                    System.out.println("Invalid arguments. Usage: " + CLICommand.OBJECTIVE.getUsage());
                    cliView.printCommandPrompt();
                    break;
                }

                int objectiveChoice = Integer.parseInt(args[1]);
                ObjectiveCard objectiveCard = cliView.getPersonalObjectiveOption(objectiveChoice);

                try {
                    serverInterface.setPersonalObjective(objectiveCard);
                } catch (RemoteException e) {
                    //TODO handle it
                    e.printStackTrace();
                }
            }
            case OBJECTIVES -> {
                cliView.printAllObjectives();
            }
            case HAND -> {
                if (args.length == 1) {
                    cliView.printHand();
                } else {
                    if (args[1] == null || args[1].isEmpty()) {
                        System.out.println("Invalid arguments. Usage: " + CLICommand.HAND.getUsage());
                        cliView.printCommandPrompt();
                        break;
                    }
                    String username = args[1];
                    cliView.printOtherHand(username);
                }
            }
            case PLAY_AREA -> {
                if (args.length == 1) {
                    cliView.printPlayArea();
                } else {
                    if (args[1] == null || args[1].isEmpty()) {
                        System.out.println("Invalid arguments. Usage: " + CLICommand.PLAY_AREA.getUsage());
                        cliView.printCommandPrompt();
                        break;
                    }
                    String username = args[1];
                    cliView.printPlayArea(username);
                }

            }
            case PLAY_CARD -> {
                if (args.length < 4
                        || args[1] == null
                        || args[2] == null
                        || args[3] == null
                        || args[1].isEmpty()
                        || args[2].isEmpty()
                        || args[3].isEmpty()
                        || (!args[2].equals("front") && !args[2].equals("back"))) {
                    System.out.println("Invalid arguments. Usage: " + CLICommand.PLAY_CARD.getUsage());
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

                PlayableCard playedCard = hand.get(index - 1);

                SideType sideType = switch (side) {
                    case "front" -> SideType.FRONT;
                    case "back" -> SideType.BACK;
                    default -> null;
                };

                try {
                    serverInterface.playCard(playedCard, sideType, new Position(x, y));
                } catch (RemoteException e) {
                    //TODO handle it
                    e.printStackTrace();
                }
            }
            case DRAW_CARD -> {
                if (args.length < 3
                        || args[1] == null
                        || args[2] == null
                        || args[1].isEmpty()
                        || args[2].isEmpty()
                        || (!args[1].equals("deck") && !args[1].equals("common"))
                        || (!args[2].equals("resource") && !args[2].equals("resources") && !args[2].equals("gold"))
                        || args[1].equals("common") && (args.length < 4 || args[3] == null || args[3].isEmpty() || (!args[3].equals("1") && !args[3].equals("2")))
                ) {
                    System.out.println("Invalid arguments. Usage: " + CLICommand.DRAW_CARD.getUsage());
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

                try {
                    serverInterface.drawCard(drawType);
                } catch (RemoteException e) {
                    //TODO handle it
                    e.printStackTrace();
                }

                cliView.printCommandPrompt();
            }
            case SCROLL_VIEW -> {
                if (
                        args.length < 3
                                || args[1] == null || args[1].isEmpty()
                                || !args[1].equals("left") && !args[1].equals("right")
                                || args[2] == null || args[2].isEmpty()
                ) {
                    System.out.println("Invalid arguments. Usage: " + CLICommand.SCROLL_VIEW.getUsage());
                    cliView.printCommandPrompt();
                    break;
                }

                try {
                    int offset = Integer.parseInt(args[2]);
                    cliView.scrollView(args[1], offset);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid arguments. Offset must be an integer.");
                }

                cliView.printCommandPrompt();
            }
            case POINTS -> {
                cliView.printPoints();
            }
            case WINNERS -> {
                cliView.printWinners();
            }
            case CHAT -> {
                if (args.length == 1) {
                    cliView.printUnreadChat();
                } else {
                    String text = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

                    try {
                        serverInterface.sendChatMessage(text);
                    } catch (RemoteException e) {
                        //TODO handle it
                        e.printStackTrace();
                    }

                    cliView.printCommandPrompt();
                }
            }
            case CHAT_HISTORY -> {
                cliView.printChatHistory();
            }
            case WHISPER -> {
                if (args.length < 3) {
                    System.out.println("Invalid arguments. Usage: " + CLICommand.WHISPER.getUsage());
                    cliView.printCommandPrompt();
                    return;
                }

                String receiverUsername = args[1];
                if (!cliView.validUsername(receiverUsername)) {
                    System.out.println("Unknown user: \"" + receiverUsername + "\"");
                    return;
                }
                if (cliView.getUsername().equals(receiverUsername)) {
                    System.out.println("You can't whisper to yourself!");
                    return;
                }

                String text = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

                try {
                    serverInterface.sendChatMessage(text, receiverUsername);
                } catch (RemoteException e) {
                    //TODO handle it
                    e.printStackTrace();
                }

                cliView.printCommandPrompt();
            }
            case LEAVE_GAME -> {
                System.out.println("Leaving the game...");
                try {
                    serverInterface.leaveGame();
                    cliView.resetToStartup();
                    System.out.println("Game left.");
                } catch (RemoteException e) {
                    //TODO handle it
                    e.printStackTrace();
                }
                cliView.printCommandPrompt();
            }
            case EXIT -> {
                System.out.println("Good bye!");
                try {
                    serverInterface.disconnect();
                } catch (RemoteException e) {
                    //TODO handle it
                    e.printStackTrace();
                }
                running = false;
            }
        }
    }

    private Set<CLICommand> commandMatch(String input) {
        Set<CLICommand> filteredCommands = allowedCommands
                .stream()
                .filter(c -> c.exactMatch(input))
                .limit(1)
                .collect(Collectors.toSet());

        if (!filteredCommands.isEmpty())
            return filteredCommands;

        return allowedCommands
                .stream()
                .filter(c -> c.matches(input))
                .collect(Collectors.toSet());
    }

    public void addCommand(CLICommand command) {
        allowedCommands.add(command);
    }

    public void removeCommand(CLICommand command) {
        allowedCommands.remove(command);
    }

    public Set<CLICommand> getAllowedCommands() {
        return allowedCommands;
    }

    public void clearCommands() {
        allowedCommands.clear();
    }
}
