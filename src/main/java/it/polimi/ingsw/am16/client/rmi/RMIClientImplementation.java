package it.polimi.ingsw.am16.client.rmi;

import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.client.view.ViewInterface;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.game.LobbyState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.ErrorType;
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

    /**
     * Creates a new RMIClientImplementation object, which the server can use to communicate with this client.
     * @param welcomeRMIServer The remote object on the server through which the client can open a new connection.
     * @param view The view which will receive updates about messages sent by the server.
     * @throws RemoteException Thrown if an error occurs while creating stubs for the remote object, or if an exception occurs while obtaining the remote object from the server.
     */
    public RMIClientImplementation(WelcomeRMIServer welcomeRMIServer, ViewInterface view) throws RemoteException {
        this.serverInterface = welcomeRMIServer.getClientHandler(this);
        this.lastPinged = new AtomicLong(System.currentTimeMillis());
        this.checkConnectionTimer = new Timer();
        this.view = view;
    }

    /**
     * @return The interface which the client can use to call methods on the server.
     */
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
        view.signalConnectionLost();
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
                    System.out.println("\nServer hasn't pinged in a while.");
                    System.out.println("Good bye!");
                    checkConnectionTimer.cancel();
                    stop();
                }
            }
        };

        checkConnectionTimer.schedule(task, 1000, 10000);
    }

    @Override
    public void notifyGames(Set<String> gameIds, Map<String, Integer> currentPlayers, Map<String, Integer> maxPlayers, Map<String, LobbyState> lobbyStates) throws RemoteException {
        view.printGames(gameIds, currentPlayers, maxPlayers, lobbyStates);
    }

    @Override
    public void joinGame(String gameId, String username, int numPlayers) throws RemoteException {
        view.joinGame(gameId, username, numPlayers);
    }

    @Override
    public void rejoinInformationStart() throws RemoteException {
        view.rejoinInformationStart();
    }

    @Override
    public void rejoinInformationEnd() throws RemoteException {
        view.rejoinInformationEnd();
    }

    @Override
    public void addPlayer(String username) throws RemoteException {
        view.addPlayer(username);
    }

    @Override
    public void setPlayers(List<String> usernames) throws RemoteException {
        view.setPlayers(usernames);
    }

    @Override
    public void setGameState(GameState state) throws RemoteException {
        view.setGameState(state);
    }

    @Override
    public void setCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) throws RemoteException {
        view.setCommonCards(commonResourceCards, commonGoldCards);
    }

    @Override
    public void setDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) throws RemoteException {
        view.setDeckTopType(whichDeck, resourceType);
    }

    @Override
    public void promptStarterChoice(StarterCard starterCard) throws RemoteException {
        view.promptStarterChoice(starterCard);
    }

    @Override
    public void choosingColors() throws RemoteException {
        view.choosingColors();
    }

    @Override
    public void promptColorChoice(List<PlayerColor> colorChoices) throws RemoteException {
        view.promptColorChoice(colorChoices);
    }

    @Override
    public void setColor(String username, PlayerColor color) throws RemoteException {
        view.setColor(username, color);
    }

    @Override
    public void drawingCards() throws RemoteException {
        view.drawingCards();
    }

    @Override
    public void setHand(List<PlayableCard> hand) throws RemoteException {
        view.setHand(hand);
    }

    @Override
    public void addCardToHand(PlayableCard card) throws RemoteException {
        view.addCardToHand(card);
    }

    @Override
    public void removeCardFromHand(PlayableCard card) throws RemoteException {
        view.removeCardFromHand(card);
    }

    @Override
    public void setOtherHand(String username, List<RestrictedCard> hand) throws RemoteException {
        view.setOtherHand(username, hand);
    }

    @Override
    public void addCardToOtherHand(String username, RestrictedCard newCard) throws RemoteException {
        view.addCardToOtherHand(username, newCard);
    }

    @Override
    public void removeCardFromOtherHand(String username, RestrictedCard cardToRemove) throws RemoteException {
        view.removeCardFromOtherHand(username, cardToRemove);
    }

    @Override
    public void setPlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) throws RemoteException {
        view.setPlayArea(username, cardPlacementOrder, field, activeSides, legalPositions, illegalPositions, resourceCounts, objectCounts);
    }

    @Override
    public void playCard(String username, BoardCard card, SideType side, Position pos, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) throws RemoteException {
        view.playCard(username, card, side, pos, addedLegalPositions, removedLegalPositions, resourceCounts, objectCounts);
    }

    @Override
    public void setGamePoints(String username, int gamePoints) throws RemoteException {
        view.setGamePoints(username, gamePoints);
    }

    @Override
    public void setObjectivePoints(String username, int objectivePoints) throws RemoteException {
        view.setObjectivePoints(username, objectivePoints);
    }

    @Override
    public void setCommonObjectives(ObjectiveCard[] commonObjectives) throws RemoteException {
        view.setCommonObjectives(commonObjectives);
    }

    @Override
    public void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) throws RemoteException {
        view.promptObjectiveChoice(possiblePersonalObjectives);
    }

    @Override
    public void setPersonalObjective(ObjectiveCard personalObjective) throws RemoteException {
        view.setPersonalObjective(personalObjective);
    }

    @Override
    public void setStartOrder(List<String> usernames) throws RemoteException {
        view.setStartOrder(usernames);
    }

    @Override
    public void turn(String username) throws RemoteException {
        view.turn(username);
    }

    @Override
    public void setWinners(List<String> winnerUsernames, Map<String, ObjectiveCard> personalObjectives) throws RemoteException {
        serverInterface.leaveGame(true);
        view.setWinners(winnerUsernames, personalObjectives);
    }

    @Override
    public void addMessages(List<ChatMessage> messages) throws RemoteException {
        view.addMessages(messages);
    }

    @Override
    public void addMessage(ChatMessage message) throws RemoteException {
        view.addMessage(message);
    }

    @Override
    public void promptError(String errorMessage, ErrorType errorType) throws RemoteException {
        view.promptError(errorMessage, errorType);
    }

    @Override
    public void notifyDontDraw() throws RemoteException {
        view.notifyDontDraw();
    }

    @Override
    public void signalDisconnection(String whoDisconnected) throws RemoteException {
        view.signalDisconnection(whoDisconnected);
    }

    @Override
    public void signalGameSuspension(String whoDisconnected) throws RemoteException {
        serverInterface.leaveGame(true);
        view.signalGameSuspension(whoDisconnected);
    }

    @Override
    public void signalGameDeletion(String whoDisconnected) throws RemoteException {
        serverInterface.leaveGame(true);
        view.signalGameDeletion(whoDisconnected);
    }

    @Override
    public void signalDeadlock(String username) throws RemoteException {
        view.signalDeadlock(username);
    }

    @Override
    public void disconnectFromGame() throws RemoteException {
        serverInterface.leaveGame(true);
    }

    @Override
    public void ping() throws RemoteException {
        lastPinged.set(System.currentTimeMillis());
        serverInterface.pong();
    }
}
