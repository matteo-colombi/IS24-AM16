package it.polimi.ingsw.am16.common.model.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.NoStarterCardException;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.cards.decks.*;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.util.RNG;

import java.io.IOException;
import java.io.Serial;
import java.util.*;

/**
 * Class to handle game(s). A game has a unique alphanumeric id and a non-variable number of players.
 */
@JsonDeserialize(using = Game.Deserializer.class)
public class Game implements GameModel {
    private final String id;
    private final int numPlayers;
    private String activePlayer;
    private String startingPlayer;
    private final List<String> winnerUsernames;
    private final ObjectiveCardsDeck objectiveCardsDeck;
    private final StarterCardsDeck starterCardsDeck;
    private final GoldCardsDeck goldCardsDeck;
    private final ResourceCardsDeck resourceCardsDeck;
    private final ObjectiveCard[] commonObjectiveCards;
    private final PlayableCard[] commonGoldCards;
    private final PlayableCard[] commonResourceCards;
    private GameState state;
    private boolean rejoiningAfterCrash;
    private final Map<String, Player> players;
    private final List<String> turnOrder;
    private final List<PlayerColor> availableColors;

    /**
     * Creates a game, initializing its ID as well as its number of players to a chosen value, its
     * other attributes are set to standard values.
     *
     * @param id         The game's ID.
     * @param numPlayers The number of players expected inside the game.
     */
    public Game(String id, int numPlayers) {
        this.id = id;
        this.numPlayers = numPlayers;
        this.startingPlayer = null;
        this.activePlayer = null;
        this.winnerUsernames = new ArrayList<>();
        this.objectiveCardsDeck = DeckFactory.getObjectiveCardsDeck();
        this.starterCardsDeck = DeckFactory.getStarterCardsDeck();
        this.goldCardsDeck = DeckFactory.getGoldCardsDeck();
        this.resourceCardsDeck = DeckFactory.getResourceCardsDeck();
        this.commonObjectiveCards = new ObjectiveCard[2];
        this.commonGoldCards = new PlayableCard[2];
        this.commonResourceCards = new PlayableCard[2];
        this.state = GameState.JOINING;
        this.rejoiningAfterCrash = false;
        this.players = new HashMap<>();
        this.availableColors = new ArrayList<>(List.of(PlayerColor.values()));
        this.turnOrder = new ArrayList<>();
    }

    /**
     * Constructs a new game with all the given attributes. Used for deserializing from JSON.
     *
     * @param id                   The game's id.
     * @param numPlayers           The number of players expected in this game.
     * @param activePlayer         The currently active player in this game.
     * @param startingPlayer       The starting player for this game.
     * @param winnerUsernames      The list of players who have won this game.
     * @param objectiveCardsDeck   The deck of objective cards used for this game.
     * @param starterCardsDeck     The deck of starter cards used for this game.
     * @param goldCardsDeck        The deck of gold cards used for this game.
     * @param resourceCardsDeck    The deck of resource cards used for this game.
     * @param commonObjectiveCards The common objectives for this game.
     * @param commonGoldCards      The common gold cards from which the players can choose to draw from.
     * @param commonResourceCards  The common resource cards from which the players can choose to draw from.
     * @param state                The game's state.
     * @param players              The players for this game.
     * @param availableColors      The currently available colors from which players can choose.
     * @param turnOrder            DOCME
     */
    private Game(
            String id,
            int numPlayers,
            String activePlayer,
            String startingPlayer,
            List<String> winnerUsernames,
            ObjectiveCardsDeck objectiveCardsDeck,
            StarterCardsDeck starterCardsDeck,
            GoldCardsDeck goldCardsDeck,
            ResourceCardsDeck resourceCardsDeck,
            ObjectiveCard[] commonObjectiveCards,
            PlayableCard[] commonGoldCards,
            PlayableCard[] commonResourceCards,
            GameState state,
            Map<String, Player> players,
            List<PlayerColor> availableColors,
            List<String> turnOrder) {
        this.id = id;
        this.numPlayers = numPlayers;
        this.activePlayer = activePlayer;
        this.startingPlayer = startingPlayer;
        this.winnerUsernames = winnerUsernames;
        this.objectiveCardsDeck = objectiveCardsDeck;
        this.starterCardsDeck = starterCardsDeck;
        this.goldCardsDeck = goldCardsDeck;
        this.resourceCardsDeck = resourceCardsDeck;
        this.commonObjectiveCards = commonObjectiveCards;
        this.commonGoldCards = commonGoldCards;
        this.commonResourceCards = commonResourceCards;
        this.state = state;
        this.rejoiningAfterCrash = true;
        this.players = players;
        this.availableColors = availableColors;
        this.turnOrder = turnOrder;
    }

