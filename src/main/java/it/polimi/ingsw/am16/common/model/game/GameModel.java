package it.polimi.ingsw.am16.common.model.game;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.NoStarterCardException;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.PlayerModel;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.List;

/**
 * The game's interface. It contains all the methods a game can use.
 */
public interface GameModel {
    /**
     *
     * @return The game's ID.
     */
    String getId();

    /**
     * Adds a new player into the game. The number of players cannot exceed numPlayers.
     * @param username The player's username.
     * @return The newly added player's id.
     * @throws UnexpectedActionException Thrown if the game is already full, if the game has already started, or if a player with the given username is already present in the game.
     */
    int addPlayer(String username) throws UnexpectedActionException;

    /**
     *
     * @return The non-variable number of players expected to play the game.
     */
    int getNumPlayers();

    /**
     * @return The number of players who joined the game.
     */
    int getCurrentPlayerCount();

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
     * Initializes the game by drawing the common gold and resource cards, and distributing the starter cards to the players.
     * @throws UnexpectedActionException Thrown if the game was already initialized, or if the game hasn't reached the required number of players.
     */
    void initializeGame() throws UnexpectedActionException;

    /**
     * Lets the player choose the side of their starter card. It can be either front or back.
     * @param playerId The player's ID.
     * @param side The card's side.
     * @throws UnexpectedActionException Thrown if the game has already started, hence all players should have already chosen their starter card side.
     */
    void setPlayerStarterSide(int playerId, SideType side) throws UnexpectedActionException, NoStarterCardException;

    /**
     * Sets the color of a player.
     * @param playerId The player's ID.
     * @param color The color a player chose.
     * @throws UnexpectedActionException Thrown if the game has already started, hence all the players should have already chosen their color, or if the given color has already been chosen by another player.
     */
    void setPlayerColor(int playerId, PlayerColor color) throws UnexpectedActionException;

    /**
     * @return A {@link List} containing all the colors that are still available for a player to choose.
     */
    List<PlayerColor> getAvailableColors();

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
     * Draws the common objective cards and distributes the personal objectives to the players.
     * This method should only be called after everyone has chosen their starter card side and color.
     */
    void initializeObjectives() throws UnexpectedActionException;

    /**
     * Sets the chosen objective card for a specific player.
     * @param playerId The player's ID.
     * @param objectiveCard The chosen objective card.
     * @throws UnknownObjectiveCardException Thrown when the given objective card is not in the player's objective card options.
     * @throws UnexpectedActionException Thrown if the objectives have not yet been distributed, or the game has already started, or the given player has already chosen their objective.
     */
    void setPlayerObjective(int playerId, ObjectiveCard objectiveCard) throws UnknownObjectiveCardException, UnexpectedActionException;

    /**
     *
     * @return Whether all the players have chosen their personal objective.
     */
    boolean allPlayersChoseObjective();

    /**
     * Starts the game by choosing the starting player and distributing the resource and gold cards.
     * @throws UnexpectedActionException Thrown if the game has already been started, or if not all players have chosen their objective card.
     */
    void startGame() throws UnexpectedActionException;

    /**
     * Lets a player place a card.
     * @param playerId The player's ID.
     * @param placedCard The card the player wants to place.
     * @param side The chosen card's side.
     * @param newCardPos The position of the card.
     * @throws IllegalMoveException Thrown if the player made an illegal move.
     * @throws UnexpectedActionException Thrown if this method is called before the game has been started.
     */
    void placeCard(int playerId, PlayableCard placedCard, SideType side, Position newCardPos) throws IllegalMoveException, UnexpectedActionException;

    /**
     * Lets the player draw a card. A card can be drawn from the deck or from the currently visible cards.
     * @param playerId The player's ID.
     * @param drawType The place a player wants to draw a card from.
     * @throws UnexpectedActionException Thrown if this method is called before the game has been started.
     * @throws IllegalMoveException Thrown if a draw is attempted from an empty deck, or from an empty common card slot.
     */
    void drawCard(int playerId, DrawType drawType) throws UnexpectedActionException, IllegalMoveException;

    /**
     * Advances the turn to the next player.
     * @throws UnexpectedActionException Thrown if the game not started yet, or if it has already ended.
     */
    void advanceTurn() throws UnexpectedActionException;

    /**
     * @return Whether the game is ready to enter the final round.
     */
    boolean checkFinalRound();

    /**
     * Triggers the game to enter the final round, if it is ready to do so; otherwise, this method does nothing.
     * @throws UnexpectedActionException Thrown if the game has not started yet, or if it has already ended.
     */
    void triggerFinalRound() throws UnexpectedActionException;

    /**
     * Ends the current game and evaluates the objective points of the players. Then, it selects a winner.
     * @throws UnexpectedActionException Thrown if the game has not yet entered its final round, or if it has already ended.
     */
    void endGame() throws UnexpectedActionException;

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

    /**
     * @return the game's state.
     */
    GameState getState();

    /**
     * @return the type of the card on top of the resource deck. This information should be visible to the players.
     */
    ResourceType getResourceDeckTopType();

    /**
     * @return the type of the card on top of the gold deck. This information should be visible to the player.
     */
    ResourceType getGoldDeckTopType();
}
