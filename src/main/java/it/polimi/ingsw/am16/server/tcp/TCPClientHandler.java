package it.polimi.ingsw.am16.server.tcp;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am16.client.view.RemoteViewInterface;
import it.polimi.ingsw.am16.common.messages.MessageType;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.messages.TCPMessage;
import it.polimi.ingsw.am16.server.controller.GameController;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TCPClientHandler implements Runnable, RemoteViewInterface {

    private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

    private final Socket clientSocket;
    private GameController gameController;
    private int playerId;

    public TCPClientHandler(Socket clientSocket, LobbyManager lobbyManager) {
        this.clientSocket = clientSocket;
        this.gameController = null;
        this.playerId = -1;
    }

    @Override
    public void run() {
        try {
            final Scanner in = new Scanner(clientSocket.getInputStream());
            final PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            while (true) {
                String serializedMessage;
                try {
                    serializedMessage = in.nextLine();
                    TCPMessage message = mapper.readValue(serializedMessage, TCPMessage.class);
                    if (message != null) {
                        if (message.getMessageType() == MessageType.DISCONNECT)
                            break;

                        switch (message.getMessageType()) {
                            case QUIT -> {
                                gameController.disconnect(playerId);
                                playerId = -1;
                                gameController = null;
                            }

                            case CREATE_GAME -> {
                                //TODO implement
                            }

                            case JOIN_GAME -> {
                                //TODO implement
                            }
                        }

                    }

                } catch (IOException e) {
                    //TODO see if this is appropriate
                    System.out.println(e.getMessage());
                }
            }
            in.close();
            out.close();
            clientSocket.close();
        } catch (final IOException e) {
            //TODO see if this is appropriate
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void addPlayer(String username) {

    }

    @Override
    public void setGameState(GameState state) {

    }

    @Override
    public void setCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) {

    }

    @Override
    public void setDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) {

    }

    @Override
    public void promptStarterChoice(StarterCard starterCard) {

    }

    @Override
    public void choosingColors() {

    }

    @Override
    public void promptColorChoice(List<PlayerColor> colorChoices) {

    }

    @Override
    public void setColor(String username, PlayerColor color) {

    }

    @Override
    public void drawingCards() {

    }

    @Override
    public void setHand(List<PlayableCard> hand) {

    }

    @Override
    public void addCardToHand(PlayableCard card) {

    }

    @Override
    public void removeCardFromHand(PlayableCard card) {

    }

    @Override
    public void setOtherHand(String username, List<RestrictedCard> hand) {

    }

    @Override
    public void addCardToOtherHand(String username, RestrictedCard newCard) {

    }

    @Override
    public void removeCardFromOtherHand(String username, RestrictedCard cardToRemove) {

    }

    @Override
    public void setPlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides) {

    }

    @Override
    public void playCard(String username, BoardCard card, SideType side, Position pos) {

    }

    @Override
    public void setGamePoints(String whosePoints, int gamePoints) {

    }

    @Override
    public void setObjectivePoints(String whosePoints, int objectivePoints) {

    }

    @Override
    public void setCommonObjectives(ObjectiveCard[] commonObjectives) {

    }

    @Override
    public void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) {

    }

    @Override
    public void setPersonalObjective(ObjectiveCard personalObjective) {

    }

    @Override
    public void setStartOrder(List<String> usernames) {

    }

    @Override
    public void turn(String username) {

    }

    @Override
    public void setWinners(List<String> winnerUsernames) {

    }

    @Override
    public void addMessages(List<ChatMessage> messages) {

    }

    @Override
    public void addMessage(ChatMessage message) {

    }

    @Override
    public void promptError(String errorMessage) {

    }

    @Override
    public void redrawView() {

    }

    @Override
    public void notifyDontDraw() {

    }

    @Override
    public void signalDisconnection(String whoDisconnected) {

    }

    @Override
    public void signalDeadlock(String username) {

    }
}
