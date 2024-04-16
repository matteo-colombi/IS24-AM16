package it.polimi.ingsw.am16.server;

import it.polimi.ingsw.am16.client.view.RemoteViewInterface;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.players.*;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatModel;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.hand.HandModel;
import it.polimi.ingsw.am16.common.util.Position;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used to store all the player views. Used by the controller to communicate with the players.
 */
public class VirtualView {

    private final Map<Integer, RemoteViewInterface> userViews;

    /**
     * Creates a new empty VirtualView.
     */
    public VirtualView() {
        this.userViews = new HashMap<>();
    }

    /**
     * Communicates to all the players that a new player has joined. Then adds the new player to the VirtualView.
     * @param playerId The new player's id.
     * @param userView The player's view interface.
     * @param username The username of the new player.
     */
    public void addPlayer(int playerId, RemoteViewInterface userView, String username) {
        userViews.values().forEach(otherUserView -> {
            try {
                otherUserView.addPlayer(username);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });

        this.userViews.put(playerId, userView);
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
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
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
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.promptColorChoice(colorChoices);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * DOCME
     * @param username
     * @param color
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
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.setHand(hand);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * DOCME
     * @param receiverPlayerId
     * @param card
     */
    public void communicateNewCard(int receiverPlayerId, PlayableCard card) {
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.addCardToHand(card);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * DOCME
     * @param receiverPlayerId
     * @param card
     */
    public void communicateRemoveCard(int receiverPlayerId, PlayableCard card) {
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
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
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.setOtherHand(username, hand);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * DOCME
     * @param receiverPlayerId
     * @param username
     * @param newCard
     */
    public void communicateNewOtherCard(int receiverPlayerId, String username, RestrictedCard newCard) {
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.addCardToOtherHand(username, newCard);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * DOCME
     * @param receiverPlayerId
     * @param username
     * @param removedCard
     */
    public void communicateRemoveOtherCard(int receiverPlayerId, String username, RestrictedCard removedCard) {
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.removeCardFromOtherHand(username, removedCard);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * DOCME
     * @param username
     * @param cardPlacementOrder
     * @param field
     * @param activeSides
     */
    public void communicatePlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides) {
        userViews.values().forEach(userView -> {
            try {
                userView.setPlayArea(username, cardPlacementOrder, field, activeSides);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }

    /**
     * DOCME
     * @param username
     * @param card
     * @param side
     * @param pos
     */
    public void communicatePlayCard(String username, BoardCard card, SideType side, Position pos) {
        userViews.values().forEach(userView -> {
            try {
                userView.playCard(username, card, side, pos);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }

    /**
     * Communicate to all players the number of game points of the specified player.
     * @param whosePoints The username of the player whose points are being given.
     * @param gamePoints The given player's number of game points.
     */
    public void communicateGamePoints(String whosePoints, int gamePoints) {
        userViews.values().forEach(userView -> {
            try {
                userView.setGamePoints(whosePoints, gamePoints);
            } catch (RemoteException e) {
                //TODO handle it
            }
        });
    }

    /**
     * Communicate to all players the number of objective points of the specified player.
     * @param whosePoints The username of the player whose points are being given.
     * @param objectivePoints The given player's number of objective points.
     */
    public void communicateObjectivePoints(String whosePoints, int objectivePoints) {
        userViews.values().forEach(userView -> {
            try {
                userView.setObjectivePoints(whosePoints, objectivePoints);
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
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
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
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
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
     * @param whoseTurn The player whose turn has started.
     */
    public void notifyTurnStart(String whoseTurn) {
        userViews.values().forEach(userView -> {
            try {
                userView.turn(whoseTurn);
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
     * DOCME
     * @param playerId
     * @param messages
     */
    public void communicateNewMessages(int playerId, List<ChatMessage> messages) {
        RemoteViewInterface userView = userViews.get(playerId);
        try {
            userView.addMessages(messages);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    /**
     * DOCME
     * @param newMessage
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
     * DOCME
     * @param playerId
     * @param newMessage
     */
    public void communicateNewMessage(int playerId, ChatMessage newMessage) {
        RemoteViewInterface userView = userViews.get(playerId);
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
        RemoteViewInterface userView = userViews.get(playerId);
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
        RemoteViewInterface userView = userViews.get(playerId);
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
}
