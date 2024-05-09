package it.polimi.ingsw.am16.common.model.players;

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
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.chat.Chat;
import it.polimi.ingsw.am16.common.model.players.hand.Hand;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.model.game.Game;

import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class to handle players in a game.
 * A player has a unique id for identification and a username. <br>
 * Each player will manage a playing area and their hand of playable cards
 *
 */
@JsonDeserialize(using = Player.Deserializer.class)
public class Player implements PlayerModel {
    private final String username;
    private int currGamePoints;
    private int currObjectivePoints;
    private ObjectiveCard personalObjective;
    private final List<ObjectiveCard> possiblePersonalObjectives;
    private StarterCard starterCard;
    private PlayerColor color;
    private final Hand hand;
    private final PlayArea playArea;
    private boolean choseStarterCardSide;
    private boolean choseObjectiveCard;
    private boolean choseColor;
    private final Chat chat;
    private boolean isConnected;

    /**
     * Creates a new player, initializing their ID and username to a chosen value, and their
     * other attributes to standard values.
     * @param username The player's in-game name
     */
    public Player(String username) {
        this.username = username;
        this.currGamePoints = 0;
        this.currObjectivePoints = 0;
        this.possiblePersonalObjectives = new ArrayList<>();
        this.hand = new Hand();
        this.playArea = new PlayArea();
        this.color = null;
        this.choseObjectiveCard = false;
        this.choseStarterCardSide = false;
        this.choseColor = false;
        this.chat = new Chat(username);
        this.isConnected = false;
    }

    /**
     * Constructs a new Player with the given attributes. Used when reloading players from a game save file.
     * This constructor is private because it should only be used by {@link Player.Deserializer}
     * @param username The player's username.
     * @param currGamePoints The player's current game points, which are points that are scored throughout the game.
     * @param currObjectivePoints The player's objective points, tallied up at the end of the game.
     * @param personalObjective The player's personal objective.
     * @param possiblePersonalObjectives The choices given to the player for the personal objective.
     * @param starterCard The player's starter card.
     * @param color The player's color.
     * @param hand The player's hand, containing the cards currently playable by the player.
     * @param playArea The player's play area, containing information on the cards placed by the player, and the resources and objects currently visible.
     * @param choseStarterCardSide Whether the player has already chosen their starter card's side.
     * @param choseObjectiveCard Whether the player has already chosen their objective.
     * @param choseColor Whether the player has already chosen their color.
     */
    private Player(
            String username,
            int currGamePoints,
            int currObjectivePoints,
            ObjectiveCard personalObjective,
            List<ObjectiveCard> possiblePersonalObjectives,
            StarterCard starterCard,
            PlayerColor color,
            Hand hand,
            PlayArea playArea,
            boolean choseStarterCardSide,
            boolean choseObjectiveCard,
            boolean choseColor,
            Chat chat
    ) {
        this.username = username;
        this.currGamePoints = currGamePoints;
        this.currObjectivePoints = currObjectivePoints;
        this.personalObjective = personalObjective;
        this.possiblePersonalObjectives = possiblePersonalObjectives;
        this.starterCard = starterCard;
        this.color = color;
        this.hand = hand;
        this.playArea = playArea;
        this.choseStarterCardSide = choseStarterCardSide;
        this.choseObjectiveCard = choseObjectiveCard;
        this.choseColor = choseColor;
        this.chat = chat;
        this.isConnected = false;
    }

    /**
     * @return The player's username.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return The player's in-game color
     */
    @Override
    public PlayerColor getPlayerColor() {
        return color;
    }

    /**
     *
     * @return The player's points gathered by placing cards on the board
     */
    @Override
    public int getGamePoints() {
        return currGamePoints;
    }

    /**
     *
     * @return The player's points gathered by fulfilling conditions on their personal
     * objective and the common objectives of the game
     */
    @Override
    public int getObjectivePoints() {
        return currObjectivePoints;
    }

