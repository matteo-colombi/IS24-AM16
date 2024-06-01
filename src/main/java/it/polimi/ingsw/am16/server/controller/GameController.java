package it.polimi.ingsw.am16.server.controller;

import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.NoStarterCardException;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCardType;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.model.game.GameModel;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.game.LobbyState;
import it.polimi.ingsw.am16.common.model.players.PlayAreaModel;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.PlayerModel;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.util.RNG;
import it.polimi.ingsw.am16.server.VirtualView;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for the Game.
 * Is responsible for receiving the requests from views, updating the Game's model,
 * and notifying the views of the changes that have occurred.
 * This class can be used by multiple threads without risk of conflicts, as all public methods are
 * synchronized GameController instance.
 */
public class GameController {
    private String choosingColor;
    private boolean hasPlacedCard;
    private final GameModel game;
    private final VirtualView virtualView;
    private final ChatController chatController;
    private final LobbyManager lobbyManager;

    private Queue<PlayerModel> playerQueue = null;

    /**
     * Constructs a new GameController for the given {@link GameModel}.
     *
     * @param game The {@link GameModel} linked to this controller.
     */
    public GameController(LobbyManager lobbyManager, GameModel game) {
        this.lobbyManager = lobbyManager;
        this.choosingColor = null;
        this.hasPlacedCard = false;
        this.game = game;
        this.virtualView = new VirtualView();
        this.chatController = new ChatController(this.virtualView);
    }

    /**
     * @return whether the game being controlled is being reloaded after a server crash.
     */
    public synchronized boolean isRejoiningAfterCrash() {
        return game.isRejoiningAfterCrash();
    }

    /**
     * Creates a new player and adds it to the game. By default, the player is considered disconnected.
     * @param username The player's username
     * @throws UnexpectedActionException Thrown if the game is already full, if the game has already started, or if a player with the given username is already present in the game.
     */
    public synchronized void createPlayer(String username) throws UnexpectedActionException {
        game.addPlayer(username);
    }

    /**
     * Sets the player with the given username's status to connected, and adds it to the list of players which should receive updates about the game.
     * @param username The player's username. If an invalid username is given, this method does nothing.
     * @param userView The {@link RemoteClientInterface}, used to communicate with the player.
     */
    public synchronized void joinPlayer(String username, RemoteClientInterface userView) throws UnexpectedActionException {
        Map<String, Player> players = game.getPlayers();

        if (!players.containsKey(username)) {
            throw new UnexpectedActionException("User " + username + " not found.");
        }

        if (players.get(username) != null && players.get(username).isConnected()) {
            throw new UnexpectedActionException("User " + username + " already reconnected.");
        }
        virtualView.addPlayer(userView, username);
        virtualView.joinGame(game.getId(), username, game.getNumPlayers());
        game.setConnected(username,true);

        if (game.isRejoiningAfterCrash()) {
            virtualView.communicateNewMessages(username, players.get(username).getChat().getMessages());
        }

        if (game.getCurrentPlayerCount() == game.getNumPlayers() && game.everybodyConnected()) {
            if (!game.isRejoiningAfterCrash()) {
                initializingProcedures();
            } else {
                restartingProcedures();
            }
        }

        chatController.subscribe(username, players.get(username).getChat());
    }

    /**
     * Method used to start the {@link GameState#INIT} phase of the game.
     */
    private void initializingProcedures() {
        try {
            game.initializeGame();
        } catch (UnexpectedActionException e) {
            e.printStackTrace();
            return;
        }

        virtualView.communicateGameState(game.getState());
        virtualView.communicateCommonCards(game.getCommonResourceCards(), game.getCommonGoldCards());
        virtualView.communicateDeckTopType(PlayableCardType.RESOURCE, game.getResourceDeckTopType());
        virtualView.communicateDeckTopType(PlayableCardType.GOLD, game.getGoldDeckTopType());

        Map<String, Player> players  = game.getPlayers();
        for(String username : players.keySet()) {
            virtualView.promptStarterChoice(username, players.get(username).getStarterCard());
        }
    }

    /**
     * Set's a player's starter card side. If the player has not yet been given a starter card, or if he has already placed his starter card, this method does nothing.
     * @param username The player's username. If an invalid username is given, this method does nothing.
     * @param starterSide The side on which the player decided to place the starter card.
     */
    public synchronized void setStarterCard(String username, SideType starterSide) {
        Map<String, Player> players = game.getPlayers();
        if (!players.containsKey(username)) return;

        try {
            game.setPlayerStarterSide(username, starterSide);
        } catch (UnexpectedActionException | NoStarterCardException e) {
            e.printStackTrace();
            return;
        }
        PlayAreaModel playArea = players.get(username).getPlayArea();

        virtualView.communicatePlayArea(username, playArea.getPlacementOrder(), playArea.getField(), playArea.getActiveSides(), playArea.getLegalPositions(), playArea.getIllegalPositions(), playArea.getResourceCounts(), playArea.getObjectCounts());

        if (game.allPlayersChoseStarterSide()) {
            initializingColors();
        }
    }

