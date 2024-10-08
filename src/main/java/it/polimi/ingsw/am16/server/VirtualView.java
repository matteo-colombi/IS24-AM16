package it.polimi.ingsw.am16.server;

import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.ErrorType;
import it.polimi.ingsw.am16.common.util.Position;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Class used to store all the player views. Used by the controller to communicate with the players.
 */
public class VirtualView {

    private final Map<String, RemoteClientInterface> userViews;

    /**
     * Creates a new empty VirtualView.
     */
    public VirtualView() {
        this.userViews = new HashMap<>();
    }

    /**
     * Communicates to the given player that they have joined the game with the given id.
     * Also tells the client the list of players that are present in the game.
     *
     * @param gameId   The id of the game that the player just joined.
     * @param username The username of the player who joined.
     */
    public void joinGame(String gameId, String username, int numPlayers) {
        RemoteClientInterface userView = userViews.get(username);
        try {
            userView.joinGame(gameId, username, numPlayers);
            userView.setPlayers(new ArrayList<>(userViews.keySet()));
        } catch (RemoteException e) {
            System.err.println("Error sending join game confirmation: " + e.getMessage());
        }
    }

    /**
     * Communicates to all the clients in this VirtualView that rejoin information about this game is about to be sent.
     */
    public void communicateRejoinInformationStart() {
        userViews.values().forEach(userView -> {
            try {
                userView.rejoinInformationStart();
            } catch (RemoteException e) {
                System.err.println("Error communicating rejoin information: " + e.getMessage());
            }
        });
    }

    /**
     * Communicates to all the clients in this VirtualView that rejoin information about the game has all been sent and the game is about to resume.
     */
    public void communicateRejoinInformationEnd() {
        userViews.values().forEach(userView -> {
            try {
                userView.rejoinInformationEnd();
            } catch (RemoteException e) {
                System.err.println("Error communicating rejoin information: " + e.getMessage());
            }
        });
    }

    /**
     * Communicates to all the players that a new player has joined. Then adds the new player to the VirtualView.
     *
     * @param newUserView The player's view interface.
     * @param username    The username of the new player.
     */
    public void addPlayer(RemoteClientInterface newUserView, String username) {
        userViews.keySet().forEach(otherPlayerUsername -> {
            try {
                userViews.get(otherPlayerUsername).addPlayer(username);
            } catch (RemoteException e) {
                System.err.println("Error sending new player username: " + e.getMessage());
            }
        });

        this.userViews.put(username, newUserView);
    }

    /**
     * Communicates the game's state to all the players in this VirtualView.
     *
     * @param state The game's state.
     */
    public void communicateGameState(GameState state) {
        userViews.values().forEach(userView -> {
            try {
                userView.setGameState(state);
            } catch (RemoteException e) {
                System.err.println("Error communicating new game state: " + e.getMessage());
            }
        });
    }

