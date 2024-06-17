package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.client.Client;
import it.polimi.ingsw.am16.client.view.ViewInterface;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.game.LobbyState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.ErrorType;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.server.ServerInterface;

import java.util.*;

import static it.polimi.ingsw.am16.client.view.cli.CLIConstants.CARD_HEIGHT;
import static it.polimi.ingsw.am16.client.view.cli.CLIConstants.CARD_WIDTH;

/**
 * DOCME
 */
public class CLI implements ViewInterface {

    // This class is thread safe. All methods are synchronized(this).
    // Technically this limits parallel execution, but that shouldn't be a problem,
    // since methods will be called only once in a while (at "user" speeds).

    private static final CLIText frontLabel = new CLIText("Fronts:");
    private static final CLIText backLabel = new CLIText("Backs:");
    private static final CLIText indexLabel = new CLIText(("Indices:"));
    private static final CLIText resourcesLabel = new CLIText("Resources:");
    private static final CLIText goldLabel = new CLIText("Gold:");
    private static final CLIText deckLabel = new CLIText("[Decks]");
    private static final CLIText oneLabel = new CLIText("[1]");
    private static final CLIText twoLabel = new CLIText("[2]");
    private static final CLIText commonObjectivesLabel = new CLIText("Common objectives:");
    private static final CLIText personalObjectivesLabel = new CLIText("Personal objective:");

    private static final Map<LobbyState, String> lobbyStateStrings = Map.of(
            LobbyState.JOINING, "Joining",
            LobbyState.REJOINING, "Rejoining",
            LobbyState.IN_GAME, "In game",
            LobbyState.ENDING, "Ending"
    );

    private final Thread inputManagerThread;
    private final CLIInputManager cliInputManager;

    private String gameId;
    private String username;
    private CLIState cliState;
    private List<String> playerUsernames;
    private GameState gameState;
    private PlayableCard[] commonResourceCards;
    private PlayableCard[] commonGoldCards;
    private ObjectiveCard[] commonObjectiveCards;
    private ResourceType resourceDeckTopType;
    private ResourceType goldDeckTopType;
    private StarterCard starterCard;
    private Map<String, PlayerColor> playerColors;
    private List<PlayerColor> colorChoices;
    private List<PlayableCard> hand;
    private Map<String, List<RestrictedCard>> otherHands;
    private Map<String, CLIPlayArea> playAreas;
    private Map<String, Integer> gamePoints;
    private Map<String, Integer> objectivePoints;
    private List<ObjectiveCard> personalObjectiveOptions;
    private ObjectiveCard personalObjective;
    private List<String> turnOrder;
    private List<String> winners;
    private boolean dontDraw;
    private String lastPrintedPlayArea;

    private boolean dontPrint;

    private List<ChatMessage> chatHistory;
    private List<ChatMessage> unreadChat;

    public CLI() {
        this.cliState = CLIState.STARTUP;
        this.cliInputManager = new CLIInputManager(this, System.in);
        this.cliInputManager.addCommand(CLICommand.HELP);
        this.cliInputManager.addCommand(CLICommand.GET_GAMES);
        this.cliInputManager.addCommand(CLICommand.JOIN_GAME);
        this.cliInputManager.addCommand(CLICommand.CREATE_GAME);
        this.cliInputManager.addCommand(CLICommand.EXIT);
        this.gameId = null;
        this.username = null;
        this.commonResourceCards = null;
        this.commonGoldCards = null;
        this.commonObjectiveCards = null;
        this.resourceDeckTopType = null;
        this.goldDeckTopType = null;
        this.starterCard = null;
        this.gamePoints = null;
        this.objectivePoints = null;
        this.turnOrder = null;
        this.playerUsernames = null;
        this.colorChoices = null;
        this.playerColors = null;
        this.otherHands = null;
        this.hand = null;
        this.playAreas = null;
        this.dontDraw = false;
        this.chatHistory = null;
        this.unreadChat = null;
        this.lastPrintedPlayArea = null;

        this.dontPrint = false;

        this.inputManagerThread = new Thread(cliInputManager);
    }

