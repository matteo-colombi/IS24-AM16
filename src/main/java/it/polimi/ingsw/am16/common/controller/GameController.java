package it.polimi.ingsw.am16.common.controller;

import it.polimi.ingsw.am16.client.RemoteViewInterface;
import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.NoStarterCardException;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.model.game.GameModel;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.lobby.LobbyManager;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.PlayerModel;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.util.RNG;
import it.polimi.ingsw.am16.server.VirtualView;

import java.rmi.RemoteException;
import java.util.*;

public class GameController {
    private boolean hasPlacedCard;
    private boolean dontDraw;
    private final GameModel game;
    private final VirtualView virtualView;
    private final LobbyManager lobbyManager;

    private Queue<PlayerModel> playerQueue = null;

    public GameController(GameModel game, LobbyManager lobbyManager) {
        this.hasPlacedCard = false;
        this.dontDraw = false;
        this.game = game;
        this.lobbyManager = lobbyManager;
        this.virtualView = new VirtualView(this);
    }

    public String getGameId() {
        return game.getId();
    }

    public void addPlayer(String username, RemoteViewInterface userView) {
        PlayerModel[] players = game.getPlayers();
        for(PlayerModel player : players) {
            if (player != null && player.getUsername().equals(username)) {
                if (player.isConnected()) {
                    try {
                        userView.promptError("User already present with same username.");
                    } catch (RemoteException e) {
                        //TODO handle it
                    }
                    return;
                }
                virtualView.addPlayer(player.getPlayerId(), userView);
                game.incrementCurrentPlayerCount();
                return;
            }
        }

        try {
            int newPlayerId = game.addPlayer(username);
            virtualView.addPlayer(newPlayerId, userView);
        } catch (UnexpectedActionException e) {
            try {
                userView.promptError("Couldn't join game. Game already full or already started.");
            } catch (RemoteException ex) {
                //TODO handle it
            }
        }

        if (game.getCurrentPlayerCount() == game.getNumPlayers()) {
            if (!game.isRejoiningAfterCrash()) {
                initializingProcedures();
            } else {
                restartingProcedures();
            }
        }
    }

    private void initializingProcedures() {
        try {
            game.initializeGame();
        } catch (UnexpectedActionException e) {
            //TODO handle it
        }

        virtualView.communicateGameState(game.getState());
        virtualView.communicateCommonCards(game.getCommonResourceCards(), game.getCommonGoldCards());

        for(PlayerModel player : game.getPlayers()) {
            virtualView.promptStarterChoice(player.getPlayerId(), player.getStarterCard());
        }
    }

    public void setStarterCard(int playerId, SideType starterSide) {
        try {
            game.setPlayerStarterSide(playerId, starterSide);
        } catch (UnexpectedActionException e) {
            //TODO handle it
        } catch (NoStarterCardException e) {
            //TODO handle it
        }

        virtualView.communicatePlayerInfo(playerId, game.getPlayers()[playerId]);
        virtualView.redrawView(playerId);

        if (game.allPlayersChoseStarterSide()) {
            initializingColors();
        }
    }

    private void initializingColors() {
        List<PlayerModel> playerModels = new ArrayList<>(List.of(game.getPlayers()));
        Collections.shuffle(playerModels, RNG.getRNG());
        playerQueue = new LinkedList<>(playerModels);
        promptColor();
    }

    private void promptColor() {
        if (playerQueue.isEmpty()) {
            assert game.allPlayersChoseColor();
            distributeCards();
            return;
        }
        PlayerModel player = playerQueue.poll();
        virtualView.promptColorChoice(player.getPlayerId(), game.getAvailableColors());
    }

    public void setPlayerColor(int playerId, PlayerColor color) {
        if (color == null) {
            color = RNG.getRNG().randomFromList(game.getAvailableColors());
        }
        try {
            game.setPlayerColor(playerId, color);
        } catch (UnexpectedActionException e) {
            //TODO handle it
        }
        virtualView.communicateColor(playerId, color);
        virtualView.redrawView(playerId);
        promptColor();
    }

