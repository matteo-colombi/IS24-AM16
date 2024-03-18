package it.polimi.ingsw.am16.common.model.players;

import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.StarterCard;
import it.polimi.ingsw.am16.common.model.players.hand.HandModel;

import java.util.List;

/**
 * Interface to give external classes access to Player-type objects. It contains all the methods
 * a player is supposed to show to the outside
 */
public interface PlayerModel {
    /**
     * @return The player's username.
     */
    String getUsername();

    /**
     *
     * @return The player's in-game color
     */
    PlayerColor getPlayerColor();

    /**
     *
     * @return The player's ID
     */
    int getPlayerId();

    /**
     *
     * @return The total points the player got at the end of the game, after evaluating
     * their completion of the objectives
     */
    int getTotalPoints();

    /**
     *
     * @return The player's points gathered by placing cards on the board
     */
    int getGamePoints();

    /**
     *
     * @return The player's points gathered by fulfilling conditions on their personal
     * objective and the common objectives of the game
     */
    int getObjectivePoints();

    /**
     *
     * @return The player's hand, giving access only to its non-modifier methods
     */
    HandModel getHand();


    ObjectiveCard getPersonalObjective();

    /**
     *
     * @return The player's board state, giving access only to its non-modifier methods
     */
    PlayAreaModel getPlayArea();

    /**
     *
     * @return The player's two objective cards from which they'll choose their personal
     * objective
     */
    List<ObjectiveCard> getPersonalObjectiveOptions();

    /**
     *
     * @return whether the player has chosen which side of their starter card to display
     */
    boolean getChoseStarterCardSide();

    /**
     *
     * @return which of the two given possible personal objectives the player chose
     */
    boolean getChosePersonalObjective();

    /**
     * @return whether the player has already chosen their color.
     */
    boolean getChoseColor();
}

