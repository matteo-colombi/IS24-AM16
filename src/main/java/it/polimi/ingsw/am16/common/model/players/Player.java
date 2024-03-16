package it.polimi.ingsw.am16.common.model.players;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.cards.StarterCard;
import it.polimi.ingsw.am16.common.model.players.hand.Hand;
import it.polimi.ingsw.am16.common.model.players.hand.HandModel;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle players in a game. A player has a unique id for identification and a username. <br>
 *
 */
public class Player implements PlayerModel {
    private final int playerId;
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
        this.possiblePersonalObjectives = new ArrayList<>();
        this.hand = new Hand();
        this.playArea = new PlayArea(this);
        this.color = null;
        this.choseObjectiveCard = false;
        this.choseStarterCardSide = false;
        this.choseColor = false;
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
    public List<ObjectiveCard> getPersonalObjectiveOptions() {
        return new ArrayList<>(possiblePersonalObjectives);
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

    public boolean getChoseColor() {
        return choseColor;
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
        this.possiblePersonalObjectives.add(firstOption);
        this.possiblePersonalObjectives.add(secondOption);
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
     * been made (links the {@link it.polimi.ingsw.am16.common.model.game.Game} and {@link PlayArea} objects).
     * @param side The chosen side
     */

    public void chooseStarterCardSide(SideType side) {
        playArea.setStarterCard(this.starterCard, side);
        this.choseStarterCardSide = true;
    }

    /**
     * Evaluates the total amount of points gathered by completing a common objective card
     * (links the {@link it.polimi.ingsw.am16.common.model.game.Game} and {@link PlayArea} objects).
     * @param commonObjective The objective card to evaluate
     * @return The earned points from said objective
     */

    public void evaluateCommonObjective(ObjectiveCard commonObjective) {
        this.currObjectivePoints += commonObjective.evaluatePoints(this.playArea);
    }

    /**
     * Evaluates the total amount of points gathered by completing the player's personal
     * objective (links the {@link it.polimi.ingsw.am16.common.model.game.Game} and {@link PlayArea} objects).
     * @return The earned points from said objective
     */

    public void evaluatePersonalObjective() {
        this.currObjectivePoints +=  this.personalObjective.evaluatePoints(this.playArea);
    }

    /**
     * Sets the player's in-game color.
     * @param color The chosen color
     */

    public void setColor(PlayerColor color){
        if(this.color == null){
            this.color = color;
            this.choseColor = true;
        }
    }


    /**
     * Checks an object is equal to the player by comparing their IDs (if the parameter object is also a player).
     * @param o The object to compare the player to
     * @return true if the two are equal, false if they aren't
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return playerId == player.playerId;
    }

    /**
     *
     * @return the ID of the player as its hash code
     */

    @Override
    public int hashCode() {
        return playerId;
    }
}