    /**
     * @return The game's ID.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Adds a new player into the game. The number of players cannot exceed numPlayers.
     *
     * @param username The player's username.
     * @throws UnexpectedActionException Thrown if the game is already full, if the game has already started, or if there is already a player with the same username present in the game.
     */
    @Override
    public void addPlayer(String username) throws UnexpectedActionException {
        if (players.containsKey(username))
            throw new UnexpectedActionException("Player already present with the same username");
        if (players.size() >= numPlayers)
            throw new UnexpectedActionException("Maximum player count reached");
        if (state != GameState.JOINING)
            throw new UnexpectedActionException("Game already started");


        Player player = new Player(username);
        players.put(username, player);
    }

    /**
     * Removes a player from the game. This method can only be used in the initial phase of the game.
     *
     * @param username The username of the player to be removed. If a player with the given username does not exist, this method does nothing.
     * @throws UnexpectedActionException Thrown if an attempt was made to remove a player from a game which has already started.
     */
    @Override
    public void removePlayer(String username) throws UnexpectedActionException {
        if (state != GameState.JOINING) {
            throw new UnexpectedActionException("Game already started.");
        }

        players.remove(username);
    }

    /**
     * @return The non-variable number of players expected to play the game.
     */
    @Override
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * @return The number of players who joined the game and are currently connected.
     */
    @Override
    @JsonIgnore
    public int getCurrentPlayerCount() {
        return players.values().stream().filter(Player::isConnected).toList().size();
    }

    /**
     * Sets a player's connection status.
     *
     * @param username  The player's username.
     * @param connected Whether the player is connected.
     */
    @Override
    public void setConnected(String username, boolean connected) {
        if (!players.containsKey(username)) return;

        players.get(username).setConnected(connected);
    }

    /**
     * @return whether all the players in this game are connected.
     */
    public boolean everybodyConnected() {
        for (Player p : players.values()) {
            if (!p.isConnected()) return false;
        }
        return true;
    }

    /**
     * @return The id of the player who has to finish their turn.
     */
    @Override
    public String getActivePlayer() {
        return activePlayer;
    }

    /**
     * @return The id of the player whose turn is the first.
     */
    @Override
    public String getStartingPlayer() {
        return startingPlayer;
    }

    /**
     * @return The id(s) of the player(s) who won.
     */
    @Override
    public List<String> getWinnerUsernames() {
        return winnerUsernames;
    }

    /**
     * Initializes the game by drawing the common gold and resource cards, and distributing the starter cards to the players.
     *
     * @throws UnexpectedActionException Thrown if the game was already initialized, or if the game hasn't reached the required number of players.
     */
    @Override
    public void initializeGame() throws UnexpectedActionException {
        if (state != GameState.JOINING && !rejoiningAfterCrash)
            throw new UnexpectedActionException("Game already started");
        if (getCurrentPlayerCount() != numPlayers)
            throw new UnexpectedActionException("Missing players");
        if (!rejoiningAfterCrash) {
            state = GameState.INIT;
            drawCommonCards();
            drawStarterCards();
        }
        rejoiningAfterCrash = false;
    }

