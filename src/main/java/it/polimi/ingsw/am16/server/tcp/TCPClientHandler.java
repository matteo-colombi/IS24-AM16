package it.polimi.ingsw.am16.server.tcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am16.client.RemoteClientInterface;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.model.game.DrawType;
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
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.tcpMessages.TCPMessage;
import it.polimi.ingsw.am16.server.controller.GameController;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * DOCME
 */
public class TCPClientHandler implements Runnable, RemoteClientInterface {

    private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

    private final Socket clientSocket;
    private final PrintWriter out;
    private final Scanner in;
    private final LobbyManager lobbyManager;
    private GameController gameController;
    private int playerId;
    private String username;
    private final AtomicBoolean running;

    private final AtomicBoolean ponged;

    private final Timer pingTimer;


    public TCPClientHandler(Socket clientSocket, LobbyManager lobbyManager) throws IOException {
        this.lobbyManager = lobbyManager;
        this.clientSocket = clientSocket;
        this.out = new PrintWriter(clientSocket.getOutputStream());
        this.in = new Scanner(clientSocket.getInputStream());
        this.gameController = null;
        this.username = null;
        this.playerId = -1;
        this.ponged = new AtomicBoolean(true);
        this.pingTimer = new Timer();
        this.running = new AtomicBoolean(true);
    }

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

                    System.out.println("Client disconnected.");

                    pingTimer.cancel();