    /**
     * Starts the color-choosing phase.
     */
    private synchronized void initializingColors() {
        List<Player> players = new ArrayList<>(game.getPlayers().values());
        Collections.shuffle(players, RNG.getRNG());
        playerQueue = new LinkedList<>(players);
        virtualView.communicateChoosingColors();
        promptColor();
    }

    /**
     * Prompts the correct player to choose their color.
     */
    private synchronized void promptColor() {
        if (playerQueue.isEmpty()) {
            assert game.allPlayersChoseColor();
            choosingColor = null;
            distributeCards();
            return;
        }
        PlayerModel player = playerQueue.poll();
        choosingColor = player.getUsername();
        virtualView.promptColorChoice(player.getUsername(), game.getAvailableColors());
    }

    /**
     * Sets a player's color. Will prompt the next player in line to choose their color.
     * If this is the last player to choose their color, the game will move on.
     * @param username The player's username. If an invalid username is given, or it is not the given player's turn to choose their color, this method does nothing.
     * @param color The color chosen by the player. If <code>null</code>, a random color will be assigned.
     */
    public synchronized void setPlayerColor(String username, PlayerColor color) {
        if (choosingColor == null || !choosingColor.equals(username)) {
            System.err.println("Wrong player!");
            return;
        }

        if (color == null || !game.getAvailableColors().contains(color)) {
            color = RNG.getRNG().randomFromList(game.getAvailableColors());
        }
        try {
            game.setPlayerColor(username, color);
        } catch (UnexpectedActionException e) {
            e.printStackTrace();
            return;
        }
        virtualView.communicateColor(username, color);
        promptColor();
    }

