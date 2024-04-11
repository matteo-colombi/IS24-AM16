package it.polimi.ingsw.am16.client;

import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatModel;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.PlayerModel;
import it.polimi.ingsw.am16.common.model.players.RestrictedPlayerModel;
import it.polimi.ingsw.am16.common.model.players.hand.HandModel;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * DOCME
 */
public interface RemoteViewInterface extends Remote {

    void setGameState(GameState state) throws RemoteException;

    void setCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) throws RemoteException;

    void promptStarterChoice(StarterCard starterCard) throws RemoteException;

    void promptColorChoice(List<PlayerColor> colorChoices) throws RemoteException;

    void setColor(PlayerColor color) throws RemoteException;

    void setHand(HandModel hand) throws RemoteException;

    void setCommonObjectives(ObjectiveCard[] commonObjectives) throws RemoteException;

    void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) throws RemoteException;

    void setPersonalObjective(ObjectiveCard personalObjective) throws RemoteException;

    void setStartOrder(int[] userIds) throws RemoteException;

    void setPlayerInfo(PlayerModel playerInfo) throws RemoteException;

    void setOtherPlayerInfo(int playerId, RestrictedPlayerModel otherPlayerInfo) throws RemoteException;

    void turn(int playerId) throws RemoteException;

    void setWinnerIds(List<Integer> winnerIds) throws RemoteException;

    void setChat(ChatModel chat) throws RemoteException;

    void promptError(String errorMessage) throws RemoteException;

    void redrawView() throws RemoteException;

    void notifyDontDraw() throws RemoteException;

}