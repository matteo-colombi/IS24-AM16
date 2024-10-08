package it.polimi.ingsw.am16.client.tcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am16.client.view.ViewInterface;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.tcpMessages.MessageType;
import it.polimi.ingsw.am16.common.tcpMessages.TCPMessage;
import it.polimi.ingsw.am16.common.tcpMessages.request.*;
import it.polimi.ingsw.am16.common.tcpMessages.response.*;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.server.ServerInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class is a Runnable that is meant to handle incoming and outgoing messages for a TCP client.
 * This class also handles the ping routine to check that the connection with the server is still active.
 */
public class TCPClient implements Runnable, ServerInterface {

    private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

    private final Socket socket;
    private final ViewInterface view;
    private final PrintWriter out;
    private final Scanner in;
    private final AtomicBoolean running;

    private final AtomicLong lastPinged;

    private final Timer checkConnectionTimer;

    /**
     * Creates a new TCP client. The client will try to connect to the server at the given address using the given port.
     * @param address The address of the server.
     * @param port The port of the server.
     * @param view This client's view.
     * @throws IOException Thrown if an error occurs when establishing the connection to the server.
     */
    public TCPClient(String address, int port, ViewInterface view) throws IOException {
        socket = new Socket(address, port);
        out = new PrintWriter(socket.getOutputStream());
        in = new Scanner(socket.getInputStream());
        this.lastPinged = new AtomicLong(System.currentTimeMillis());
        this.running = new AtomicBoolean(true);
        this.checkConnectionTimer = new Timer();
        this.view = view;
    }

