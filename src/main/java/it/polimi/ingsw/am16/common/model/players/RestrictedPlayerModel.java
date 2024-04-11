package it.polimi.ingsw.am16.common.model.players;

import it.polimi.ingsw.am16.common.model.players.hand.HandModel;
import it.polimi.ingsw.am16.common.model.players.hand.RestrictedHandModel;

/**
 * DOCME
 */
public interface RestrictedPlayerModel {
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
     * @return
     */
    RestrictedHandModel getHand();

    /**
     * @return The player's board state, giving access only to its non-modifier methods
     */
    PlayAreaModel getPlayArea();
}