    /**
     * Communicates the common resource and gold cards to all the players in this VirtualView.
     *
     * @param commonResourceCards The common resource cards.
     * @param commonGoldCards     The common gold cards.
     */
    public void communicateCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) {
        userViews.values().forEach(userView -> {
            try {
                userView.setCommonCards(commonResourceCards, commonGoldCards);
            } catch (RemoteException e) {
                System.err.println("Error communicating common cards:" + e.getMessage());
            }
        });
    }

    /**
     * Communicates the card type of the given deck to all the players in this VirtualView.
     *
     * @param whichDeck    The deck which we are communicating the top card type of.
     * @param resourceType The type of the top card on the given deck.
     */
    public void communicateDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) {
        userViews.values().forEach(userView -> {
            try {
                userView.setDeckTopType(whichDeck, resourceType);
            } catch (RemoteException e) {
                System.err.println("Error communicating deck top type:" + e.getMessage());
            }
        });
    }

    /**
     * Prompts the player with the given username to choose their starter card side.
     *
     * @param receiverPlayerUsername The player which this communication should be sent to.
     * @param card                   The starter card.
     */
    public void promptStarterChoice(String receiverPlayerUsername, StarterCard card) {
        RemoteClientInterface userView = userViews.get(receiverPlayerUsername);
        try {
            userView.promptStarterChoice(card);
        } catch (RemoteException e) {
            System.err.println("Error prompting starter card side choice: " + e.getMessage());
        }
    }

    /**
     * Communicates to all the players in this VirtualView that the game has entered the color-choosing phase.
     */
    public void communicateChoosingColors() {
        userViews.values().forEach(userView -> {
            try {
                userView.choosingColors();
            } catch (RemoteException e) {
                System.err.println("Error communicating choosing color phase start: " + e.getMessage());
            }
        });
    }

    /**
     * Prompts the player with the given username to choose their color.
     *
     * @param receiverPlayerUsername The player which this communication should be sent to.
     * @param colorChoices           The list of colors the player can choose from.
     */
    public void promptColorChoice(String receiverPlayerUsername, List<PlayerColor> colorChoices) {
        RemoteClientInterface userView = userViews.get(receiverPlayerUsername);
        try {
            userView.promptColorChoice(colorChoices);
        } catch (RemoteException e) {
            System.err.println("Error prompting color choice: " + e.getMessage());
        }
    }

    /**
     * Communicates the given player's color to all the players in this VirtualView.
     *
     * @param username The username of the player whose color is being given.
     * @param color    The player's color.
     */
    public void communicateColor(String username, PlayerColor color) {
        userViews.values().forEach(userView -> {
            try {
                userView.setColor(username, color);
            } catch (RemoteException e) {
                System.err.println("Error communicating assigned color: " + e.getMessage());
            }
        });
    }

    /**
     * Tells all the players in this VirtualView that the cards for the game are being drawn.
     */
    public void communicateDrawingCards() {
        userViews.values().forEach(userView -> {
            try {
                userView.drawingCards();
            } catch (RemoteException e) {
                System.err.println("Error communicating drawing cards phase starter: " + e.getMessage());
            }
        });
    }

    /**
     * Communicates the hand to the player with the given username.
     *
     * @param receiverPlayerUsername The player which this communication should be sent to.
     * @param hand                   The player's hand.
     */
    public void communicateHand(String receiverPlayerUsername, List<PlayableCard> hand) {
        RemoteClientInterface userView = userViews.get(receiverPlayerUsername);
        try {
            userView.setHand(hand);
        } catch (RemoteException e) {
            System.err.println("Error communicating assigned hand: " + e.getMessage());
        }
    }

    /**
     * Communicates to the given player that they have a new card in their hand.
     *
     * @param receiverPlayerUsername The player which this communication should be sent to.
     * @param card                   The new card in the player's hand.
     */
    public void communicateNewCard(String receiverPlayerUsername, PlayableCard card) {
        RemoteClientInterface userView = userViews.get(receiverPlayerUsername);
        try {
            userView.addCardToHand(card);
        } catch (RemoteException e) {
            System.err.println("Error communicating drawn card: " + e.getMessage());
        }
    }

    /**
     * Communicates to the given player that they no longer have a card in their hand.
     *
     * @param receiverPlayerUsername The player which this communication should be sent to.
     * @param card                   The card to be removed from the player's hand.
     */
    public void communicateRemoveCard(String receiverPlayerUsername, PlayableCard card) {
        RemoteClientInterface userView = userViews.get(receiverPlayerUsername);
        try {
            userView.removeCardFromHand(card);
        } catch (RemoteException e) {
            System.err.println("Error communicating card removal from hand: " + e.getMessage());
        }
    }

    /**
     * Communicates the restricted hand view of a player to the player with the given username.
     *
     * @param receiverPlayerUsername The player which this communication should be sent to.
     * @param username               The player whose hand is being sent.
     * @param hand                   The restricted hand.
     */
    public void communicateOtherHand(String receiverPlayerUsername, String username, List<RestrictedCard> hand) {
        RemoteClientInterface userView = userViews.get(receiverPlayerUsername);
        try {
            userView.setOtherHand(username, hand);
        } catch (RemoteException e) {
            System.err.println("Error communicating other player hand: " + e.getMessage());
        }
    }

    /**
     * Communicates to the given player that another player has a new card in their hand.
     *
     * @param receiverPlayerUsername The player which this communication should be sent to.
     * @param username               The player whose hand the card should be added to.
     * @param newCard                The restricted card to be added to the player's hand.
     */
    public void communicateNewOtherCard(String receiverPlayerUsername, String username, RestrictedCard newCard) {
        RemoteClientInterface userView = userViews.get(receiverPlayerUsername);
        try {
            userView.addCardToOtherHand(username, newCard);
        } catch (RemoteException e) {
            System.err.println("Error communicating new restricted card: " + e.getMessage());
        }
    }

    /**
     * Communicates to the given player that another player no longer has a card in their hand.
     *
     * @param receiverPlayerUsername The player which this communication should be sent to.
     * @param username               The player whose hand the card should be removed from.
     * @param removedCard            The restricted card to be removed from the player's hand.
     */
    public void communicateRemoveOtherCard(String receiverPlayerUsername, String username, RestrictedCard removedCard) {
        RemoteClientInterface userView = userViews.get(receiverPlayerUsername);
        try {
            userView.removeCardFromOtherHand(username, removedCard);
        } catch (RemoteException e) {
            System.err.println("Error communicating restricted card removal: " + e.getMessage());
        }
    }

    /**
     * Communicates a play area to all the players in this VirtualView.
     *
     * @param username           The username of the player whose play area is being given.
     * @param cardPlacementOrder The order in which the cards were played in this play area.
     * @param field              The play area's field.
     * @param legalPositions     The set of positions on which the player can place cards.
     * @param illegalPositions   The set of positions on which the player must not place cards.
     * @param resourceCounts     A map containing the amount of each resource that the player has.
     * @param objectCounts       A map containing the amount of each object that the player has.
     * @param activeSides        The map that keeps track of what side each card was played on.
     */
    public void communicatePlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        userViews.values().forEach(userView -> {
            try {
                userView.setPlayArea(username, cardPlacementOrder, field, activeSides, legalPositions, illegalPositions, resourceCounts, objectCounts);
            } catch (RemoteException e) {
                System.err.println("Error communicating target player's play area: " + e.getMessage());
            }
        });
    }

    /**
     * Communicates to all players in this VirtualView that a new card was played.
     *
     * @param username              The player who played the card.
     * @param card                  The played card.
     * @param side                  The side on which the card was placed on.
     * @param pos                   The position where the new card was placed.
     * @param addedLegalPositions   The set of new positions in which the player can play a card, following the move which was just made.
     * @param removedLegalPositions The set of positions in which the player can no longer play a card, following the move which was just made.
     * @param resourceCounts        A map containing the amount of each resource that the player has, following the move which was just made.
     * @param objectCounts          A map containing the amount of each object that the player has, following the move which was just made.
     */
    public void communicatePlayCard(String username, BoardCard card, SideType side, Position pos, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        userViews.values().forEach(userView -> {
            try {
                userView.playCard(username, card, side, pos, addedLegalPositions, removedLegalPositions, resourceCounts, objectCounts);
            } catch (RemoteException e) {
                System.err.println("Error communicating new played card: " + e.getMessage());
            }
        });
    }

    /**
     * Communicate to all players the number of game points of the specified player.
     *
     * @param username   The username of the player whose points are being given.
     * @param gamePoints The given player's number of game points.
     */
    public void communicateGamePoints(String username, int gamePoints) {
        userViews.values().forEach(userView -> {
            try {
                userView.setGamePoints(username, gamePoints);
            } catch (RemoteException e) {
                System.err.println("Error communicating game points: " + e.getMessage());
            }
        });
    }

    /**
     * Communicate to all players the number of objective points of the specified player.
     *
     * @param username        The username of the player whose points are being given.
     * @param objectivePoints The given player's number of objective points.
     */
    public void communicateObjectivePoints(String username, int objectivePoints) {
        userViews.values().forEach(userView -> {
            try {
                userView.setObjectivePoints(username, objectivePoints);
            } catch (RemoteException e) {
                System.err.println("Error communicating objective points: " + e.getMessage());
            }
        });
    }

    /**
     * Prompts the player with the given username to choose their objective.
     *
     * @param receiverPlayerUsername     The player which this communication should be sent to.
     * @param possiblePersonalObjectives The possible objectives which the player can choose from.
     */
    public void promptObjectiveChoice(String receiverPlayerUsername, List<ObjectiveCard> possiblePersonalObjectives) {
        RemoteClientInterface userView = userViews.get(receiverPlayerUsername);
        try {
            userView.promptObjectiveChoice(possiblePersonalObjectives);
        } catch (RemoteException e) {
            System.err.println("Error communicating objective choices: " + e.getMessage());
        }
    }

    /**
     * Communicates to a player their personal objective.
     *
     * @param receiverPlayerUsername The player which this communication should be sent to.
     * @param personalObjective      The player's personal objective.
     */
    public void communicatePersonalObjective(String receiverPlayerUsername, ObjectiveCard personalObjective) {
        RemoteClientInterface userView = userViews.get(receiverPlayerUsername);
        try {
            userView.setPersonalObjective(personalObjective);
        } catch (RemoteException e) {
            System.err.println("Error communicating personal objective: " + e.getMessage());
        }
    }

    /**
     * Communicates the common objectives for this game to all players in this VirtualView.
     *
     * @param commonObjectives The game's common objectives.
     */
    public void communicateCommonObjectives(ObjectiveCard[] commonObjectives) {
        userViews.values().forEach(userView -> {
            try {
                userView.setCommonObjectives(commonObjectives);
            } catch (RemoteException e) {
                System.err.println("Error communicating common objectives: " + e.getMessage());
            }
        });
    }

    /**
     * Communicates the order in which the players will play to all the players in this VirtualView.
     *
     * @param usernames The list of usernames, in the order in which the players will play.
     */
    public void communicateTurnOrder(List<String> usernames) {
        userViews.values().forEach(userView -> {
            try {
                userView.setStartOrder(usernames);
            } catch (RemoteException e) {
                System.err.println("Error communicating turn order: " + e.getMessage());
            }
        });
    }

    /**
     * Notifies all the players in this VirtualView that the player with the given username's turn has started.
     *
     * @param username The player whose turn has started.
     */
    public void notifyTurnStart(String username) {
        userViews.values().forEach(userView -> {
            try {
                userView.turn(username);
            } catch (RemoteException e) {
                System.err.println("Error communicating turn start: " + e.getMessage());
            }
        });
    }

    /**
     * Communicates to all the players in this VirtualView the winners for this game.
     *
     * @param winnerUsernames The list of winners for this game.
     */
    public void communicateWinners(List<String> winnerUsernames, Map<String, ObjectiveCard> personalObjectives) {
        userViews.values().forEach(userView -> {
            try {
                userView.setWinners(winnerUsernames, personalObjectives);
            } catch (RemoteException e) {
                System.err.println("Error communicating winners: " + e.getMessage());
            }
        });
    }

    /**
     * Communicates to the given player that they have new messages in the chat.
     *
     * @param receiverUsername The player which this communication should be sent to.
     * @param messages         The list of new messages.
     */
    public void communicateNewMessages(String receiverUsername, List<ChatMessage> messages) {
        RemoteClientInterface userView = userViews.get(receiverUsername);
        try {
            userView.addMessages(messages);
        } catch (RemoteException e) {
            System.err.println("Error communicating new chat message: " + e.getMessage());
        }
    }

    /**
     * Communicates to the given player that they have a new message in chat.
     *
     * @param receiverUsername The player which this communication should be sent to.
     * @param newMessage       The new message.
     */
    public void communicateNewMessage(String receiverUsername, ChatMessage newMessage) {
        RemoteClientInterface userView = userViews.get(receiverUsername);
        if (userView == null) return;
        try {
            userView.addMessage(newMessage);
        } catch (RemoteException e) {
            System.err.println("Error communicating new chat message: " + e.getMessage());
        }
    }

    /**
     * Tells the player with the given username that an error has occurred.
     *
     * @param receiverUsername The player which this communication should be sent to.
     * @param errorMessage     The error message.
     * @param errorType        The type of error that occurred.
     */
    public void promptError(String receiverUsername, String errorMessage, ErrorType errorType) {
        RemoteClientInterface userView = userViews.get(receiverUsername);
        try {
            userView.promptError(errorMessage, errorType);
        } catch (RemoteException e) {
            System.err.println("Error communicating the erroring of the errored error: " + e.getMessage());
        }
    }

    /**
     * Communicates to all the players in this VirtualView that from now on they shouldn't draw cards anymore.
     */
    public void communicateDontDraw() {
        userViews.values().forEach(userView -> {
            try {
                userView.notifyDontDraw();
            } catch (RemoteException e) {
                System.err.println("Error communicating don't draw phase: " + e.getMessage());
            }
        });
    }

    /**
     * Communicates to all players that a player has disconnected from the game.
     *
     * @param disconnectedUsername The username of the player who disconnected.
     */
    public void signalDisconnection(String disconnectedUsername) {
        userViews.remove(disconnectedUsername);

        userViews.forEach((username, userView) -> {
            if (!username.equals(disconnectedUsername)) {
                try {
                    userView.signalDisconnection(disconnectedUsername);
                } catch (RemoteException e) {
                    System.err.println("Error disconnecting a player: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Communicates to all players that a player has disconnected from the game, and thus the game has been paused.
     *
     * @param disconnectedUsername The username of the player who disconnected.
     */
    public void signalGameSuspension(String disconnectedUsername) {
        userViews.forEach((username, userView) -> {
            if (!username.equals(disconnectedUsername)) {
                try {
                    userView.signalGameSuspension(disconnectedUsername);
                } catch (RemoteException e) {
                    System.err.println("Error suspending the game: " + e.getMessage());
                }
            }
        });
        userViews.clear();
    }

    /**
     * Communicates to all players that a player has disconnected from the game, and the game is being deleted as a result.
     *
     * @param disconnectedUsername The username of the player who disconnected.
     */
    public void signalGameDeletion(String disconnectedUsername) {
        userViews.forEach((username, userView) -> {
            if (!username.equals(disconnectedUsername)) {
                try {
                    userView.signalGameDeletion(disconnectedUsername);
                } catch (RemoteException e) {
                    System.err.println("Error communicating player disconnection:" + e.getMessage());
                }
            }
        });
        userViews.clear();
    }

    /**
     * Communicates to all users in this VirtualView that a player has skipped their turn because they have no more legal moves to make.
     *
     * @param username The player who skipped the turn because of a deadlock.
     */
    public void communicateDeadlock(String username) {
        userViews.values().forEach(userView -> {
            try {
                userView.signalDeadlock(username);
            } catch (RemoteException e) {
                System.err.println("Error communicating deadlock:" + e.getMessage());
            }
        });
    }

    /**
     * Removes every player from this virtual view.
     */
    public void disconnectEverybody() {
        userViews.values().forEach(userView -> {
            try {
                userView.disconnectFromGame();
            } catch (RemoteException e) {
                System.err.println("Error disconnecting:" + e.getMessage());
            }
        });

        userViews.clear();
    }
}
