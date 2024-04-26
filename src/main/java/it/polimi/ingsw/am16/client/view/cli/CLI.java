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
    private static final CLIText indexLabel = new CLIText(("Index:"));

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
    private CLICardAsset starterCardAsset;
    private Map<String, PlayerColor> playerColors;
    private List<PlayableCard> hand;
    private Map<String, List<RestrictedCard>> otherHands;
    private Map<String, CLIPlayArea> playAreas;
    private Map<String, Integer> gamePoints;
    private Map<String, Integer> objectivePoints;
    private List<String> turnOrder;
    private boolean dontDraw;

    /**
     * DOCME
     */
    public CLI() {
        this.cliState = CLIState.STARTUP;
        this.playerUsernames = new ArrayList<>();
        this.playerColors = new HashMap<>();
        this.otherHands = new HashMap<>();
        this.hand = new ArrayList<>();
        this.playAreas = new HashMap<>();
        this.dontDraw = false;
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
     * Sets the game state. To be called when the game's state changes.
     *
     * @param state The new game state.
     */
    @Override
    public void setGameState(GameState state) {
        this.gameState = state;
        System.out.printf("\n%s\n", state.toString());
        //TODO print some cool messages and set the cli state
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

        System.out.println("\nChoose a color between:");
        for(PlayerColor color : colorChoices) {
            System.out.println(color.name().toLowerCase());
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
        System.out.printf("\nPlayer %s's color is %s.\n\n", username, color.name().toLowerCase());
    }

    /**
     * Tells the client that the cards for the game are being drawn.
     */
    @Override
    public void drawingCards() throws RemoteException {
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
        if (username.equals(this.username)) {
            if (dontDraw) {
                cliState = CLIState.IN_GAME;
            } else {
                cliState = CLIState.DRAWING_CARD;
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

        System.out.println("\nChoose an objective between:");
        printObjectives(possiblePersonalObjectives);
        System.out.println();
    }

    /**
     * Sets the player's personal objective.
     *
     * @param personalObjective The player's personal objective.
     */
    @Override
    public void setPersonalObjective(ObjectiveCard personalObjective) throws RemoteException {
        System.out.println("\nYour personal objective is:");
        CLIAssetRegistry.getCLIAssetRegistry().getCard(personalObjective.getName()).front().printText();
        System.out.println();
    }

    /**
     * Sets the turn order for the game.
     *
     * @param usernames The turn order. Should always contain as many usernames as were added at the beginning of the game.
     */
    @Override
    public void setStartOrder(List<String> usernames) {
        this.turnOrder = usernames;
        System.out.println("P\nlayers will play in the order:");
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
    public void turn(String username) throws RemoteException {
        if(username.equals(this.username)) {
            System.out.println("\nIt's your turn! You can now play a card, then draw one.");
            cliState = CLIState.PLAYING_CARD;
        } else {
            System.out.printf("\nIt's %s turn.", username);
        }
    }

    /**
     * Tells the client the winners of the game.
     *
     * @param winnerUsernames The winners of the game.
     */
    @Override
    public void setWinners(List<String> winnerUsernames) throws RemoteException {
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

    }

    /**
     * Adds the given message to the player's chat.
     *
     * @param message The new message.
     */
    @Override
    public void addMessage(ChatMessage message) {

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
        cliState = CLIState.STARTUP;
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
     * DOCME
     */
    public void printHand() {
        System.out.println("\nYour hand is:\n");
        CLIText hand = new CLIText();
        hand.mergeText(frontLabel, 0, 0);
        hand.mergeText(backLabel, CARD_HEIGHT+2, 0);
        hand.mergeText(indexLabel, 2*CARD_HEIGHT+4, 0);
        for(int i = 0; i<this.hand.size(); i++) {
            CLICardAsset cardAsset = CLIAssetRegistry.getCLIAssetRegistry().getCard(this.hand.get(i).getName());
            CLIText thisIndexLabel = new CLIText(String.format("[%d]", i));
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
        System.out.printf("\nThe backs of %s's cards are:\n\n", username);
        List<RestrictedCard> restrictedHand = otherHands.get(username);
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
        printObjectives(List.of(commonObjectiveCards));
        printCommandPrompt();
    }

    /**
     * DOCME
     * @param objectives
     */
    public void printObjectives(List<ObjectiveCard> objectives) {
        System.out.println();
        CLIText commonObjectives = new CLIText();
        for(int i = 0; i<objectives.size(); i++) {
            ObjectiveCard card = objectives.get(i);
            CLIText cardAsset = CLIAssetRegistry.getCLIAssetRegistry().getCard(card.getName()).front();
            commonObjectives.mergeText(cardAsset, 0, 3+i*(CARD_WIDTH+2));
        }
        commonObjectives.printText();
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

    /**
     * DOCME
     */
    public void printHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("\thelp");
        if (cliState == CLIState.STARTUP) {
            System.out.println("\tjoin_game [username] [gameId]");
            System.out.println("\tcreate_game [username]");
        }
        System.out.println("\texit");
        printCommandPrompt();
    }

    /**
     * DOCME
     */
    public void printCommandPrompt() {
        System.out.print("\n>> ");
    }

    /**
     * DOCME
     */
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