    /**
     *
     * @return The total points the player got at the end of the game, after evaluating
     * their completion of the objectives
     */
    @Override
    @JsonIgnore
    public int getTotalPoints() {
        return currGamePoints + currObjectivePoints;
    }

    /**
     *
     * @return The player's personal objective card
     */
    @Override
    public ObjectiveCard getPersonalObjective() {
        return personalObjective;
    }

    /**
     * @return The player's starter card, regardless of whether they have already chosen the side to use.
     */
    @Override
    public StarterCard getStarterCard() {
        return starterCard;
    }

    /**
     *
     * @return The player's hand, giving access only to its non-modifier methods
     */
    @Override
    public Hand getHand() {
        return hand;
    }

    /**
     *
     * @return The player's board state, giving access only to its non-modifier methods
     */
    @Override
    public PlayArea getPlayArea() {
        return playArea;
    }

    /**
     *
     * @return The player's two objective cards from which they'll choose their personal
     * objective
     */
    @Override
    public List<ObjectiveCard> getPersonalObjectiveOptions() {
        return List.copyOf(possiblePersonalObjectives);
    }

    /**
     *
     * @return whether the player has chosen which side of their starter card to display
     */
    @Override
    public boolean getChoseStarterCardSide() {
        return choseStarterCardSide;
    }

    /**
     *
     * @return which of the two given possible personal objectives the player chose
     */
    @Override
    public boolean getChosePersonalObjective() {
        return choseObjectiveCard;
    }

    /**
     * @return whether the player has already chosen their color.
     */
    @Override
    public boolean getChoseColor() {
        return choseColor;
    }

    /**
     * Adds a card to the player's hand. This should be triggered at the end of each of
     * the player's turns to replace the card just placed beforehand.
     * @param card The card to be added
     */
    public void giveCard(PlayableCard card) {
        this.hand.addCard(card);
    }

    /**
     * Removes a card from the player's hand, if present.
     *
     * @param card The card to remove from the player's hand
     */
    public void removeCard(PlayableCard card) {
        this.hand.removeCard(card);
    }

    /**
     * Places a card from the player's hand on the board.
     * @param card The card to be placed
     * @param side The side the card will be placed on
     * @param newCardPos The position of the card on the player's board
     * @throws IllegalMoveException Thrown if the player made an illegal move
     */
    public void playCard(PlayableCard card, SideType side, Position newCardPos) throws IllegalMoveException {
        if (!hand.contains(card)) throw new IllegalMoveException("No such card.");
        this.playArea.playCard(card, side, newCardPos);
        this.currGamePoints += this.playArea.awardGamePoints(card);
        removeCard(card);
    }

    /**
     * Gives the player the two objective cards to choose from, adding them to the options
     * attribute.
     * @param firstOption The first card
     * @param secondOption The second card
     */
    public void giveObjectiveOptions(ObjectiveCard firstOption, ObjectiveCard secondOption) {
        this.possiblePersonalObjectives.add(firstOption);
        this.possiblePersonalObjectives.add(secondOption);
    }

    /**
     * Gives the player their starter card.
     * @param starterCard The given starter card
     */
    public void giveStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    /**
     * Sets the player's objective card of choice between the two possible ones, signaling
     * that the choice has been made.
     * @param objectiveCard The chosen objective card
     */
    public void setObjectiveCard(ObjectiveCard objectiveCard) throws UnknownObjectiveCardException{
        if(possiblePersonalObjectives.contains(objectiveCard)){
            this.personalObjective = objectiveCard;
            this.choseObjectiveCard = true;
        } else {
            throw new UnknownObjectiveCardException("Objective card not included in the choices for the player");
        }
    }

    /**
     * Sets the active side for the player's starter card, signaling that the choice has
     * been made (links the {@link Game} and {@link PlayArea} objects).
     * @param side The chosen side
     * @throws NoStarterCardException If the player has no starter card to place (either not given yet, or already placed).
     */
    public void chooseStarterCardSide(SideType side) throws NoStarterCardException {
        if(this.starterCard != null){
            playArea.setStarterCard(this.starterCard, side);
            this.choseStarterCardSide = true;
            this.starterCard = null;
        } else {
            throw new NoStarterCardException("Player has no starter card to place");
        }
    }

