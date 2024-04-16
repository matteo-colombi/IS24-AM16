package it.polimi.ingsw.am16.client;

import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.chat.ChatModel;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayAreaModel;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.hand.HandModel;
import it.polimi.ingsw.am16.common.util.Position;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TestRemoteViewImplementation implements RemoteViewInterface {

    String username;

    public TestRemoteViewImplementation(String username) {
        this.username = username;
    }

    @Override
    public void addPlayer(String username) throws RemoteException {
        System.out.print("[" + this.username + "]: ");
        System.out.println("Player " + username + " joined the game");
    }

    @Override
    public void setGameState(GameState state) throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.out.println(state);
    }

    @Override
    public void setCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.out.println("Common resource cards: " + Arrays.toString(commonResourceCards));
        System.out.println("Common gold cards: " + Arrays.toString(commonGoldCards));
    }

    @Override
    public void setDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.out.println(whichDeck + " deck top card is of type " + resourceType);
    }

    @Override
    public void promptStarterChoice(StarterCard starterCard) throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.out.println("Your starter card: " + starterCard);
    }

    @Override
    public void choosingColors() throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.out.println("Choosing colors!");
    }

    @Override
    public void promptColorChoice(List<PlayerColor> colorChoices) throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.out.println("Choose color between " + colorChoices);
    }

    @Override
    public void setColor(String username, PlayerColor color) throws RemoteException {
        System.out.print("[" + this.username + "]: ");
        System.out.println(username + " chose " + color);

    }

    @Override
    public void drawingCards() throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.out.println("Drawing cards!");
    }

    @Override
    public void setHand(List<PlayableCard> hand) throws RemoteException {
        System.out.print("[" + this.username + "]: ");
        System.out.println("Your hand: " + hand);
    }

    @Override
    public void addCardToHand(PlayableCard card) throws RemoteException {
        System.out.print("[" + this.username + "]: ");
        System.out.print("You drew " + card.getName());
    }

    @Override
    public void removeCardFromHand(PlayableCard card) throws RemoteException {
        System.out.print("[" + this.username + "]: ");
        System.out.println("You don't have " + card.getName() + " anymore");
    }

    @Override
    public void setOtherHand(String username, List<RestrictedCard> hand) throws RemoteException {
        System.out.print("[" + this.username + "]: ");
        System.out.println(username + "'s hand: " + hand);
    }

    @Override
    public void addCardToOtherHand(String username, RestrictedCard newCard) throws RemoteException {
        System.out.print("[" + this.username + "]: ");
        System.out.println(username + " acquired " + newCard);
    }

    @Override
    public void removeCardFromOtherHand(String username, RestrictedCard cardToRemove) throws RemoteException {
        System.out.print("[" + this.username + "]: ");
        System.out.println(username + " doesn't have " + cardToRemove + " anymore");
    }

    @Override
    public void setPlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides) throws RemoteException {
        System.out.print("[" + this.username + "]: ");
        System.out.println(username + "'s play area: " + cardPlacementOrder + ", " + field + ", " + activeSides);
    }

    @Override
    public void playCard(String username, BoardCard card, SideType side, Position pos) throws RemoteException {
        System.out.print("[" + this.username + "]: ");
        System.out.println(username + " played " + card.getName() + " on the " + side + " at " + pos);
    }

    @Override
    public void setGamePoints(String username, int gamePoints) throws RemoteException {
        System.out.print("[" + this.username + "]: ");
        System.out.println("Player " + username + "'s game points: " + gamePoints);
    }

    @Override
    public void setObjectivePoints(String username, int gamePoints) throws RemoteException {
        System.out.print("[" + this.username + "]: ");
        System.out.println("Player " + username + "'s objective points: " + gamePoints);
    }

    @Override
    public void setCommonObjectives(ObjectiveCard[] commonObjectives) throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.out.println("Common objectives: " + Arrays.toString(commonObjectives));
    }

    @Override
    public void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.out.println("Choose one objective between " + possiblePersonalObjectives);
    }

    @Override
    public void setPersonalObjective(ObjectiveCard personalObjective) throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.out.println("Your personal objective is: " + personalObjective);
    }

    @Override
    public void setStartOrder(List<String> userIds) throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.out.println("The order of players is: " + userIds);
    }

    @Override
    public void turn(String username) throws RemoteException {
        System.out.print("[" + this.username + "]: ");
        System.out.println("It's " + username + "'s turn");
    }

    @Override
    public void setWinners(List<String> winnerUsernames) throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.out.println("Winners: " + winnerUsernames);
    }

    @Override
    public void addMessages(List<ChatMessage> messages) throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.out.println("Received new messages: " + messages);
    }

    @Override
    public void addMessage(ChatMessage message) throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.out.println("Received new message: " + message);
    }

    @Override
    public void promptError(String errorMessage) throws RemoteException {
        System.err.print("[" + username + "]: ");
        System.err.println(errorMessage);
    }

    @Override
    public void redrawView() throws RemoteException {
        //Doesn't apply to this test
    }

    @Override
    public void notifyDontDraw() throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.out.println("You should no longer draw after you placed a card.");
    }

    @Override
    public void signalDisconnection(String whoDisconnected) throws RemoteException {
        System.out.print("[" + username + "]: ");
        System.err.println("Player " + whoDisconnected + " has disconnected!");
    }
}
