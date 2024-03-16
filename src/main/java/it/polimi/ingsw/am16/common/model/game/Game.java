package it.polimi.ingsw.am16.common.model.game;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.cards.decks.*;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.PlayerModel;
import it.polimi.ingsw.am16.common.model.players.hand.Hand;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.util.RNG;

import java.util.ArrayList;
import java.util.List;

public class Game implements GameModel {
    private final String id;
    private final int numPlayers;
    private int activePlayer;
    private int startingPlayer;
    private List<Integer> winnerIds;
    private boolean isEndGame;
    private ObjectiveCardsDeck objectiveCardsDeck;
    private StarterCardsDeck starterCardsDeck;
    private GoldCardsDeck goldCardsDeck;
    private ResourceCardsDeck resourceCardsDeck;
    private ObjectiveCard[] commonObjectiveCards;
    private GoldCard[] commonGoldCards;
    private ResourceCard[] commonResourceCards;
    private GameState state;
    private Player[] players;
    private int currentPlayerCount;


    public Game(String id, int numPlayers) {
        this.id = id;
        this.numPlayers = numPlayers;
        this.startingPlayer = -1;
        this.activePlayer = -1;
        this.winnerIds = new ArrayList<>();
        this.isEndGame = false;
        this.objectiveCardsDeck = DeckFactory.getObjectiveCardsDeck();
        this.starterCardsDeck = DeckFactory.getStarterCardsDeck();
        this.goldCardsDeck = DeckFactory.getGoldCardsDeck();
        this.resourceCardsDeck = DeckFactory.getResourceCardsDeck();
        this.commonObjectiveCards = new ObjectiveCard[2];
        this.commonGoldCards = new GoldCard[2];
        this.commonResourceCards = new ResourceCard[2];
        this.state = GameState.INIT;
        this.players = new Player[numPlayers];
        this.currentPlayerCount = 0;
    }

    @Override
    public String getid() {
        return id;
    }

    @Override
    public void addPlayer(String username) {
        Player player = new Player(getCurrentPlayerCount(), username);
        players[getCurrentPlayerCount()] = player;
        setCurrentPlayerCount(getCurrentPlayerCount() + 1);
    }
    @Override
    public int getNumPlayers() {
        return numPlayers;
    }

    public int getCurrentPlayerCount(){
        return currentPlayerCount;
    }
    public void setCurrentPlayerCount(int currentPlayerCount){
        this.currentPlayerCount = currentPlayerCount;
    }
    @Override
    public int getActivePlayer() {
        return activePlayer;
    }

    @Override
    public int getStartingPlayer() {
        return startingPlayer;
    }
    @Override
    public List<Integer> getWinnerIds() {
        return winnerIds;
    }

    @Override
    public boolean getIsEndGame() {
        return isEndGame;
    }

    // TODO
    @Override
    public void initializeGame() {

    }

    @Override
    public void drawCommonCards() {
        commonResourceCards[0] = resourceCardsDeck.drawCard();
        commonResourceCards[1] = resourceCardsDeck.drawCard();
        commonGoldCards[0] = goldCardsDeck.drawCard();
        commonGoldCards[1] = goldCardsDeck.drawCard();
    }



    @Override
    public void drawStarterCards() {
        for (int i = 0; i < numPlayers; i++) {
           players[i].setStarterCard(starterCardsDeck.drawCard());
        }
    }

    @Override
    public void setPlayerStarterSide(int playerId, SideType side) {
        for(int i = 0; i < numPlayers; i++) {
            if(players[i].getPlayerId() == playerId) {
                players[i].chooseStarterCardSide(side);
            }
        }
    }


    @Override
    public void setPlayerColor(int playerId, PlayerColor color) {
        for(int i = 0; i < numPlayers; i++) {
            if(players[i].getPlayerId() == playerId) {
                players[i].setColor(color);
            }
        }
    }