    /**
     * Draws two gold cards and two resource cards. The players can see them and choose to draw them.
     */
    private void drawCommonCards() {
        commonResourceCards[0] = resourceCardsDeck.drawCard();
        commonResourceCards[1] = resourceCardsDeck.drawCard();
        commonGoldCards[0] = goldCardsDeck.drawCard();
        commonGoldCards[1] = goldCardsDeck.drawCard();
    }


    /**
     * Draws numPlayers starter cards and gives one to each player.
     */
    private void drawStarterCards() {
        players.values().forEach(p -> p.giveStarterCard(starterCardsDeck.drawCard()));
    }

    /**
     * Lets the player choose the side of their starter card. It can be either front or back.
     *
     * @param username The player's username.
     * @param side     The card's side.
     * @throws UnexpectedActionException Thrown if the game has already started, hence all players should have already chosen their starter card side.
     */
    @Override
    public void setPlayerStarterSide(String username, SideType side) throws UnexpectedActionException, NoStarterCardException {
        if (state != GameState.INIT)
            throw new UnexpectedActionException("Game already started");

        players.get(username).chooseStarterCardSide(side);
    }

    /**
     * Sets the color of a player.
     *
     * @param username The player's username.
     * @param color    The color a player chose.
     * @throws UnexpectedActionException Thrown if the game has already started, hence all the players should have already chosen their color, or if the given color has already been chosen by another player.
     */
    @Override
    public void setPlayerColor(String username, PlayerColor color) throws UnexpectedActionException {
        if (state != GameState.INIT)
            throw new UnexpectedActionException("Game already started");

        if (!availableColors.contains(color))
            throw new UnexpectedActionException("Color already chosen");

        players.get(username).setColor(color);
        availableColors.remove(color);
    }

    /**
     * @return A {@link List} containing all the colors that are still available for a player to choose.
     */
    @Override
    public List<PlayerColor> getAvailableColors() {
        return availableColors;
    }