                    if (gameController != null && playerId != -1) {
                        gameController.disconnect(playerId);
                        playerId = -1;
                        gameController = null;
                    }
                    running.set(false);
                    continue;
                }

                if (tcpMessage != null) {
//                    System.out.println("Received message of type: " + tcpMessage.messageType());

                    switch (tcpMessage.messageType()) {
                        case LEAVE_GAME -> {
                            if (gameController != null && playerId != -1) {
                                gameController.disconnect(playerId);
                            }
                            playerId = -1;
                            gameController = null;
                        }
                        case DISCONNECT -> {
                            if (gameController != null && playerId != -1) {
                                gameController.disconnect(playerId);
                            }
                            playerId = -1;
                            gameController = null;
                            running.set(false);
                        }
                        case CREATE_GAME -> {
                            if (gameController != null) {
                                promptError("You are already in a game.");
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
                                promptError(e.getMessage());
                                break;
                            }

                            gameController = lobbyManager.getGame(gameId);
                            try {
                                playerId = gameController.createPlayer(username);
                            } catch (UnexpectedActionException e) {
                                promptError("Couldn't join game: " + e.getMessage());
                                break;
                            }

                            try {
                                gameController.joinPlayer(playerId, this);
                                this.username = username;
                            } catch (UnexpectedActionException e) {
                                System.err.println("Unexpected error: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        case JOIN_GAME_REQUEST -> {
                            if (gameController != null) {
                                promptError("You are already in a game.");
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
                                promptError("No game with the given id.");
                                break;
                            }

                            if (gameController.isRejoiningAfterCrash()) {
                                try {
                                    playerId = gameController.getPlayerId(username);
                                } catch (IllegalArgumentException e) {
                                    promptError("Couldn't join game: " + e.getMessage());
                                    playerId = -1;
                                    gameController = null;
                                    this.username = null;
                                    break;
                                }
                            } else {
                                try {
                                    playerId = gameController.createPlayer(username);
                                } catch (UnexpectedActionException e) {
                                    promptError("Couldn't join game: " + e.getMessage());
                                    playerId = -1;
                                    gameController = null;
                                    this.username = null;
                                    break;
                                }
                            }

                            try {
                                gameController.joinPlayer(playerId, this);
                                this.username = username;
                            } catch (UnexpectedActionException e) {
                                promptError("User already rejoined the game.");
                                gameController = null;
                                playerId = -1;
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
                                gameController.setStarterCard(playerId, side);
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
                                gameController.setPlayerColor(playerId, color);
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
                                gameController.setPlayerObjective(playerId, objective);
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
                                gameController.placeCard(playerId, playedCard, side, pos);
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
                                gameController.drawCard(playerId, drawType);
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
                        gameController.disconnect(playerId);
                    }

                    playerId = -1;
                    gameController = null;
                    running.set(false);
                }
            }
        };

        pingTimer.schedule(task, 1000, 10000);
    }

    private void sendTCPMessage(TCPMessage tcpMessage) {
        try {
            out.println(mapper.writeValueAsString(tcpMessage));
            out.flush();
        } catch (IOException e) {
            //TODO see if this is appropriate
            System.err.println(e.getMessage());
        }
    }

    /**
     * Tells the view that they have joined a game with the given username.
     *
     * @param gameId
     * @param username The username the player has joined the game with.
     */
    @Override
    public void joinGame(String gameId, String username) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.JOIN_GAME_RESPONSE, new JoinGameResponse(gameId, username));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the game controller.
     *
     * @param username The new player's username.
     */
    @Override
    public void addPlayer(String username) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.ADD_PLAYER, new AddPlayer(username));
        sendTCPMessage(tcpMessage);
    }

    /**
     * DOCME
     *
     * @param usernames
     */
    @Override
    public void setPlayers(List<String> usernames) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_PLAYERS, new SetPlayers(usernames));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the game state.
     *
     * @param state The new game state.
     */
    @Override
    public void setGameState(GameState state) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_GAME_STATE, new SetGameState(state));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the common cards for the game.
     *
     * @param commonResourceCards The common resource cards (may also contain gold cards if the resource card deck is empty). Should always be of length 2.
     * @param commonGoldCards     The common gold cards (may also contain resource cards if the gold card deck is empty). Should always be of length 2.
     */
    @Override
    public void setCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_COMMON_CARDS, new SetCommonCards(commonResourceCards, commonGoldCards));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the top card of a deck.
     *
     * @param whichDeck    The deck which we are setting the top card of.
     * @param resourceType The resource type of the card on top of the given deck.
     */
    @Override
    public void setDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_DECK_TOP_TYPE, new SetDeckTopType(whichDeck, resourceType));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Prompts the client with a choice of starter cards.
     *
     * @param starterCard The starter card of the player.
     */
    @Override
    public void promptStarterChoice(StarterCard starterCard) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.PROMPT_STARTER_CHOICE, new PromptStarterChoice(starterCard));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Notifies the client that they should choose a color.
     */
    @Override
    public void choosingColors() {
        TCPMessage tcpMessage = new TCPMessage(MessageType.CHOOSING_COLORS, null);
        sendTCPMessage(tcpMessage);
    }

    /**
     * Prompts the client with a choice of colors.
     *
     * @param colorChoices The possible choices for the player's color.
     */
    @Override
    public void promptColorChoice(List<PlayerColor> colorChoices) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.PROMPT_COLOR_CHOICE, new PromptColorChoice(colorChoices));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the color of a player.
     *
     * @param username The username whose color is being given.
     * @param color    The color assigned to the player.
     */
    @Override
    public void setColor(String username, PlayerColor color) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_COLOR, new SetColor(username, color));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Notifies the client that they should draw cards.
     */
    @Override
    public void drawingCards() {
        TCPMessage tcpMessage = new TCPMessage(MessageType.DRAWING_CARDS, null);
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the player's hand.
     *
     * @param hand The player's hand.
     */
    @Override
    public void setHand(List<PlayableCard> hand) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_HAND, new SetHand(hand));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card The card to be added.
     */
    @Override
    public void addCardToHand(PlayableCard card) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.ADD_CARD_TO_HAND, new AddCardToHand(card));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Removes a card from the player's hand.
     *
     * @param card The card to be removed.
     */
    @Override
    public void removeCardFromHand(PlayableCard card) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.REMOVE_CARD_FROM_HAND, new RemoveCardFromHand(card));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the hand of another player.
     *
     * @param username The username of the player whose hand is being given.
     * @param hand     The restricted hand.
     */
    @Override
    public void setOtherHand(String username, List<RestrictedCard> hand) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_OTHER_HAND, new SetOtherHand(username, hand));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Adds a card to another player's hand.
     *
     * @param username The user to add this card to.
     * @param newCard  The restricted card to be added.
     */
    @Override
    public void addCardToOtherHand(String username, RestrictedCard newCard) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.ADD_CARD_TO_OTHER_HAND, new AddCardToOtherHand(username, newCard));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Removes a card from another player's hand.
     *
     * @param username     The user to remove this card from.
     * @param cardToRemove The restricted card to be removed.
     */
    @Override
    public void removeCardFromOtherHand(String username, RestrictedCard cardToRemove) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.REMOVE_CARD_FROM_OTHER_HAND, new RemoveCardFromOtherHand(username, cardToRemove));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the play area for a player.
     *
     * @param username           The player whose play area is being given.
     * @param cardPlacementOrder The order in which the cards were played in this play area.
     * @param field              The user's field.
     * @param activeSides        The map keeping track of which side every card is placed on.
     * @param legalPositions     DOCME
     * @param illegalPositions   DOCME
     * @param resourceCounts     DOCME
     * @param objectCounts       DOCME
     */
    @Override
    public void setPlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_PLAY_AREA, new SetPlayArea(username, cardPlacementOrder, field, activeSides, legalPositions, illegalPositions, resourceCounts, objectCounts));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Plays a card on the board.
     *
     * @param username              The username of the player who played the card.
     * @param card                  The played card.
     * @param side                  The card the new card was played on.
     * @param pos                   The position where the new card was played.
     * @param addedLegalPositions   DOCME
     * @param removedLegalPositions DOCME
     * @param resourceCounts        DOCME
     * @param objectCounts          DOCME
     */
    @Override
    public void playCard(String username, BoardCard card, SideType side, Position pos, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.PLAY_CARD_RESPONSE, new PlayCardResponse(username, card, side, pos, addedLegalPositions, removedLegalPositions, resourceCounts, objectCounts));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the game points for a player.
     *
     * @param whosePoints The username of the player whose points are being set.
     * @param gamePoints  The given player's number of game points.
     */
    @Override
    public void setGamePoints(String whosePoints, int gamePoints) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_GAME_POINTS, new SetGamePoints(whosePoints, gamePoints));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the objective points for a player.
     *
     * @param whosePoints     The username of the player whose points are being set.
     * @param objectivePoints The given player's number of objective points.
     */
    @Override
    public void setObjectivePoints(String whosePoints, int objectivePoints) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_OBJECTIVE_POINTS, new SetObjectivePoints(whosePoints, objectivePoints));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the common objectives for the game.
     *
     * @param commonObjectives The common objectives. Should always contain 2 elements.
     */
    @Override
    public void setCommonObjectives(ObjectiveCard[] commonObjectives) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_COMMON_OBJECTIVES, new SetCommonObjectives(commonObjectives));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Prompts the client with a choice of personal objectives.
     *
     * @param possiblePersonalObjectives The possible objectives the player can choose from. Should always contain 2 cards.
     */
    @Override
    public void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.PROMPT_OBJECTIVE_CHOICE, new PromptObjectiveChoice(possiblePersonalObjectives));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the player's personal objective.
     *
     * @param personalObjective The player's personal objective.
     */
    @Override
    public void setPersonalObjective(ObjectiveCard personalObjective) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_PERSONAL_OBJECTIVE, new SetPersonalObjective(personalObjective));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the turn order for the game.
     *
     * @param usernames The turn order. Should always contain as many usernames as were added at the beginning of the game.
     */
    @Override
    public void setStartOrder(List<String> usernames) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_START_ORDER, new SetStartOrder(usernames));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Notifies the client that it is their turn.
     *
     * @param username The player's username.
     */
    @Override
    public void turn(String username) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.TURN, new Turn(username));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the winners of the game.
     *
     * @param winnerUsernames The winners of the game.
     */
    @Override
    public void setWinners(List<String> winnerUsernames) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_WINNERS, new SetWinners(winnerUsernames));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Adds chat messages to the chat.
     *
     * @param messages The chat messages to add.
     */
    @Override
    public void addMessages(List<ChatMessage> messages) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.ADD_MESSAGES, new AddMessages(messages));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Adds a message to the chat.
     *
     * @param message The new message.
     */
    @Override
    public void addMessage(ChatMessage message) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.ADD_MESSAGE, new AddMessage(message));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Prompts the client with an error message.
     *
     * @param errorMessage The message that should be displayed to the user.
     */
    @Override
    public void promptError(String errorMessage) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.PROMPT_ERROR, new PromptError(errorMessage));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Notifies the client to redraw the view.
     */
    @Override
    public void redrawView() {
        TCPMessage tcpMessage = new TCPMessage(MessageType.REDRAW_VIEW, null);
        sendTCPMessage(tcpMessage);
    }

    /**
     * Notifies the client that they should not draw a card.
     */
    @Override
    public void notifyDontDraw() {
        TCPMessage tcpMessage = new TCPMessage(MessageType.NOTIFY_DONT_DRAW, null);
        sendTCPMessage(tcpMessage);
    }

    /**
     * Signals a disconnection to the client.
     *
     * @param whoDisconnected The username of the player who disconnected.
     */
    @Override
    public void signalDisconnection(String whoDisconnected) {
        gameController = null;
        playerId = -1;

        TCPMessage tcpMessage = new TCPMessage(MessageType.SIGNAL_DISCONNECTION, new SignalDisconnection(whoDisconnected));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Signals a deadlock to the client.
     *
     * @param username The username of the player who skipped their turn.
     */
    @Override
    public void signalDeadlock(String username) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SIGNAL_DEADLOCK, new SignalDeadlock(username));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Ping request used by the server to check that the client is still connected.
     */
    @Override
    public void ping() {
        TCPMessage tcpMessage = new TCPMessage(MessageType.PING, null);
        sendTCPMessage(tcpMessage);
    }
}
