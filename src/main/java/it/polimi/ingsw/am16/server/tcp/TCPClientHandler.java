package it.polimi.ingsw.am16.server.tcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am16.client.RemoteViewInterface;
import it.polimi.ingsw.am16.common.tcpMessages.*;
import it.polimi.ingsw.am16.common.tcpMessages.response.*;
import it.polimi.ingsw.am16.common.tcpMessages.request.*;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.ChatMessage;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.tcpMessages.response.AddPlayer;
import it.polimi.ingsw.am16.common.tcpMessages.response.JoinGame;
import it.polimi.ingsw.am16.common.tcpMessages.response.SignalDisconnection;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.tcpMessages.TCPMessage;
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
                    TCPMessage tcpMessage = mapper.readValue(serializedMessage, TCPMessage.class);
                    if (tcpMessage != null) {
                        if (tcpMessage.getMessageType() == MessageType.SIGNAL_DISCONNECTION)
                            break;

                        switch (tcpMessage.getMessageType()) {
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

    private void sendTCPMessage(TCPMessage tcpMessage) {
        try {
            clientSocket.getOutputStream().write(mapper.writeValueAsString(tcpMessage).getBytes());
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
        TCPMessage tcpMessage = new TCPMessage(MessageType.JOIN_GAME, new JoinGame(gameId, username));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the game controller.
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
        TCPMessage tcpMessage = new TCPMessage(MessageType.ADD_PLAYER, new SetPlayers(usernames));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the game state.
     * @param state The new game state.
     */
    @Override
    public void setGameState(GameState state) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_GAME_STATE, new SetGameState(state));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the common cards for the game.
     * @param commonResourceCards The common resource cards (may also contain gold cards if the resource card deck is empty). Should always be of length 2.
     * @param commonGoldCards The common gold cards (may also contain resource cards if the gold card deck is empty). Should always be of length 2.
     */
    @Override
    public void setCommonCards(PlayableCard[] commonResourceCards, PlayableCard[] commonGoldCards) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_COMMON_CARDS, new SetCommonCards(commonResourceCards, commonGoldCards));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the top card of a deck.
     * @param whichDeck The deck which we are setting the top card of.
     * @param resourceType The resource type of the card on top of the given deck.
     */
    @Override
    public void setDeckTopType(PlayableCardType whichDeck, ResourceType resourceType) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_DECK_TOP_TYPE, new SetDeckTopType(whichDeck, resourceType));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Prompts the client with a choice of starter cards.
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
     * @param colorChoices The possible choices for the player's color.
     */
    @Override
    public void promptColorChoice(List<PlayerColor> colorChoices) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.PROMPT_COLOR_CHOICE, new PromptColorChoice(colorChoices));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the color of a player.
     * @param username The username whose color is being given.
     * @param color The color assigned to the player.
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
     * @param hand The player's hand.
     */
    @Override
    public void setHand(List<PlayableCard> hand) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_HAND, new SetHand(hand));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Adds a card to the player's hand.
     * @param card The card to be added.
     */
    @Override
    public void addCardToHand(PlayableCard card) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.ADD_CARD_TO_HAND, new AddCardToHand(card));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Removes a card from the player's hand.
     * @param card The card to be removed.
     */
    @Override
    public void removeCardFromHand(PlayableCard card) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.REMOVE_CARD_FROM_HAND, new RemoveCardFromHand(card));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the hand of another player.
     * @param username The username of the player whose hand is being given.
     * @param hand The restricted hand.
     */
    @Override
    public void setOtherHand(String username, List<RestrictedCard> hand) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_OTHER_HAND, new SetOtherHand(username, hand));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Adds a card to another player's hand.
     * @param username The user to add this card to.
     * @param newCard The restricted card to be added.
     */
    @Override
    public void addCardToOtherHand(String username, RestrictedCard newCard) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.ADD_CARD_TO_OTHER_HAND, new AddCardToOtherHand(username, newCard));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Removes a card from another player's hand.
     * @param username The user to remove this card from.
     * @param cardToRemove The restricted card to be removed.
     */
    @Override
    public void removeCardFromOtherHand(String username, RestrictedCard cardToRemove) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.REMOVE_CARD_FROM_OTHER_HAND, new RemoveCardFromOtherHand(username, cardToRemove));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the play area for a player.
     * @param username The player whose play area is being given.
     * @param cardPlacementOrder The order in which the cards were played in this play area.
     * @param field The user's field.
     * @param activeSides The map keeping track of which side every card is placed on.
     */
    @Override
    public void setPlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_PLAY_AREA, new SetPlayArea(username, cardPlacementOrder, field, activeSides));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Plays a card on the board.
     * @param username The username of the player who played the card.
     * @param card The played card.
     * @param side The card the new card was played on.
     * @param pos The position where the new card was played.
     */
    @Override
    public void playCard(String username, BoardCard card, SideType side, Position pos) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.PLAY_CARD, new PlayCard(username, card, side, pos));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the game points for a player.
     * @param whosePoints The username of the player whose points are being set.
     * @param gamePoints The given player's number of game points.
     */
    @Override
    public void setGamePoints(String whosePoints, int gamePoints) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_GAME_POINTS, new SetGamePoints(whosePoints, gamePoints));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the objective points for a player.
     * @param whosePoints The username of the player whose points are being set.
     * @param objectivePoints The given player's number of objective points.
     */
    @Override
    public void setObjectivePoints(String whosePoints, int objectivePoints) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_OBJECTIVE_POINTS, new SetObjectivePoints(whosePoints, objectivePoints));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the common objectives for the game.
     * @param commonObjectives The common objectives. Should always contain 2 elements.
     */
    @Override
    public void setCommonObjectives(ObjectiveCard[] commonObjectives) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_COMMON_OBJECTIVES, new SetCommonObjectives(commonObjectives));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Prompts the client with a choice of personal objectives.
     * @param possiblePersonalObjectives The possible objectives the player can choose from. Should always contain 2 cards.
     */
    @Override
    public void promptObjectiveChoice(List<ObjectiveCard> possiblePersonalObjectives) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.PROMPT_OBJECTIVE_CHOICE, new PromptObjectiveChoice(possiblePersonalObjectives));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the player's personal objective.
     * @param personalObjective The player's personal objective.
     */
    @Override
    public void setPersonalObjective(ObjectiveCard personalObjective) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_PERSONAL_OBJECTIVE, new SetPersonalObjective(personalObjective));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the turn order for the game.
     * @param usernames The turn order. Should always contain as many usernames as were added at the beginning of the game.
     */
    @Override
    public void setStartOrder(List<String> usernames) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_START_ORDER, new SetStartOrder(usernames));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Notifies the client that it is their turn.
     * @param username The player's username.
     */
    @Override
    public void turn(String username) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.TURN, new Turn(username));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Sets the winners of the game.
     * @param winnerUsernames The winners of the game.
     */
    @Override
    public void setWinners(List<String> winnerUsernames) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SET_WINNERS, new SetWinners(winnerUsernames));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Adds chat messages to the chat.
     * @param messages The chat messages to add.
     */
    @Override
    public void addMessages(List<ChatMessage> messages) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.ADD_MESSAGES, new AddMessages(messages));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Adds a message to the chat.
     * @param message The new message.
     */
    @Override
    public void addMessage(ChatMessage message) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.ADD_MESSAGE, new AddMessage(message));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Prompts the client with an error message.
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
     * @param whoDisconnected The username of the player who disconnected.
     */
    @Override
    public void signalDisconnection(String whoDisconnected) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SIGNAL_DISCONNECTION, new SignalDisconnection(whoDisconnected));
        sendTCPMessage(tcpMessage);
    }

    /**
     * Signals a deadlock to the client.
     * @param username The username of the player who skipped their turn.
     */
    @Override
    public void signalDeadlock(String username) {
        TCPMessage tcpMessage = new TCPMessage(MessageType.SIGNAL_DEADLOCK, new SignalDeadlock(username));
        sendTCPMessage(tcpMessage);
    }

    /**
     * You saw nothing ;)
     */
    @Override
    public void rick() {
        TCPMessage tcpMessage = new TCPMessage(MessageType.RICK, null);
        sendTCPMessage(tcpMessage);
    }
}
