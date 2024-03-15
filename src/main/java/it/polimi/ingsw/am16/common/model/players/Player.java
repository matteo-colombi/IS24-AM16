package it.polimi.ingsw.am16.common.model.players;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.model.PlayArea;
import it.polimi.ingsw.am16.common.model.PlayAreaModel;
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
public class Player {
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

    public int getPlayerId() {
        return playerId;
    }

    public int getGamePoints() {
        return currGamePoints;
    }

    public int getObjectivePoints() {
        return currObjectivePoints;
    }

    public int getTotalPoints(){
        return currGamePoints + currObjectivePoints;
    }

    public ObjectiveCard getPersonalObjective() {
        return personalObjective;
    }

    public PlayerColor getColor() {
        return color;
    }

    public HandModel getHand() {
        return hand;
    }

    public PlayArea getPlayArea() {
        return playArea;
    }

    public ObjectiveCard[] getPossiblePersonalObjectives() {
        return possiblePersonalObjectives;
    }

    public void addGamePoints(int points){
        currGamePoints += points;
    }

    public void addObjectivePoints(int points){
        currObjectivePoints += points;
    }

    public void giveCard(PlayableCard card){
        this.hand.getCards().add(card);
    }

    public boolean removeCard(PlayableCard card){
        return this.hand.getCards().remove(card);
    }

    public void playCard(PlayableCard card, Position newCardPos, SideType side) throws IllegalMoveException {
        if(playArea.checkLegalMove(card, newCardPos, side)){
            this.playArea.playCard(card, newCardPos, side);
        } else {
            throw new IllegalMoveException("Illegal move");
        }

    }

    public void giveObjectiveOptions(ObjectiveCard firstOption, ObjectiveCard secondOption){
        this.possiblePersonalObjectives[0] = firstOption;
        this.possiblePersonalObjectives[1] = secondOption;
    }

    public void setStarterCard(StarterCard starterCard){
        this.starterCard = starterCard;
    }

    public void setObjectiveCard(ObjectiveCard objectiveCard){
        this.personalObjective = objectiveCard;
    }

    public void chooseStarterCardSide(SideType side){
        playArea.setStarterCard(this.starterCard, side);
    }

    public int evaluateCommonObjective(ObjectiveCard commonObjective){
        return commonObjective.evaluatePoints(this.playArea);
    }

    public int evaluatePersonalObjective(){
        return this.personalObjective.evaluatePoints(this.playArea);
    }




}