    /**
     * Handles the updating of all the views after a game has restarted after a server crash.
     */
    private void restartingProcedures() {
        virtualView.communicateRejoinInformationStart();

        virtualView.communicateGameState(game.getState());
        if (game.getState() == GameState.FINAL_ROUND) {
            virtualView.communicateDontDraw();
        }
        virtualView.communicateCommonCards(game.getCommonResourceCards(), game.getCommonGoldCards());
        virtualView.communicateDeckTopType(PlayableCardType.RESOURCE, game.getResourceDeckTopType());
        virtualView.communicateDeckTopType(PlayableCardType.GOLD, game.getGoldDeckTopType());
        virtualView.communicateCommonObjectives(game.getCommonObjectiveCards());

        for(Player p : game.getPlayers().values()) {
            virtualView.communicatePlayArea(p.getUsername(), p.getPlayArea().getPlacementOrder(), p.getPlayArea().getField(), p.getPlayArea().getActiveSides(), p.getPlayArea().getLegalPositions(), p.getPlayArea().getIllegalPositions(), p.getPlayArea().getResourceCounts(), p.getPlayArea().getObjectCounts());
            virtualView.communicateColor(p.getUsername(), p.getPlayerColor());
            virtualView.communicatePersonalObjective(p.getUsername(), p.getPersonalObjective());
            virtualView.communicateGamePoints(p.getUsername(), p.getGamePoints());
            virtualView.communicateObjectivePoints(p.getUsername(), p.getObjectivePoints());
            virtualView.communicateHand(p.getUsername(), p.getHand().getCards());

            for (Player p2 : game.getPlayers().values()) {
                if (!p2.equals(p)) {
                    virtualView.communicateOtherHand(p2.getUsername(), p.getUsername(), p.getHand().getRestrictedVersion());
                }
            }
        }
        virtualView.communicateTurnOrder(game.getTurnOrder());

        virtualView.communicateRejoinInformationEnd();

        virtualView.notifyTurnStart(game.getActivePlayer());

        try {
            game.initializeGame();
        } catch (UnexpectedActionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the card distribution phase of the game.
     */
    private void distributeCards() {
        virtualView.communicateDrawingCards();

        game.distributeCards();

        virtualView.communicateDeckTopType(PlayableCardType.RESOURCE, game.getResourceDeckTopType());
        virtualView.communicateDeckTopType(PlayableCardType.GOLD, game.getGoldDeckTopType());

        for(Player p : game.getPlayers().values()) {
            virtualView.communicateHand(p.getUsername(), p.getHand().getCards());

            for(Player p2 : game.getPlayers().values())
                if (p2 != p)
                    virtualView.communicateOtherHand(p2.getUsername(), p.getUsername(), p.getHand().getRestrictedVersion());

        }

        distributeObjectives();
    }

    /**
     * Starts the objective distribution phase of the game.
     */
    private void distributeObjectives() {
        game.initializeObjectives();

        virtualView.communicateCommonObjectives(game.getCommonObjectiveCards());
        for(Player p : game.getPlayers().values()) {
            virtualView.promptObjectiveChoice(p.getUsername(), p.getPersonalObjectiveOptions());
        }
    }

    /**
     * Sets the player's objective.
     * If all players have chosen their objective, the game will move on.
     * @param username The player's username. If an invalid username is given, this method does nothing.
     * @param objectiveCard The chosen objective card. If the user doesn't have this card, this method will do nothing.
     */
    public synchronized void setPlayerObjective(String username, ObjectiveCard objectiveCard) {
        Map<String, Player> players = game.getPlayers();

        if (!players.containsKey(username)) return;

        try {
            game.setPlayerObjective(username, objectiveCard);
        } catch (UnknownObjectiveCardException | UnexpectedActionException e) {
            e.printStackTrace();
            return;
        }

        virtualView.communicatePersonalObjective(username, objectiveCard);

        if (game.allPlayersChoseObjective()) {
            startGame();
        }
    }

    /**
     * Actually starts the game.
     * After this, the game will be periodically saved.
     */
    public synchronized void startGame() {
        try {
            game.startGame();
        } catch (UnexpectedActionException e) {
            e.printStackTrace();
            return;
        }

        virtualView.communicateGameState(game.getState());
        virtualView.communicateTurnOrder(game.getTurnOrder());
        virtualView.notifyTurnStart(game.getActivePlayer());
    }

    /**
     * Decides whose turn it is, and prompts the correct player to play.
     * Saves the game if a complete lap has been done.
     */
    private void turnManager() {
        hasPlacedCard = false;

        try {
            game.advanceTurn();
        } catch (UnexpectedActionException e) {
            e.printStackTrace();
            return;
        }

        if (Objects.equals(game.getActivePlayer(), game.getStartingPlayer())) {
            lobbyManager.saveGame(game);
            if (game.getState() == GameState.FINAL_ROUND) {
                endGame();
                return;
            }

            if (game.checkFinalRound()) {
                try {
                    game.triggerFinalRound();
                } catch (UnexpectedActionException e) {
                    e.printStackTrace();
                    return;
                }
                virtualView.communicateGameState(game.getState());
                virtualView.communicateDontDraw();
            }
        }

        if (game.getPlayers().get(game.getActivePlayer()).isDeadlocked()) {
            virtualView.communicateDeadlock(game.getActivePlayer());
            turnManager();
        } else {
            virtualView.notifyTurnStart(game.getActivePlayer());
        }
    }

    /**
     * Places a card on the player's board. If an illegal move was made, this method does nothing.
     * @param username The player's username. If the username is invalid, or not the current player's username, this method does nothing.
     * @param card The played card.
     * @param side The side on which the card is played.
     * @param newPos The position of the newly placed card.
     */
    public synchronized void placeCard(String username, PlayableCard card, SideType side, Position newPos) {
        if ((game.getState() != GameState.STARTED && game.getState() != GameState.FINAL_ROUND)
                || hasPlacedCard
                || !Objects.equals(username, game.getActivePlayer())
        ) return;

        try {
            game.placeCard(username, card, side, newPos);
        } catch (IllegalMoveException e) {
            virtualView.promptError(username, "Illegal move.");
            return;
        } catch (UnexpectedActionException e) {
            e.printStackTrace();
            return;
        }

        hasPlacedCard = true;

        Map<String, Player> players = game.getPlayers();

        PlayAreaModel playArea = players.get(username).getPlayArea();

        virtualView.communicatePlayCard(username, card, side, newPos, playArea.getAddedLegalPositions(), playArea.getRemovedLegalPositions(), playArea.getResourceCounts(), playArea.getObjectCounts());
        virtualView.communicateGamePoints(username, players.get(username).getGamePoints());
        virtualView.communicateRemoveCard(username, card);
        for(Player p : players.values()) {
            if (!p.getUsername().equals(username)) {
                virtualView.communicateRemoveOtherCard(p.getUsername(), username, card.getRestrictedVersion());
            }
        }

        if (game.getState() == GameState.FINAL_ROUND) {
            turnManager();
        }
    }

    /**
     * Draws a card for the given player.
     * @param username The player's username. If the given username is invalid, or it is not the right moment to draw a card, or if it's not the given player's turn, this method does nothing.
     * @param drawType The type of draw the player has decided to perform.
     */
    public synchronized void drawCard(String username, DrawType drawType) {
        if ((game.getState() != GameState.STARTED)
                || !hasPlacedCard
                || !Objects.equals(username, game.getActivePlayer())
        ) return;

        PlayableCard drawnCard;

        try {
            drawnCard = game.drawCard(username, drawType);
        } catch (IllegalMoveException e) {
            virtualView.promptError(username, "Illegal draw.");
            return;
        } catch (UnexpectedActionException e) {
            //TODO handle it
            return;
        }

        switch (drawType) {
            case RESOURCE_1, RESOURCE_2:
                virtualView.communicateCommonCards(game.getCommonResourceCards(), game.getCommonGoldCards());
            case RESOURCE_DECK:
                virtualView.communicateDeckTopType(PlayableCardType.RESOURCE, game.getResourceDeckTopType());
                break;
            case GOLD_1, GOLD_2:
                virtualView.communicateCommonCards(game.getCommonResourceCards(), game.getCommonGoldCards());
            case GOLD_DECK:
                virtualView.communicateDeckTopType(PlayableCardType.GOLD, game.getGoldDeckTopType());
                break;
        }

        virtualView.communicateNewCard(username, drawnCard);
        for(Player p : game.getPlayers().values()) {
            if (!p.getUsername().equals(username)) {
                virtualView.communicateNewOtherCard(p.getUsername(), username, drawnCard.getRestrictedVersion());
            }
        }

        turnManager();
    }

    /**
     * Ends the game and selects the winners. Communicates to the players the winners of the game.
     * After this, the game's save-file is deleted.
     */
    private void endGame() {
        try {
            game.endGame();
        } catch (UnexpectedActionException e) {
            e.printStackTrace();
            return;
        }

        virtualView.communicateGameState(game.getState());
        for(Player p : game.getPlayers().values()) {
            virtualView.communicateObjectivePoints(p.getUsername(), p.getObjectivePoints());
        }
        List<String> winnerUsernames = game.getWinnerUsernames();

        Map<String, ObjectiveCard> personalObjectives =
                game.getPlayers()
                        .values()
                        .stream()
                        .collect(
                                Collectors.toMap(Player::getUsername, Player::getPersonalObjective)
                        );

        virtualView.communicateWinners(winnerUsernames, personalObjectives);

        lobbyManager.deleteGame(game.getId());
    }

    /**
     * Sends a chat message to everyone in the game.
     *
     * @param senderUsername The sender's username.
     * @param text           The message content.
     */
    public synchronized void sendChatMessage(String senderUsername, String text) {
        chatController.sendMessage(senderUsername, text);
    }

    /**
     * Sends a chat message to the given player.
     *
     * @param senderUsername    The sender's username.
     * @param text              The message content.
     * @param receiverUsernames The set of players who should receive the message.
     */
    public synchronized void sendChatMessage(String senderUsername, String text, Set<String> receiverUsernames) {
        chatController.sendMessage(senderUsername, text, receiverUsernames, true);
    }

    /**
     * Pauses the game because of the disconnection of the given player.
     * The game can resume if all players reconnect.
     * @param username The player who disconnected. If the given username is not valid, this method does nothing.
     */
    public synchronized void disconnect(String username) {
        Map<String, Player> players = game.getPlayers();

        if (!players.containsKey(username) || game.getState() == GameState.ENDED) return;

        if (game.getState() == GameState.STARTED || game.getState() == GameState.FINAL_ROUND) {
            virtualView.signalGameSuspension(username);
            chatController.clear();
            game.pause();
        } else if (game.getState() == GameState.INIT) {
            virtualView.signalGameDeletion(username);
            lobbyManager.deleteGame(game.getId());
        } else {
            virtualView.signalDisconnection(username);
            try {
                game.removePlayer(username);
            } catch (UnexpectedActionException e) {
                System.err.println("Unexpected error: " + e.getMessage());
            }
            chatController.unsubscribe(username);
            if (game.getState() == GameState.INIT || (game.getCurrentPlayerCount() == 0 && !game.isRejoiningAfterCrash())) {
                lobbyManager.deleteGame(game.getId());
            }
        }
    }

    /**
     * @return The non-variable number of players expected to play the game controlled by this controller.
     */
    public synchronized int getNumPlayers() {
        return game.getNumPlayers();
    }

    /**
     * @return The {@link LobbyState} of the game controlled by this controller.
     */
    public synchronized LobbyState getLobbyState() {
        if (isRejoiningAfterCrash()) {
            return LobbyState.REJOINING;
        }

        GameState gameState = game.getState();

        return switch (gameState) {
            case JOINING -> LobbyState.JOINING;
            case INIT, STARTED -> LobbyState.IN_GAME;
            case FINAL_ROUND, ENDED -> LobbyState.ENDING;
        };
    }

    /**
     * @return The number of players who joined the game.
     */
    public synchronized int getCurrentPlayerCount() {
        return game.getCurrentPlayerCount();
    }

}
