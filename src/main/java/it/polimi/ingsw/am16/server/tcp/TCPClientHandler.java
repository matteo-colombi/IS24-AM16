package it.polimi.ingsw.am16.server.tcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.model.game.DrawType;
import it.polimi.ingsw.am16.common.model.game.LobbyState;
import it.polimi.ingsw.am16.common.tcpMessages.*;
import it.polimi.ingsw.am16.common.tcpMessages.request.*;
import it.polimi.ingsw.am16.common.tcpMessages.response.*;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.tcpMessages.response.AddPlayer;
import it.polimi.ingsw.am16.common.tcpMessages.response.JoinGameResponse;
import it.polimi.ingsw.am16.common.tcpMessages.response.SignalDisconnection;
import it.polimi.ingsw.am16.common.util.ErrorType;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.tcpMessages.TCPMessage;
import it.polimi.ingsw.am16.server.controller.GameController;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class that is used by the server to handle a single client connected via TCP/Socket.
 * This class handles both incoming and outgoing TCP messages to and from the client which it is handling.
 */
public class TCPClientHandler implements Runnable, RemoteClientInterface {

    private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

    private final Socket clientSocket;
    private final PrintWriter out;
    private final Scanner in;
    private final LobbyManager lobbyManager;
    private GameController gameController;
    private String username;
    private final AtomicBoolean running;

    private final AtomicBoolean ponged;

    private final Timer pingTimer;

    /**
     * Constructs a new TCP client handler for a specific client.
     * @param clientSocket The socket through which the client and server will exchange messages.
     * @param lobbyManager The lobby manager that contains information about active games.
     * @throws IOException Thrown if an error occurs while creating input and output streams for the socket.
     */
    public TCPClientHandler(Socket clientSocket, LobbyManager lobbyManager) throws IOException {
        this.lobbyManager = lobbyManager;
        this.clientSocket = clientSocket;
        this.out = new PrintWriter(clientSocket.getOutputStream());
        this.in = new Scanner(clientSocket.getInputStream());
        this.gameController = null;
        this.username = null;
        this.ponged = new AtomicBoolean(true);
        this.pingTimer = new Timer();
        this.running = new AtomicBoolean(true);
    }

