package it.polimi.ingsw.am16.common.model.game;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.PlayerModel;
import it.polimi.ingsw.am16.common.util.Position;
import javafx.geometry.Side;

import java.util.List;

public interface GameModel {
    String getid();
    void addPlayer(String username);
    int getNumPlayers();
    int getActivePlayer();
    int getStartingPlayer();
    List<Integer> getWinnerIds();
    boolean getIsEndGame();
    void initializeGame();
    void drawCommonCards();
    void drawStarterCards();
    void setPlayerStarterSide(int playerId, SideType side);
    void setPlayerColor(int playerId, PlayerColor color);
    boolean allPlayersChoseStarterSide();
    boolean allPlayersChoseColor();
    void distributeCards();
    void drawCommonObjectives();
    void distributePersonalObjectives();
    void setPlayerObjective(int playerId, ObjectiveCard objectiveCard);
    boolean allPlayersChoseObjective();
    void startGame();
    int chooseStartingPlayer();
    void placeCard(int playerId, PlayableCard placedCard, SideType side, Position newCardPos) throws IllegalMoveException;
    void drawCard(int playerId, DrawType drawType);
    boolean checkEndGame();
    void evaluateObjectivePoints();
    void endGame();
    int selectWinner();
    PlayerModel[] getPlayers();
    ObjectiveCard[] getCommonObjectiveCards();
    GoldCard[] getCommonGoldCards();
    ResourceCard[] getCommonResourceCards();
}
