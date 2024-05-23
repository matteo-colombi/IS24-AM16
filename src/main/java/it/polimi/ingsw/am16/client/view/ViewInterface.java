package it.polimi.ingsw.am16.client.view;

import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface that includes all the methods needed to set up, start and update the game view.
 */
public interface ViewInterface {

    /**
     * Starts the view. This includes the view's user input manager.
     * DOCME
     */
    void startView(String[] args);

//    /**
//     * Set's the view's {@link ServerInterface}. This interface will be used by the view to send communications to the server.
//     *
//     * @param serverInterface The interface which this view should use to communicate with the server.
//     */
//    void setServerInterface(ServerInterface serverInterface);

    /**
     * Show the existing game IDs to the player.
     *
     * @param gameIds        The existing games' IDs.
     * @param currentPlayers The number of current players
     * @param maxPlayers     The maximum number of players
     */
    void printGames(Set<String> gameIds, Map<String, Integer> currentPlayers, Map<String, Integer> maxPlayers);

    /**
     * Tells the view that the player has joined a game with the given username.
     *
     * @param gameId   The id of the game which the player just joined.
     * @param username The username the player has joined the game with.
     */
    void joinGame(String gameId, String username);

    /**
     * Adds a player to the game. Used to communicate the connection of a new player.
     *
     * @param username The new player's username.
     */
    void addPlayer(String username);

    /**
     * Tells the view all the usernames of the players present in the game.
     *
     * @param usernames The list of usernames of the players present in the game.
     */
    void setPlayers(List<String> usernames);

    /**
     * Sets the game state. To be called when the game's state changes.
     *
     * @param state The new game state.
     */
    void setGameState(GameState state);

