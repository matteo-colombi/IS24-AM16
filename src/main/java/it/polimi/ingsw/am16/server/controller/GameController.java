package it.polimi.ingsw.am16.server.controller;

import it.polimi.ingsw.am16.client.RemoteViewInterface;
import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.NoStarterCardException;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.model.game.GameModel;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayAreaModel;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.PlayerModel;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.util.RNG;
import it.polimi.ingsw.am16.server.VirtualView;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Controller for the Game.
 * Is responsible for receiving the requests from views, updating the Game's model,
 * and notifying the views of the changes that have occurred.
 */
public class GameController {
    private int choosingColor;
    private boolean hasPlacedCard;
    private final GameModel game;
    private final VirtualView virtualView;
    private final ChatController chatController;
    private final LobbyManager lobbyManager;

    private Queue<PlayerModel> playerQueue = null;

    /**
     * Constructs a new GameController for the given {@link GameModel}.
     * @param game The {@link GameModel} linked to this controller.
     */
    public GameController(LobbyManager lobbyManager, GameModel game) {
        this.lobbyManager = lobbyManager;
        this.choosingColor = -1;
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
     * Retrieves the given player's id.
     * @param username The player whose id is being queried.
     * @return The id of the player with the given username
     * @throws IllegalArgumentException Thrown if no player with the given username is found in the game.
     */
    public synchronized int getPlayerId(String username) throws IllegalArgumentException {
        PlayerModel[] players = game.getPlayers();
        for(PlayerModel player : players) {
            if (player.getUsername().equals(username)) {
                return player.getPlayerId();
            }
        }
        throw new IllegalArgumentException("No player with the given username in the game.");
    }

    /**
     * Creates a new player and adds it to the game. By default, the player is considered disconnected.
     * @param username The player's username
     * @return The newly added player's id.
     * @throws UnexpectedActionException Thrown if the game is already full, if the game has already started, or if a player with the given username is already present in the game.
     */
    public synchronized int createPlayer(String username) throws UnexpectedActionException {
        return game.addPlayer(username);
    }

    /**
     * Sets the player with the given playerId's status to connected, and adds it to the list of players which should receive updates about the game.
     * @param playerId The player's id. If an invalid id is given, this method does nothing.
     * @param userView The {@link RemoteViewInterface}, used to communicate with the player.
     */
    public synchronized void joinPlayer(int playerId, RemoteViewInterface userView) {
        if (playerId < 0 || playerId >= game.getCurrentPlayerCount())
            return;

        PlayerModel[] players = game.getPlayers();
        if (players[playerId] != null && players[playerId].isConnected()) {
            try {
                userView.promptError("User already present with same username.");
            } catch (RemoteException e) {
                e.printStackTrace();
                //TODO handle it
                return;
            }
            return;
        }
        virtualView.addPlayer(playerId, userView, players[playerId].getUsername());
        virtualView.joinGame(playerId, game.getId(), players[playerId].getUsername());
        players[playerId].setConnected(true);
        if (game.isRejoiningAfterCrash()) {
            virtualView.communicateNewMessages(playerId, players[playerId].getChat().getMessages());
        }

        if (game.getCurrentPlayerCount() == game.getNumPlayers() && game.everybodyConnected()) {
            if (!game.isRejoiningAfterCrash()) {
                initializingProcedures();
            } else {
                restartingProcedures();
            }
        }

        chatController.subscribe(players[playerId].getPlayerId(), players[playerId].getUsername(), players[playerId].getChat());
    }

    /**
     * Method used to start the {@link GameState} INIT phase of the game.
     */
    private void initializingProcedures() {
        try {
            game.initializeGame();
        } catch (UnexpectedActionException e) {
            //TODO handle it
            e.printStackTrace();
            return;
        }

        virtualView.communicateGameState(game.getState());
        virtualView.communicateCommonCards(game.getCommonResourceCards(), game.getCommonGoldCards());
        virtualView.communicateDeckTopType(PlayableCardType.RESOURCE, game.getResourceDeckTopType());
        virtualView.communicateDeckTopType(PlayableCardType.GOLD, game.getGoldDeckTopType());

        for(PlayerModel player : game.getPlayers()) {
            virtualView.promptStarterChoice(player.getPlayerId(), player.getStarterCard());
        }
    }

    /**
     * Set's a player's starter card side. If the player has not yet been given a starter card, or if he has already placed his starter card, this method does nothing.
     * @param playerId The player id. If an invalid id is given, this method does nothing.
     * @param starterSide The side on which the player decided to place the starter card.
     */
    public synchronized void setStarterCard(int playerId, SideType starterSide) {
        if (playerId < 0 || playerId >= game.getCurrentPlayerCount())
            return;

        StarterCard card = game.getPlayers()[playerId].getStarterCard();

        try {
            game.setPlayerStarterSide(playerId, starterSide);
        } catch (UnexpectedActionException e) {
            //TODO handle it
            e.printStackTrace();
            return;
        } catch (NoStarterCardException e) {
            //TODO handle it
            e.printStackTrace();
            return;
        }
        PlayAreaModel playArea = game.getPlayers()[playerId].getPlayArea();

        virtualView.communicatePlayArea(game.getPlayers()[playerId].getUsername(), playArea.getPlacementOrder(), playArea.getField(), playArea.getActiveSides());
        virtualView.redrawView(playerId);

        if (game.allPlayersChoseStarterSide()) {
            initializingColors();
        }
    }

    /**
     * Starts the color-choosing phase.
     */
    private synchronized void initializingColors() {
        List<PlayerModel> playerModels = new ArrayList<>(List.of(game.getPlayers()));
        Collections.shuffle(playerModels, RNG.getRNG());
        playerQueue = new LinkedList<>(playerModels);
        virtualView.communicateChoosingColors();
        promptColor();
    }

    /**
     * Prompts the correct player to choose their color.
     */
    private synchronized void promptColor() {
        if (playerQueue.isEmpty()) {
            assert game.allPlayersChoseColor();
            choosingColor = -1;
            distributeCards();
            return;
        }
        PlayerModel player = playerQueue.poll();
        choosingColor = player.getPlayerId();
        virtualView.promptColorChoice(player.getPlayerId(), game.getAvailableColors());
    }

    /**
     * Sets a player's color. Will prompt the next player in line to choose their color.
     * If this is the last player to choose their color, the game will move on.
     * @param playerId The player's id. If an invalid id is given, or it is not the given player's turn to choose their color, this method does nothing.
     * @param color The color chosen by the player. If <code>null</code>, a random color will be assigned.
     */
    public synchronized void setPlayerColor(int playerId, PlayerColor color) {
        if (choosingColor == -1 || choosingColor != playerId) {
            System.err.println("Wrong player!");
            //TODO handle it better?
            return;
        }

        if (color == null || !game.getAvailableColors().contains(color)) {
            color = RNG.getRNG().randomFromList(game.getAvailableColors());
        }
        try {
            game.setPlayerColor(playerId, color);
        } catch (UnexpectedActionException e) {
            //TODO handle it
            e.printStackTrace();
            return;
        }
        virtualView.communicateColor(game.getPlayers()[playerId].getUsername(), color);
        virtualView.redrawView(playerId);
        promptColor();
    }

    /**
     * Handles the updating of all the views after a game has restarted after a server crash.
     */
    private void restartingProcedures() {
        virtualView.communicateGameState(game.getState());
        if (game.getState() == GameState.FINAL_ROUND) {
            virtualView.communicateDontDraw();
        }
        virtualView.communicateCommonCards(game.getCommonResourceCards(), game.getCommonGoldCards());
        virtualView.communicateDeckTopType(PlayableCardType.RESOURCE, game.getResourceDeckTopType());
        virtualView.communicateDeckTopType(PlayableCardType.GOLD, game.getGoldDeckTopType());
        virtualView.communicateCommonObjectives(game.getCommonObjectiveCards());
        virtualView.communicateTurnOrder(game.getTurnOrder());

        for(PlayerModel player : game.getPlayers()) {
            virtualView.communicateHand(player.getPlayerId(), player.getHand().getCards());
            virtualView.communicatePlayArea(player.getUsername(), player.getPlayArea().getPlacementOrder(), player.getPlayArea().getField(), player.getPlayArea().getActiveSides());
            virtualView.communicatePersonalObjective(player.getPlayerId(), player.getPersonalObjective());

            for (PlayerModel player2 : game.getPlayers()) {
                if (player2 != player) {
                    virtualView.communicateOtherHand(player2.getPlayerId(), player.getUsername(), player.getHand().getRestrictedVersion());
                }
            }

            virtualView.communicateNewMessages(player.getPlayerId(), player.getChat().getMessages());
        }

        //TODO maybe remove this?
        assert game.getStartingPlayer() == game.getActivePlayer();

        virtualView.notifyTurnStart(game.getPlayers()[game.getActivePlayer()].getUsername());

        virtualView.redrawView();

        try {
            game.initializeGame();
        } catch (UnexpectedActionException e) {
            //TODO handle it
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

        for(PlayerModel player : game.getPlayers()) {
            virtualView.communicateHand(player.getPlayerId(), player.getHand().getCards());

            for(PlayerModel player2 : game.getPlayers())
                if (player2 != player)
                    virtualView.communicateOtherHand(player2.getPlayerId(), player.getUsername(), player.getHand().getRestrictedVersion());

        }

        distributeObjectives();
    }

    /**
     * Starts the objective distribution phase of the game.
     */
    private void distributeObjectives() {
        try {
            game.initializeObjectives();
        } catch (UnexpectedActionException e) {
            //TODO handle it
        }

        virtualView.communicateCommonObjectives(game.getCommonObjectiveCards());
        for(PlayerModel player : game.getPlayers()) {
            virtualView.promptObjectiveChoice(player.getPlayerId(), player.getPersonalObjectiveOptions());
            virtualView.redrawView(player.getPlayerId());
        }
    }

    /**
     * Sets the player's objective.
     * If all players have chosen their objective, the game will move on.
     * @param playerId The player's id. If an invalid id is given, this method does nothing.
     * @param objectiveCard The chosen objective card. If the user doesn't have this card, this method will do nothing.
     */
    public synchronized void setPlayerObjective(int playerId, ObjectiveCard objectiveCard) {
        if (playerId < 0 || playerId >= game.getCurrentPlayerCount())
            return;

        try {
            game.setPlayerObjective(playerId, objectiveCard);
        } catch (UnknownObjectiveCardException e) {
            //TODO handle it better?
            return;
        } catch (UnexpectedActionException e) {
            e.printStackTrace();
            return;
            //TODO handle it
        }

        virtualView.communicatePersonalObjective(playerId, objectiveCard);
        virtualView.redrawView(playerId);

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
            //TODO handle it
        }

        virtualView.communicateGameState(game.getState());
        virtualView.communicateTurnOrder(game.getTurnOrder());
        virtualView.notifyTurnStart(game.getPlayers()[game.getActivePlayer()].getUsername());
        virtualView.redrawView();
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
            //TODO handle it
        }

        if (game.getActivePlayer() == game.getStartingPlayer()) {
            //TODO maybe change when the game is saved?
            // Options: periodically (every x seconds, at the end of the turn)
            //          or after every turn?
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
                    //TODO handle it
                }
                virtualView.communicateGameState(game.getState());
                virtualView.communicateDontDraw();
            }
        }

        if (game.getPlayers()[game.getActivePlayer()].isDeadlocked()) {
            virtualView.communicateDeadlock(game.getPlayers()[game.getActivePlayer()].getUsername());
            turnManager();
        } else {
            virtualView.notifyTurnStart(game.getPlayers()[game.getActivePlayer()].getUsername());
        }
    }

    /**
     * Places a card on the player's board. If an illegal move was made, this method does nothing.
     * @param playerId The player's id. If the id is invalid, or not the current player's id, this method does nothing.
     * @param card The played card.
     * @param side The side on which the card is played.
     * @param newPos The position of the newly placed card.
     */
    public synchronized void placeCard(int playerId, PlayableCard card, SideType side, Position newPos) {
        if ((game.getState() != GameState.STARTED && game.getState() != GameState.FINAL_ROUND)
                || hasPlacedCard
                || playerId != game.getActivePlayer()
        ) return;

        try {
            game.placeCard(playerId, card, side, newPos);
        } catch (IllegalMoveException e) {
            System.err.println("Illegal move.");
            //TODO handle it better?
            return;
        } catch (UnexpectedActionException e) {
            e.printStackTrace();
            return;
            //TODO handle it
        }

        hasPlacedCard = true;

        virtualView.communicatePlayCard(game.getPlayers()[playerId].getUsername(), card, side, newPos);
        virtualView.communicateGamePoints(game.getPlayers()[playerId].getUsername(), game.getPlayers()[playerId].getGamePoints());
        virtualView.communicateRemoveCard(playerId, card);
        for(PlayerModel player : game.getPlayers()) {
            if (player.getPlayerId() != playerId) {
                virtualView.communicateRemoveOtherCard(player.getPlayerId(), game.getPlayers()[playerId].getUsername(), card.getRestrictedVersion());
                virtualView.redrawView(player.getPlayerId());
            }
        }
        virtualView.redrawView();

        if (game.getState() == GameState.FINAL_ROUND) {
            turnManager();
        }
    }

    /**
     * Draws a card for the given player.
     * @param playerId The player's id. If the given id is invalid, or it is not the right moment to draw a card, or if it's not the given player's turn, this method does nothing.
     * @param drawType The type of draw the player has decided to perform.
     */
    public synchronized void drawCard(int playerId, DrawType drawType) {
        if ((game.getState() != GameState.STARTED)
                || !hasPlacedCard
                || playerId != game.getActivePlayer()
        ) return;

        PlayableCard drawnCard;

        try {
            drawnCard = game.drawCard(playerId, drawType);
        } catch (IllegalMoveException e) {
            System.err.println("Illegal draw.");
            //TODO handle it
            return;
        } catch (UnexpectedActionException e) {
            //TODO handle it
            e.printStackTrace();
            return;
        }

        switch (drawType) {
            case GOLD_DECK -> virtualView.communicateDeckTopType(PlayableCardType.GOLD, game.getGoldDeckTopType());
            case RESOURCE_DECK -> virtualView.communicateDeckTopType(PlayableCardType.RESOURCE, game.getResourceDeckTopType());
            default -> virtualView.communicateCommonCards(game.getCommonResourceCards(), game.getCommonGoldCards());
        }

        virtualView.communicateNewCard(playerId, drawnCard);
        for(PlayerModel player : game.getPlayers()) {
            if (player.getPlayerId() != playerId) {
                virtualView.communicateNewOtherCard(player.getPlayerId(), game.getPlayers()[playerId].getUsername(), drawnCard.getRestrictedVersion());
                virtualView.redrawView(player.getPlayerId());
            }
        }
        virtualView.redrawView(playerId);

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
            //TODO handle it
            return;
        }

        virtualView.communicateGameState(game.getState());
        for(PlayerModel player : game.getPlayers()) {
            virtualView.communicateObjectivePoints(player.getUsername(), player.getObjectivePoints());
        }
        List<String> winnerUsernames = game.getWinnerIds().stream().map(id -> game.getPlayers()[id].getUsername()).toList();
        virtualView.communicateWinners(winnerUsernames);
        virtualView.redrawView();

        lobbyManager.deleteGame(game);
    }

    /**
     * Sends a chat message to everyone in the game.
     * @param senderUsername The sender's username.
     * @param text The message content.
     */
    public synchronized void sendChatMessage(String senderUsername, String text) {
        chatController.sendMessage(senderUsername, text);
    }

    /**
     * Sends a chat message to the given player.
     * @param senderUsername The sender's username.
     * @param text The message content.
     * @param receiverUsernames The set of players who should receive the message.
     */
    public synchronized void sendChatMessage(String senderUsername, String text, Set<String> receiverUsernames) {
        chatController.sendMessage(senderUsername, text, receiverUsernames, true);
    }

    /**
     * Ends the game because of the disconnection of the given player.
     * @param playerId The player who disconnected. If the given id is not valid, this method does nothing.
     */
    public synchronized void disconnect(int playerId) {
        if (playerId < 0 || playerId >= game.getCurrentPlayerCount()) return;
        //TODO maybe make it so that disconnections are allowed in the lobby (if the game has not started).
        virtualView.signalDisconnection(playerId, game.getPlayers()[playerId].getUsername());

        //TODO maybe the save file shouldn't be deleted? The requirements say that we should save in case the server crashes, not one of the clients.
        lobbyManager.deleteGame(game);
    }

}
