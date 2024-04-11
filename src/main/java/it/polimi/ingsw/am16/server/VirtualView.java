package it.polimi.ingsw.am16.server;

import it.polimi.ingsw.am16.client.RemoteViewInterface;
import it.polimi.ingsw.am16.common.controller.GameController;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatModel;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.PlayerModel;
import it.polimi.ingsw.am16.common.model.players.RestrictedPlayerModel;
import it.polimi.ingsw.am16.common.model.players.hand.HandModel;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DOCME
 */
public class VirtualView {

    private final Map<Integer, RemoteViewInterface> userViews;
    private final GameController gameController;

    public VirtualView(GameController gameController) {
        this.userViews = new HashMap<>();
        this.gameController = gameController;
    }

    public void addPlayer(int playerId, RemoteViewInterface userView) {
        this.userViews.put(playerId, userView);
    }

    public void communicateGameState(GameState state) {
        for(RemoteViewInterface userView : userViews.values()) {
            try {
                userView.setGameState(state);
            } catch (RemoteException e) {
                //TODO handle it
            }
        }
    }

    public void communicateCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) {
        for(RemoteViewInterface userView : userViews.values()) {
            try {
                userView.setCommonCards(commonResourceCards, commonGoldCards);
            } catch (RemoteException e) {
                //TODO handle it
            }
        }
    }

    public void promptStarterChoice(int receiverPlayerId, StarterCard card) {
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.promptStarterChoice(card);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    public void promptColorChoice(int receiverPlayerId, List<PlayerColor> colorChoices) {
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.promptColorChoice(colorChoices);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    public void communicateColor(int receiverPlayerId, PlayerColor color) {
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.setColor(color);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    public void communicateHand(int receiverPlayerId, HandModel hand) {
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.setHand(hand);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    public void promptObjectiveChoice(int receiverPlayerId, List<ObjectiveCard> possiblePersonalObjectives) {
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.promptObjectiveChoice(possiblePersonalObjectives);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    public void communicatePersonalObjective(int receiverPlayerId, ObjectiveCard personalObjective) {
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.setPersonalObjective(personalObjective);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    public void communicateCommonObjectives(ObjectiveCard[] commonObjectives) {
        for(RemoteViewInterface userView : userViews.values()) {
            try {
                userView.setCommonObjectives(commonObjectives);
            } catch (RemoteException e) {
                //TODO handle it
            }
        }
    }

    public void communicateTurnOrder(int[] playerIds) {
        for(RemoteViewInterface userView : userViews.values()) {
            try {
                userView.setStartOrder(playerIds);
            } catch (RemoteException e) {
                //TODO handle it
            }
        }
    }

    public void communicatePlayerInfo(int receiverPlayerId, PlayerModel playerInfo) {
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.setPlayerInfo(playerInfo);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    public void communicateOtherPlayer(int receiverPlayerId, int whosePlayerInfo, RestrictedPlayerModel playerInfo) {
        RemoteViewInterface userView = userViews.get(receiverPlayerId);
        try {
            userView.setOtherPlayerInfo(whosePlayerInfo, playerInfo);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    public void notifyTurnStart(int whoseTurn) {
        for(RemoteViewInterface userView : userViews.values()) {
            try {
                userView.turn(whoseTurn);
            } catch (RemoteException e) {
                //TODO handle it
            }
        }
    }

    public void communicateWinners(List<Integer> winnerIds) {
        for(RemoteViewInterface userView : userViews.values()) {
            try {
                userView.setWinnerIds(winnerIds);
            } catch (RemoteException e) {
                //TODO handle it
            }
        }
    }

    public void communicateChat(int playerId, ChatModel chat) {
        RemoteViewInterface userView = userViews.get(playerId);
        try {
            userView.setChat(chat);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    public void promptError(int playerId, String errorMessage) {
        RemoteViewInterface userView = userViews.get(playerId);
        try {
            userView.promptError(errorMessage);
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    public void communicateDontDraw() {
        for(RemoteViewInterface userView : userViews.values()) {
            try {
                userView.notifyDontDraw();
            } catch (RemoteException e) {
                //TODO handle it
            }
        }
    }

    public void redrawView(int playerId) {
        RemoteViewInterface userView = userViews.get(playerId);
        try {
            userView.redrawView();
        } catch (RemoteException e) {
            //TODO handle it
        }
    }

    public void redrawView() {
        for(int id : userViews.keySet()) {
            redrawView(id);
        }
    }
}
