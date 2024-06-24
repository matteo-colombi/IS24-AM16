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
import java.util.function.Predicate;

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
     * @param turnOrder            The order in which players play in this game.
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

    @Override
    public String getId() {
        return id;
    }

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

    @Override
    public void removePlayer(String username) throws UnexpectedActionException {
        if (state != GameState.JOINING) {
            throw new UnexpectedActionException("Game already started.");
        }

        players.remove(username);
    }

    @Override
    public int getNumPlayers() {
        return numPlayers;
    }

    @Override
    @JsonIgnore
    public int getCurrentPlayerCount() {
        return players.values().stream().filter(Player::isConnected).toList().size();
    }

    @Override
    public void setConnected(String username, boolean connected) {
        if (!players.containsKey(username)) return;

        players.get(username).setConnected(connected);
    }

    /**
     * @return whether all the players in this game are connected.
     */
    @Override
    public boolean everybodyConnected() {
        for (Player player : players.values()) {
            if (!player.isConnected())
                return false;
        }
        return true;
    }

    @Override
    public String getActivePlayer() {
        return activePlayer;
    }

    @Override
    public String getStartingPlayer() {
        return startingPlayer;
    }

    @Override
    public List<String> getWinnerUsernames() {
        return winnerUsernames;
    }

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

    @Override
    public void setPlayerStarterSide(String username, SideType side) throws UnexpectedActionException, NoStarterCardException {
        if (state != GameState.INIT)
            throw new UnexpectedActionException("Game already started");

        players.get(username).chooseStarterCardSide(side);
    }

    @Override
    public void setPlayerColor(String username, PlayerColor color) throws UnexpectedActionException {
        if (state != GameState.INIT)
            throw new UnexpectedActionException("Game already started");

        if (!availableColors.contains(color))
            throw new UnexpectedActionException("Color already chosen");

        players.get(username).setColor(color);
        availableColors.remove(color);
    }

    @Override
    public List<PlayerColor> getAvailableColors() {
        return availableColors;
    }

    @Override
    public boolean allPlayersChoseStarterSide() {
        for (Player player : players.values()) {
            if (!player.getChoseStarterCardSide()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean allPlayersChoseColor() {
        for (Player player : players.values()) {
            if (!player.getChoseColor()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void initializeObjectives() {
        drawCommonObjectives();
        distributePersonalObjectives();
    }

    @Override
    public void distributeCards() {
        for (Player player : players.values()) {
            player.giveCard(resourceCardsDeck.drawCard());
            player.giveCard(resourceCardsDeck.drawCard());
            player.giveCard(goldCardsDeck.drawCard());
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
        for (Player player : players.values()) {
            player.giveObjectiveOptions(objectiveCardsDeck.drawCard(), objectiveCardsDeck.drawCard());
        }
    }

    @Override
    public void setPlayerObjective(String username, ObjectiveCard objectiveCard) throws UnexpectedActionException, UnknownObjectiveCardException {
        if (state != GameState.INIT)
            throw new UnexpectedActionException("Objectives not yet distributed, or game already started");
        if (players.get(username).getPersonalObjective() != null)
            throw new UnexpectedActionException("Player already chose their objective");
        players.get(username).setObjectiveCard(objectiveCard);
    }

    @Override
    public boolean allPlayersChoseObjective() {
        for (Player player : players.values()) {
            if (!player.getChosePersonalObjective()) {
                return false;
            }
        }

        return true;
    }

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

    @Override
    public void placeCard(String username, PlayableCard placedCard, SideType side, Position newCardPos) throws IllegalMoveException, UnexpectedActionException {
        if (state != GameState.STARTED && state != GameState.FINAL_ROUND)
            throw new UnexpectedActionException("Game not started");
        players.get(username).playCard(placedCard, side, newCardPos);
    }

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

    @Override
    public void advanceTurn() throws UnexpectedActionException {
        if (state != GameState.STARTED && state != GameState.FINAL_ROUND)
            throw new UnexpectedActionException("Game has not started or has already ended");

        int activePlayerIndex = turnOrder.indexOf(activePlayer);
        activePlayerIndex++;
        activePlayerIndex %= numPlayers;
        activePlayer = turnOrder.get(activePlayerIndex);
    }

    @Override
    public boolean checkFinalRound() {
        if (resourceCardsDeck.isEmpty() && goldCardsDeck.isEmpty())
            return true;

        for (Player p : players.values()) {
            if (p.getGamePoints() >= 20) {
                return true;
            }
        }
        return false;
    }

    /**
     * Evaluates every player's points earned by fulfilling the objective cards' (both common and personal) requests.
     */
    private void evaluateObjectivePoints() {
        for (Player p : players.values()) {
            p.evaluateCommonObjectives(getCommonObjectiveCards()[0]);
            p.evaluateCommonObjectives(getCommonObjectiveCards()[1]);
            p.evaluatePersonalObjective();
        }
    }

    @Override
    public void triggerFinalRound() throws UnexpectedActionException {
        if (state != GameState.STARTED) throw new UnexpectedActionException("Game has not started or already ended");
        if (!checkFinalRound()) return;

        state = GameState.FINAL_ROUND;
    }

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
        winnerUsernames.clear();
        winnerUsernames.addAll(
                players.values().stream()
                        .sorted(
                                Comparator.comparingInt(Player::getTotalPoints)
                                        .thenComparingInt(Player::getObjectivePoints).reversed())
                        .filter(new Predicate<>() {
                            private int maxTotal = 0;
                            private int maxObjective = 0;

                            @Override
                            public boolean test(Player player) {
                                maxTotal = Math.max(player.getTotalPoints(), maxTotal);
                                maxObjective = Math.max(player.getObjectivePoints(), maxObjective);
                                return player.getTotalPoints() == maxTotal && player.getObjectivePoints() == maxObjective;
                            }
                        })
                        .map(Player::getUsername)
                        .toList()
        );
    }

    @Override
    public Map<String, Player> getPlayers() {
        return players;
    }

    @Override
    public ObjectiveCard[] getCommonObjectiveCards() {
        return commonObjectiveCards.clone();
    }

    @Override
    public PlayableCard[] getCommonGoldCards() {
        return commonGoldCards.clone();
    }

    @Override
    public PlayableCard[] getCommonResourceCards() {
        return commonResourceCards.clone();
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    @JsonIgnore
    public boolean isRejoiningAfterCrash() {
        return rejoiningAfterCrash;
    }

    @Override
    @JsonIgnore
    public ResourceType getResourceDeckTopType() {
        ResourceCard card = resourceCardsDeck.peekTop();
        if (card == null) return null;
        return card.getType();
    }

    @Override
    @JsonIgnore
    public ResourceType getGoldDeckTopType() {
        GoldCard card = goldCardsDeck.peekTop();
        if (card == null) return null;
        return card.getType();
    }

    @Override
    public List<String> getTurnOrder() {
        return turnOrder;
    }

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
    public GoldCardsDeck getGoldCardsDeck() {
        return goldCardsDeck;
    }

    /**
     * @return The deck of resource cards. Used for JSON serialization.
     */
    public ResourceCardsDeck getResourceCardsDeck() {
        return resourceCardsDeck;
    }

    /**
     * @return The deck of objective cards. Used for JSON serialization
     */
    public ObjectiveCardsDeck getObjectiveCardsDeck() {
        return objectiveCardsDeck;
    }

    /**
     * @return The deck of starter cards. Used for JSON serialization.
     */
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

            TypeReference<ArrayList<String>> winnerUsernamesTypeRef = new TypeReference<>() {
            };
            List<String> winnerUsernames = mapper.readValue(node.get("winnerUsernames").toString(), winnerUsernamesTypeRef);

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

