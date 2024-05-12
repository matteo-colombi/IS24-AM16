package it.polimi.ingsw.am16.client.rmi;

import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.client.view.ViewInterface;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.server.ServerInterface;
import it.polimi.ingsw.am16.server.rmi.WelcomeRMIServer;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of the client's RMI interface. This class is used to communicate with the server through Java RMI.
 */
public class RMIClientImplementation extends UnicastRemoteObject implements RemoteClientInterface {

    @Serial
    private static final long serialVersionUID = 2908073746520322817L;

    private final ViewInterface view;

    private final AtomicLong lastPinged;
    private final Timer checkConnectionTimer;
    private final ServerInterface serverInterface;

    public RMIClientImplementation(WelcomeRMIServer welcomeRMIServer, ViewInterface view) throws RemoteException {
        this.serverInterface = welcomeRMIServer.getClientHandler(this);
        this.lastPinged = new AtomicLong(System.currentTimeMillis());
        this.checkConnectionTimer = new Timer();
        this.view = view;
    }

    public ServerInterface getServerInterface() {
        return serverInterface;
    }

    /**
     * Starts the RMI client; that includes the connection check routine and the user interface.
     */
    public void start() {
        checkConnectionRoutine();
    }

    /**
     * Stops the RMI client
     */
    public void stop() {
        System.exit(0);
    }

