package it.polimi.ingsw.am16.common.model.game;

/**
 * Enum that contains the possible states of a lobby.
 */
public enum LobbyState {
    /**
     * Players are still joining the game.
     */
    JOINING,

    /**
     * The lobby is waiting for previous players to resume the game.
     */
    REJOINING,

    /**
     * The lobby contains a game which is currently ongoing.
     */
    IN_GAME,

    /**
     * The lobby contains a game which is about to end, or has already ended.
     */
    ENDING
}