    /**
     * Sets the common cards for the game. Should be called whenever these change.
     *
     * @param commonResourceCards The common resource cards (may also contain gold cards if the resource card deck is empty). Should always be of length 2.
     * @param commonGoldCards     The common gold cards (may also contain resource cards if the gold card deck is empty). Should always be of length 2.
     */
    void setCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards);

    /**
     * Sets the types of cards at the top of the respective deck.
     *
     * @param whichDeck    The deck which we are setting the top card of.
     * @param resourceType The resource type of the card on top of the given deck.
     */
    void setDeckTopType(PlayableCardType whichDeck, ResourceType resourceType);

    /**
     * Prompts the user to choose the side of the given starter card.
     *
     * @param starterCard The starter card of the player.
     */
    void promptStarterChoice(StarterCard starterCard);

    /**
     * Tells the client that the color-choosing phase has begun.
     */
    void choosingColors();

    /**
     * Prompts the client to choose their color.
     *
     * @param colorChoices The possible choices for the player's color.
     */
    void promptColorChoice(List<PlayerColor> colorChoices);

    /**
     * Sets the player's color. If the player is still in the prompt because he didn't choose in time, the prompt is invalidated
     *
     * @param username The username whose color is being given.
     * @param color    The color assigned to the player.
     */
    void setColor(String username, PlayerColor color);

    /**
     * Tells the client that the cards for the game are being drawn.
     */
    void drawingCards();

    /**
     * Sets the player's hand.
     *
     * @param hand The player's hand.
     */
    void setHand(List<PlayableCard> hand);

    /**
     * Adds the given card to this player's hand.
     *
     * @param card The card to be added.
     */
    void addCardToHand(PlayableCard card);

    /**
     * Removed the given card from this player's hand.
     *
     * @param card The card to be removed.
     */
    void removeCardFromHand(PlayableCard card);

    /**
     * Sets the given player's restricted hand.
     *
     * @param username The username of the player whose hand is being given.
     * @param hand     The restricted hand.
     */
    void setOtherHand(String username, List<RestrictedCard> hand);

    /**
     * Adds the given restricted card to the given user's hand.
     *
     * @param username The user to add this card to.
     * @param newCard  The restricted card to be added.
     */
    void addCardToOtherHand(String username, RestrictedCard newCard);

    /**
     * Removes the given restricted card from the given user's hand.
     *
     * @param username     The user to remove this card from.
     * @param cardToRemove The restricted card to be removed.
     */
    void removeCardFromOtherHand(String username, RestrictedCard cardToRemove);

    /**
     * Sets the given player's play area.
     *
     * @param username           The player whose play area is being given.
     * @param cardPlacementOrder The order in which the cards were played in this play area.
     * @param field              The user's field.
     * @param activeSides        The map keeping track of which side every card is placed on.
     * @param legalPositions     The set of positions on which the player can place cards.
     * @param illegalPositions   The set of positions on which the player must not place cards.
     * @param resourceCounts     A map containing the amount of each resource that the player has.
     * @param objectCounts       A map containing the amount of each object that the player has.
     */
    void setPlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts);

    /**
     * Adds the given card to the given player's play area.
     *
     * @param username              The username of the player who played the card.
     * @param card                  The played card.
     * @param side                  The card the new card was played on.
     * @param pos                   The position where the new card was played.
     * @param addedLegalPositions   The set of new positions in which the player can play a card, following the move which was just made.
     * @param removedLegalPositions The set of positions in which the player can no longer play a card, following the move which was just made.
     * @param resourceCounts        A map containing the amount of each resource that the player has, following the move which was just made.
     * @param objectCounts          A map containing the amount of each object that the player has, following the move which was just made.
     */
    void playCard(String username, BoardCard card, SideType side, Position pos, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts);

    /**
     * Sets a player's number of game points.
     *
     * @param username   The username of the player whose points are being set.
     * @param gamePoints The given player's number of game points.
     */
    void setGamePoints(String username, int gamePoints);

    /**
     * Sets a player's number of objective points.
     *
     * @param username        The username of the player whose points are being set.
     * @param objectivePoints The given player's number of objective points.
     */
    void setObjectivePoints(String username, int objectivePoints);

    /**
     * Sets the common objectives for the game.
     *
     * @param commonObjectives The common objectives. Should always contain 2 elements.
     */
    void setCommonObjectives(ObjectiveCard[] commonObjectives);

    /**
     * Prompts the player to choose their objective from the ones given.
     *
     * @param possiblePersonalObjectives The possible objectives the player can choose from. Should always contain 2 cards.
     */
    void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives);

    /**
     * Sets the player's personal objective.
     *
     * @param personalObjective The player's personal objective.
     */
    void setPersonalObjective(ObjectiveCard personalObjective);

    /**
     * Sets the turn order for the game.
     *
     * @param usernames The turn order. Should always contain as many usernames as were added at the beginning of the game.
     */
    void setStartOrder(List<String> usernames);

    /**
     * Tells the client that it is the given player's turn to play.
     *
     * @param username The player's username.
     */
    void turn(String username);

    /**
     * Tells the client the winners of the game.
     *
     * @param winnerUsernames The winners of the game.
     */
    void setWinners(List<String> winnerUsernames);

    /**
     * Adds all the messages given to the player's chat.
     *
     * @param messages The chat messages to add.
     */
    void addMessages(List<ChatMessage> messages);

    /**
     * Adds the given message to the player's chat.
     *
     * @param message The new message.
     */
    void addMessage(ChatMessage message);

    /**
     * Tells the client that an error has occurred.
     *
     * @param errorMessage The message that should be displayed to the user.
     */
    void promptError(String errorMessage);

    /**
     * Forces the client to redraw the view.
     */
    void redrawView();

    /**
     * Notifies the client that from now on they shouldn't draw cards anymore.
     */
    void notifyDontDraw();

    /**
     * Tells the client that another client has disconnected. This ends the game, if it had started. If the game hadn't started already, the player is simply removed.
     *
     * @param whoDisconnected The username of the player who disconnected.
     */
    void signalDisconnection(String whoDisconnected);

    /**
     * DOCME
     * @param whoDisconnected
     */
    void signalGameSuspension(String whoDisconnected);

    /**
     * Tells the client that a player has skipped their turn because of a deadlock.
     *
     * @param username The username of the player who skipped their turn.
     */
    void signalDeadlock(String username);

    /**
     * DOCME
     */
    void signalConnectionLost();

}
