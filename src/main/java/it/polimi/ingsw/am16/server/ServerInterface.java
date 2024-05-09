package it.polimi.ingsw.am16.server;

import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface used to handle the communication between the server and the clients.
 */
public interface ServerInterface extends Remote {

    /**
     * Creates a new game with the given number of players.
     * @param username The username of the player that created the game.
     * @param numPlayers The number of players that will play the game.
     * @throws RemoteException If an error occurs while creating the game.
     */
    void createGame(String username, int numPlayers) throws RemoteException;

    /**
     * Joins the game with the given ID.
     * @param gameId The ID of the game to join.
     * @param username The username of the player that wants to join the game.
     * @throws RemoteException If an error occurs while joining the game.
     */
    void joinGame(String gameId, String username) throws RemoteException;

    /**
     * Sets the starter card for the game.
     * @param side The side of the starter card.
     * @throws RemoteException If an error occurs while setting the starter card.
     */
    void setStarterCard(SideType side) throws RemoteException;

    /**
     * Sets the player's color.
     * @param color The color of the player.
     * @throws RemoteException If an error occurs while setting the player's color.
     */
    void setColor(PlayerColor color) throws RemoteException;

    /**
     * Sets the player's personal objective.
     * @param objectiveCard The player's personal objective.
     * @throws RemoteException If an error occurs while setting the player's personal objective.
     */
    void setPersonalObjective(ObjectiveCard objectiveCard) throws RemoteException;

    /**
     * Plays a card on the given side and position.
     * @param playedCard The card to play.
     *  @param side The side on which the card will be played.
     *  @param pos The position where the card will be played.
     * @throws RemoteException If an error occurs while setting the player's private objective.
     */
    void playCard(PlayableCard playedCard, SideType side, Position pos) throws RemoteException;

    /**
     * Draws a card from the deck or from the common cards.
     * @param drawType The type of draw to perform (deck or common).
     * @throws RemoteException If an error occurs while drawing the card.
     */
    void drawCard(DrawType drawType) throws RemoteException;

    /**
     * Sends a chat message to all the players in the game.
     * @param text The text of the message.
     * @throws RemoteException If an error occurs while sending the message.
     */
    void sendChatMessage(String text) throws RemoteException;

    /**
     * Sends a chat message to the given player.
     * @param text The text of the message.
     * @param receiverUsername The username of the player that will receive the message.
     * @throws RemoteException If an error occurs while sending the message.
     */
    void sendChatMessage(String text, String receiverUsername) throws RemoteException;

    /**
     * Disconnects the player from the game.
     * @throws RemoteException If an error occurs while disconnecting the player.
     */
    void disconnect() throws RemoteException;

    /**
     * Lets the player leave the game, disconnecting them.
     * @throws RemoteException If an error occurs while leaving the game.
     */
    void leaveGame() throws RemoteException;

    /**
     * Pings the server to check if it's still alive.
     * @throws RemoteException
     */
    void pong() throws RemoteException;
}