    /**
     * This method is the main loop of the client.
     * It reads messages from the server and calls the appropriate methods on the view.
     */
    @Override
    public void run() {
        checkConnectionRoutine();
        try {
            while (running.get()) {
                String serializedMessage = null;
                TCPMessage message;
                try {
                    serializedMessage = in.nextLine();
                    message = mapper.readValue(serializedMessage, TCPMessage.class);
                } catch (IOException e) {

                    //Error while deserializing message
                    System.err.println("Server sent a malformed message: " + serializedMessage);
                    continue;
                } catch (NoSuchElementException | IllegalStateException ignored) {

                    //Connection lost with server

                    checkConnectionTimer.cancel();
                    view.signalConnectionLost();

                    running.set(false);
                    continue;
                }

                switch (message.messageType()) {
                    case GET_GAMES_RESPONSE -> {
                        GetGamesResponse payload;
                        try {
                            payload = (GetGamesResponse) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.printGames(payload.getGameIds(), payload.getCurrentPlayers(), payload.getMaxPlayers(), payload.getLobbyStates());
                    }
                    case JOIN_GAME_RESPONSE -> {
                        JoinGameResponse payload;
                        try {
                            payload = (JoinGameResponse) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.joinGame(payload.getGameId(), payload.getUsername(), payload.getNumPlayers());
                    }
                    case REJOIN_INFORMATION_START -> view.rejoinInformationStart();
                    case REJOIN_INFORMATION_END -> view.rejoinInformationEnd();
                    case SET_GAME_STATE -> {
                        SetGameState payload;
                        try {
                            payload = (SetGameState) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.setGameState(payload.getGameState());
                    }
                    case ADD_PLAYER -> {
                        AddPlayer payload;
                        try {
                            payload = (AddPlayer) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.addPlayer(payload.getUsername());
                    }
                    case SET_PLAYERS -> {
                        SetPlayers payload;
                        try {
                            payload = (SetPlayers) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.setPlayers(payload.getUsernames());
                    }
                    case SET_COMMON_CARDS -> {
                        SetCommonCards payload;
                        try {
                            payload = (SetCommonCards) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.setCommonCards(payload.getCommonResourceCards(), payload.getCommonGoldCards());
                    }
                    case SET_DECK_TOP_TYPE -> {
                        SetDeckTopType payload;
                        try {
                            payload = (SetDeckTopType) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.setDeckTopType(payload.getWhichDeck(), payload.getResourceType());
                    }
                    case PROMPT_STARTER_CHOICE -> {
                        PromptStarterChoice payload;
                        try {
                            payload = (PromptStarterChoice) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.promptStarterChoice(payload.getStarterCard());
                    }
                    case SET_PLAY_AREA -> {
                        SetPlayArea payload;
                        try {
                            payload = (SetPlayArea) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.setPlayArea(payload.getUsername(), payload.getCardPlacementOrder(), payload.getField(), payload.getActiveSides(), payload.getLegalPositions(), payload.getIllegalPositions(), payload.getResourceCounts(), payload.getObjectCounts());
                    }
                    case PROMPT_COLOR_CHOICE -> {
                        PromptColorChoice payload;
                        try {
                            payload = (PromptColorChoice) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.promptColorChoice(payload.getColorChoices());
                    }
                    case CHOOSING_COLORS -> view.choosingColors();
                    case SET_COLOR -> {
                        SetColor payload;
                        try {
                            payload = (SetColor) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.setColor(payload.getUsername(), payload.getColor());
                    }
                    case SET_COMMON_OBJECTIVES -> {
                        SetCommonObjectives payload;
                        try {
                            payload = (SetCommonObjectives) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.setCommonObjectives(payload.getCommonObjectives());
                    }
                    case PROMPT_OBJECTIVE_CHOICE -> {
                        PromptObjectiveChoice payload;
                        try {
                            payload = (PromptObjectiveChoice) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.promptObjectiveChoice(payload.getPossiblePersonalObjectives());
                    }
                    case SET_PERSONAL_OBJECTIVE -> {
                        SetPersonalObjective payload;
                        try {
                            payload = (SetPersonalObjective) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.setPersonalObjective(payload.getPersonalObjective());
                    }
                    case SET_START_ORDER -> {
                        SetStartOrder payload;
                        try {
                            payload = (SetStartOrder) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.setStartOrder(payload.getUsernames());
                    }
                    case SET_HAND -> {
                        SetHand payload;
                        try {
                            payload = (SetHand) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.setHand(payload.getHand());
                    }
                    case SET_OTHER_HAND -> {
                        SetOtherHand payload;
                        try {
                            payload = (SetOtherHand) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.setOtherHand(payload.getUsername(), payload.getHand());
                    }
                    case ADD_CARD_TO_HAND -> {
                        AddCardToHand payload;
                        try {
                            payload = (AddCardToHand) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.addCardToHand(payload.getCard());
                    }
                    case ADD_CARD_TO_OTHER_HAND -> {
                        AddCardToOtherHand payload;
                        try {
                            payload = (AddCardToOtherHand) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.addCardToOtherHand(payload.getUsername(), payload.getNewCard());
                    }
                    case REMOVE_CARD_FROM_HAND -> {
                        RemoveCardFromHand payload;
                        try {
                            payload = (RemoveCardFromHand) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.removeCardFromHand(payload.getCard());
                    }
                    case REMOVE_CARD_FROM_OTHER_HAND -> {
                        RemoveCardFromOtherHand payload;
                        try {
                            payload = (RemoveCardFromOtherHand) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.removeCardFromOtherHand(payload.getUsername(), payload.getCardToRemove());
                    }
                    case DRAWING_CARDS -> view.drawingCards();
                    case TURN -> {
                        Turn payload;
                        try {
                            payload = (Turn) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.turn(payload.getUsername());
                    }
                    case PLAY_CARD_RESPONSE -> {
                        PlayCardResponse payload;
                        try {
                            payload = (PlayCardResponse) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.playCard(payload.getUsername(), payload.getCard(), payload.getSide(), payload.getPos(), payload.getAddedLegalPositions(), payload.getRemovedLegalPositions(), payload.getResourceCounts(), payload.getObjectCounts());
                    }
                    case SET_GAME_POINTS -> {
                        SetGamePoints payload;
                        try {
                            payload = (SetGamePoints) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.setGamePoints(payload.getWhosePoints(), payload.getGamePoints());
                    }
                    case SET_OBJECTIVE_POINTS -> {
                        SetObjectivePoints payload;
                        try {
                            payload = (SetObjectivePoints) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.setObjectivePoints(payload.getWhosePoints(), payload.getObjectivePoints());
                    }
                    case NOTIFY_DONT_DRAW -> view.notifyDontDraw();
                    case SET_WINNERS -> {
                        SetWinners payload;
                        try {
                            payload = (SetWinners) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.setWinners(payload.getWinnerUsernames(), payload.getPersonalObjectives());
                    }
                    case ADD_MESSAGE -> {
                        AddMessage payload;
                        try {
                            payload = (AddMessage) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.addMessage(payload.getMessage());
                    }
                    case ADD_MESSAGES -> {
                        AddMessages payload;
                        try {
                            payload = (AddMessages) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.addMessages(payload.getMessages());
                    }
                    case SIGNAL_DEADLOCK -> {
                        SignalDeadlock payload;
                        try {
                            payload = (SignalDeadlock) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.signalDeadlock(payload.getUsername());
                    }
                    case SIGNAL_DISCONNECTION -> {
                        SignalDisconnection payload;
                        try {
                            payload = (SignalDisconnection) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.signalDisconnection(payload.getWhoDisconnected());

                    }
                    case SIGNAL_GAME_SUSPENSION -> {
                        SignalGameSuspension payload;
                        try {
                            payload = (SignalGameSuspension) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.signalGameSuspension(payload.getWhoDisconnected());
                    }
                    case SIGNAL_GAME_DELETION -> {
                        SignalGameDeletion payload;
                        try {
                            payload = (SignalGameDeletion) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.signalGameDeletion(payload.getWhoDisconnected());
                    }
                    case PROMPT_ERROR -> {
                        PromptError payload;
                        try {
                            payload = (PromptError) message.payload();
                        } catch (ClassCastException e) {
                            break;
                        }

                        view.promptError(payload.getErrorMessage(), payload.getErrorType());
                    }
                    case PING -> {
                        lastPinged.set(System.currentTimeMillis());
                        pong();
                    }
                    default -> System.err.println("Unknown message type: " + message.messageType());
                }
            }
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * This method starts a routine that checks if the server has pinged in the last 15 seconds. If it hasn't, the connection is considered lost and the client is stopped.
     */
    private void checkConnectionRoutine() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                long diff = System.currentTimeMillis() - lastPinged.get();
                if (diff > 15000) {
                    System.err.println("\nServer hasn't pinged in a while.");
                    System.out.println("Good bye!");
                    checkConnectionTimer.cancel();
                    in.close();
                    out.close();
                    try {
                        socket.close();
                    } catch (IOException ignored) {}
                    view.signalConnectionLost();
                }
            }
        };

        checkConnectionTimer.schedule(task, 1000, 10000);
    }

    /**
     * This method sends a message to the server.
     * @param tcpMessage the message to be sent.
     */
    private void sendTCPMessage(TCPMessage tcpMessage) {
        try {
            out.println(mapper.writeValueAsString(tcpMessage));
            out.flush();
        } catch (IOException e) {
            System.out.println("An error occurred while communicating with the server.");
            System.out.println("No action was performed.");
        }
    }

    @Override
    public void getGames() {
        TCPMessage message = new TCPMessage(MessageType.GET_GAMES_REQUEST, null);
        sendTCPMessage(message);
    }

    @Override
    public void createGame(String username, int numPlayers) {
        TCPMessage message = new TCPMessage(MessageType.CREATE_GAME, new CreateGame(username, numPlayers));
        sendTCPMessage(message);
    }

    @Override
    public void joinGame(String gameId, String username) {
        TCPMessage message = new TCPMessage(MessageType.JOIN_GAME_REQUEST, new JoinGameRequest(gameId, username));
        sendTCPMessage(message);
    }

    @Override
    public void setStarterCard(SideType side) {
        TCPMessage message = new TCPMessage(MessageType.CHOOSE_STARTER_SIDE, new ChooseStarterSide(side));
        sendTCPMessage(message);
    }

    @Override
    public void setColor(PlayerColor color) {
        TCPMessage message = new TCPMessage(MessageType.CHOOSE_COLOR, new ChooseColor(color));
        sendTCPMessage(message);
    }

    @Override
    public void setPersonalObjective(ObjectiveCard objectiveCard) {
        TCPMessage message = new TCPMessage(MessageType.CHOOSE_OBJECTIVE, new ChooseObjective(objectiveCard));
        sendTCPMessage(message);
    }

    @Override
    public void playCard(PlayableCard playedCard, SideType side, Position pos) {
        TCPMessage message = new TCPMessage(MessageType.PLAY_CARD_REQUEST, new PlayCardRequest(playedCard, side, pos));
        sendTCPMessage(message);
    }

    @Override
    public void drawCard(DrawType drawType) {
        TCPMessage message = new TCPMessage(MessageType.DRAW_CARD, new DrawCard(drawType));
        sendTCPMessage(message);
    }

    @Override
    public void sendChatMessage(String text) {
        TCPMessage message = new TCPMessage(MessageType.SEND_CHAT_MESSAGE, new SendChatMessage(text));
        sendTCPMessage(message);
    }

    @Override
    public void sendChatMessage(String text, String receiverUsername) {
        TCPMessage message = new TCPMessage(MessageType.SEND_PRIVATE_CHAT_MESSAGE, new SendPrivateChatMessage(text, receiverUsername));
        sendTCPMessage(message);
    }

    @Override
    public void disconnect() {
        TCPMessage message = new TCPMessage(MessageType.DISCONNECT, null);
        sendTCPMessage(message);
        running.set(false);
        checkConnectionTimer.cancel();
        System.exit(0);
    }

    @Override
    public void leaveGame() {
        leaveGame(false);
    }

    @Override
    public void leaveGame(boolean ignored) {
        TCPMessage message = new TCPMessage(MessageType.LEAVE_GAME, null);
        sendTCPMessage(message);
    }

    @Override
    public void pong() {
        TCPMessage message = new TCPMessage(MessageType.PONG, null);
        sendTCPMessage(message);
    }
}