    @Override
    public synchronized void startView(String[] args) {
        List<String> argsList = Arrays.asList(args);
        String protocol;
        int protocolIndex = argsList.indexOf("--socket");
        if (protocolIndex == -1) {
            protocolIndex = argsList.indexOf("--rmi");
            protocol = "rmi";
        } else {
            protocol = "socket";
        }

        if (protocolIndex + 1 >= argsList.size()) {
            System.out.println("Missing server address and port. Use -h for more information.");
            return;
        }
        String[] hostAndPort = argsList.get(protocolIndex + 1).split(":");
        if (hostAndPort.length < 2) {
            System.out.println("Invalid arguments. Use -h for more information.");
            return;
        }

        int port = -1;

        try {
            port = Integer.parseInt(hostAndPort[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid port: " + hostAndPort[1]);
            System.exit(1);
        }

        ServerInterface serverInterface;

        try {
            serverInterface = Client.serverInterfaceFactory(protocol, hostAndPort[0], port, this);
        } catch (IllegalArgumentException e) {
            return;
        }

        this.cliInputManager.setServerInterface(serverInterface);

        printBanner();
        printWelcome();

        this.inputManagerThread.start();
    }

    @Override
    public void printGames(Set<String> gameIds, Map<String, Integer> currentPlayers, Map<String, Integer> maxPlayers, Map<String, LobbyState> lobbyStates) {
        if (gameIds.isEmpty()) {
            System.out.println("\nThere are no available games.");
        } else {
            System.out.println("\nAvailable games:");

            for (String gameId : gameIds) {
                System.out.printf("\t- %s %d/%d\t%s\n", gameId, currentPlayers.get(gameId), maxPlayers.get(gameId), lobbyStateStrings.get(lobbyStates.get(gameId)));
            }
        }

        printCommandPrompt();
    }

    @Override
    public synchronized void joinGame(String gameId, String username, int numPlayers) {
        this.gameId = gameId;
        this.username = username;
        this.cliState = CLIState.LOBBY;
        this.gamePoints = new HashMap<>();
        this.objectivePoints = new HashMap<>();
        this.playerUsernames = new ArrayList<>();
        this.playerColors = new HashMap<>();
        this.otherHands = new HashMap<>();
        this.hand = new ArrayList<>();
        this.playAreas = new HashMap<>();
        this.chatHistory = new ArrayList<>();
        this.unreadChat = new ArrayList<>();
        this.cliInputManager.removeCommand(CLICommand.GET_GAMES);
        this.cliInputManager.removeCommand(CLICommand.JOIN_GAME);
        this.cliInputManager.removeCommand(CLICommand.CREATE_GAME);
        this.cliInputManager.removeCommand(CLICommand.EXIT);
        this.cliInputManager.addCommand(CLICommand.ID);
        this.cliInputManager.addCommand(CLICommand.PLAYERS);
        this.cliInputManager.addCommand(CLICommand.CHAT);
        this.cliInputManager.addCommand(CLICommand.CHAT_HISTORY);
        this.cliInputManager.addCommand(CLICommand.WHISPER);
        this.cliInputManager.addCommand(CLICommand.LEAVE_GAME);
        System.out.printf("\nJoined the game (ID %s). Your username is %s. The expected number of players for this game is %d.\n\n", gameId, username, numPlayers);
        printCommandPrompt();
    }

    /**
     * Tells the view that information about a game which is being resumed is about to be sent.
     */
    @Override
    public void rejoinInformationStart() {
        System.out.println("\nResuming the game...");
        dontPrint = true;
    }

    /**
     * Tells the view that information about the has all been sent and the game is about to resume.
     */
    @Override
    public void rejoinInformationEnd() {
        System.out.println();
        dontPrint = false;
    }

    @Override
    public synchronized void addPlayer(String username) {
        this.playerUsernames.add(username);
        System.out.printf("\nPlayer %s joined the game!", username);
        printCommandPrompt();
    }

    @Override
    public synchronized void setPlayers(List<String> usernames) {
        this.playerUsernames.addAll(usernames);
    }

    @Override
    public synchronized void setGameState(GameState state) {
        this.gameState = state;
        switch (state) {
            case JOINING -> {
                if (!dontPrint) System.out.println("\nWe are waiting for players to join.");
                this.cliState = CLIState.LOBBY;
            }
            case INIT -> {
                this.cliState = CLIState.PRE_GAME;
                if (!dontPrint) {
                    System.out.println("\nWe have reached the player count!");
                    System.out.println("The game will now start.");
                }
            }
            case STARTED -> {
                this.cliInputManager.addCommand(CLICommand.POINTS);
                this.cliState = CLIState.IN_GAME;
            }
            case FINAL_ROUND -> {
                System.out.println();
                CLIAssetRegistry.getCLIAssetRegistry().getFinalRoundLabel().printText();
            }
            case ENDED -> {
                this.cliState = CLIState.GAME_ENDED;
                System.out.println("\nThe game has ended.");
            }
        }
    }

    @Override
    public synchronized void setCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) {
        this.commonResourceCards = commonResourceCards;
        this.commonGoldCards = commonGoldCards;
        this.cliInputManager.addCommand(CLICommand.DRAW_OPTIONS);
    }

    @Override
    public synchronized void setDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) {
        switch (whichDeck) {
            case RESOURCE -> this.resourceDeckTopType = resourceType;
            case GOLD -> this.goldDeckTopType = resourceType;
        }
    }

