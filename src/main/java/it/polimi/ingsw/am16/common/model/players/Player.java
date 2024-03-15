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
 */
public class Player implements PlayerModel {
    private final int playerId;
    private final String username;
    private int currGamePoints;
    private int currObjectivePoints;
    private ObjectiveCard personalObjective;
    private final ObjectiveCard[] possiblePersonalObjectives;
    private StarterCard starterCard;
    private PlayerColor color;
    private final Hand hand;
    private final PlayArea playArea;

    public Player(int playerId, String username) {
        this.playerId = playerId;
        this.username = username;
        this.currGamePoints = 0;
        this.currObjectivePoints = 0;
        this.possiblePersonalObjectives = new ObjectiveCard[2];
        this.hand = new Hand();
        this.playArea = new PlayArea(this);
    }

    @Override
    public PlayerColor getPlayerColor() {
        return color;
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }

    @Override
    public int getGamePoints() {
        return currGamePoints;
    }

    @Override
    public int getObjectivePoints() {
        return currObjectivePoints;
    }

    @Override
    public int getTotalPoints() {
        return currGamePoints + currObjectivePoints;
    }

    @Override
    public ObjectiveCard getPersonalObjective() {
        return personalObjective;
    }

    @Override
    public HandModel getHand() {
        return hand;
    }

    @Override
    public PlayArea getPlayArea() {
        return playArea;
    }

    @Override
    public ObjectiveCard[] getPersonalObjectiveOptions() {
        //FIXME make me more robust
        return new ObjectiveCard[]{possiblePersonalObjectives[0], possiblePersonalObjectives[1]};
    }

    public void addGamePoints(int points) {
        currGamePoints += points;
    }

    public void addObjectivePoints(int points) {
        currObjectivePoints += points;
    }

    public void giveCard(PlayableCard card) {
        this.hand.getCards().add(card);
    }

    public boolean removeCard(PlayableCard card) {
        return this.hand.getCards().remove(card);
    }

    public void playCard(PlayableCard card, SideType side, Position newCardPos) throws IllegalMoveException {
        this.playArea.playCard(card, side, newCardPos);
    }

    public void giveObjectiveOptions(ObjectiveCard firstOption, ObjectiveCard secondOption) {
        this.possiblePersonalObjectives[0] = firstOption;
        this.possiblePersonalObjectives[1] = secondOption;
    }

    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    public void setObjectiveCard(ObjectiveCard objectiveCard) {
        this.personalObjective = objectiveCard;
    }

    public void chooseStarterCardSide(SideType side) {
        playArea.setStarterCard(this.starterCard, side);
    }

    public int evaluateCommonObjective(ObjectiveCard commonObjective) {
        return commonObjective.evaluatePoints(this.playArea);
    }

    public int evaluatePersonalObjective() {
        return this.personalObjective.evaluatePoints(this.playArea);
    }


}
