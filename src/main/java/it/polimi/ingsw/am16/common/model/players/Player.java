package it.polimi.ingsw.am16.common.model.players;

import it.polimi.ingsw.am16.common.model.PlayArea;
import it.polimi.ingsw.am16.common.model.PlayAreaModel;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.cards.StarterCard;
import it.polimi.ingsw.am16.common.model.players.hand.Hand;
import it.polimi.ingsw.am16.common.model.players.hand.HandModel;
import it.polimi.ingsw.am16.common.util.Position;

public class Player {
    private int playerId;
    private String username;
    private int currGamePoints;
    private int currObjectivePoints;
    private ObjectiveCard personalObjective;
    private ObjectiveCard[] possiblePersonalObjectives;
    private StarterCard starterCard;
    private PlayerColor color;
    private Hand hand;
    private PlayArea playArea;

    public Player(int playerId, String username) {
        this.playerId = playerId;
        this.username = username;
        this.currGamePoints = 0;
        this.currObjectivePoints = 0;
        this.possiblePersonalObjectives = new ObjectiveCard[2];
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
        try{
            this.hand.getCards().remove(card);
            return true;
        } catch(Exception e){
            return false;
        }
    }

    public void playCard(PlayableCard card, Position newCardPos){
        //TODO (quando arriva playarea)
    }

    public void giveObjectiveOptions(ObjectiveCard firstOption, ObjectiveCard secondOption){
        this.possiblePersonalObjectives[0] = firstOption;
        this.possiblePersonalObjectives[1] = secondOption;
    }

    public void giveStarterCard(StarterCard starterCard){
        this.starterCard = starterCard;
    }

    public void setObjectiveCard(ObjectiveCard objectiveCard){
        this.personalObjective = objectiveCard;
    }

    public void chooseStarterCardSide(SideType side){
        if(side == SideType.FRONT){
            //TODO (quando arriva playarea)
        } else {
            //TODO (quando arriva playarea)
        }
    }

    public int evaluateCommonObjective(ObjectiveCard commonObjective){
        //TODO (quando arrivano le carte obiettivo)
        return 0; //band aid
    }

    public int evaluatePersonalObjective(ObjectiveCard commonObjective){
        //TODO (quando arrivano le carte obiettivo)
        return 0; //band aid
    }




}