    /**
     * Starts the client handler, and sets it up to receive and send messages to the client.
     * Also starts a ping routine to check that the connection between client and server is always active.
     */
    @Override
    public void run() {
        pingRoutine();
        try {
            while (running.get()) {
                String serializedMessage = null;
                TCPMessage tcpMessage;
                try {
                    serializedMessage = in.nextLine();
                    tcpMessage = mapper.readValue(serializedMessage, TCPMessage.class);
                } catch (IOException ignored) {

                    //Error while deserializing message

                    System.out.println("Client sent a malformed message: " + serializedMessage);
                    continue;
                } catch (NoSuchElementException | IllegalStateException ignored) {

                    //Connection lost with client.

                    System.out.println("TCP client disconnected.");

                    pingTimer.cancel();

                    if (gameController != null && username != null) {
                        gameController.disconnect(username);
                        username = null;
                        gameController = null;
                    }
                    running.set(false);
                    continue;
                }

                if (tcpMessage != null) {

                    switch (tcpMessage.messageType()) {
                        case LEAVE_GAME -> {
                            if (gameController != null && username != null) {
                                gameController.disconnect(username);
                            }
                            username = null;
                            gameController = null;
                        }
                        case DISCONNECT -> {
                            if (gameController != null && username != null) {
                                gameController.disconnect(username);
                            }
                            username = null;
                            gameController = null;
                            running.set(false);
                        }
                        case GET_GAMES_REQUEST -> {
                            if (gameController != null) {
                                promptError("You are already in a game.", ErrorType.JOIN_ERROR);
                                break;
                            }

                            Set<String> gameIds = lobbyManager.getGameIds();
                            Map<String, Integer> currentPlayers = new HashMap<>();
                            Map<String, Integer> maxPlayers = new HashMap<>();
                            Map<String, LobbyState> lobbyStates = new HashMap<>();

                            for (String gameId : gameIds) {
                                GameController game = lobbyManager.getGame(gameId);

                                currentPlayers.put(gameId, game.getCurrentPlayerCount());
                                maxPlayers.put(gameId, game.getNumPlayers());
                                lobbyStates.put(gameId, game.getLobbyState());
                            }

                            notifyGames(gameIds, currentPlayers, maxPlayers, lobbyStates);
                        }
                        case CREATE_GAME -> {
                            if (gameController != null) {
                                promptError("You are already in a game.", ErrorType.JOIN_ERROR);
                                break;
                            }

                            CreateGame payload;
                            try {
                                payload = (CreateGame) tcpMessage.payload();
                            } catch (ClassCastException e) {
                                break;
                            }

                            String username = payload.getUsername();
                            int numPlayers = payload.getNumPlayers();

                            String gameId;

                            try {
                                gameId = lobbyManager.createGame(numPlayers);
                            } catch (IllegalArgumentException e) {
                                promptError(e.getMessage(), ErrorType.JOIN_ERROR);
                                break;
                            }

                            gameController = lobbyManager.getGame(gameId);
                            try {
                                gameController.createPlayer(username);
                            } catch (UnexpectedActionException e) {
                                promptError("Couldn't join game: " + e.getMessage(), ErrorType.JOIN_ERROR);
                                break;
                            }

                            try {
                                gameController.joinPlayer(username, this);
                                this.username = username;
                            } catch (UnexpectedActionException e) {
                                gameController = null;
                                System.err.println("Unexpected error: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        case JOIN_GAME_REQUEST -> {
                            if (gameController != null) {
                                promptError("You are already in a game.", ErrorType.JOIN_ERROR);
                                break;
                            }

                            JoinGameRequest payload;
                            try {
                                payload = (JoinGameRequest) tcpMessage.payload();
                            } catch (ClassCastException e) {
                                break;
                            }

                            String gameId = payload.getGameId();
                            String username = payload.getUsername();

                            gameController = lobbyManager.getGame(gameId);
                            if (gameController == null) {
                                promptError("No game with the given id.", ErrorType.JOIN_ERROR);
                                break;
                            }

                            if (!gameController.isRejoiningAfterCrash()) {
                                try {
                                    gameController.createPlayer(username);
                                } catch (UnexpectedActionException e) {
                                    promptError("Couldn't join game: " + e.getMessage(), ErrorType.JOIN_ERROR);
                                    gameController = null;
                                    this.username = null;
                                    break;
                                }
                            }

                            try {
                                gameController.joinPlayer(username, this);
                                this.username = username;
                            } catch (UnexpectedActionException e) {
                                promptError(e.getMessage(), ErrorType.JOIN_ERROR);
                                gameController = null;
                                this.username = null;
                            }
                        }
                        case CHOOSE_STARTER_SIDE -> {
                            ChooseStarterSide payload;
                            try {
                                payload = (ChooseStarterSide) tcpMessage.payload();
                            } catch (ClassCastException e) {
                                break;
                            }

                            SideType side = payload.getSide();

                            if (gameController != null) {
                                gameController.setStarterCard(username, side);
                            }
                        }
                        case CHOOSE_COLOR -> {
                            ChooseColor payload;
                            try {
                                payload = (ChooseColor) tcpMessage.payload();
                            } catch (ClassCastException e) {
                                break;
                            }

                            PlayerColor color = payload.getColor();

                            if (gameController != null) {
                                gameController.setPlayerColor(username, color);
                            }
                        }
                        case CHOOSE_OBJECTIVE -> {
                            ChooseObjective payload;
                            try {
                                payload = (ChooseObjective) tcpMessage.payload();
                            } catch (ClassCastException e) {
                                break;
                            }

                            ObjectiveCard objective = payload.getObjective();

                            if (gameController != null) {
                                gameController.setPlayerObjective(username, objective);
                            }
                        }
                        case PLAY_CARD_REQUEST -> {
                            PlayCardRequest payload;
                            try {
                                payload = (PlayCardRequest) tcpMessage.payload();
                            } catch (ClassCastException e) {
                                break;
                            }

                            PlayableCard playedCard = payload.getPlayedCard();
                            SideType side = payload.getSide();
                            Position pos = payload.getPos();

                            if (gameController != null) {
                                gameController.placeCard(username, playedCard, side, pos);
                            }
                        }
                        case DRAW_CARD -> {
                            DrawCard payload;
                            try {
                                payload = (DrawCard) tcpMessage.payload();
                            } catch (ClassCastException e) {
                                break;
                            }

                            DrawType drawType = payload.getDrawType();

                            if (gameController != null) {
                                gameController.drawCard(username, drawType);
                            }
                        }
                        case SEND_CHAT_MESSAGE -> {
                            SendChatMessage payload;
                            try {
                                payload = (SendChatMessage) tcpMessage.payload();
                            } catch (ClassCastException e) {
                                break;
                            }

                            String text = payload.getText();

                            if (gameController != null) {
                                gameController.sendChatMessage(username, text);
                            }
                        }
                        case SEND_PRIVATE_CHAT_MESSAGE -> {
                            SendPrivateChatMessage payload;
                            try {
                                payload = (SendPrivateChatMessage) tcpMessage.payload();
                            } catch (ClassCastException e) {
                                break;
                            }

                            String text = payload.getText();
                            String receiverUsername = payload.getReceiver();

                            if (gameController != null) {
                                gameController.sendChatMessage(username, text, Set.of(receiverUsername));
                            }
                        }
                        case PONG -> {
                            ponged.set(true);
                        }
                        default -> {
                            System.out.println("Client " + username + " sent a malformed message.");
                        }
                    }
                }
            }
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts a routine that sends the client a ping message every 10 seconds.
     * If the client does not respond to a ping message in the time between one ping and the other,
     * the client is considered disconnected and the connection is closed.
     */
    private void pingRoutine() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (ponged.get()) {
                    ponged.set(false);
                    ping();
                } else {
                    System.out.println("Client didn't answer the last ping.");
                    pingTimer.cancel();

                    if (gameController != null) {
                        gameController.disconnect(username);
                    }

                    username = null;
                    gameController = null;
                    running.set(false);
                }
            }
        };

        pingTimer.schedule(task, 1000, 10000);
    }

    /**
     * Sends the given message to the client through the open socket in this handler.
     *
     * @param tcpMessage The message to be sent.
     */
    private void sendTCPMessage(TCPMessage tcpMessage) {
        try {
            out.println(mapper.writeValueAsString(tcpMessage));
            out.flush();
        } catch (IOException e) {
            System.err.println("Error serializing object to send to a TCP client. Printing the error message:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void notifyGames(Set<String> gameIds, Map<String, Integer> currentPlayers, Map<String, Integer> maxPlayers, Map<String, LobbyState> lobbyStates) throws RemoteException {
        TCPMessage tcpMessage = new TCPMessage(MessageType.GET_GAMES_RESPONSE, new GetGamesResponse(gameIds, currentPlayers, maxPlayers, lobbyStates));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void joinGame(String gameId, String username, int numPlayers) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.JOIN_GAME_RESPONSE, new JoinGameResponse(gameId, username, numPlayers));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void rejoinInformationStart() throws RemoteException {
        TCPMessage tcpMessage = new TCPMessage(MessageType.REJOIN_INFORMATION_START, null);
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void rejoinInformationEnd() throws RemoteException {
        TCPMessage tcpMessage = new TCPMessage(MessageType.REJOIN_INFORMATION_END, null);
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void addPlayer(String username) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.ADD_PLAYER, new AddPlayer(username));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void setPlayers(List<String> usernames) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_PLAYERS, new SetPlayers(usernames));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void setGameState(GameState state) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_GAME_STATE, new SetGameState(state));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void setCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_COMMON_CARDS, new SetCommonCards(commonResourceCards, commonGoldCards));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void setDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_DECK_TOP_TYPE, new SetDeckTopType(whichDeck, resourceType));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void promptStarterChoice(StarterCard starterCard) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.PROMPT_STARTER_CHOICE, new PromptStarterChoice(starterCard));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void choosingColors() {
        TCPMessage tcpMessage = new TCPMessage(MessageType.CHOOSING_COLORS, null);
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void promptColorChoice(List<PlayerColor> colorChoices) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.PROMPT_COLOR_CHOICE, new PromptColorChoice(colorChoices));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void setColor(String username, PlayerColor color) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_COLOR, new SetColor(username, color));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void drawingCards() {
        TCPMessage tcpMessage = new TCPMessage(MessageType.DRAWING_CARDS, null);
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void setHand(List<PlayableCard> hand) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_HAND, new SetHand(hand));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void addCardToHand(PlayableCard card) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.ADD_CARD_TO_HAND, new AddCardToHand(card));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void removeCardFromHand(PlayableCard card) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.REMOVE_CARD_FROM_HAND, new RemoveCardFromHand(card));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void setOtherHand(String username, List<RestrictedCard> hand) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_OTHER_HAND, new SetOtherHand(username, hand));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void addCardToOtherHand(String username, RestrictedCard newCard) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.ADD_CARD_TO_OTHER_HAND, new AddCardToOtherHand(username, newCard));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void removeCardFromOtherHand(String username, RestrictedCard cardToRemove) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.REMOVE_CARD_FROM_OTHER_HAND, new RemoveCardFromOtherHand(username, cardToRemove));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void setPlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_PLAY_AREA, new SetPlayArea(username, cardPlacementOrder, field, activeSides, legalPositions, illegalPositions, resourceCounts, objectCounts));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void playCard(String username, BoardCard card, SideType side, Position pos, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.PLAY_CARD_RESPONSE, new PlayCardResponse(username, card, side, pos, addedLegalPositions, removedLegalPositions, resourceCounts, objectCounts));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void setGamePoints(String whosePoints, int gamePoints) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_GAME_POINTS, new SetGamePoints(whosePoints, gamePoints));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void setObjectivePoints(String whosePoints, int objectivePoints) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_OBJECTIVE_POINTS, new SetObjectivePoints(whosePoints, objectivePoints));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void setCommonObjectives(ObjectiveCard[] commonObjectives) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_COMMON_OBJECTIVES, new SetCommonObjectives(commonObjectives));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.PROMPT_OBJECTIVE_CHOICE, new PromptObjectiveChoice(possiblePersonalObjectives));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void setPersonalObjective(ObjectiveCard personalObjective) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_PERSONAL_OBJECTIVE, new SetPersonalObjective(personalObjective));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void setStartOrder(List<String> usernames) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_START_ORDER, new SetStartOrder(usernames));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void turn(String username) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.TURN, new Turn(username));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void setWinners(List<String> winnerUsernames, Map<String, ObjectiveCard> personalObjectives) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_WINNERS, new SetWinners(winnerUsernames, personalObjectives));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void addMessages(List<ChatMessage> messages) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.ADD_MESSAGES, new AddMessages(messages));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void addMessage(ChatMessage message) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.ADD_MESSAGE, new AddMessage(message));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Notifies the client that an error has occured.
     * @param errorMessage The message that should be displayed to the user.
     * @param errorType The type of error that occured.
     */
    @Override
    public void promptError(String errorMessage, ErrorType errorType) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.PROMPT_ERROR, new PromptError(errorMessage, errorType));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void notifyDontDraw() {
        TCPMessage tcpMessage = new TCPMessage(MessageType.NOTIFY_DONT_DRAW, null);
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void signalDisconnection(String whoDisconnected) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SIGNAL_DISCONNECTION, new SignalDisconnection(whoDisconnected));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void signalGameSuspension(String whoDisconnected) throws RemoteException {
        gameController = null;
        username = null;

        TCPMessage tcpMessage = new TCPMessage(MessageType.SIGNAL_GAME_SUSPENSION, new SignalGameSuspension(whoDisconnected));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void signalGameDeletion(String whoDisconnected) throws RemoteException {
        //TODO implement
    }

    @Override
    public void signalDeadlock(String username) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SIGNAL_DEADLOCK, new SignalDeadlock(username));
        sendTCPMessage(tcpMessage);
    }

    @Override
    public void ping() {
        TCPMessage tcpMessage = new TCPMessage(MessageType.PING, null);
        sendTCPMessage(tcpMessage);
    }
}
