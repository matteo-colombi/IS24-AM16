package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.client.RemoteViewInterface;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;

import java.rmi.RemoteException;
import java.util.*;

import static it.polimi.ingsw.am16.client.view.cli.CLIConstants.CARD_HEIGHT;
import static it.polimi.ingsw.am16.client.view.cli.CLIConstants.CARD_WIDTH;

public class CLI implements RemoteViewInterface {

    private static final CLIText frontLabel = new CLIText("Fronts:");
    private static final CLIText backLabel = new CLIText("Backs:");
    private static final CLIText indexLabel = new CLIText(("Indices:"));
    private static final CLIText resourcesLabel = new CLIText("Resources:");
    private static final CLIText goldLabel = new CLIText("Gold:");
    private static final CLIText deckLabel = new CLIText("[Decks]");
    private static final CLIText oneLabel = new CLIText("[1]");
    private static final CLIText twoLabel = new CLIText("[2]");

    private String username;
    private CLIState cliState;
    private final Set<String> allowedCommands;
    private List<String> playerUsernames;
    private GameState gameState;
    private PlayableCard[] commonResourceCards;
    private PlayableCard[] commonGoldCards;
    private ObjectiveCard[] commonObjectiveCards;
    private ResourceType resourceDeckTopType;
    private ResourceType goldDeckTopType;
    private StarterCard starterCard;
    private CLICardAsset starterCardAsset;
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
    private boolean dontDraw;
    private String lastPrintedPlayArea;

    private List<ChatMessage> chatHistory;
    private List<ChatMessage> unreadChat;

    /**
     * DOCME
     */
    public CLI() {
        this.cliState = CLIState.STARTUP;
        this.allowedCommands = new HashSet<>();
        this.allowedCommands.add("help");
        this.allowedCommands.add("join_game");
        this.allowedCommands.add("create_game");
        this.allowedCommands.add("exit");
        this.username = null;
        this.commonResourceCards = null;
        this.commonGoldCards = null;
        this.commonObjectiveCards = null;
        this.resourceDeckTopType = null;
        this.goldDeckTopType = null;
        this.starterCard = null;
        this.starterCardAsset = null;
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
    }

    /**
     * Tells the view that they have joined a game with the given username.
     *
     * @param username The username the player has joined the game with.
     */
    @Override
    public void joinGame(String username) {
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
        this.allowedCommands.add("players");
        this.allowedCommands.add("chat_history");
        this.allowedCommands.add("chat");
        this.allowedCommands.add("chat_private");
        this.allowedCommands.add("rick");
        System.out.println("\nJoined the game. Your username is: " + username + "\n");
        printCommandPrompt();
    }

