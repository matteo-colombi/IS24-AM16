package it.polimi.ingsw.am16.common.model.game;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.NoStarterCardException;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.List;
import java.util.Map;

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
     * @throws UnexpectedActionException Thrown if the game is already full, if the game has already started, or if there is already a player with the same username present in the game.
     */
    void addPlayer(String username) throws UnexpectedActionException;

    /**
     * Removes a player from the game. This method can only be used in the initial phase of the game.
     *
     * @param username The username of the player to be removed. If a player with the given username does not exist, this method does nothing.
     * @throws UnexpectedActionException Thrown if an attempt was made to remove a player from a game which has already started.
     */
    void removePlayer(String username) throws UnexpectedActionException;

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
     * Sets a player's connection status.
     * @param username The player's username.
     * @param connected Whether the player is connected.
     */
    void setConnected(String username, boolean connected);

    /**
     * @return whether all the players in this game are connected.
     */
    boolean everybodyConnected();

    /**
     *
     * @return The username of the player who has to finish their turn.
     */
    String getActivePlayer();

    /**
     *
     * @return The username of the player whose turn is the first.
     */
    String getStartingPlayer();

    /**
     *
     * @return The id(s) of the player(s) who won.
     */
    List<String> getWinnerUsernames();

    /**
     * Initializes the game by drawing the common gold and resource cards, and distributing the starter cards to the players.
     * @throws UnexpectedActionException Thrown if the game was already initialized, or if the game hasn't reached the required number of players.
     */
    void initializeGame() throws UnexpectedActionException;

    /**
     * Lets the player choose the side of their starter card. It can be either front or back.
     * @param username The player's username.
     * @param side The card's side.
     * @throws UnexpectedActionException Thrown if the game has already started, hence all players should have already chosen their starter card side.
     */
    void setPlayerStarterSide(String username, SideType side) throws UnexpectedActionException, NoStarterCardException;

    /**
     * Sets the color of a player.
     * @param username The player's username.
     * @param color The color a player chose.
     * @throws UnexpectedActionException Thrown if the game has already started, hence all the players should have already chosen their color, or if the given color has already been chosen by another player.
     */
    void setPlayerColor(String username, PlayerColor color) throws UnexpectedActionException;

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
     * Distributes two resource cards and a gold card so that the game can start.
     */
    void distributeCards();

    /**
     * Draws the common objective cards and distributes the personal objectives to the players.
     * This method should only be called after everyone has chosen their starter card side and color.
     */
    void initializeObjectives();

    /**
     * Sets the chosen objective card for a specific player.
     * @param username The player's username.
     * @param objectiveCard The chosen objective card.
     * @throws UnexpectedActionException Thrown if the objectives have not yet been distributed, or the game has already started, or the given player has already chosen their objective.
     */
    void setPlayerObjective(String username, ObjectiveCard objectiveCard) throws UnexpectedActionException, UnknownObjectiveCardException;

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
     * @param username The player's username.
     * @param placedCard The card the player wants to place.
     * @param side The chosen card's side.
     * @param newCardPos The position of the card.
     * @throws IllegalMoveException Thrown if the player made an illegal move.
     * @throws UnexpectedActionException Thrown if this method is called before the game has been started.
     */
    void placeCard(String username, PlayableCard placedCard, SideType side, Position newCardPos) throws IllegalMoveException, UnexpectedActionException;

    /**
     * Lets the player draw a card. A card can be drawn from the deck or from the currently visible cards.
     * If the card is drawn from one of the common cards, it is replaced with a card of the same type if available. If a card of the same type is not available, it is replaced with a card of the other type.
     * @param username The player's username.
     * @param drawType The place a player wants to draw a card from.
     * @return The drawn card.
     * @throws UnexpectedActionException Thrown if this method is called before the game has been started.
     * @throws IllegalMoveException Thrown if a draw is attempted from an empty deck, or from an empty common card slot.
     */
    PlayableCard drawCard(String username, DrawType drawType) throws UnexpectedActionException, IllegalMoveException;

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
    Map<String, Player> getPlayers();

    /**
     *
     * @return The common objective cards.
     */
    ObjectiveCard[] getCommonObjectiveCards();

    /**
     *
     * @return The visible and drawable gold cards.
     */
    PlayableCard[] getCommonGoldCards();

    /**
     *
     * @return The visible and drawable resource cards.
     */
    PlayableCard[] getCommonResourceCards();

    /**
     * @return the game's state.
     */
    GameState getState();

    /**
     * @return whether the players are rejoining after a server crash.
     */
    boolean isRejoiningAfterCrash();

    /**
     * @return the type of the card on top of the resource deck. This information should be visible to the players.
     */
    ResourceType getResourceDeckTopType();

    /**
     * @return the type of the card on top of the gold deck. This information should be visible to the player.
     */
    ResourceType getGoldDeckTopType();

    /**
     * @return a list containing the usernames of the players in the order in which they play.
     */
    List<String> getTurnOrder();

    /**
     * Pauses the game. Used when a player disconnects from the game.
     */
    void pause();
}