    @Override
    public synchronized void promptStarterChoice(StarterCard starterCard) {
        this.starterCard = starterCard;
        this.cliState = CLIState.CHOOSING_STARTER;
        this.cliInputManager.addCommand(CLICommand.STARTER);

        printStarterCard();
    }

    @Override
    public synchronized void choosingColors() {
        System.out.println("\nPlayers are now choosing their color.");
        System.out.println("\nPlease wait for your turn.");
        printCommandPrompt();
    }

    @Override
    public synchronized void promptColorChoice(List<PlayerColor> colorChoices) {
        this.cliState = CLIState.CHOOSING_COLOR;
        this.cliInputManager.addCommand(CLICommand.COLOR);
        this.colorChoices = colorChoices;

        printColorOptions();
    }

    @Override
    public synchronized void setColor(String username, PlayerColor color) {
        this.playerColors.put(username, color);
        if (this.username.equals(username)) {
            this.cliInputManager.removeCommand(CLICommand.COLOR);
        }
        if (!dontPrint) {
            System.out.printf("\nPlayer %s's color is %s.\n", username, color.name().toLowerCase());
            printCommandPrompt();
        }
    }

    @Override
    public synchronized void drawingCards() {
        if (!dontPrint) System.out.println("\nCards are being drawn.\n");
    }

    @Override
    public synchronized void setHand(List<PlayableCard> hand) {
        this.hand = new ArrayList<>(hand);
        this.cliInputManager.addCommand(CLICommand.HAND);

        if (!dontPrint) printHand();
    }

    @Override
    public synchronized void addCardToHand(PlayableCard card) {
        this.hand.add(card);
        this.cliInputManager.removeCommand(CLICommand.DRAW_CARD);
        this.cliState = CLIState.IN_GAME;

        if (!dontPrint) printHand();
    }

    @Override
    public synchronized void removeCardFromHand(PlayableCard card) {
        this.hand.remove(card);
    }

    @Override
    public synchronized void setOtherHand(String username, List<RestrictedCard> hand) {
        this.otherHands.put(username, new ArrayList<>(hand));
        this.cliInputManager.addCommand(CLICommand.HAND);
    }

    @Override
    public synchronized void addCardToOtherHand(String username, RestrictedCard newCard) {
        this.otherHands.get(username).add(newCard);
    }

    @Override
    public synchronized void removeCardFromOtherHand(String username, RestrictedCard cardToRemove) {
        this.otherHands.get(username).remove(cardToRemove);
    }