    /**
     * Evaluates the total amount of points gathered by completing a common objective card
     * (links the {@link Game} and {@link PlayArea} objects), thus increasing
     * the player's current objective points.
     * @param commonObjective The objective card to evaluate
     */
    public void evaluateCommonObjectives(ObjectiveCard commonObjective) {
        this.currObjectivePoints += commonObjective.evaluatePoints(this.playArea);
    }

    /**
     * Evaluates the total amount of points gathered by completing the player's personal
     * objective (links the {@link Game} and {@link PlayArea} objects), thus increasing
     * the player's current objective points.
     *
     */
    public void evaluatePersonalObjective() {
        this.currObjectivePoints +=  this.personalObjective.evaluatePoints(this.playArea);
    }

    /**
     * Sets the player's in-game color.
     * @param color The chosen color
     */
    public void setColor(PlayerColor color){
        if(!this.choseColor){
            this.color = color;
            this.choseColor = true;
        }
    }

    public Chat getChat() {
        return chat;
    }

    /**
     * @return whether the player is connected.
     */
    @Override
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * @param isConnected Whether the player is connected or not.
     */
    @Override
    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    /**
     * @return Whether the player is deadlocked.
     */
    @Override
    public boolean isDeadlocked() {
        return playArea.isDeadlocked();
    }

    /**
     * Checks an object is equal to the player by comparing their usernames (if the parameter object is also a player).
     * @param o The object to compare the player to
     * @return true if the two are equal, false if they aren't
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return Objects.equals(username, player.username);
    }

    /**
     * @return the player's username's hash code.
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }

    /**
     * Custom deserializer for {@link Player}, used for reloading player data from a game save file.
     */
    public static class Deserializer extends StdDeserializer<Player> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        @Serial
        private static final long serialVersionUID = -3708720078355433100L;

        protected Deserializer() {
            super(Player.class);
        }

        /**
         * Deserializes the {@link Player} from the given JSON.
         * @param p Parser used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized player.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public Player deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node  = p.getCodec().readTree(p);

            // Deserialize player attributes

            String username = node.get("username").asText();

            int currGamePoints = node.get("gamePoints").asInt();
            int currObjectivePoints = node.get("objectivePoints").asInt();

            TypeReference<ArrayList<ObjectiveCard>> typeReferencePossiblePersonalObjectives = new TypeReference<>() {};
            List<ObjectiveCard> possiblePersonalObjectives = mapper.readValue(node.get("personalObjectiveOptions").toString(), typeReferencePossiblePersonalObjectives);
            ObjectiveCard personalObjective = mapper.readValue(node.get("personalObjective").toString(), ObjectiveCard.class);

            StarterCard starterCard = mapper.readValue(node.get("starterCard").toString(), StarterCard.class);

            PlayerColor color = mapper.readValue(node.get("playerColor").toString(), PlayerColor.class);

            Hand hand = mapper.readValue(node.get("hand").toString(), Hand.class);

            PlayArea playArea = mapper.readValue(node.get("playArea").toString(), PlayArea.class);

            boolean choseStarterCardSide = node.get("choseStarterCardSide").asBoolean();
            boolean choseObjectiveCard = node.get("chosePersonalObjective").asBoolean();
            boolean choseColor = node.get("choseColor").asBoolean();

            Chat chat = mapper.readValue(node.get("chat").toString(), Chat.class);

            return new Player(
                    username,
                    currGamePoints,
                    currObjectivePoints,
                    personalObjective,
                    possiblePersonalObjectives,
                    starterCard,
                    color,
                    hand,
                    playArea,
                    choseStarterCardSide,
                    choseObjectiveCard,
                    choseColor,
                    chat);
        }
    }
}
