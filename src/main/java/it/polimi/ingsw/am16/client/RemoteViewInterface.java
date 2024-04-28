package it.polimi.ingsw.am16.client;

import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * Interface that contains the methods used by the server to communicate with client views.
 */
public interface RemoteViewInterface extends Remote {

    /**
     * Tells the view that they have joined a game with the given username.
     * @param username The username the player has joined the game with.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void joinGame(String username) throws RemoteException;

    /**
     * Adds a player to the game. Used to communicate the connection of a new player.
     * @param username The new player's username.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void addPlayer(String username) throws RemoteException;

    /**
     * DOCME
     * @param usernames
     * @throws RemoteException
     */
    void setPlayers(List<String> usernames) throws RemoteException;

    /**
     * Sets the game state. To be called when the game's state changes.
     * @param state The new game state.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void setGameState(GameState state) throws RemoteException;

    /**
     * Sets the common cards for the game. Should be called whenever these change.
     * @param commonResourceCards The common resource cards (may also contain gold cards if the resource card deck is empty). Should always be of length 2.
     * @param commonGoldCards The common gold cards (may also contain resource cards if the gold card deck is empty). Should always be of length 2.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void setCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) throws RemoteException;

    /**
     * Sets the types of cards at the top of the respective deck.
     * @param whichDeck The deck which we are setting the top card of.
     * @param resourceType The resource type of the card on top of the given deck.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void setDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) throws RemoteException;

    /**
     * Prompts the user to choose the side of the given starter card.
     * @param starterCard The starter card of the player.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void promptStarterChoice(StarterCard starterCard) throws RemoteException;

    /**
     * Tells the client that the color-choosing phase has begun.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void choosingColors() throws RemoteException;

    /**
     * Prompts the client to choose their color.
     * @param colorChoices The possible choices for the player's color.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void promptColorChoice(List<PlayerColor> colorChoices) throws RemoteException;

    /**
     * Sets the player's color. If the player is still in the prompt because he didn't choose in time, the prompt is invalidated
     * @param username The username whose color is being given.
     * @param color The color assigned to the player.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void setColor(String username, PlayerColor color) throws RemoteException;

    /**
     * Tells the client that the cards for the game are being drawn.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void drawingCards() throws RemoteException;

    /**
     * Sets the player's hand.
     * @param hand The player's hand.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void setHand(List<PlayableCard> hand) throws RemoteException;

    /**
     * Adds the given card to this player's hand.
     * @param card The card to be added.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void addCardToHand(PlayableCard card) throws RemoteException;

    /**
     * Removed the given card from this player's hand.
     * @param card The card to be removed.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void removeCardFromHand(PlayableCard card) throws RemoteException;

    /**
     * Sets the given player's restricted hand.
     * @param username The username of the player whose hand is being given.
     * @param hand The restricted hand.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void setOtherHand(String username, List<RestrictedCard> hand) throws RemoteException;

    /**
     * Adds the given restricted card to the given user's hand.
     * @param username The user to add this card to.
     * @param newCard The restricted card to be added.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void addCardToOtherHand(String username, RestrictedCard newCard) throws RemoteException;

    /**
     * Removes the given restricted card from the given user's hand.
     * @param username The user to remove this card from.
     * @param cardToRemove The restricted card to be removed.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void removeCardFromOtherHand(String username, RestrictedCard cardToRemove) throws RemoteException;

    /**
     * Sets the given player's play area.
     * @param username The player whose play area is being given.
     * @param cardPlacementOrder The order in which the cards were played in this play area.
     * @param field The user's field.
     * @param activeSides The map keeping track of which side every card is placed on.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void setPlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides) throws RemoteException;

    /**
     * Adds the given card to the given player's play area.
     * @param username The username of the player who played the card.
     * @param card The played card.
     * @param side The card the new card was played on.
     * @param pos The position where the new card was played.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void playCard(String username, BoardCard card, SideType side, Position pos) throws RemoteException;

    /**
     * Sets a player's number of game points.
     * @param username The username of the player whose points are being set.
     * @param gamePoints The given player's number of game points.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void setGamePoints(String username, int gamePoints) throws RemoteException;

    /**
     * Sets a player's number of objective points.
     * @param username The username of the player whose points are being set.
     * @param objectivePoints The given player's number of objective points.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void setObjectivePoints(String username, int objectivePoints) throws RemoteException;

    /**
     * Sets the common objectives for the game.
     * @param commonObjectives The common objectives. Should always contain 2 elements.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void setCommonObjectives(ObjectiveCard[] commonObjectives) throws RemoteException;

    /**
     * Prompts the player to choose their objective from the ones given.
     * @param possiblePersonalObjectives The possible objectives the player can choose from. Should always contain 2 cards.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) throws RemoteException;

    /**
     * Sets the player's personal objective.
     * @param personalObjective The player's personal objective.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void setPersonalObjective(ObjectiveCard personalObjective) throws RemoteException;

    /**
     * Sets the turn order for the game.
     * @param usernames The turn order. Should always contain as many usernames as were added at the beginning of the game.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void setStartOrder(List<String> usernames) throws RemoteException;

    /**
     * Tells the client that it is the given player's turn to play.
     * @param username The player's username.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void turn(String username) throws RemoteException;

    /**
     * Tells the client the winners of the game.
     * @param winnerUsernames The winners of the game.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void setWinners(List<String> winnerUsernames) throws RemoteException;

    /**
     * Adds all the messages given to the player's chat.
     * @param messages The chat messages to add.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void addMessages(List<ChatMessage> messages) throws RemoteException;

    /**
     * Adds the given message to the player's chat.
     * @param message The new message.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void addMessage(ChatMessage message) throws RemoteException;

    /**
     * Tells the client that an error has occured.
     * @param errorMessage The message that should be displayed to the user.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void promptError(String errorMessage) throws RemoteException;

    /**
     * Forces the client to redraw the view.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void redrawView() throws RemoteException;

    /**
     * Notifies the client that from now on they shouldn't draw cards anymore.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void notifyDontDraw() throws RemoteException;

    /**
     * Tells the client that another client has disconnected. This ends the game, if it had started. If the game hadn't started already, the player is simply removed.
     * @param whoDisconnected The username of the player who disconnected.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void signalDisconnection(String whoDisconnected) throws RemoteException;

    /**
     * Tells the client that a player has skipped their turn because of a deadlock.
     * @param username The username of the player who skipped their turn.
     * @throws RemoteException thrown if an error occurs during Java RMI communication.
     */
    void signalDeadlock(String username) throws RemoteException;

    /**
     * You saw nothing ;)
     */
    void rick() throws RemoteException;

}