    /**
     * Checks if the server has pinged in the last 15 seconds. If it hasn't, the connection is considered lost and the client is stopped.
     */
    private void checkConnectionRoutine() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                long diff = System.currentTimeMillis() - lastPinged.get();
                if (diff > 15000) {
                    System.out.println("\nServer hasn't pinged in a while. Considering connection as lost.");
                    System.out.println("Good bye!");
                    stop();
                }
            }
        };

        checkConnectionTimer.schedule(task, 1000, 10000);
    }

    /**
     * Show the existing game IDs to the player.
     *
     * @param gameIds        The existing games' IDs.
     * @param currentPlayers The number of current players
     * @param maxPlayers     The maximum number of players
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void notifyGames(Set<String> gameIds, Map<String, Integer> currentPlayers, Map<String, Integer> maxPlayers) throws RemoteException {
        view.printGames(gameIds, currentPlayers, maxPlayers);
    }

    /**
     * Tells the view that they have joined a game with the given username.
     *
     * @param gameId The id of the game which the player just joined.
     * @param username The username the player has joined the game with.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void joinGame(String gameId, String username) throws RemoteException {
        view.joinGame(gameId, username);
    }

    /**
     * Adds a player to the game. Used to communicate the connection of a new player.
     *
     * @param username The new player's username.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void addPlayer(String username) throws RemoteException {
        view.addPlayer(username);
    }

    /**
     * Tells the client all the usernames of the players present in the game.
     * @param usernames The list of usernames of the players present in the game.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void setPlayers(List<String> usernames) throws RemoteException {
        view.setPlayers(usernames);
    }

    /**
     * Sets the game state. To be called when the game's state changes.
     *
     * @param state The new game state.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void setGameState(GameState state) throws RemoteException {
        view.setGameState(state);
    }

    /**
     * Sets the common cards for the game. Should be called whenever these change.
     *
     * @param commonResourceCards The common resource cards (may also contain gold cards if the resource card deck is empty). Should always be of length 2.
     * @param commonGoldCards     The common gold cards (may also contain resource cards if the gold card deck is empty). Should always be of length 2.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void setCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) throws RemoteException {
        view.setCommonCards(commonResourceCards, commonGoldCards);
    }

    /**
     * Sets the types of cards at the top of the respective deck.
     *
     * @param whichDeck    The deck which we are setting the top card of.
     * @param resourceType The resource type of the card on top of the given deck.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void setDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) throws RemoteException {
        view.setDeckTopType(whichDeck, resourceType);
    }

    /**
     * Prompts the user to choose the side of the given starter card.
     *
     * @param starterCard The starter card of the player.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void promptStarterChoice(StarterCard starterCard) throws RemoteException {
        view.promptStarterChoice(starterCard);
    }

    /**
     * Tells the client that the color-choosing phase has begun.
     *
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void choosingColors() throws RemoteException {
        view.choosingColors();
    }

    /**
     * Prompts the client to choose their color.
     *
     * @param colorChoices The possible choices for the player's color.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void promptColorChoice(List<PlayerColor> colorChoices) throws RemoteException {
        view.promptColorChoice(colorChoices);
    }

    /**
     * Sets the player's color. If the player is still in the prompt because he didn't choose in time, the prompt is invalidated
     *
     * @param username The username whose color is being given.
     * @param color    The color assigned to the player.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void setColor(String username, PlayerColor color) throws RemoteException {
        view.setColor(username, color);
    }

    /**
     * Tells the client that the cards for the game are being drawn.
     *
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void drawingCards() throws RemoteException {
        view.drawingCards();
    }

    /**
     * Sets the player's hand.
     *
     * @param hand The player's hand.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void setHand(List<PlayableCard> hand) throws RemoteException {
        view.setHand(hand);
    }

    /**
     * Adds the given card to this player's hand.
     *
     * @param card The card to be added.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void addCardToHand(PlayableCard card) throws RemoteException {
        view.addCardToHand(card);
    }

    /**
     * Removed the given card from this player's hand.
     *
     * @param card The card to be removed.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void removeCardFromHand(PlayableCard card) throws RemoteException {
        view.removeCardFromHand(card);
    }

    /**
     * Sets the given player's restricted hand.
     *
     * @param username The username of the player whose hand is being given.
     * @param hand     The restricted hand.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void setOtherHand(String username, List<RestrictedCard> hand) throws RemoteException {
        view.setOtherHand(username, hand);
    }

    /**
     * Adds the given restricted card to the given user's hand.
     *
     * @param username The user to add this card to.
     * @param newCard  The restricted card to be added.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void addCardToOtherHand(String username, RestrictedCard newCard) throws RemoteException {
        view.addCardToOtherHand(username, newCard);
    }

    /**
     * Removes the given restricted card from the given user's hand.
     *
     * @param username     The user to remove this card from.
     * @param cardToRemove The restricted card to be removed.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void removeCardFromOtherHand(String username, RestrictedCard cardToRemove) throws RemoteException {
        view.removeCardFromOtherHand(username, cardToRemove);
    }

    /**
     * Sets the given player's play area.
     * @param username The player whose play area is being given.
     * @param cardPlacementOrder The order in which the cards were played in this play area.
     * @param field The user's field.
     * @param activeSides The map keeping track of which side every card is placed on.
     * @param legalPositions The set of positions on which the player can place cards.
     * @param illegalPositions The set of positions on which the player must not place cards.
     * @param resourceCounts A map containing the amount of each resource that the player has.
     * @param objectCounts A map containing the amount of each object that the player has.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void setPlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) throws RemoteException {
        view.setPlayArea(username, cardPlacementOrder, field, activeSides, legalPositions, illegalPositions, resourceCounts, objectCounts);
    }

    /**
     * Adds the given card to the given player's play area.
     * @param username The username of the player who played the card.
     * @param card The played card.
     * @param side The card the new card was played on.
     * @param pos The position where the new card was played.
     * @param addedLegalPositions The set of new positions in which the player can play a card, following the move which was just made.
     * @param removedLegalPositions The set of positions in which the player can no longer play a card, following the move which was just made.
     * @param resourceCounts A map containing the amount of each resource that the player has, following the move which was just made.
     * @param objectCounts A map containing the amount of each object that the player has, following the move which was just made.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void playCard(String username, BoardCard card, SideType side, Position pos, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) throws RemoteException {
        view.playCard(username, card, side, pos, addedLegalPositions, removedLegalPositions, resourceCounts, objectCounts);
    }

    /**
     * Sets a player's number of game points.
     *
     * @param username   The username of the player whose points are being set.
     * @param gamePoints The given player's number of game points.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void setGamePoints(String username, int gamePoints) throws RemoteException {
        view.setGamePoints(username, gamePoints);
    }

    /**
     * Sets a player's number of objective points.
     *
     * @param username        The username of the player whose points are being set.
     * @param objectivePoints The given player's number of objective points.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void setObjectivePoints(String username, int objectivePoints) throws RemoteException {
        view.setObjectivePoints(username, objectivePoints);
    }

    /**
     * Sets the common objectives for the game.
     *
     * @param commonObjectives The common objectives. Should always contain 2 elements.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void setCommonObjectives(ObjectiveCard[] commonObjectives) throws RemoteException {
        view.setCommonObjectives(commonObjectives);
    }

    /**
     * Prompts the player to choose their objective from the ones given.
     *
     * @param possiblePersonalObjectives The possible objectives the player can choose from. Should always contain 2 cards.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) throws RemoteException {
        view.promptObjectiveChoice(possiblePersonalObjectives);
    }

    /**
     * Sets the player's personal objective.
     *
     * @param personalObjective The player's personal objective.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void setPersonalObjective(ObjectiveCard personalObjective) throws RemoteException {
        view.setPersonalObjective(personalObjective);
    }

    /**
     * Sets the turn order for the game.
     *
     * @param usernames The turn order. Should always contain as many usernames as were added at the beginning of the game.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void setStartOrder(List<String> usernames) throws RemoteException {
        view.setStartOrder(usernames);
    }

    /**
     * Tells the client that it is the given player's turn to play.
     *
     * @param username The player's username.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void turn(String username) throws RemoteException {
        view.turn(username);
    }

    /**
     * Tells the client the winners of the game.
     *
     * @param winnerUsernames The winners of the game.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void setWinners(List<String> winnerUsernames) throws RemoteException {
        view.setWinners(winnerUsernames);
    }

    /**
     * Adds all the messages given to the player's chat.
     *
     * @param messages The chat messages to add.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void addMessages(List<ChatMessage> messages) throws RemoteException {
        view.addMessages(messages);
    }

    /**
     * Adds the given message to the player's chat.
     *
     * @param message The new message.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void addMessage(ChatMessage message) throws RemoteException {
        view.addMessage(message);
    }

    /**
     * Tells the client that an error has occurred.
     *
     * @param errorMessage The message that should be displayed to the user.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void promptError(String errorMessage) throws RemoteException {
        view.promptError(errorMessage);
    }

    /**
     * Forces the client to redraw the view.
     *
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void redrawView() throws RemoteException {
        view.redrawView();
    }

    /**
     * Notifies the client that from now on they shouldn't draw cards anymore.
     *
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void notifyDontDraw() throws RemoteException {
        view.notifyDontDraw();
    }

    /**
     * Tells the client that another client has disconnected. This ends the game, if it had started. If the game hadn't started already, the player is simply removed.
     *
     * @param whoDisconnected The username of the player who disconnected.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void signalDisconnection(String whoDisconnected) throws RemoteException {
        view.signalDisconnection(whoDisconnected);
    }

    /**
     * DOCME
     *
     * @param whoDisconnected
     * @throws RemoteException
     */
    @Override
    public void signalGameSuspension(String whoDisconnected) throws RemoteException {
        view.signalGameSuspension(whoDisconnected);
    }

    /**
     * Tells the client that a player has skipped their turn because of a deadlock.
     *
     * @param username The username of the player who skipped their turn.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void signalDeadlock(String username) throws RemoteException {
        view.signalDeadlock(username);
    }

    /**
     * Ping request used by the server to check that the client is still connected.
     *
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    @Override
    public void ping() throws RemoteException {
        lastPinged.set(System.currentTimeMillis());
        serverInterface.pong();
    }
}
