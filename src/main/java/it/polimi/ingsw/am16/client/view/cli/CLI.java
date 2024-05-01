package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.client.RemoteViewInterface;
import it.polimi.ingsw.am16.client.view.ViewInterface;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.server.ServerInterface;

import java.rmi.RemoteException;
import java.util.*;

import static it.polimi.ingsw.am16.client.view.cli.CLIConstants.CARD_HEIGHT;
import static it.polimi.ingsw.am16.client.view.cli.CLIConstants.CARD_WIDTH;

/**
 * DOCME
 */
public class CLI implements ViewInterface {

    // This class is thread safe. All methods are synchronized(this).
    // Technically this limits parallel execution, but that shouldn't be a problem,
    // since methods will be called only once in a while.

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

    private final Set<String> allowedCommands;

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

    private List<ChatMessage> chatHistory;
    private List<ChatMessage> unreadChat;

    public CLI() {
        this.cliState = CLIState.STARTUP;
        this.allowedCommands = new HashSet<>();
        this.allowedCommands.add("help");
        this.allowedCommands.add("join_game");
        this.allowedCommands.add("create_game");
        this.allowedCommands.add("exit");
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

        this.cliInputManager = new CLIInputManager(this, System.in);
        this.inputManagerThread = new Thread(cliInputManager);
    }

    /**
     * DOCME
     * @param serverInterface
     */
    public synchronized void setServerInterface(ServerInterface serverInterface) {
        cliInputManager.setServerInterface(serverInterface);
    }

    /**
     * DOCME
     */
    @Override
    public synchronized void start() {
        printBanner();
        printWelcome();

        this.inputManagerThread.start();
    }

    /**
     * Tells the view that they have joined a game with the given username.
     *
     * @param username The username the player has joined the game with.
     */
    @Override
    public synchronized void joinGame(String gameId, String username) {
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
        this.allowedCommands.remove("join_game");
        this.allowedCommands.remove("create_game");
        this.allowedCommands.add("id");
        this.allowedCommands.add("players");
        this.allowedCommands.add("chat_history");
        this.allowedCommands.add("chat");
        this.allowedCommands.add("whisper");
        System.out.printf("\nJoined the game (ID %s). Your username is %s.\n\n", gameId, username);
        printCommandPrompt();
    }

    /**
     * Adds a player to the game. Used to communicate the connection of a new player.
     *
     * @param username The new player's username.
     */
    @Override
    public synchronized void addPlayer(String username) {
        this.playerUsernames.add(username);
        System.out.printf("\nPlayer %s joined the game!", username);
        printCommandPrompt();
    }

    /**
     * DOCME
     *
     * @param usernames
     * @throws RemoteException
     */
    @Override
    public synchronized void setPlayers(List<String> usernames) {
        this.playerUsernames.addAll(usernames);
    }

    /**
     * Sets the game state. To be called when the game's state changes.
     *
     * @param state The new game state.
     */
    @Override
    public synchronized void setGameState(GameState state) {
        this.gameState = state;
        switch (state) {
            case JOINING -> {
                System.out.println("\nWe are waiting for players to join.");
                this.cliState = CLIState.LOBBY;
            }
            case INIT -> {
                this.cliState = CLIState.PRE_GAME;
                System.out.println("\nWe have reached the player count!");
                System.out.println("The game will now start.");
            }
            case STARTED -> {
                this.allowedCommands.add("points");
                this.cliState = CLIState.IN_GAME;
            }
            case FINAL_ROUND -> {
                System.out.println("\nThe game is almost over!");
            }
            case ENDED -> {
                this.cliState = CLIState.GAME_ENDED;
                System.out.println("\nThe game has ended.");
            }
        }
    }

