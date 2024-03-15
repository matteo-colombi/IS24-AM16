package it.polimi.ingsw.am16.common.model.game;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
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


    // TODO Not yet
    @Override
    public void drawStarterCards() {
        for (int i = 0; i < numPlayers; i++) {
           // players[i].giveStarterCard(starterCardsDeck.drawCard());
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

    // TODO Not yet
    @Override
    public void setPlayerColor(int playerId, PlayerColor color) {

    }

    // TODO Not yet
    @Override
    public boolean allPlayersChoseStarterSide() {
        return false;
    }

    // TODO Not yet
    @Override
    public boolean allPlayersChoseColor() {
        return false;
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
    public void setPlayerObjective(int playerId, ObjectiveCard objectiveCard) {
        for(int i = 0; i < numPlayers; i++) {
            if(players[i].getPlayerId() == playerId) {
                players[i].setObjectiveCard(objectiveCard);
            }
        }
    }


    // TODO Not yet
    @Override
    public boolean allPlayersChoseObjective() {
        return false;
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

    // TODO asap
    @Override
    public void evaluateObjectivePoints() {

    }

    //TODO
    @Override
    public void endGame() {

    }

    // TODO Draw case
    @Override
    public int selectWinner() {
        int tmpPoints = 0;
        int tmpId = -1;
        for(int i = 0; i < numPlayers; i++) {
            if(players[i].getTotalPoints() > tmpPoints) {
                tmpPoints = players[i].getTotalPoints();
                tmpId = players[i].getPlayerId();
            }
        }
        return tmpId;
    }


    @Override
    public PlayerModel[] getPlayers() {
        return players;
    }

    @Override
    public ObjectiveCard[] getCommonObjectiveCards() {
        return commonObjectiveCards;
    }

    @Override
    public GoldCard[] getCommonGoldCards() {
        return commonGoldCards;
    }

    @Override
    public ResourceCard[] getCommonResourceCards() {
        return commonResourceCards;
    }



}

