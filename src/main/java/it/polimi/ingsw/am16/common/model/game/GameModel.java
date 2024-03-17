package it.polimi.ingsw.am16.common.model.game;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.PlayerModel;
import it.polimi.ingsw.am16.common.util.Position;
import javafx.geometry.Side;

import java.util.List;

/**
 * The game's interface. It contains all the methods a game can use.
 */
public interface GameModel {
    /**
     *
     * @return The game's ID.
     */
    String getid();

    /**
     * Adds a new player into the game. The number of players cannot exceed numPlayers.
     * @param username The player's username.
     */
    void addPlayer(String username);

    /**
     *
     * @return The non-variable number of players expected to play the game.
     */
    int getNumPlayers();

    /**
     *
     * @return The id of the player who has to finish their turn.
     */
    int getActivePlayer();

    /**
     *
     * @return The id of the player whose turn is the first.
     */
    int getStartingPlayer();

    /**
     *
     * @return The id(s) of the player(s) who won.
     */
    List<Integer> getWinnerIds();

    /**
     *
     * @return Whether the game has reached the "End Game" state.
     */
    boolean getIsEndGame();

    void initializeGame();

    /**
     * Draws two gold cards and two resource cards. The players can see them and choose to draw them.
     */
    void drawCommonCards();

    /**
     * Draws numPlayers starter cards and gives one to each player.
     */
    void drawStarterCards();

    /**
     * Lets the player choose the side of their starter card. It can be either front or back.
     * @param playerId The player's ID.
     * @param side The card's side.
     */
    void setPlayerStarterSide(int playerId, SideType side);

    /**
     * Sets the color of a player.
     * @param playerId The player's ID.
     * @param color The color a player chose.
     */
    void setPlayerColor(int playerId, PlayerColor color);

    /**
     *
     * @return Whether all the players have chosen the side of their starter card.
     */
    boolean allPlayersChoseStarterSide();

    /**
     *
     * @return Whether all the players have chosen their color.
     */
    boolean allPlayersChoseColor();

    /**
     * Distributes two resource cards and a gold card so that the game can start.
     */
    void distributeCards();

    /**
     * Draws two objective cards, every player can see them.
     */
    void drawCommonObjectives();

    /**
     * Gives every player two objective cards.
     */
    void distributePersonalObjectives();

    /**
     * Sets the chosen objective card for a specific player.
     * @param playerId The player's ID.
     * @param objectiveCard The chosen objective card.
     * @throws UnknownObjectiveCardException Thrown when the objective card is unknown.
     */
    void setPlayerObjective(int playerId, ObjectiveCard objectiveCard) throws UnknownObjectiveCardException;

    /**
     *
     * @return Whether all the players have chosen their personal objective.
     */
    boolean allPlayersChoseObjective();


    void startGame();

    /**
     * Chooses the starting player randomly.
     * @return The starting player's ID.
     */
    int chooseStartingPlayer();

    /**
     * Lets a player place a card.
     * @param playerId The player's ID.
     * @param placedCard The card the player wants to place.
     * @param side The chosen card's side.
     * @param newCardPos The position of the card.
     * @throws IllegalMoveException Thrown if the player made an illegal move.
     */
    void placeCard(int playerId, PlayableCard placedCard, SideType side, Position newCardPos) throws IllegalMoveException;

    /**
     * Lets the player draw a card. A card can be drawn from the deck or from the currently visible cards.
     * @param playerId The player's ID.
     * @param drawType The place a player wants to draw a card from.
     */
    void drawCard(int playerId, DrawType drawType);

    /**
     *
     * @return Whether the game switched to the "End Game" state.
     */
    boolean checkEndGame();

    /**
     * Evaluates every player's points earned by fulfilling the objective cards' (both common and personal) requests.
     */
    void evaluateObjectivePoints();


    void endGame();

    /**
     * Chooses the winner(s) of the game.
     */
    void selectWinners();

    /**
     *
     * @return The players inside the game.
     */
    PlayerModel[] getPlayers();

    /**
     *
     * @return The common objective cards.
     */
    ObjectiveCard[] getCommonObjectiveCards();

    /**
     *
     * @return The visible and drawable gold cards.
     */
    GoldCard[] getCommonGoldCards();

    /**
     *
     * @return The visible and drawable resource cards.
     */
    ResourceCard[] getCommonResourceCards();
}