    /**
     * Sets the common cards for the game. Should be called whenever these change.
     *
     * @param commonResourceCards The common resource cards (may also contain gold cards if the resource card deck is empty). Should always be of length 2.
     * @param commonGoldCards     The common gold cards (may also contain resource cards if the gold card deck is empty). Should always be of length 2.
     */
    @Override
    public synchronized void setCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) {
        this.commonResourceCards = commonResourceCards;
        this.commonGoldCards = commonGoldCards;
        this.allowedCommands.add("draw_options");
    }

    /**
     * Sets the types of cards at the top of the respective deck.
     *
     * @param whichDeck    The deck which we are setting the top card of.
     * @param resourceType The resource type of the card on top of the given deck.
     */
    @Override
    public synchronized void setDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) {
        switch (whichDeck) {
            case RESOURCE -> this.resourceDeckTopType = resourceType;
            case GOLD -> this.goldDeckTopType = resourceType;
        }
    }

    /**
     * Prompts the user to choose the side of the given starter card.
     *
     * @param starterCard The starter card of the player.
     */
    @Override
    public synchronized void promptStarterChoice(StarterCard starterCard) {
        this.starterCard = starterCard;
        this.cliState = CLIState.CHOOSING_STARTER;
        this.allowedCommands.add("starter");

        printStarterCard();

        System.out.println();
    }

    /**
     * Tells the client that the color-choosing phase has begun.
     */
    @Override
    public synchronized void choosingColors() {
        System.out.println("\nPlayers are now choosing their color.");
        System.out.println("\nPlease wait for your turn.");
    }

    /**
     * Prompts the client to choose their color.
     *
     * @param colorChoices The possible choices for the player's color.
     */
    @Override
    public synchronized void promptColorChoice(List<PlayerColor> colorChoices) {
        this.cliState = CLIState.CHOOSING_COLOR;
        this.allowedCommands.add("color");
        this.colorChoices = colorChoices;

        System.out.println("\nChoose a color between:");
        for(PlayerColor color : colorChoices) {
            CLIText colorLabel = new CLIText("██", color);
            CLIText colorLabel2 = new CLIText(color.name().toLowerCase());
            System.out.print("\t");
            colorLabel.mergeText(colorLabel2, 0, 3);
            colorLabel.printText();
        }
        System.out.println();
    }

    /**
     * Sets the player's color. If the player is still in the prompt because he didn't choose in time, the prompt is invalidated
     *
     * @param username The username whose color is being given.
     * @param color    The color assigned to the player.
     */
    @Override
    public synchronized void setColor(String username, PlayerColor color) {
        this.playerColors.put(username, color);
        if (this.username.equals(username)) {
            this.allowedCommands.remove("color");
        }
        System.out.printf("\nPlayer %s's color is %s.\n", username, color.name().toLowerCase());
        printCommandPrompt();
    }

    /**
     * Tells the client that the cards for the game are being drawn.
     */
    @Override
    public synchronized void drawingCards() {
        System.out.println("\nCards are being drawn.\n");
    }

    /**
     * Sets the player's hand.
     *
     * @param hand The player's hand.
     */
    @Override
    public synchronized void setHand(List<PlayableCard> hand) {
        this.hand = new ArrayList<>(hand);
        this.allowedCommands.add("hand");

        printHand();
    }

    /**
     * DOCME
     *
     * @param card
     */
    @Override
    public synchronized void addCardToHand(PlayableCard card) {
        this.hand.add(card);

        printHand();
    }

    /**
     * DOCME
     *
     * @param card
     */
    @Override
    public synchronized void removeCardFromHand(PlayableCard card) {
        this.hand.remove(card);
    }

    /**
     * Sets the given player's restricted hand.
     *
     * @param username The username of the player whose hand is being given.
     * @param hand     The restricted hand.
     */
    @Override
    public synchronized void setOtherHand(String username, List<RestrictedCard> hand) {
        this.otherHands.put(username, new ArrayList<>(hand));
        this.allowedCommands.add("hand");
    }

    /**
     * DOCME
     *
     * @param username
     * @param newCard
     */
    @Override
    public synchronized void addCardToOtherHand(String username, RestrictedCard newCard) {
        this.otherHands.get(username).add(newCard);
    }

    /**
     * DOCME
     *
     * @param username
     * @param cardToRemove
     */
    @Override
    public synchronized void removeCardFromOtherHand(String username, RestrictedCard cardToRemove) {
        this.otherHands.get(username).remove(cardToRemove);
    }

    /**
     * DOCME
     *
     * @param username
     * @param cardPlacementOrder
     * @param field
     * @param activeSides
     */
    @Override
    public synchronized void setPlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides) {
        if (this.username.equals(username)) {
            this.allowedCommands.remove("starter");
        }
        this.allowedCommands.add("play_area");
        this.allowedCommands.add("scroll_view");
        this.playAreas.put(username, new CLIPlayArea(cardPlacementOrder, field, activeSides));
    }

    /**
     * Adds the given card to the given player's play area.
     *
     * @param username The username of the player who played the card.
     * @param card     The played card.
     * @param side     The card the new card was played on.
     * @param pos      The position where the new card was played.
     */
    @Override
    public synchronized void playCard(String username, BoardCard card, SideType side, Position pos) {
        this.playAreas.get(username).addCard(card, side, pos);
        this.allowedCommands.remove("play_card");
        if (username.equals(this.username)) {
            if (dontDraw) {
                cliState = CLIState.IN_GAME;
            } else {
                cliState = CLIState.DRAWING_CARD;
                this.allowedCommands.add("draw_card");
            }
        }
    }

    /**
     * Sets a player's number of game points.
     *
     * @param username The username of the player whose points are being set.
     * @param gamePoints  The given player's number of game points.
     */
    @Override
    public synchronized void setGamePoints(String username, int gamePoints) {
        this.gamePoints.put(username, gamePoints);
    }

    /**
     * Sets a player's number of objective points.
     *
     * @param username     The username of the player whose points are being set.
     * @param objectivePoints The given player's number of objective points.
     */
    @Override
    public synchronized void setObjectivePoints(String username, int objectivePoints) {
        this.objectivePoints.put(username, objectivePoints);
    }

    /**
     * Sets the common objectives for the game.
     *
     * @param commonObjectives The common objectives. Should always contain 2 elements.
     */
    @Override
    public synchronized void setCommonObjectives(ObjectiveCard[] commonObjectives) {
        this.commonObjectiveCards = commonObjectives;
        this.allowedCommands.add("common_objectives");
        printCommonObjectives();
    }

    /**
     * Prompts the player to choose their objective from the ones given.
     *
     * @param possiblePersonalObjectives The possible objectives the player can choose from. Should always contain 2 cards.
     */
    @Override
    public synchronized void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) {
        this.cliState = CLIState.CHOOSING_OBJECTIVE;
        this.personalObjectiveOptions = possiblePersonalObjectives;
        this.allowedCommands.add("objective");
        printObjectiveOptions();

        printCommandPrompt();
    }

    /**
     * Sets the player's personal objective.
     *
     * @param personalObjective The player's personal objective.
     */
    @Override
    public synchronized void setPersonalObjective(ObjectiveCard personalObjective) {
        this.allowedCommands.remove("objective");
        this.allowedCommands.add("objectives");
        this.personalObjective = personalObjective;
        System.out.println("\nYour personal objective is:");
        CLIAssetRegistry.getCLIAssetRegistry().getCard(personalObjective.getName()).front().printText();
        printCommandPrompt();
    }

    /**
     * Sets the turn order for the game.
     *
     * @param usernames The turn order. Should always contain as many usernames as were added at the beginning of the game.
     */
    @Override
    public synchronized void setStartOrder(List<String> usernames) {
        this.turnOrder = usernames;
        System.out.println("\nPlayers will play in the order:");
        for(String username : usernames) {
            System.out.printf("%s%s", usernames.indexOf(username) == 0 ? "" : ", ",username);
        }
        System.out.print("\n\n");
    }

    /**
     * Tells the client that it is the given player's turn to play.
     *
     * @param username The player's username.
     */
    @Override
    public synchronized void turn(String username) {
        if(username.equals(this.username)) {
            System.out.println("\nIt's your turn! You can now play a card, then draw one.");
            cliState = CLIState.PLAYING_CARD;
            this.allowedCommands.add("play_card");
        } else {
            System.out.printf("\nIt's %s's turn.", username);
        }

        printCommandPrompt();
    }

    /**
     * Tells the client the winners of the game.
     *
     * @param winnerUsernames The winners of the game.
     */
    @Override
    public synchronized void setWinners(List<String> winnerUsernames) {
        this.allowedCommands.add("winners");
        this.winners = winnerUsernames;
        printWinners();
    }

    /**
     * Adds all the messages given to the player's chat.
     *
     * @param messages The chat messages to add.
     */
    @Override
    public synchronized void addMessages(List<ChatMessage> messages) {
        this.unreadChat.addAll(messages);
    }

    /**
     * Adds the given message to the player's chat.
     *
     * @param message The new message.
     */
    @Override
    public synchronized void addMessage(ChatMessage message) {
        if (message.text().equals("rick")) {
            printRick();
        } else {
            this.unreadChat.add(message);
        }
    }

    /**
     * Tells the client that an error has occured.
     *
     * @param errorMessage The message that should be displayed to the user.
     */
    @Override
    public synchronized void promptError(String errorMessage) {
        System.err.println(errorMessage);
        printCommandPrompt();
    }

    /**
     * Forces the client to redraw the view.
     */
    @Override
    public synchronized void redrawView() {

    }

    /**
     * Notifies the client that from now on they shouldn't draw cards anymore.
     */
    @Override
    public synchronized void notifyDontDraw() {
        this.dontDraw = true;
        System.out.println("\nFrom now on, you shouldn't draw cards anymore.");
    }

    /**
     * Tells the client that another client has disconnected. This ends the game, if it had started. If the game hadn't started already, the player is simply removed.
     *
     * @param whoDisconnected The username of the player who disconnected.
     */
    @Override
    public synchronized void signalDisconnection(String whoDisconnected) {
        System.out.printf("\n%s disconnected. The game ends here.\n", whoDisconnected);
        resetToStartup();
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

        allowedCommands.clear();
        allowedCommands.add("help");
        allowedCommands.add("join_game");
        allowedCommands.add("create_game");
        allowedCommands.add("exit");
    }

    @Override
    public synchronized void signalDeadlock(String username) {
        if (username.equals(this.username)) {
            System.out.println("\nYou have deadlocked yourself!");
        } else {
            System.out.printf("\n%s has deadlocked themselves!\n", username);
        }

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
        hand.mergeText(backLabel, CARD_HEIGHT+2, 0);
        hand.mergeText(indexLabel, 2*CARD_HEIGHT+4, 0);
        for(int i = 0; i<this.hand.size(); i++) {
            CLICardAsset cardAsset = CLIAssetRegistry.getCLIAssetRegistry().getCard(this.hand.get(i).getName());
            CLIText thisIndexLabel = new CLIText(String.format("[%d]", i+1));
            hand.mergeText(thisIndexLabel, 2*CARD_HEIGHT+4, 2+i*(CARD_WIDTH+2)+CARD_WIDTH/2);
            hand.mergeText(cardAsset.front(), 1, 3+i*(CARD_WIDTH+2));
            hand.mergeText(cardAsset.back(), CARD_HEIGHT+3, 3+i*(CARD_WIDTH+2));
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
        for(int i = 0; i<restrictedHand.size(); i++) {
            CLIText backAsset = CLIAssetRegistry.getCLIAssetRegistry().getCardBack(restrictedHand.get(i));
            hand.mergeText(backAsset, 0, 3+i*(CARD_WIDTH+2));
        }
        hand.printText();
        printCommandPrompt();
    }

    public synchronized void printObjectiveOptions() {
        System.out.println("\nChoose an objective between:");
        printObjectives(this.personalObjectiveOptions, true);
        printCommandPrompt();
    }

    public synchronized void printCommonObjectives() {
        System.out.println("\nThe common objectives are:");
        printObjectives(List.of(commonObjectiveCards), false);
        printCommandPrompt();
    }

    public synchronized void printDrawOptions() {
        System.out.println("\nThe draw options are:");
        CLIText drawOptions = new CLIText();
        drawOptions.mergeText(resourcesLabel, 0, 0);
        drawOptions.mergeText(goldLabel, CARD_HEIGHT+2, 0);
        drawOptions.mergeText(indexLabel, 2*CARD_HEIGHT+4, 0);

        CLIText resourceDeckTop = CLIAssetRegistry.getCLIAssetRegistry().getCardBack(new RestrictedCard(PlayableCardType.RESOURCE, resourceDeckTopType));
        CLIText goldDeckTop = CLIAssetRegistry.getCLIAssetRegistry().getCardBack(new RestrictedCard(PlayableCardType.GOLD, goldDeckTopType));
        CLIText commonResource1 = CLIAssetRegistry.getCLIAssetRegistry().getCard(commonResourceCards[0].getName()).front();
        CLIText commonResource2 = CLIAssetRegistry.getCLIAssetRegistry().getCard(commonResourceCards[1].getName()).front();
        CLIText commonGold1 = CLIAssetRegistry.getCLIAssetRegistry().getCard(commonGoldCards[0].getName()).front();
        CLIText commonGold2 = CLIAssetRegistry.getCLIAssetRegistry().getCard(commonGoldCards[1].getName()).front();

        drawOptions.mergeText(resourceDeckTop, 1, 5);
        drawOptions.mergeText(goldDeckTop, CARD_HEIGHT+3, 5);
        drawOptions.mergeText(commonResource1, 1, 10+(CARD_WIDTH+2));
        drawOptions.mergeText(commonResource2, 1, 10+2*(CARD_WIDTH+2));
        drawOptions.mergeText(commonGold1, CARD_HEIGHT+3, 10+(CARD_WIDTH+2));
        drawOptions.mergeText(commonGold2, CARD_HEIGHT+3, 10+2*(CARD_WIDTH+2));

        drawOptions.mergeText(deckLabel, 2*CARD_HEIGHT+4, 2+CARD_WIDTH/2);
        drawOptions.mergeText(oneLabel, 2*CARD_HEIGHT+4, 9+(CARD_WIDTH+2)+CARD_WIDTH/2);
        drawOptions.mergeText(twoLabel, 2*CARD_HEIGHT+4, 9+2*(CARD_WIDTH+2)+CARD_WIDTH/2);

        drawOptions.printText();
        printCommandPrompt();
    }

    public synchronized void printObjectives(List<ObjectiveCard> objectives, boolean printIndices) {
        System.out.println();
        CLIText objectivesText = new CLIText();
        if (printIndices) {
            objectivesText.mergeText(indexLabel, CARD_HEIGHT+1, 0);
        }

        for(int i = 0; i<objectives.size(); i++) {
            ObjectiveCard card = objectives.get(i);
            CLIText cardAsset = CLIAssetRegistry.getCLIAssetRegistry().getCard(card.getName()).front();
            if (printIndices) {
                CLIText thisIndexLabel = new CLIText(String.format("[%d]", i + 1));
                objectivesText.mergeText(thisIndexLabel, CARD_HEIGHT + 1, 2 + i * (CARD_WIDTH + 2) + CARD_WIDTH / 2);
            }
            objectivesText.mergeText(cardAsset, 0, 3+i*(CARD_WIDTH+2));
        }
        objectivesText.printText();
        printCommandPrompt();
    }

    public synchronized void printAllObjectives() {
        CLIText objectiveText = new CLIText();
        CLIText personalObjective = CLIAssetRegistry.getCLIAssetRegistry().getCard(this.personalObjective.getName()).front();
        CLIText commonObjective1 = CLIAssetRegistry.getCLIAssetRegistry().getCard(commonObjectiveCards[0].getName()).front();
        CLIText commonObjective2 = CLIAssetRegistry.getCLIAssetRegistry().getCard(commonObjectiveCards[1].getName()).front();

        objectiveText.mergeText(commonObjectivesLabel, 0, 5+(CARD_WIDTH+2));
        objectiveText.mergeText(personalObjectivesLabel, 0, 0);
        objectiveText.mergeText(personalObjective, 1, 5);
        objectiveText.mergeText(commonObjective1, 1, 10+(CARD_WIDTH+2));
        objectiveText.mergeText(commonObjective2, 1, 10+2*(CARD_WIDTH+2));

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

    public synchronized void printGameId() {
        System.out.printf("This game's id is %s.\n", this.gameId);
        printCommandPrompt();
    }

    public synchronized void printPlayers() {
        System.out.println("Players currently in the game:");

        for(String username : (turnOrder != null ? turnOrder : playerUsernames)) {
            CLIText colorLabel = new CLIText("██", this.playerColors.get(username));
            CLIText userLabel = new CLIText(username + (turnOrder != null && turnOrder.indexOf(username) == 1 ? "(starter player)" : ""));
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
            case "center" -> this.playAreas.get(lastPrintedPlayArea).resetView();
        }
    }

    public synchronized void printPoints() {
        System.out.println("\nCurrent points (in order from most to least points):");

        List<String> sortedUsernames =
                this.playerUsernames
                .stream()
                .sorted(
                        (s1, s2) -> Integer.compare(
                                this.gamePoints.getOrDefault(s1, 0) + this.objectivePoints.getOrDefault(s1, 0),
                                this.gamePoints.getOrDefault(s2, 0) + this.objectivePoints.getOrDefault(s2, 0)
                        )
                ).toList().reversed();

        for(String username : sortedUsernames) {
            CLIText colorLabel = new CLIText("██", this.playerColors.get(username));
            CLIText pointsLabel;
            if (this.objectivePoints.get(username) != null) {
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

        System.out.println("\nThe winners are...");
        for(String username : this.winners) {
            System.out.printf("%s%s", this.winners.indexOf(username) == 0 ? "" : ", ", username);
        }
        System.out.print("\n");
        printCommandPrompt();
    }

    public synchronized void printHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("\thelp - Prints the list of available commands.");
        if (cliState == CLIState.STARTUP) {
            System.out.println("\tjoin_game [username] [gameId] - Joins a game with the given username.");
            System.out.println("\tcreate_game [username] [numPlayers] - Creates a new game with the given number of players, and joins it with the given username.");
            System.out.println("\texit - Closes the game.");
        } else {
            //The player is either in a lobby or in a game
            System.out.println("\tid - Prints this game's id.");
            System.out.println("\tplayers - Prints the usernames of the players in the game. If a turn order has been chosen, the usernames will be printed in the correct order.");
            if (cliState != CLIState.GAME_ENDED) {
                System.out.println("\tchat - Prints unread chat messages.");
                System.out.println("\tchat_history - Prints the whole chat history.");
                System.out.println("\tchat [message] - Sends a message to the public chat.");
                System.out.println("\twhisper [receiver username] [message] - Sends a private message to the specified player.");
            }
            //Specific commands if the player is in a game
            if (cliState != CLIState.LOBBY) {
                //At this point common cards have been drawn, so you can see them
                System.out.println("\tdraw_options - Prints the options from which everyone can draw from (decks and common cards).");
                switch (cliState) {
                    case CLIState.CHOOSING_STARTER -> System.out.println("\tstarter [front|back] - Places your starter card on the specified side. If no side is specified, prints your starter card.");
                    case CLIState.CHOOSING_COLOR -> System.out.println("\tcolor [color] - Chooses your color. If no color is given, prints the options available.");
                    case CLIState.CHOOSING_OBJECTIVE -> {
                        System.out.println("\tcommon_objectives - Prints the common objectives for this game.");
                        System.out.println("\tobjective [1|2] - Sets your objective. If no index is given, prints your objective options.");
                    }
                    default -> {
                        //The game is ongoing, so you can see other people's hands and play areas, as well as your own
                        System.out.println("\thand [username] - Prints the hand of the specified player. If no username is given, prints your own hand.");
                        System.out.println("\tplay_area [username] - Prints the play area of the specified player. If no username is given, prints your own play area.");
                        System.out.println("\tscroll_view [left|right|center] [(if left/right) offset] - Scrolls the view you have of the last printed play area in the given direction; \"center\" resets the view so that the starter card is centered.");
                        System.out.println("\tobjectives - Prints your personal objective, and the game's common objectives.");
                        System.out.println("\tpoints - Prints the amount of points each player has currently.");
                        switch (cliState) {
                            case PLAYING_CARD -> {
                                //The player must play a card, so the relative command is available
                                System.out.println("\tplay_card [index] [front|back] [position: x;y] - Plays the specified card, on the given side, in the given position.");
                            }
                            case DRAWING_CARD -> {
                                //The player must draw a card, so the relative command is available
                                System.out.println("\tdraw_card [deck|common] [resource|gold] [(if common) index] - Draws the specified card.");
                            }
                            case GAME_ENDED -> {
                                //The game has ended, so the winner usernames are available.
                                System.out.println("\twinners - Prints the usernames of the players who won this game.");
                            }
                        }
                    }
                }
            }
            System.out.println("\texit - Disconnects from the current game.");
        }
        printCommandPrompt();
    }

    public synchronized void printUnreadChat() {
        if (unreadChat.isEmpty()) {
            System.out.println("You have no unread messages. Type \"chat_history\" to see older messages");
            return;
        }

        System.out.printf("%d unread message%s:\n", unreadChat.size(), unreadChat.size() == 1 ? "" : "s");
        for(ChatMessage message : unreadChat) {
            System.out.println(message);
        }

        chatHistory.addAll(unreadChat);
        unreadChat.clear();

        printCommandPrompt();
    }

    public synchronized void printChatHistory() {
        for(ChatMessage message : chatHistory) {
            System.out.println(message);
        }

        if (!unreadChat.isEmpty()) {
            System.out.printf("You also have %d unread message%s! Type \"chat\" to see them.\n", unreadChat.size(), unreadChat.size() == 1 ? "" : "s");
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
    }

    public synchronized boolean allowedCommand(String command) {
        return this.allowedCommands.contains(command);
    }

    public synchronized boolean validUsername(String username) {
        return this.playerUsernames.contains(username);
    }

    public void printCommandPrompt() {
        System.out.print("\n>> ");
    }

    public synchronized CLIState getCliState() {
        return cliState;
    }

    public synchronized boolean validColorChoice(PlayerColor playerColor) {
        return this.colorChoices.contains(playerColor);
    }

    public synchronized ObjectiveCard getPersonalObjectiveOption(int index) {
        return this.personalObjectiveOptions.get(index-1);
    }

    public synchronized List<PlayableCard> getHand() {
        return hand;
    }

    public enum CLIState {
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
