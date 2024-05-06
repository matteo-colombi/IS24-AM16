package it.polimi.ingsw.am16.server;

import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.players.*;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.util.Position;

import java.rmi.RemoteException;
import java.util.*;

/**
 * Class used to store all the player views. Used by the controller to communicate with the players.
 */
public class VirtualView {

    private final Map<Integer, RemoteClientInterface> userViews;
    private final List<String> usernames;

    /**
     * Creates a new empty VirtualView.
     */
    public VirtualView() {
        this.userViews = new HashMap<>();
        this.usernames = new ArrayList<>();
    }

    public void joinGame(int playerId, String gameId, String username) {
        RemoteClientInterface userView = userViews.get(playerId);
        try {
            userView.joinGame(gameId, username);
            userView.setPlayers(new ArrayList<>(usernames));
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * Communicates to all the players that a new player has joined. Then adds the new player to the VirtualView.
     * @param playerId The new player's id.
     * @param newUserView The player's view interface.
     * @param username The username of the new player.
     */
    public void addPlayer(int playerId, RemoteClientInterface newUserView, String username) {
        userViews.keySet().forEach(otherPlayerId -> {
            try {
                userViews.get(otherPlayerId).addPlayer(username);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });

        this.userViews.put(playerId, newUserView);
        this.usernames.add(username);
    }

    /**
     * Communicates the game's state to all the players in this VirtualView.
     * @param state The game's state.
     */
    public void communicateGameState(GameState state) {
        userViews.values().forEach(userView -> {
            try {
                userView.setGameState(state);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }

    /**
     * Communicates the common resource and gold cards to all the players in this VirtualView.
     * @param commonResourceCards The common resource cards.
     * @param commonGoldCards The common gold cards.
     */
    public void communicateCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) {
        userViews.values().forEach(userView -> {
            try {
                userView.setCommonCards(commonResourceCards, commonGoldCards);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }

    /**
     * Communicates the card type of the given deck to all the players in this VirtualView.
     * @param whichDeck The deck which we are communicating the top card type of.
     * @param resourceType The type of the top card on the given deck.
     */
    public void communicateDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) {
        userViews.values().forEach(userView -> {
            try {
                userView.setDeckTopType(whichDeck, resourceType);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }

    /**
     * Prompts the player with the given id to choose their starter card side.
     * @param receiverPlayerId The player which this communication should be sent to.
     * @param card The starter card.
     */
    public void promptStarterChoice(int receiverPlayerId, StarterCard card) {
        RemoteClientInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.promptStarterChoice(card);
        } catch (RemoteException e) {
            //TODO handle it
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
                //TODO handle it
            }
        });
    }

    /**
     * Prompts the player with the given id to choose their color.
     * @param receiverPlayerId The player which this communication should be sent to.
     * @param colorChoices The list of colors the player can choose from.
     */
    public void promptColorChoice(int receiverPlayerId, List<PlayerColor> colorChoices) {
        RemoteClientInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.promptColorChoice(colorChoices);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * Communicates the given player's color to all the players in this VirtualView.
     * @param username The username of the player whose color is being given.
     * @param color The player's color.
     */
    public void communicateColor(String username, PlayerColor color) {
        userViews.values().forEach(userView -> {
            try {
                userView.setColor(username, color);
            } catch (RemoteException e) {
                //TODO handle it
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
                //TODO handle it
            }
        });
    }

    /**
     * Communicates the hand to the player with the given id.
     * @param receiverPlayerId The player which this communication should be sent to.
     * @param hand The player's hand.
     */
    public void communicateHand(int receiverPlayerId, List<PlayableCard> hand) {
        RemoteClientInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.setHand(hand);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * Communicates to the given player that they have a new card in their hand.
     * @param receiverPlayerId The player which this communication should be sent to.
     * @param card The new card in the player's hand.
     */
    public void communicateNewCard(int receiverPlayerId, PlayableCard card) {
        RemoteClientInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.addCardToHand(card);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * Communicates to the given player that they no longer have a card in their hand.
     * @param receiverPlayerId The player which this communication should be sent to.
     * @param card The card to be removed from the player's hand.
     */
    public void communicateRemoveCard(int receiverPlayerId, PlayableCard card) {
        RemoteClientInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.removeCardFromHand(card);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * Communicates the restricted hand view of a player to the player with the given id.
     * @param receiverPlayerId The player which this communication should be sent to.
     * @param username The player whose hand is being sent.
     * @param hand The restricted hand.
     */
    public void communicateOtherHand(int receiverPlayerId, String username, List<RestrictedCard> hand) {
        RemoteClientInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.setOtherHand(username, hand);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * Communicates to the given player that another player has a new card in their hand.
     * @param receiverPlayerId The player which this communication should be sent to.
     * @param username The player whose hand the card should be added to.
     * @param newCard The restricted card to be added to the player's hand.
     */
    public void communicateNewOtherCard(int receiverPlayerId, String username, RestrictedCard newCard) {
        RemoteClientInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.addCardToOtherHand(username, newCard);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * Communicates to the given player that another player no longer has a card in their hand.
     * @param receiverPlayerId The player which this communication should be sent to.
     * @param username The player whose hand the card should be removed from.
     * @param removedCard The restricted card to be removed from the player's hand.
     */
    public void communicateRemoveOtherCard(int receiverPlayerId, String username, RestrictedCard removedCard) {
        RemoteClientInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.removeCardFromOtherHand(username, removedCard);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * Communicates a play area to all the players in this VirtualView.
     * @param username The username of the player whose play area is being given.
     * @param cardPlacementOrder The order in which the cards were played in this play area.
     * @param field The play area's field.
     * @param legalPositions DOCME
     * @param illegalPositions DOCME
     * @param resourceCounts DOCME
     * @param objectCounts DOCME
     * @param activeSides The map that keeps track of what side each card was played on.
     */
    public void communicatePlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        userViews.values().forEach(userView -> {
            try {
                userView.setPlayArea(username, cardPlacementOrder, field, activeSides, legalPositions, illegalPositions, resourceCounts, objectCounts);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }

    /**
     * Communicates to all players in this VirtualView that a new card was played.
     * @param username The player who played the card.
     * @param card The played card.
     * @param side The side on which the card was placed on.
     * @param pos The position where the new card was placed.
     * @param addedLegalPositions DOCME
     * @param removedLegalPositions DOCME
     * @param resourceCounts DOCME,
     * @param objectCounts DOCME
     */
    public void communicatePlayCard(String username, BoardCard card, SideType side, Position pos, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        userViews.values().forEach(userView -> {
            try {
                userView.playCard(username, card, side, pos, addedLegalPositions, removedLegalPositions, resourceCounts, objectCounts);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }

    /**
     * Communicate to all players the number of game points of the specified player.
     * @param username The username of the player whose points are being given.
     * @param gamePoints The given player's number of game points.
     */
    public void communicateGamePoints(String username, int gamePoints) {
        userViews.values().forEach(userView -> {
            try {
                userView.setGamePoints(username, gamePoints);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }

    /**
     * Communicate to all players the number of objective points of the specified player.
     * @param username The username of the player whose points are being given.
     * @param objectivePoints The given player's number of objective points.
     */
    public void communicateObjectivePoints(String username, int objectivePoints) {
        userViews.values().forEach(userView -> {
            try {
                userView.setObjectivePoints(username, objectivePoints);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }

    /**
     * Prompts the player with the given id to choose their objective.
     * @param receiverPlayerId The player which this communication should be sent to.
     * @param possiblePersonalObjectives The possible objectives which the player can choose from.
     */
    public void promptObjectiveChoice(int receiverPlayerId, List<ObjectiveCard> possiblePersonalObjectives) {
        RemoteClientInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.promptObjectiveChoice(possiblePersonalObjectives);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * Communicates to a player their personal objective.
     * @param receiverPlayerId The player which this communication should be sent to.
     * @param personalObjective The player's personal objective.
     */
    public void communicatePersonalObjective(int receiverPlayerId, ObjectiveCard personalObjective) {
        RemoteClientInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.setPersonalObjective(personalObjective);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * Communicates the common objectives for this game to all players in this VirtualView.
     * @param commonObjectives The game's common objectives.
     */
    public void communicateCommonObjectives(ObjectiveCard[] commonObjectives) {
        userViews.values().forEach(userView -> {
            try {
                userView.setCommonObjectives(commonObjectives);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }

    /**
     * Communicates the order in which the players will play to all the players in this VirtualView.
     * @param usernames The list of usernames, in the order in which the players will play.
     */
    public void communicateTurnOrder(List<String> usernames) {
        userViews.values().forEach(userView -> {
            try {
                userView.setStartOrder(usernames);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }

    /**
     * Notifies all the players in this VirtualView that the player with the given username's turn has started.
     * @param username The player whose turn has started.
     */
    public void notifyTurnStart(String username) {
        userViews.values().forEach(userView -> {
            try {
                userView.turn(username);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }

    /**
     * Communicates to all the players in this VirtualView the winners for this game.
     * @param winnerUsernames The list of winners for this game.
     */
    public void communicateWinners(List<String> winnerUsernames) {
        userViews.values().forEach(userView -> {
            try {
                userView.setWinners(winnerUsernames);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }

    /**
     * Communicates to the given player that they have new messages in the chat.
     * @param playerId The player which this communication should be sent to.
     * @param messages The list of new messages.
     */
    public void communicateNewMessages(int playerId, List<ChatMessage> messages) {
        RemoteClientInterface userView = userViews.get(playerId);
        try {
            userView.addMessages(messages);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * Communicates to all the players in this VirtualView that there is a new message in chat.
     * @param newMessage The new message.
     */
    public void communicateNewMessage(ChatMessage newMessage) {
        userViews.values().forEach(userView -> {
            try {
                userView.addMessage(newMessage);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }

    /**
     * Communicates to the given player that they have a new message in chat.
     * @param playerId The player which this communication should be sent to.
     * @param newMessage The new message.
     */
    public void communicateNewMessage(int playerId, ChatMessage newMessage) {
        RemoteClientInterface userView = userViews.get(playerId);
        if (userView == null) return;
        try {
            userView.addMessage(newMessage);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * Tells the player with the given id that an error has occurred.
     * @param playerId The player which this communication should be sent to.
     * @param errorMessage The error message.
     */
    public void promptError(int playerId, String errorMessage) {
        RemoteClientInterface userView = userViews.get(playerId);
        try {
            userView.promptError(errorMessage);
        } catch (RemoteException e) {
            //TODO handle it
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
                //TODO handle it
            }
        });
    }

    /**
     * Tells the player with the given player id to redraw their view.
     * @param playerId The player which this communication should be sent to.
     */
    public void redrawView(int playerId) {
        RemoteClientInterface userView = userViews.get(playerId);
        if (userView == null) return;
        try {
            userView.redrawView();
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * Tells all the players in this VirtualView that they should redraw their view.
     */
    public void redrawView() {
        for(int id : userViews.keySet()) {
            redrawView(id);
        }
    }

    /**
     * Communicates to all players that a player has disconnected from the game.
     * @param disconnectedId The id of the player who disconnected.
     * @param disconnectedUsername The username of the player who disconnected.
     */
    public void signalDisconnection(int disconnectedId, String disconnectedUsername) {
        userViews.forEach((id, userView) -> {
            if (id != disconnectedId) {
                try {
                    userView.signalDisconnection(disconnectedUsername);
                } catch (RemoteException e) {
                    //TODO handle it
                }
            }
        });
    }

    /**
     * Communicates to all users in this VirtualView that a player has skipped their turn because they have no more legal moves to make.
     * @param username The player who skipped the turn because of a deadlock.
     */
    public void communicateDeadlock(String username) {
        userViews.forEach((id, userView) -> {
            try {
                userView.signalDeadlock(username);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }
}
