package it.polimi.ingsw.am16.client.view.cli;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CLIInputManager implements Runnable {

    private boolean running;
    private final CLI cliView;
    private final InputStream inputStream;
    private ServerInterface serverInterface;
    private final Set<CLICommand> allowedCommands;

    public CLIInputManager(CLI cliView, InputStream inputStream) {
        this.cliView = cliView;
        this.inputStream = inputStream;
        this.allowedCommands = new HashSet<>();
    }

    public void setServerInterface(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    public void addCommand(CLICommand command) {
        allowedCommands.add(command);
    }

    public void removeCommand(CLICommand command) {
        allowedCommands.remove(command);
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
        String[] args = input.split(" ");

        String inputCommand = args[0].toLowerCase();

        Set<CLICommand> matchingCommands = commandMatch(inputCommand);

        if (matchingCommands.size() > 1) {
            System.out.println("Ambiguous command: \"" + inputCommand + "\"");
            System.out.println("Possible matches:");
            for(CLICommand command : matchingCommands) {
                System.out.println("\t- " + command);
            }
            return;
        } else if (matchingCommands.isEmpty()) {
            System.out.println("Unknown command: \"" + inputCommand + "\"");
            return;
        }

        CLICommand command = matchingCommands.iterator().next();

        switch (command) {
            case HELP -> {
                cliView.printHelp();
            }
            case CREATE_GAME -> {
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

                try {
                    serverInterface.createGame(username, numPlayers);
                } catch (RemoteException e) {
                    //TODO handle it
                    e.printStackTrace();
                }
            }
            case JOIN_GAME -> {
                if (args.length < 3 || args[1] == null || args[2] == null || args[1].isEmpty() || args[2].isEmpty()) {
                    System.out.println("Invalid arguments. Usage: join_game [username] [gameId]");
                    cliView.printCommandPrompt();
                    break;
                }

                String username = args[1];
                String gameId = args[2];

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

                try {
                    serverInterface.setStarterCard(sideType);
                } catch (RemoteException e) {
                    //TODO handle it
                    e.printStackTrace();
                }
            }
            case COLOR -> {
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
                    System.out.println("Invalid arguments. Usage: objective [1|2]");
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
                        System.out.println("Invalid arguments. Usage: hand [username]");
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
                        System.out.println("Invalid arguments. Usage: play_area [username]");
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

                PlayableCard playedCard = hand.get(index-1);

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

                try {
                    serverInterface.drawCard(drawType);
                } catch (RemoteException e) {
                    //TODO handle it
                    e.printStackTrace();
                }
            }
            case SCROLL_VIEW -> {
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
                }
            }
            case CHAT_HISTORY -> {
                cliView.printChatHistory();
            }
            case WHISPER -> {
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

                try {
                    serverInterface.sendChatMessage(text, receiverUsername);
                } catch (RemoteException e) {
                    //TODO handle it
                    e.printStackTrace();
                }
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

    public Set<CLICommand> commandMatch(String input) {
        return allowedCommands
                .stream()
                .filter(c -> c.matches(input))
                .collect(Collectors.toSet());
    }

    public Set<CLICommand> getAllowedCommands() {
        return allowedCommands;
    }

    public void clearCommands() {
        allowedCommands.clear();
    }
}
