package it.polimi.ingsw.am16.common.model.players;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.cards.StarterCard;
import it.polimi.ingsw.am16.common.model.players.hand.Hand;
import it.polimi.ingsw.am16.common.model.players.hand.HandModel;
import it.polimi.ingsw.am16.common.util.Position;

/**
 * Class to handle players in a game. A player has a unique id for identification and a username. <br>
 *
 */
public class Player implements PlayerModel {
    /**
     * Unique identifier for the player.
     */
    private final int playerId;
    /**
     * The player's in-game name.
     */
    private final String username;
    /**
     * The player's points gathered by placing cards on the board during a game, before adding the
     * points from their personal objective and the common objectives of the game.
     */
    private int currGamePoints;
    /**
     * The player's points gathered by fulfilling the conditions on their objective cards, which
     * will be added to the game points at the end of the game.
     */
    private int currObjectivePoints;
    /**
     * The player's personal objective card. Unlike common objectives, this one mustn't be
     * shown to the rest of the players.
     */
    private ObjectiveCard personalObjective;
    /**
     * Before the game starts, each player will have to choose between two possible
     * personal objectives, which will be stored here before they make their choice.
     */
    private final ObjectiveCard[] possiblePersonalObjectives;
    /**
     * The player's starter card, given randomly at the start of the game.
     */
    private StarterCard starterCard;
    /**
     * The player's in-game color, ideally represented on the points board.
     */
    private PlayerColor color;
    /**
     * The player's hand of resource and gold cards.
     */
    private final Hand hand;
    /**
     * The player's board, containing their starter card and their subsequently placed
     * resource/gold cards.
     */
    private final PlayArea playArea;
    /**
     * An indicator to mark whether the player chose which side to place their starter card on.
     */
    private boolean choseStarterCardSide;
    /**
     * An indicator to mark whether the player chose their objective card between the two
     * possible ones.
     *
     */
    private boolean choseObjectiveCard;

    /**
     * Creates a new player, initializing their ID and username to a chosen value, and their
     * other attributes to standard values.
     * @param playerId The player's ID
     * @param username The player's in-game name
     */

    public Player(int playerId, String username) {
        this.playerId = playerId;
        this.username = username;
        this.currGamePoints = 0;
        this.currObjectivePoints = 0;
        this.possiblePersonalObjectives = new ObjectiveCard[2];
        this.hand = new Hand();
        this.playArea = new PlayArea(this);
        this.color = null;
        this.choseObjectiveCard = false;
        this.choseStarterCardSide = false;
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
     * @return The player's ID
     */

    @Override
    public int getPlayerId() {
        return playerId;
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
     *
     * @return The player's hand, giving access only to its non-modifier methods
     */

    @Override
    public HandModel getHand() {
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
    public ObjectiveCard[] getPersonalObjectiveOptions() {
        //FIXME make me more robust
        return new ObjectiveCard[]{possiblePersonalObjectives[0], possiblePersonalObjectives[1]};
    }

    /**
     * Increases the amount of points gathered by placing a card on the board.
     * @param points The amount of points earned in an action
     */

    public void addGamePoints(int points) {
        currGamePoints += points;
    }

    /**
     * Increases the amount of points gathered by evaluating the objectives.
     * @param points The amount of points earned in an action
     */

    public void addObjectivePoints(int points) {
        currObjectivePoints += points;
    }

    /**
     * Adds a card to the player's hand. This should be triggered at the end of each of
     * the player's turns to replace the card just placed beforehand.
     * @param card The card to be added
     */

    public void giveCard(PlayableCard card) {
        this.hand.getCards().add(card);
    }

    /**
     * Removes a card from the player's hand, if present.
     * @param card The card to remove from the player's hand
     * @return True if the card was present and was removed, false if it wasn't
     * (and thus wasn't removed)
     */

    public boolean removeCard(PlayableCard card) {
        return this.hand.getCards().remove(card);
    }

    /**
     * Places a card from the player's hand on the board.
     * @param card The card to be placed
     * @param side The side the card will be placed on
     * @param newCardPos The position of the card on the player's board
     * @throws IllegalMoveException Thrown if the player made an illegal move
     */

    public void playCard(PlayableCard card, SideType side, Position newCardPos) throws IllegalMoveException {
        this.playArea.playCard(card, side, newCardPos);
    }

    /**
     * Gives the player the two objective cards to choose from, adding them to the options
     * attribute.
     * @param firstOption The first card
     * @param secondOption The second card
     */

    public void giveObjectiveOptions(ObjectiveCard firstOption, ObjectiveCard secondOption) {
        this.possiblePersonalObjectives[0] = firstOption;
        this.possiblePersonalObjectives[1] = secondOption;
    }

    /**
     * Gives the player their starter card.
     * @param starterCard The given starter card
     */

    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    /**
     * Sets the player's objective card of choice between the two possible ones, signaling
     * that the choice has been made.
     * @param objectiveCard The chosen objective card
     */

    public void setObjectiveCard(ObjectiveCard objectiveCard) {
        this.personalObjective = objectiveCard;
        this.choseObjectiveCard = true;
        //TODO check if the card is contained in possiblePersonalObjectives
    }

    /**
     * Sets the active side for the player's starter card, signaling that the choice has
     * been made (links the Game and PlayArea objects).
     * @param side The chosen side
     */

    public void chooseStarterCardSide(SideType side) {
        playArea.setStarterCard(this.starterCard, side);
        this.choseStarterCardSide = true;
    }

    /**
     * Evaluates the total amount of points gathered by completing a common objective card
     * (links the Game and PlayArea objects).
     * @param commonObjective The objective card to evaluate
     * @return The earned points from said objective
     */

    public int evaluateCommonObjective(ObjectiveCard commonObjective) {
        return commonObjective.evaluatePoints(this.playArea);
    }

    /**
     * Evaluates the total amount of points gathered by completing the player's personal
     * objective (links the Game and PlayArea objects).
     * @return The earned points from said objective
     */

    public int evaluatePersonalObjective() {
        return this.personalObjective.evaluatePoints(this.playArea);
    }

    /**
     * Sets the player's in-game color.
     * @param color The chosen color
     */

    public void setColor(PlayerColor color){
        if(this.color == null){
            this.color = color;
        }
    }


}