    private void restartingProcedures() {
        virtualView.communicateGameState(game.getState());
        virtualView.communicateCommonCards(game.getCommonResourceCards(), game.getCommonGoldCards());
        virtualView.communicateCommonObjectives(game.getCommonObjectiveCards());
        virtualView.communicateTurnOrder(game.getTurnOrder());

        for(PlayerModel player : game.getPlayers()) {
            virtualView.communicatePlayerInfo(player.getPlayerId(), player);

            for (PlayerModel player2 : game.getPlayers()) {
                if (player2 != player)
                    virtualView.communicateOtherPlayer(player2.getPlayerId(), player.getPlayerId(), player);
            }
        }

        virtualView.notifyTurnStart(game.getActivePlayer());

        virtualView.redrawView();

        try {
            game.initializeGame();
        } catch (UnexpectedActionException e) {
            //TODO handle it
        }
    }

    private void distributeCards() {
        game.distributeCards();
        distributeObjectives();
    }

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

    public void setPlayerObjective(int playerId, ObjectiveCard objectiveCard) {
        try {
            game.setPlayerObjective(playerId, objectiveCard);
        } catch (UnknownObjectiveCardException e) {
            //TODO handle it
        } catch (UnexpectedActionException e) {
            //TODO handle it
        }

        virtualView.communicatePersonalObjective(playerId, objectiveCard);
        virtualView.redrawView(playerId);

        if (game.allPlayersChoseObjective()) {
            startGame();
        }
    }

    public void startGame() {
        try {
            game.startGame();
        } catch (UnexpectedActionException e) {
            //TODO handle it
        }

        virtualView.communicateGameState(game.getState());
        virtualView.communicateTurnOrder(game.getTurnOrder());
        virtualView.notifyTurnStart(game.getActivePlayer());
        virtualView.redrawView();
    }

    public void turnManager() {
        hasPlacedCard = false;

        try {
            game.advanceTurn();
        } catch (UnexpectedActionException e) {
            //TODO handle it
        }

        if (game.getActivePlayer() == game.getStartingPlayer()) {
            saveGame();
            if (dontDraw) {
                endGame();
                return;
            }
            if (game.getState() == GameState.FINAL_ROUND) {
                dontDraw = true;
                virtualView.communicateDontDraw();
            }
        }

        virtualView.notifyTurnStart(game.getActivePlayer());
    }

    public void placeCard(int playerId, PlayableCard card, SideType side, Position newPos) {
        if (hasPlacedCard) return;

        try {
            game.placeCard(playerId, card, side, newPos);
        } catch (IllegalMoveException e) {
            //TODO handle it
        } catch (UnexpectedActionException e) {
            //TODO handle it
        }

        hasPlacedCard = true;

        if (game.checkFinalRound()) {
            try {
                game.triggerFinalRound();
                virtualView.communicateGameState(game.getState());
            } catch (UnexpectedActionException e) {
                //TODO handle it
            }
        }

        virtualView.communicatePlayerInfo(playerId, game.getPlayers()[playerId]);
        for(PlayerModel player : game.getPlayers()) {
            if (player.getPlayerId() != playerId) {
                virtualView.communicateOtherPlayer(player.getPlayerId(), playerId, game.getPlayers()[playerId]);
            }
        }
        virtualView.redrawView();

        if (dontDraw) {
            turnManager();
        }
    }

    public void drawCard(int playerId, DrawType drawType) {
        if (!hasPlacedCard || dontDraw) return;

        try {
            game.drawCard(playerId, drawType);
        } catch (IllegalMoveException e) {
            //TODO handle it
        } catch (UnexpectedActionException e) {
            //TODO handle it
        }

        virtualView.communicatePlayerInfo(playerId, game.getPlayers()[playerId]);
        for(PlayerModel player : game.getPlayers()) {
            if (player.getPlayerId() != playerId) {
                virtualView.communicateOtherPlayer(player.getPlayerId(), playerId, game.getPlayers()[playerId]);
            }
        }
        virtualView.redrawView();

        turnManager();
    }

    private void endGame() {
        try {
            game.endGame();
        } catch (UnexpectedActionException e) {
            //TODO handle it
        }

        virtualView.communicateGameState(game.getState());
        virtualView.communicateWinners(game.getWinnerIds());
        virtualView.redrawView();
    }

    private void saveGame() {
        lobbyManager.saveGame(game.getId());
    }
}