    /**
     * @return Whether all the players have chosen the side of their starter card.
     */
    @Override
    public boolean allPlayersChoseStarterSide() {
        for (int i = 0; i < numPlayers; i++) {
            if (!players[i].getChoseStarterCardSide()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return Whether all the players have chosen their color.
     */
    @Override
    public boolean allPlayersChoseColor() {
        for (int i = 0; i < numPlayers; i++) {
            if (!players[i].getChoseColor()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Draws the common objective cards and distributes the personal objectives to the players.
     * This method should only be called after everyone has chosen their starter card side and color.
     */
    @Override
    public void initializeObjectives() {
        drawCommonObjectives();
        distributePersonalObjectives();
    }

    /**
     * Distributes two resource cards and a gold card so that the game can start.
     */
    @Override
    public void distributeCards() {
        for (int i = 0; i < numPlayers; i++) {
            players[i].giveCard(resourceCardsDeck.drawCard());
            players[i].giveCard(resourceCardsDeck.drawCard());
            players[i].giveCard(goldCardsDeck.drawCard());
        }
    }

    /**
     * Draws two objective cards, every player can see them.
     */
    private void drawCommonObjectives() {
        commonObjectiveCards[0] = objectiveCardsDeck.drawCard();
        commonObjectiveCards[1] = objectiveCardsDeck.drawCard();
    }

    /**
     * Gives every player two objective cards.
     */
    private void distributePersonalObjectives() {
        for (int i = 0; i < numPlayers; i++) {
            players[i].giveObjectiveOptions(objectiveCardsDeck.drawCard(), objectiveCardsDeck.drawCard());
        }
    }

    /**
     * Sets the chosen objective card for a specific player.
     *
     * @param playerId      The player's ID.
     * @param objectiveCard The chosen objective card.
     * @throws UnknownObjectiveCardException Thrown when the given objective card is not in the player's objective card options.
     * @throws UnexpectedActionException     Thrown if the objectives have not yet been distributed, or the game has already started, or the given player has already chosen their objective.
     */
    @Override
    public void setPlayerObjective(String username, ObjectiveCard objectiveCard) throws UnexpectedActionException, UnknownObjectiveCardException {
        if (state != GameState.INIT)
            throw new UnexpectedActionException("Objectives not yet distributed, or game already started");
        if (players.get(username).getPersonalObjective() != null)
            throw new UnexpectedActionException("Player already chose their objective");
        players.get(username).setObjectiveCard(objectiveCard);
    }


    /**
     * @return Whether all the players have chosen their personal objective.
     */
    @Override
    public boolean allPlayersChoseObjective() {
        for (int i = 0; i < numPlayers; i++) {
            if (!players[i].getChosePersonalObjective()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Starts the game by choosing the starting player and distributing the resource and gold cards.
     *
     * @throws UnexpectedActionException Thrown if the game has already been started, or if not all players have chosen their objective card.
     */
    @Override
    public void startGame() throws UnexpectedActionException {
        if (state != GameState.INIT)
            throw new UnexpectedActionException("Game already started");
        if (!allPlayersChoseObjective())
            throw new UnexpectedActionException("Not all players chose their objective yet");

        startingPlayer = chooseStartingPlayer();
        activePlayer = startingPlayer;
        state = GameState.STARTED;
    }

    /**
     * Chooses the starting player randomly.
     *
     * @return The starting player's ID.
     */
    private String chooseStartingPlayer() {
        turnOrder.clear();
        turnOrder.addAll(players.keySet());
        Collections.shuffle(turnOrder, RNG.getRNG());
        return turnOrder.getFirst();
    }

    /**
     * Lets a player place a card.
     *
     * @param playerId   The player's ID.
     * @param placedCard The card the player wants to place.
     * @param side       The chosen card's side.
     * @param newCardPos The position of the card.
     * @throws IllegalMoveException      Thrown if the player made an illegal move.
     * @throws UnexpectedActionException Thrown if this method is called before the game has been started.
     */
    @Override
    public void placeCard(int playerId, PlayableCard placedCard, SideType side, Position newCardPos) throws IllegalMoveException, UnexpectedActionException {
        if (state != GameState.STARTED && state != GameState.FINAL_ROUND)
            throw new UnexpectedActionException("Game not started");
        players[playerId].playCard(placedCard, side, newCardPos);
    }

    /**
     * Lets the player draw a card. A card can be drawn from the deck or from the currently visible cards.
     * If the card is drawn from one of the common cards, it is replaced with a card of the same type if available. If a card of the same type is not available, it is replaced with a card of the other type.
     *
     * @param playerId The player's ID.
     * @param drawType The place a player wants to draw a card from.
     * @return The drawn card.
     * @throws UnexpectedActionException Thrown if this method is called before the game has been started.
     * @throws IllegalMoveException      Thrown if a draw is attempted from an empty deck, or from an empty common card slot.
     */
    @Override
    public PlayableCard drawCard(String username, DrawType drawType) throws UnexpectedActionException, IllegalMoveException {
        if (state != GameState.STARTED) throw new UnexpectedActionException("Game not started");

        PlayableCard drawnCard = null;

        switch (drawType) {
            case GOLD_1 -> {
                if (commonGoldCards[0] == null)
                    throw new IllegalMoveException("Illegal draw: no card");

                drawnCard = commonGoldCards[0];
                players.get(username).giveCard(commonGoldCards[0]);
                commonGoldCards[0] = goldCardsDeck.drawCard();
                if (commonGoldCards[0] == null) {
                    commonGoldCards[0] = resourceCardsDeck.drawCard();
                }
            }
            case GOLD_2 -> {
                if (commonGoldCards[1] == null)
                    throw new IllegalMoveException("Illegal draw: no card");

                drawnCard = commonGoldCards[1];
                players.get(username).giveCard(commonGoldCards[1]);
                commonGoldCards[1] = goldCardsDeck.drawCard();
                if (commonGoldCards[1] == null) {
                    commonGoldCards[1] = resourceCardsDeck.drawCard();
                }
            }
            case GOLD_DECK -> {
                if (goldCardsDeck.isEmpty())
                    throw new IllegalMoveException("Illegal draw: empty deck");

                drawnCard = goldCardsDeck.drawCard();

                players.get(username).giveCard(drawnCard);
            }
            case RESOURCE_1 -> {
                if (commonResourceCards[0] == null)
                    throw new IllegalMoveException("Illegal draw: no card");

                drawnCard = commonResourceCards[0];
                players.get(username).giveCard(commonResourceCards[0]);
                commonResourceCards[0] = resourceCardsDeck.drawCard();
                if (commonResourceCards[0] == null) {
                    commonResourceCards[0] = goldCardsDeck.drawCard();
                }
            }
            case RESOURCE_2 -> {
                if (commonResourceCards[1] == null)
                    throw new IllegalMoveException("Illegal draw: no card");

                drawnCard = commonResourceCards[1];
                players.get(username).giveCard(commonResourceCards[1]);
                commonResourceCards[1] = resourceCardsDeck.drawCard();
                if (commonResourceCards[1] == null) {
                    commonResourceCards[1] = goldCardsDeck.drawCard();
                }
            }
            case RESOURCE_DECK -> {
                if (resourceCardsDeck.isEmpty())
                    throw new IllegalMoveException("Illegal draw: empty deck");

                drawnCard = resourceCardsDeck.drawCard();

                players.get(username).giveCard(drawnCard);
            }
        }

        return drawnCard;
    }

    /**
     * Advances the turn to the next player.
     *
     * @throws UnexpectedActionException Thrown if the game not started yet, or if it has already ended.
     */
    @Override
    public void advanceTurn() throws UnexpectedActionException {
        if (state != GameState.STARTED && state != GameState.FINAL_ROUND)
            throw new UnexpectedActionException("Game has not started or has already ended");

        int activePlayerIndex = turnOrder.indexOf(activePlayer);
        activePlayerIndex++;
        activePlayerIndex %= numPlayers;
        activePlayer = turnOrder.get(activePlayerIndex);
    }

    /**
     * @return Whether the game is ready to enter the final round.
     */
    @Override
    public boolean checkFinalRound() {
        if (resourceCardsDeck.isEmpty() && goldCardsDeck.isEmpty())
            return true;

        for (int i = 0; i < numPlayers; i++) {
            if (players[i].getGamePoints() >= 20) {
                return true;
            }
        }
        return false;
    }

    /**
     * Evaluates every player's points earned by fulfilling the objective cards' (both common and personal) requests.
     */
    private void evaluateObjectivePoints() {
        for (int i = 0; i < numPlayers; i++) {
            players[i].evaluateCommonObjectives(getCommonObjectiveCards()[0]);
            players[i].evaluateCommonObjectives(getCommonObjectiveCards()[1]);
            players[i].evaluatePersonalObjective();
        }
    }

    /**
     * Triggers the game to enter the final round, if it is ready to do so; otherwise, this method does nothing.
     *
     * @throws UnexpectedActionException Thrown if the game has not started yet, or if it has already ended.
     */
    @Override
    public void triggerFinalRound() throws UnexpectedActionException {
        if (state != GameState.STARTED) throw new UnexpectedActionException("Game has not started or already ended");
        if (!checkFinalRound()) return;

        state = GameState.FINAL_ROUND;
    }

    /**
     * Ends the current game and evaluates the objective points of the players. Then, it selects a winner.
     *
     * @throws UnexpectedActionException Thrown if the game has not yet entered its final round, or if it has already ended.
     */
    @Override
    public void endGame() throws UnexpectedActionException {
        if (state != GameState.FINAL_ROUND)
            throw new UnexpectedActionException("Final round not triggered yet, or game already ended");
        state = GameState.ENDED;
        evaluateObjectivePoints();
        selectWinners();
    }

    /**
     * Chooses the winner(s) of the game.
     */
    private void selectWinners() {
        //TODO check that this actually works.
        // there's probably a better way to do it too
        int tmpPoints = 0;
        String tmpUsername = null;
        int tmpObjPoints = 0;
        List<Integer> tmpWinners = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            if (players[i].getTotalPoints() > tmpPoints) {
                tmpPoints = players[i].getTotalPoints();
                tmpId = players[i].getPlayerId();
            }
        }
        tmpWinners.add(tmpId);
        for (int j = 0; j < numPlayers; j++) {
            if (players[j].getTotalPoints() == tmpPoints && players[j].getPlayerId() != tmpId) {
                tmpWinners.add(players[j].getPlayerId());
            }
        }
        if (tmpWinners.size() == 1) {
            winnerIds.add(tmpId);
            return;
        }
        if (tmpWinners.size() > 1) {
            for (int k = 0; k < numPlayers; k++) {
                if (players[k].getObjectivePoints() > tmpObjPoints && tmpWinners.contains(players[k].getPlayerId())) {
                    tmpObjPoints = players[k].getObjectivePoints();
                    tmpId = players[k].getPlayerId();
                }
            }
        }
        winnerIds.add(tmpId);
        for (int l = 0; l < numPlayers; l++) {
            if (players[l].getObjectivePoints() == tmpObjPoints && players[l].getPlayerId() != tmpId && tmpWinners.contains(players[l].getPlayerId())) {
                winnerIds.add(players[l].getPlayerId());
            }
        }
    }

    /**
     * @return The players inside the game.
     */
    @Override
    public Map<String, Player> getPlayers() {
        return players;
    }

    /**
     * @return The common objective cards.
     */
    @Override
    public ObjectiveCard[] getCommonObjectiveCards() {
        return commonObjectiveCards.clone();
    }

    /**
     * @return The visible and drawable gold cards.
     */
    @Override
    public PlayableCard[] getCommonGoldCards() {
        return commonGoldCards.clone();
    }

    /**
     * @return The visible and drawable resource cards.
     */
    @Override
    public PlayableCard[] getCommonResourceCards() {
        return commonResourceCards.clone();
    }

    /**
     * @return the game's state.
     */
    @Override
    public GameState getState() {
        return state;
    }

    /**
     * @return whether the players are rejoining after a server crash.
     */
    @Override
    @JsonIgnore
    public boolean isRejoiningAfterCrash() {
        return rejoiningAfterCrash;
    }

    /**
     * @return the type of the card on top of the resource deck, or null if the deck is empty. This information should be visible to the players.
     */
    @Override
    @JsonIgnore
    public ResourceType getResourceDeckTopType() {
        ResourceCard card = resourceCardsDeck.peekTop();
        if (card == null) return null;
        return card.getType();
    }

    /**
     * @return the type of the card on top of the gold deck, or null if the deck is empty. This information should be visible to the player.
     */
    @Override
    @JsonIgnore
    public ResourceType getGoldDeckTopType() {
        GoldCard card = goldCardsDeck.peekTop();
        if (card == null) return null;
        return card.getType();
    }

    /**
     * @return a list containing the ids of the players in the order in which they play.
     */
    @Override
    public List<String> getTurnOrder() {
        List<String> turnOrder = new ArrayList<>();
        for (int id = 0; id < numPlayers; id++) {
            turnOrder.add(players[(id + startingPlayer) % numPlayers].getUsername());
        }
        return turnOrder;
    }

    /**
     * Pauses the game. Used when a player disconnects from the game.
     */
    @Override
    public void pause() {
        this.rejoiningAfterCrash = true;
        for (Player p : players.values()) {
            p.setConnected(false);
        }
    }

    /**
     * @return The deck of gold cards. Used for JSON serialization.
     */
    @SuppressWarnings("unused") //Suppressing because this method is being used by the JSON serializer.
    public GoldCardsDeck getGoldCardsDeck() {
        return goldCardsDeck;
    }

    /**
     * @return The deck of resource cards. Used for JSON serialization.
     */
    @SuppressWarnings("unused") //Suppressing because this method is being used by the JSON serializer.
    public ResourceCardsDeck getResourceCardsDeck() {
        return resourceCardsDeck;
    }

    /**
     * @return The deck of objective cards. Used for JSON serialization
     */
    @SuppressWarnings("unused") //Suppressing because this method is being used by the JSON serializer.
    public ObjectiveCardsDeck getObjectiveCardsDeck() {
        return objectiveCardsDeck;
    }

    /**
     * @return The deck of starter cards. Used for JSON serialization.
     */
    @SuppressWarnings("unused") //Suppressing because this method is being used by the JSON serializer.
    public StarterCardsDeck getStarterCardsDeck() {
        return starterCardsDeck;
    }

    /**
     * Deserializer used to reload a game from a JSON file.
     */
    public static class Deserializer extends StdDeserializer<Game> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        @Serial
        private static final long serialVersionUID = -1629341944590439653L;

        protected Deserializer() {
            super(Game.class);
        }

        /**
         * Reloads a {@link Game} object from the given JSON.
         *
         * @param p    Parsed used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *             this deserialization activity.
         * @return The deserialized {@link Game}.
         * @throws IOException      Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public Game deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);

            String id = node.get("id").asText();

            int numPlayers = node.get("numPlayers").asInt();
            String activePlayer = mapper.readValue(node.get("activePlayer").toString(), String.class);
            String startingPlayer = mapper.readValue(node.get("startingPlayer").toString(), String.class);

            TypeReference<ArrayList<Integer>> winnerIdsTypeRef = new TypeReference<>() {
            };
            List<Integer> winnerIds = mapper.readValue(node.get("winnerIds").toString(), winnerIdsTypeRef);

            ObjectiveCardsDeck objectiveCardsDeck = mapper.readValue(node.get("objectiveCardsDeck").toString(), ObjectiveCardsDeck.class);
            StarterCardsDeck starterCardsDeck = mapper.readValue(node.get("starterCardsDeck").toString(), StarterCardsDeck.class);
            GoldCardsDeck goldCardsDeck = mapper.readValue(node.get("goldCardsDeck").toString(), GoldCardsDeck.class);
            ResourceCardsDeck resourceCardsDeck = mapper.readValue(node.get("resourceCardsDeck").toString(), ResourceCardsDeck.class);

            ObjectiveCard[] commonObjectiveCards = mapper.readValue(node.get("commonObjectiveCards").toString(), ObjectiveCard[].class);
            PlayableCard[] commonGoldCards = mapper.readValue(node.get("commonGoldCards").toString(), PlayableCard[].class);
            PlayableCard[] commonResourceCards = mapper.readValue(node.get("commonResourceCards").toString(), PlayableCard[].class);

            GameState state = mapper.readValue(node.get("state").toString(), GameState.class);

            TypeReference<HashMap<String, Player>> playersTypeRef = new TypeReference<>() {
            };
            Map<String, Player> players = mapper.readValue(node.get("players").toString(), playersTypeRef);

            TypeReference<ArrayList<PlayerColor>> availableColorsTypeRef = new TypeReference<>() {
            };
            List<PlayerColor> availableColors = mapper.readValue(node.get("availableColors").toString(), availableColorsTypeRef);

            TypeReference<ArrayList<String>> turnOrderTypeRef = new TypeReference<>() {
            };
            List<String> turnOrder = mapper.readValue(node.get("turnOrder").toString(), turnOrderTypeRef);

            return new Game(
                    id,
                    numPlayers,
                    activePlayer,
                    startingPlayer,
                    winnerUsernames,
                    objectiveCardsDeck,
                    starterCardsDeck,
                    goldCardsDeck,
                    resourceCardsDeck,
                    commonObjectiveCards,
                    commonGoldCards,
                    commonResourceCards,
                    state,
                    players,
                    availableColors,
                    turnOrder
            );
        }
    }
}