    /**
     * Adds a player to the game. Used to communicate the connection of a new player.
     *
     * @param username The new player's username.
     */
    @Override
    public void addPlayer(String username) {
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
    public void setPlayers(List<String> usernames) {
        this.playerUsernames.addAll(usernames);
    }

    /**
     * Sets the game state. To be called when the game's state changes.
     *
     * @param state The new game state.
     */
    @Override
    public void setGameState(GameState state) {
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
    public void setCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) {
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
    public void setDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) {
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
    public void promptStarterChoice(StarterCard starterCard) {
        this.starterCard = starterCard;
        this.starterCardAsset = CLIAssetRegistry.getCLIAssetRegistry().getCard(starterCard.getName());
        this.cliState = CLIState.CHOOSING_STARTER;
        this.allowedCommands.add("starter");

        System.out.println("\nChoose on which side to place your starter card.");
        printStarterCard(starterCard);

        System.out.println();
    }

    /**
     * Tells the client that the color-choosing phase has begun.
     */
    @Override
    public void choosingColors() {
        System.out.println("\nPlayers are now choosing their color.");
        System.out.println("\nPlease wait for your turn.");
    }

    /**
     * Prompts the client to choose their color.
     *
     * @param colorChoices The possible choices for the player's color.
     */
    @Override
    public void promptColorChoice(List<PlayerColor> colorChoices) {
        this.cliState = CLIState.CHOOSING_COLOR;
        this.allowedCommands.add("color");
        this.colorChoices = colorChoices;

        System.out.println("\nChoose a color between:");
        for(PlayerColor color : colorChoices) {
            CLIText colorLabel = new CLIText("██", color);
            CLIText colorLabel2 = new CLIText(color.name().toLowerCase());
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
    public void setColor(String username, PlayerColor color) {
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
    public void drawingCards() {
        System.out.println("\nCards are being drawn.\n");
    }

    /**
     * Sets the player's hand.
     *
     * @param hand The player's hand.
     */
    @Override
    public void setHand(List<PlayableCard> hand) {
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
    public void addCardToHand(PlayableCard card) {
        this.hand.add(card);

        printHand();
    }

    /**
     * DOCME
     *
     * @param card
     */
    @Override
    public void removeCardFromHand(PlayableCard card) {
        this.hand.remove(card);

        printHand();
    }

    /**
     * Sets the given player's restricted hand.
     *
     * @param username The username of the player whose hand is being given.
     * @param hand     The restricted hand.
     */
    @Override
    public void setOtherHand(String username, List<RestrictedCard> hand) {
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
    public void addCardToOtherHand(String username, RestrictedCard newCard) {
        this.otherHands.get(username).add(newCard);
    }

    /**
     * DOCME
     *
     * @param username
     * @param cardToRemove
     */
    @Override
    public void removeCardFromOtherHand(String username, RestrictedCard cardToRemove) {
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
    public void setPlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides) {
        if (this.username.equals(username)) {
            this.allowedCommands.remove("starter");
        }
        this.allowedCommands.add("play_area");
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
    public void playCard(String username, BoardCard card, SideType side, Position pos) {
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
    public void setGamePoints(String username, int gamePoints) {
        this.gamePoints.put(username, gamePoints);
    }

    /**
     * Sets a player's number of objective points.
     *
     * @param username     The username of the player whose points are being set.
     * @param objectivePoints The given player's number of objective points.
     */
    @Override
    public void setObjectivePoints(String username, int objectivePoints) {
        this.objectivePoints.put(username, objectivePoints);
    }

    /**
     * Sets the common objectives for the game.
     *
     * @param commonObjectives The common objectives. Should always contain 2 elements.
     */
    @Override
    public void setCommonObjectives(ObjectiveCard[] commonObjectives) {
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
    public void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) {
        this.cliState = CLIState.CHOOSING_OBJECTIVE;
        this.personalObjectiveOptions = possiblePersonalObjectives;
        this.allowedCommands.add("objective");
        System.out.println("\nChoose an objective between:");
        printObjectives(this.personalObjectiveOptions, true);
        System.out.println();
    }

    /**
     * Sets the player's personal objective.
     *
     * @param personalObjective The player's personal objective.
     */
    @Override
    public void setPersonalObjective(ObjectiveCard personalObjective) {
        this.allowedCommands.remove("objective");
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
    public void setStartOrder(List<String> usernames) {
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
    public void turn(String username) {
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
    public void setWinners(List<String> winnerUsernames) {
        this.allowedCommands.add("winners");
        System.out.println("\nThe winners are...");
        for(String username : winnerUsernames) {
            System.out.printf("%s%s", winnerUsernames.indexOf(username) == 0 ? "" : ", ",username);
        }
        System.out.print("\n\n");
    }

    /**
     * Adds all the messages given to the player's chat.
     *
     * @param messages The chat messages to add.
     */
    @Override
    public void addMessages(List<ChatMessage> messages) {
        this.unreadChat.addAll(messages);
    }

    /**
     * Adds the given message to the player's chat.
     *
     * @param message The new message.
     */
    @Override
    public void addMessage(ChatMessage message) {
        this.unreadChat.add(message);
    }

    /**
     * Tells the client that an error has occured.
     *
     * @param errorMessage The message that should be displayed to the user.
     */
    @Override
    public void promptError(String errorMessage) {
        System.err.println("\nAn error has occurred: " + errorMessage);
        printCommandPrompt();
    }

    /**
     * Forces the client to redraw the view.
     */
    @Override
    public void redrawView() {

    }

    /**
     * Notifies the client that from now on they shouldn't draw cards anymore.
     */
    @Override
    public void notifyDontDraw() {
        this.dontDraw = true;
        System.out.println("\nFrom now on, you shouldn't draw cards anymore.");
    }

    /**
     * Tells the client that another client has disconnected. This ends the game, if it had started. If the game hadn't started already, the player is simply removed.
     *
     * @param whoDisconnected The username of the player who disconnected.
     */
    @Override
    public void signalDisconnection(String whoDisconnected) {
        System.out.printf("\n%s disconnected. The game ends here.\n", whoDisconnected);
        resetToStartup();
    }

    /**
     * DOCME
     */
    public void resetToStartup() {
        cliState = CLIState.STARTUP;
        username = null;
        commonResourceCards = null;
        commonGoldCards = null;
        commonObjectiveCards = null;
        resourceDeckTopType = null;
        goldDeckTopType = null;
        starterCard = null;
        starterCardAsset = null;
        gamePoints = null;
        objectivePoints = null;
        turnOrder = null;
        playerUsernames = null;
        playerColors = null;
        otherHands = null;
        hand = null;
        playAreas = null;
        dontDraw = false;

        allowedCommands.clear();
        allowedCommands.add("help");
        allowedCommands.add("join_game");
        allowedCommands.add("create_game");
        allowedCommands.add("exit");
    }

    /**
     * DOCME
     *
     * @param username
     */
    @Override
    public void signalDeadlock(String username) {
        if (username.equals(this.username)) {
            System.out.println("\nYou have deadlocked yourself!");
        } else {
            System.out.printf("\n%s has deadlocked themselves!\n", username);
        }

    }

    /**
     * You saw nothing ;)
     */
    @Override
    public void rick() throws RemoteException {
        CLIText rick = CLIAssetRegistry.getCLIAssetRegistry().getRick();
        System.out.print("\n\n");
        rick.printText();
        System.out.println("You got rickrolled!\n");
        printCommandPrompt();
    }

    /**
     * DOCME
     */
    public void printHand() {
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

    /**
     * DOCME
     * @param username
     */
    public void printOtherHand(String username) {
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

    /**
     * DOCME
     */
    public void printCommonObjectives() {
        System.out.println("\nThe common objectives are:");
        printObjectives(List.of(commonObjectiveCards), false);
        printCommandPrompt();
    }

    public void printDrawOptions() {
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
        drawOptions.mergeText(commonResource1, 1, 9+(CARD_WIDTH+2));
        drawOptions.mergeText(commonResource2, 1, 9+2*(CARD_WIDTH+2));
        drawOptions.mergeText(commonGold1, CARD_HEIGHT+3, 9+(CARD_WIDTH+2));
        drawOptions.mergeText(commonGold2, CARD_HEIGHT+3, 9+2*(CARD_WIDTH+2));

        drawOptions.mergeText(deckLabel, 2*CARD_HEIGHT+4, 2+CARD_WIDTH/2);
        drawOptions.mergeText(oneLabel, 2*CARD_HEIGHT+4, 8+(CARD_WIDTH+2)+CARD_WIDTH/2);
        drawOptions.mergeText(twoLabel, 2*CARD_HEIGHT+4, 8+2*(CARD_WIDTH+2)+CARD_WIDTH/2);

        drawOptions.printText();
        printCommandPrompt();
    }

    /**
     * DOCME
     * @param objectives
     */
    public void printObjectives(List<ObjectiveCard> objectives, boolean printIndices) {
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
                objectivesText.mergeText(thisIndexLabel, CARD_HEIGHT + 2, 2 + i * (CARD_WIDTH + 2) + CARD_WIDTH / 2);
            }
            objectivesText.mergeText(cardAsset, 0, 3+i*(CARD_WIDTH+1));
        }
        objectivesText.printText();
        printCommandPrompt();
    }

    /**
     * DOCME
     * @param starterCard
     */
    public void printStarterCard(StarterCard starterCard) {
        System.out.println();

        CLIText starterCardText = new CLIText();
        CLIText frontLabel = new CLIText("Front:");
        CLIText backLabel = new CLIText("Back:");

        CLICardAsset cardAsset = CLIAssetRegistry.getCLIAssetRegistry().getCard(starterCard.getName());

        starterCardText.mergeText(frontLabel, 0, 0);
        starterCardText.mergeText(backLabel, 0, 25);
        starterCardText.mergeText(cardAsset.front(), 1, 2);
        starterCardText.mergeText(cardAsset.back(), 1, 27);
        starterCardText.printText();

        printCommandPrompt();
    }

    public void printPlayers() {
        System.out.println("Players currently in the game:");
//        if (turnOrder == null) {
//            CLIText colorLabel = new CLIText("██", this.playerColors.get(this.username));
//            CLIText userLabel = new CLIText(this.username);
//            userLabel.mergeText(colorLabel, 0, -3);
//            System.out.print("\t");
//            userLabel.printText();
//        }
        for(String username : (turnOrder != null ? turnOrder : playerUsernames)) {
            CLIText colorLabel = new CLIText("██", this.playerColors.get(username));
            CLIText userLabel = new CLIText(username);
            userLabel.mergeText(colorLabel, 0, -3);
            System.out.print("\t");
            userLabel.printText();
        }
        printCommandPrompt();
    }

    public void printPlayArea() {
        printPlayArea(username);
    }

    public void printPlayArea(String username) {
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

        printCommandPrompt();
    }

    public void scrollView(String direction, int offset) {
        if (lastPrintedPlayArea == null) return;

        switch (direction) {
            case "left" -> this.playAreas.get(lastPrintedPlayArea).moveView(-offset);
            case "right" -> this.playAreas.get(lastPrintedPlayArea).moveView(offset);
            case "center" -> this.playAreas.get(lastPrintedPlayArea).resetView();
        }
    }

    /**
     * DOCME
     */
    public void printHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("\thelp - Prints the list of available commands.");
        if (cliState == CLIState.STARTUP) {
            System.out.println("\tjoin_game [username] [gameId] - Joins a game with the given username.");
            System.out.println("\tcreate_game [username] - Creates a new game");
            System.out.println("\texit - Closes the game.");
        } else {
            //The player is either in a lobby or in a game
            System.out.println("\tplayers - Prints the usernames of the players in the game. If a turn order has been chosen, the usernames will be printed in the correct order.");
            if (cliState != CLIState.GAME_ENDED) {
                System.out.println("\tchat - Prints unread chat messages.");
                System.out.println("\tchat_history - Prints the whole chat history.");
                System.out.println("\tchat [message] - Sends a message to the public chat.");
                System.out.println("\tchat_private [receiver username] [message] - Sends a private message to the specified player.");
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
                        System.out.println("\tcommon_objectives - Prints the common objectives for this game.");
                        System.out.println("\tpersonal_objective - Prints your own personal objective.");
                        System.out.println("\tpoints - Prints the amount of points each player has currently.");
                        switch (cliState) {
                            case PLAYING_CARD -> {
                                //The player must play a card, so the relative command is available
                                System.out.println("\tplay_card [index] [front|back] [position: (x,y)] - Plays the specified card, on the given side, in the given position.");
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

    public void printUnreadChat() {
        for(ChatMessage message : unreadChat) {
            System.out.println(message);
        }
        chatHistory.addAll(unreadChat);
        unreadChat.clear();
        printCommandPrompt();
    }

    public void printChatHistory() {
        for(ChatMessage message : chatHistory) {
            System.out.println(message);
        }
        printCommandPrompt();
    }

    public boolean allowedCommand(String command) {
        return this.allowedCommands.contains(command);
    }

    /**
     * DOCME
     */
    public void printCommandPrompt() {
        System.out.print("\n>> ");
    }

    public CLIState getCliState() {
        return cliState;
    }

    public boolean validColorChoice(PlayerColor playerColor) {
        return this.colorChoices.contains(playerColor);
    }

    public ObjectiveCard getPersonalObjectiveOption(int index) {
        return this.personalObjectiveOptions.get(index-1);
    }

    /**
     * DOCME
     */
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