    @Override
    public boolean allPlayersChoseStarterSide() {
        for(int i = 0; i < numPlayers; i++){
            if(!players[i].getChoseStarterCardSide()) {
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean allPlayersChoseColor() {
        for(int i = 0; i < numPlayers; i++){
            if(!players[i].getChoseColor()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void distributeCards() {
        for(int i = 0; i < numPlayers; i++) {
            players[i].giveCard(resourceCardsDeck.drawCard());
            players[i].giveCard(resourceCardsDeck.drawCard());
            players[i].giveCard(goldCardsDeck.drawCard());
        }
    }

    @Override
    public void drawCommonObjectives() {
        commonObjectiveCards[0] = objectiveCardsDeck.drawCard();
        commonObjectiveCards[1] = objectiveCardsDeck.drawCard();
    }

    @Override
    public void distributePersonalObjectives() {
        for(int i = 0; i < numPlayers; i++) {
            players[i].giveObjectiveOptions(objectiveCardsDeck.drawCard(), objectiveCardsDeck.drawCard());
        }
    }

    @Override
    public void setPlayerObjective(int playerId, ObjectiveCard objectiveCard) throws UnknownObjectiveCardException {
        for(int i = 0; i < numPlayers; i++) {
            if(players[i].getPlayerId() == playerId) {
                players[i].setObjectiveCard(objectiveCard);
            }
        }
        //TODO how do we catch that exception?
    }



    @Override
    public boolean allPlayersChoseObjective() {
        for(int i = 0; i < numPlayers; i++){
            if(!players[i].getChosePersonalObjective()) {
                return false;
            }
        }
        return true;
    }

    // TODO
    @Override
    public void startGame() {

    }

    @Override
    public int chooseStartingPlayer() {
        return RNG.getRNG().nextInt(numPlayers);
    }

    @Override
    public void placeCard(int playerId, PlayableCard placedCard, SideType side, Position newCardPos) throws IllegalMoveException {
        for(int i = 0; i < numPlayers; i++) {
            if(players[i].getPlayerId() == playerId) {
                players[i].playCard(placedCard, side, newCardPos);
            }
        }
    }


    @Override
    public void drawCard(int playerId, DrawType drawType) {
        for(int i = 0; i < numPlayers; i++) {
            if(players[i].getPlayerId() == playerId) {
                switch (drawType) {
                    case GOLD_1 -> {
                        players[i].giveCard(commonGoldCards[0]);
                        commonGoldCards[0] = goldCardsDeck.drawCard();
                    }
                    case GOLD_2 -> {
                        players[i].giveCard(commonGoldCards[1]);
                        commonGoldCards[1] = goldCardsDeck.drawCard();
                    }
                    case GOLD_DECK -> {
                        players[i].giveCard(goldCardsDeck.drawCard());
                    }
                    case RESOURCE_1 -> {
                        players[i].giveCard(commonResourceCards[0]);
                        commonResourceCards[0] = resourceCardsDeck.drawCard();
                    }
                    case RESOURCE_2 -> {
                        players[i].giveCard(commonResourceCards[1]);
                        commonResourceCards[1] = resourceCardsDeck.drawCard();
                    }
                    case RESOURCE_DECK -> {
                        players[i].giveCard(resourceCardsDeck.drawCard());
                    }
                }
            }
        }
    }

    @Override
    public boolean checkEndGame() {
        for(int i = 0; i < numPlayers; i++){
            if(players[i].getGamePoints() >= 20 || (resourceCardsDeck.isEmpty() && goldCardsDeck.isEmpty())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void evaluateObjectivePoints() {
        for(int i = 0; i < numPlayers; i++) {
            players[i].evaluateCommonObjective(getCommonObjectiveCards()[0]);
            players[i].evaluateCommonObjective(getCommonObjectiveCards()[0]);
            players[i].evaluatePersonalObjective();
        }
    }

    //TODO
    @Override
    public void endGame() {

    }

    @Override
    public void selectWinners() {
        int tmpPoints = 0;
        int tmpId = -1;
        int tmpObjPoints = 0;
        List<Integer> tmpWinners = new ArrayList<>();
        for(int i = 0; i < numPlayers; i++) {
            if(players[i].getTotalPoints() > tmpPoints) {
                tmpPoints = players[i].getTotalPoints();
                tmpId = players[i].getPlayerId();
            }
        }
        tmpWinners.add(tmpId);
        for(int j = 0; j < numPlayers; j++){
            if(players[j].getTotalPoints() == tmpPoints && players[j].getPlayerId() != tmpId) {
                tmpWinners.add(players[j].getPlayerId());
            }
        }
        if(tmpWinners.size() == 1) {
            winnerIds.add(tmpId);
            return;
        }
        if(tmpWinners.size() > 1) {
            for(int k = 0; k < numPlayers; k++) {
                if(players[k].getObjectivePoints() > tmpObjPoints && tmpWinners.contains(players[k].getPlayerId())){
                    tmpObjPoints = players[k].getObjectivePoints();
                    tmpId = players[k].getPlayerId();
                }
            }
        }
        winnerIds.add(tmpId);
        for(int l = 0; l < numPlayers; l++) {
            if(players[l].getObjectivePoints() == tmpObjPoints && players[l].getPlayerId() != tmpId && tmpWinners.contains(players[l].getPlayerId())) {
                winnerIds.add(players[l].getPlayerId());
            }
        }
    }


    @Override
    public PlayerModel[] getPlayers() {
        return players;
    }

    @Override
    public ObjectiveCard[] getCommonObjectiveCards() {
        ObjectiveCard[] tmp = commonObjectiveCards.clone();
        return tmp;
    }

    @Override
    public GoldCard[] getCommonGoldCards() {
        GoldCard[] tmp = commonGoldCards.clone();
        return tmp;
    }

    @Override
    public ResourceCard[] getCommonResourceCards() {
        ResourceCard[] tmp = commonResourceCards.clone();
        return tmp;
    }



}