    @Override
    public synchronized void setPlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        if (this.username.equals(username)) {
            this.cliInputManager.removeCommand(CLICommand.STARTER);
        }
        this.cliInputManager.addCommand(CLICommand.PLAY_AREA);
        this.cliInputManager.addCommand(CLICommand.SCROLL_VIEW);
        this.playAreas.put(username, new CLIPlayArea(cardPlacementOrder, field, activeSides, legalPositions, illegalPositions, resourceCounts, objectCounts, playerUsernames, playerColors, gamePoints, objectivePoints));
        if (username.equals(this.username)) {
            if (!dontPrint) {
                System.out.println("Starter card played! Type \"play_area\" to see your play area.");
                printCommandPrompt();
            }
        }
    }

    @Override
    public synchronized void playCard(String username, BoardCard card, SideType side, Position pos, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        this.playAreas.get(username).addCard(card, side, pos, addedLegalPositions, removedLegalPositions, resourceCounts, objectCounts);
        this.cliInputManager.removeCommand(CLICommand.PLAY_CARD);
        if (username.equals(this.username)) {
            if (dontDraw) {
                if (!dontPrint) {
                    System.out.println("Card played.");
                    printCommandPrompt();
                }
                cliState = CLIState.IN_GAME;
            } else {
                if (!dontPrint) {
                    System.out.println("Card played. Use \"draw_card\" to draw a card.");
                    printCommandPrompt();
                }
                cliState = CLIState.DRAWING_CARD;
                this.cliInputManager.addCommand(CLICommand.DRAW_CARD);
            }
        }
    }

    @Override
    public synchronized void setGamePoints(String username, int gamePoints) {
        this.gamePoints.put(username, gamePoints);

        for (CLIPlayArea playArea : playAreas.values()) {
            playArea.updatePoints(this.gamePoints, this.objectivePoints);
        }
    }

    @Override
    public synchronized void setObjectivePoints(String username, int objectivePoints) {
        this.objectivePoints.put(username, objectivePoints);

        for (CLIPlayArea playArea : playAreas.values()) {
            playArea.updatePoints(this.gamePoints, this.objectivePoints);
        }
    }

    @Override
    public synchronized void setCommonObjectives(ObjectiveCard[] commonObjectives) {
        this.commonObjectiveCards = commonObjectives;
        this.cliInputManager.addCommand(CLICommand.COMMON_OBJECTIVES);
        if (!dontPrint) printCommonObjectives();
    }

    @Override
    public synchronized void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) {
        this.cliState = CLIState.CHOOSING_OBJECTIVE;
        this.personalObjectiveOptions = possiblePersonalObjectives;
        this.cliInputManager.addCommand(CLICommand.OBJECTIVE);
        if (!dontPrint) printObjectiveOptions();
    }

    @Override
    public synchronized void setPersonalObjective(ObjectiveCard personalObjective) {
        this.cliInputManager.removeCommand(CLICommand.OBJECTIVE);
        this.cliInputManager.removeCommand(CLICommand.COMMON_OBJECTIVES);
        this.cliInputManager.addCommand(CLICommand.OBJECTIVES);
        this.cliState = CLIState.PRE_GAME;
        this.personalObjective = personalObjective;
        if (!dontPrint) {
            System.out.println("\nYour personal objective is:");
            CLIAssetRegistry.getCLIAssetRegistry().getCard(personalObjective.getName()).front().printText();
            printCommandPrompt();
        }
    }

    @Override
    public synchronized void setStartOrder(List<String> usernames) {
        this.turnOrder = usernames;
        if (!dontPrint) {
            System.out.println("\nPlayers will play in the order:");
            for (String username : usernames) {
                System.out.printf("%s%s", usernames.indexOf(username) == 0 ? "" : ", ", username);
            }
            printCommandPrompt();
        }
    }

    @Override
    public synchronized void turn(String username) {
        if (username.equals(this.username)) {
            CLIText turnText = new CLIText("It's your turn! You can now play a card, then draw one.", this.playerColors.get(username));
            System.out.println();
            turnText.printText(true);
            cliState = CLIState.PLAYING_CARD;
            this.cliInputManager.addCommand(CLICommand.PLAY_CARD);
        } else {
            CLIText turnText = new CLIText("It's " + username + "'s turn.", this.playerColors.get(username));
            System.out.println();
            turnText.printText(true);
        }

        printCommandPrompt();
    }

    @Override
    public synchronized void setWinners(List<String> winnerUsernames, Map<String, ObjectiveCard> personalObjectives) {
        this.cliInputManager.addCommand(CLICommand.WINNERS);
        this.winners = winnerUsernames;
        printPoints();
        printWinners();
        printPersonalObjectives(personalObjectives);
    }

    @Override
    public synchronized void addMessages(List<ChatMessage> messages) {
        this.unreadChat.addAll(messages);
    }

    @Override
    public synchronized void addMessage(ChatMessage message) {
        if (message.text().equals("rick")) {
            if (!dontPrint) printRick();
        } else {
            this.unreadChat.add(message);
            if (!message.senderUsername().equals(username) && !dontPrint) {
                System.out.println("\nYou have a new chat message. Type \"chat\" to read it!");
                printCommandPrompt();
            }
        }
    }

    @Override
    public synchronized void promptError(String errorMessage, ErrorType ignored) {
        System.out.println(errorMessage);
        printCommandPrompt();
    }

    @Override
    public synchronized void notifyDontDraw() {
        this.dontDraw = true;
        if (!dontPrint) System.out.println("\nFrom now on, you shouldn't draw cards anymore.");
    }

    @Override
    public synchronized void signalDisconnection(String whoDisconnected) {
        System.out.printf("\n%s disconnected.\n", whoDisconnected);

        printCommandPrompt();
        playerUsernames.remove(whoDisconnected);
    }

    @Override
    public synchronized void signalGameSuspension(String whoDisconnected) {
        System.out.printf("\n%s disconnected. The game is suspended and all players have to rejoin.\n\n", whoDisconnected);
        resetToStartup();

        printWelcome();
    }

    @Override
    public synchronized void signalGameDeletion(String whoDisconnected) {
        System.out.printf("\n%s disconnected. The game has been deleted.\n\n", whoDisconnected);
        resetToStartup();

        printWelcome();
    }

    @Override
    public synchronized void signalDeadlock(String username) {
        if (username.equals(this.username)) {
            System.out.println("\nYou have deadlocked yourself! Your turn is skipped.");
        } else {
            System.out.printf("\n%s has deadlocked themselves! Their turn is skipped.\n", username);
        }

    }

    @Override
    public synchronized void signalConnectionLost() {
        System.err.println("Connection lost to the server.");

        System.exit(1);
    }

    public synchronized void resetToStartup() {
        cliState = CLIState.STARTUP;
        username = null;
        commonResourceCards = null;
        commonGoldCards = null;
        commonObjectiveCards = null;
        resourceDeckTopType = null;
        goldDeckTopType = null;
        starterCard = null;
        gamePoints = null;
        objectivePoints = null;
        turnOrder = null;
        playerUsernames = null;
        playerColors = null;
        otherHands = null;
        hand = null;
        playAreas = null;
        dontDraw = false;
        chatHistory = null;
        unreadChat = null;
        lastPrintedPlayArea = null;
        colorChoices = null;

        cliInputManager.clearCommands();
        cliInputManager.addCommand(CLICommand.HELP);
        cliInputManager.addCommand(CLICommand.GET_GAMES);
        cliInputManager.addCommand(CLICommand.JOIN_GAME);
        cliInputManager.addCommand(CLICommand.CREATE_GAME);
        cliInputManager.addCommand(CLICommand.EXIT);
    }

    public synchronized void printHand() {
        if (this.hand.isEmpty()) {
            System.out.println("\nYour hand is empty.\n");
            printCommandPrompt();
            return;
        }

        System.out.println("\nYour hand is:\n");
        CLIText hand = new CLIText();
        hand.mergeText(frontLabel, 0, 0);
        hand.mergeText(backLabel, CARD_HEIGHT + 2, 0);
        hand.mergeText(indexLabel, 2 * CARD_HEIGHT + 4, 0);
        for (int i = 0; i < this.hand.size(); i++) {
            CLICardAsset cardAsset = CLIAssetRegistry.getCLIAssetRegistry().getCard(this.hand.get(i).getName());
            CLIText thisIndexLabel = new CLIText(String.format("[%d]", i + 1));
            hand.mergeText(thisIndexLabel, 2 * CARD_HEIGHT + 4, 2 + i * (CARD_WIDTH + 2) + CARD_WIDTH / 2);
            hand.mergeText(cardAsset.front(), 1, 3 + i * (CARD_WIDTH + 2));
            hand.mergeText(cardAsset.back(), CARD_HEIGHT + 3, 3 + i * (CARD_WIDTH + 2));
        }
        hand.printText();
        printCommandPrompt();
    }

    public synchronized void printOtherHand(String username) {
        if (!this.playerUsernames.contains(username)) {
            System.out.println("Invalid username: \"" + username + "\"");
            printCommandPrompt();
            return;
        }
        if (!this.otherHands.containsKey(username)) {
            System.out.println("Hand not available for " + username + "\n");
            printCommandPrompt();
            return;
        }

        List<RestrictedCard> restrictedHand = otherHands.get(username);

        if (restrictedHand.isEmpty()) {
            System.out.printf("%s's hand is empty.\n", username);
            printCommandPrompt();
            return;
        }

        System.out.printf("\nThe backs of %s's cards are:\n", username);
        CLIText hand = new CLIText();
        for (int i = 0; i < restrictedHand.size(); i++) {
            CLIText backAsset = CLIAssetRegistry.getCLIAssetRegistry().getCardBack(restrictedHand.get(i));
            hand.mergeText(backAsset, 0, 3 + i * (CARD_WIDTH + 2));
        }
        hand.printText();
        printCommandPrompt();
    }

    public synchronized void printObjectiveOptions() {
        System.out.println("\nChoose an objective between:");
        printObjectives(this.personalObjectiveOptions, true);
    }

    public synchronized void printCommonObjectives() {
        System.out.println("\nThe common objectives are:");
        printObjectives(List.of(commonObjectiveCards), false);
    }

    public synchronized void printDrawOptions() {
        System.out.println("\nThe draw options are:");
        CLIText drawOptions = new CLIText();
        drawOptions.mergeText(resourcesLabel, 0, 0);
        drawOptions.mergeText(goldLabel, CARD_HEIGHT + 2, 0);
        drawOptions.mergeText(indexLabel, 2 * CARD_HEIGHT + 4, 0);

        CLIText resourceDeckTop = CLIAssetRegistry.getCLIAssetRegistry().getCardBack(new RestrictedCard(PlayableCardType.RESOURCE, resourceDeckTopType));
        CLIText goldDeckTop = CLIAssetRegistry.getCLIAssetRegistry().getCardBack(new RestrictedCard(PlayableCardType.GOLD, goldDeckTopType));
        CLIText commonResource1 = CLIAssetRegistry.getCLIAssetRegistry().getCard(commonResourceCards[0].getName()).front();
        CLIText commonResource2 = CLIAssetRegistry.getCLIAssetRegistry().getCard(commonResourceCards[1].getName()).front();
        CLIText commonGold1 = CLIAssetRegistry.getCLIAssetRegistry().getCard(commonGoldCards[0].getName()).front();
        CLIText commonGold2 = CLIAssetRegistry.getCLIAssetRegistry().getCard(commonGoldCards[1].getName()).front();

        drawOptions.mergeText(resourceDeckTop, 1, 5);
        drawOptions.mergeText(goldDeckTop, CARD_HEIGHT + 3, 5);
        drawOptions.mergeText(commonResource1, 1, 10 + (CARD_WIDTH + 2));
        drawOptions.mergeText(commonResource2, 1, 10 + 2 * (CARD_WIDTH + 2));
        drawOptions.mergeText(commonGold1, CARD_HEIGHT + 3, 10 + (CARD_WIDTH + 2));
        drawOptions.mergeText(commonGold2, CARD_HEIGHT + 3, 10 + 2 * (CARD_WIDTH + 2));

        drawOptions.mergeText(deckLabel, 2 * CARD_HEIGHT + 4, 2 + CARD_WIDTH / 2);
        drawOptions.mergeText(oneLabel, 2 * CARD_HEIGHT + 4, 9 + (CARD_WIDTH + 2) + CARD_WIDTH / 2);
        drawOptions.mergeText(twoLabel, 2 * CARD_HEIGHT + 4, 9 + 2 * (CARD_WIDTH + 2) + CARD_WIDTH / 2);

        drawOptions.printText();
        printCommandPrompt();
    }

    public synchronized void printObjectives(List<ObjectiveCard> objectives, boolean printIndices) {
        System.out.println();
        CLIText objectivesText = new CLIText();
        if (printIndices) {
            objectivesText.mergeText(indexLabel, CARD_HEIGHT + 1, 0);
        }

        for (int i = 0; i < objectives.size(); i++) {
            ObjectiveCard card = objectives.get(i);
            CLIText cardAsset = CLIAssetRegistry.getCLIAssetRegistry().getCard(card.getName()).front();
            if (printIndices) {
                CLIText thisIndexLabel = new CLIText(String.format("[%d]", i + 1));
                objectivesText.mergeText(thisIndexLabel, CARD_HEIGHT + 1, 2 + i * (CARD_WIDTH + 2) + CARD_WIDTH / 2);
            }
            objectivesText.mergeText(cardAsset, 0, 3 + i * (CARD_WIDTH + 2));
        }
        objectivesText.printText();
        printCommandPrompt();
    }

    public synchronized void printAllObjectives() {
        CLIText objectiveText = new CLIText();
        CLIText personalObjective = CLIAssetRegistry.getCLIAssetRegistry().getCard(this.personalObjective.getName()).front();
        CLIText commonObjective1 = CLIAssetRegistry.getCLIAssetRegistry().getCard(commonObjectiveCards[0].getName()).front();
        CLIText commonObjective2 = CLIAssetRegistry.getCLIAssetRegistry().getCard(commonObjectiveCards[1].getName()).front();

        objectiveText.mergeText(commonObjectivesLabel, 0, 5 + (CARD_WIDTH + 2));
        objectiveText.mergeText(personalObjectivesLabel, 0, 0);
        objectiveText.mergeText(personalObjective, 1, 5);
        objectiveText.mergeText(commonObjective1, 1, 10 + (CARD_WIDTH + 2));
        objectiveText.mergeText(commonObjective2, 1, 10 + 2 * (CARD_WIDTH + 2));

        objectiveText.printText();

        printCommandPrompt();
    }

    public synchronized void printPersonalObjectives(Map<String, ObjectiveCard> personalObjectives) {
        System.out.println("\nHere is everyone's personal objective: ");
        CLIText objectiveText = new CLIText();
        int i = 0;
        for(String username : personalObjectives.keySet()) {

            ObjectiveCard card = personalObjectives.get(username);
            CLIText nameLabel = new CLIText(username);
            CLIText personalObjective = CLIAssetRegistry.getCLIAssetRegistry().getCard(card.getName()).front();
            objectiveText.mergeText(nameLabel, 0, 3 + i * (CARD_WIDTH + 5));
            objectiveText.mergeText(personalObjective, 1, 5 + i * (CARD_WIDTH + 5));
            i++;
        }

        objectiveText.printText();

        printCommandPrompt();
    }

    public synchronized void printStarterCard() {
        System.out.println("\nChoose on which side to place your starter card.");

        CLIText starterCardText = new CLIText();
        CLIText frontLabel = new CLIText("Front:");
        CLIText backLabel = new CLIText("Back:");

        CLICardAsset cardAsset = CLIAssetRegistry.getCLIAssetRegistry().getCard(this.starterCard.getName());

        starterCardText.mergeText(frontLabel, 0, 0);
        starterCardText.mergeText(backLabel, 0, 25);
        starterCardText.mergeText(cardAsset.front(), 1, 2);
        starterCardText.mergeText(cardAsset.back(), 1, 27);
        starterCardText.printText();

        printCommandPrompt();
    }

    public synchronized void printColorOptions() {
        System.out.println("\nChoose a color between:");
        for (PlayerColor color : colorChoices) {
            CLIText colorLabel = new CLIText("██", color);
            CLIText colorLabel2 = new CLIText(color.name().toLowerCase());
            System.out.print("\t");
            colorLabel.mergeText(colorLabel2, 0, 3);
            colorLabel.printText();
        }
        printCommandPrompt();
    }

    public synchronized void printGameId() {
        System.out.printf("This game's id is %s.\n", this.gameId);
        printCommandPrompt();
    }

    public synchronized void printPlayers() {
        System.out.println("Players currently in the game:");

        for (String username : (turnOrder != null ? turnOrder : playerUsernames)) {
            CLIText colorLabel = new CLIText("██", this.playerColors.get(username));
            CLIText userLabel = new CLIText(username + (turnOrder != null && turnOrder.indexOf(username) == 0 ? " (starter player)" : ""));
            userLabel.mergeText(colorLabel, 0, -3);
            System.out.print("\t");
            userLabel.printText();
        }

        printCommandPrompt();
    }

    public synchronized void printPlayArea() {
        printPlayArea(username);
    }

    public synchronized void printPlayArea(String username) {
        if (!this.playerUsernames.contains(username)) {
            System.out.println("\nInvalid username: \"" + username + "\"");
            printCommandPrompt();
            return;
        }
        if (!this.playAreas.containsKey(username)) {
            System.out.printf("\n%s has not yet chosen their starter card.\n", username);
            printCommandPrompt();
            return;
        }

        if (this.username.equals(username)) {
            System.out.println("\nYour play area is:");
        } else {
            System.out.printf("\n%s's play area:\n", username);
        }

        this.playAreas.get(username).printPlayArea();
        this.lastPrintedPlayArea = username;

        printCommandPrompt();
    }

    public synchronized void scrollView(String direction, int offset) {
        if (lastPrintedPlayArea == null) return;

        switch (direction) {
            case "left" -> this.playAreas.get(lastPrintedPlayArea).moveView(-offset);
            case "right" -> this.playAreas.get(lastPrintedPlayArea).moveView(offset);
        }

        this.playAreas.get(lastPrintedPlayArea).printPlayArea();
    }

    public synchronized void printPoints() {
        System.out.println("\nCurrent points (in order from most to least points):");
        Map<String, Integer> totalPoints = new HashMap<>();
        for (String username : playerUsernames) {
            totalPoints.put(username, gamePoints.getOrDefault(username, 0) + objectivePoints.getOrDefault(username, 0));
        }
        List<String> sortedUsernames = this.playerUsernames
                .stream()
                .sorted((s1, s2) -> {
                            if (totalPoints.get(s1).equals(totalPoints.get(s2))) {
                                return Integer.compare(objectivePoints.getOrDefault(s1, 0), objectivePoints.getOrDefault(s2, 0));
                            } else {
                                return Integer.compare(totalPoints.get(s1), totalPoints.get(s2));
                            }
                        }
                ).toList().reversed();

        for (String username : sortedUsernames) {
            CLIText colorLabel = new CLIText("██", this.playerColors.get(username));
            CLIText pointsLabel;
            if (this.objectivePoints.get(username) != null && this.objectivePoints.get(username) != 0) {
                pointsLabel = new CLIText(username + ": " + (this.gamePoints.getOrDefault(username, 0) + this.objectivePoints.getOrDefault(username, 0)) + " (" + this.objectivePoints.getOrDefault(username, 0) + " from objectives)");
            } else {
                pointsLabel = new CLIText(username + ": " + this.gamePoints.getOrDefault(username, 0));
            }
            pointsLabel.mergeText(colorLabel, 0, -3);
            System.out.print("\t");
            pointsLabel.printText();
        }

        printCommandPrompt();
    }

    public synchronized void printWinners() {
        if (cliState != CLIState.GAME_ENDED) {
            System.out.println("\nNo winners yet!");
            printCommandPrompt();
            return;
        }

        System.out.printf("\nThe winner%s...\n", winners.size() == 1 ? " is" : "s are");
        for (String username : this.winners) {
            System.out.printf("%s%s", this.winners.indexOf(username) == 0 ? "" : ", ", username);
        }
        System.out.print("\n");
        printCommandPrompt();
    }

    public synchronized void printHelp() {
        Set<CLICommand> allowedCommands = cliInputManager.getAllowedCommands();
        if (cliState == CLIState.STARTUP) {
            System.out.println("\nAvailable commands:");
        } else {
            System.out.println("\nGame actions:");
            allowedCommands
                    .stream()
                    .filter(CLICommand::isGameCommand)
                    .sorted()
                    .forEach(c -> System.out.println("\t- " + c));

            System.out.println("\nGeneric commands:");
        }

        allowedCommands
                .stream()
                .filter(c -> !c.isGameCommand())
                .sorted()
                .forEach(c -> System.out.println("\t- " + c));

        printCommandPrompt();
    }

    public synchronized void printUnreadChat() {
        if (unreadChat.isEmpty()) {
            System.out.println("You have no unread messages. Type \"chat_history\" to see older messages");
            printCommandPrompt();
            return;
        }

        System.out.printf("%d unread message%s:\n", unreadChat.size(), unreadChat.size() == 1 ? "" : "s");
        for (ChatMessage message : unreadChat) {
            System.out.println(message);
        }

        chatHistory.addAll(unreadChat);
        unreadChat.clear();

        printCommandPrompt();
    }

    public synchronized void printChatHistory() {
        if (chatHistory.isEmpty()) {
            System.out.println("You have no messages in the chat history.");
        } else {
            for (ChatMessage message : chatHistory) {
                System.out.println(message);
            }
        }

        if (!unreadChat.isEmpty()) {
            System.out.printf("You have %d unread message%s! Type \"chat\" to see them.\n", unreadChat.size(), unreadChat.size() == 1 ? "" : "s");
        }

        printCommandPrompt();
    }

    public synchronized void printRick() {
        System.out.println();
        CLIText rick = CLIAssetRegistry.getCLIAssetRegistry().getRick();
        rick.printText();
        System.out.println("You have been rickrolled!");
        printCommandPrompt();
    }

    public synchronized void printBanner() {
        System.out.println();
        CLIAssetRegistry.getCLIAssetRegistry().getBanner().printText();
        System.out.println();
    }

    public synchronized void printWelcome() {
        System.out.println("Welcome to Codex Naturalis!");
        System.out.println("Type \"help\" to see all available commands.");
        printCommandPrompt();
    }

    public synchronized boolean validUsername(String username) {
        return this.playerUsernames.contains(username);
    }

    public synchronized void printCommandPrompt() {
        System.out.print("\n>> ");
    }

    public synchronized boolean validColorChoice(PlayerColor playerColor) {
        return this.colorChoices.contains(playerColor);
    }

    public synchronized ObjectiveCard getPersonalObjectiveOption(int index) {
        return this.personalObjectiveOptions.get(index - 1);
    }

    public synchronized List<PlayableCard> getHand() {
        return hand;
    }

    public synchronized String getUsername() {
        return username;
    }

    private enum CLIState {
        STARTUP,
        LOBBY,
        PRE_GAME,
        CHOOSING_STARTER,
        CHOOSING_COLOR,
        CHOOSING_OBJECTIVE,
        IN_GAME,
        PLAYING_CARD,
        DRAWING_CARD,
        GAME_ENDED
    }
}
