package it.polimi.ingsw.am16.common.model.game;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.players.Player;
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
     * @throws UnexpectedActionException TODO write
     */
    void addPlayer(String username) throws UnexpectedActionException;

    /**
     *
     * @return The non-variable number of players expected to play the game.
     */
    int getNumPlayers();

    /**
     * TODO write doc
     * @return
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
     * TODO write doc
     * @throws UnexpectedActionException
     */
    void initializeGame() throws UnexpectedActionException;

    /**
     * Lets the player choose the side of their starter card. It can be either front or back.
     * @param playerId The player's ID.
     * @param side The card's side.
     * @throws UnexpectedActionException TODO write doc
     */
    void setPlayerStarterSide(int playerId, SideType side) throws UnexpectedActionException;

    /**
     * Sets the color of a player.
     * @param playerId The player's ID.
     * @param color The color a player chose.
     * @throws UnexpectedActionException
     */
    void setPlayerColor(int playerId, PlayerColor color) throws UnexpectedActionException;

    /**
     * TODO write doc
     * @return
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
     * TODO write doc
     * @throws UnexpectedActionException
     */
    void initializeObjectives() throws UnexpectedActionException;

    /**
     * Sets the chosen objective card for a specific player.
     * @param playerId The player's ID.
     * @param objectiveCard The chosen objective card.
     * @throws UnknownObjectiveCardException Thrown when the objective card is unknown.
     * @throws UnexpectedActionException TODO write doc
     */
    void setPlayerObjective(int playerId, ObjectiveCard objectiveCard) throws UnknownObjectiveCardException, UnexpectedActionException;

    /**
     *
     * @return Whether all the players have chosen their personal objective.
     */
    boolean allPlayersChoseObjective();

    /**
     * TODO write
     * @throws UnexpectedActionException
     */
    void startGame() throws UnexpectedActionException;

    /**
     * Lets a player place a card.
     * @param playerId The player's ID.
     * @param placedCard The card the player wants to place.
     * @param side The chosen card's side.
     * @param newCardPos The position of the card.
     * @throws IllegalMoveException Thrown if the player made an illegal move.
     * @throws UnexpectedActionException TODO write
     */
    void placeCard(int playerId, PlayableCard placedCard, SideType side, Position newCardPos) throws IllegalMoveException, UnexpectedActionException;

    /**
     * Lets the player draw a card. A card can be drawn from the deck or from the currently visible cards.
     * @param playerId The player's ID.
     * @param drawType The place a player wants to draw a card from.
     * @throws UnexpectedActionException TODO write
     */
    void drawCard(int playerId, DrawType drawType) throws UnexpectedActionException;

    /**
     * TODO write doc
     */
    void advanceTurn() throws UnexpectedActionException;

    /**
     *
     * @return Whether the game switched to the {@link GameState}<code>.FINAL_ROUND</code> state.
     */
    boolean checkFinalRound();

    /**
     * TODO write doc
     * @throws UnexpectedActionException
     */
    void triggerFinalRound() throws UnexpectedActionException;

    /**
     * TODO write doc
     * @throws UnexpectedActionException
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
     * TODO write doc
     * @return
     */
    GameState getState();

    /**
     * TODO write doc
     * @return
     */
    ResourceType getResourceDeckTopType();

    /**
     * TODO write doc
     * @return
     */
    ResourceType getGoldDeckTopType();
}
