package it.polimi.ingsw.am16.server;

import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.Position;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    void createGame(String username, int numPlayers) throws RemoteException;

    void joinGame(String gameId, String username) throws RemoteException;

    void setStarterCard(SideType side) throws RemoteException;

    void setColor(PlayerColor color) throws RemoteException;

    void setPersonalObjective(ObjectiveCard objectiveCard) throws RemoteException;

    void playCard(PlayableCard playedCard, SideType side, Position pos) throws RemoteException;

    void drawCard(DrawType drawType) throws RemoteException;

    void sendChatMessage(String text) throws RemoteException;

    void sendChatMessage(String text, String receiverUsername) throws RemoteException;

    void disconnect() throws RemoteException;

    void leaveGame() throws RemoteException;

    void pong() throws RemoteException;
}
