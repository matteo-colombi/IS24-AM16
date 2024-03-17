package it.polimi.ingsw.am16.common.model.game;

import it.polimi.ingsw.am16.common.exceptions.IllegalMoveException;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.*;
import it.polimi.ingsw.am16.common.model.cards.decks.*;
import it.polimi.ingsw.am16.common.model.players.Player;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.PlayerModel;
import it.polimi.ingsw.am16.common.util.Position;
import it.polimi.ingsw.am16.common.util.RNG;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle game(s). A game has a unique alphanumeric id and a non-variable number of players.
 */
public class Game implements GameModel {
    private final String id;
    private final int numPlayers;
    private int currentPlayerCount;
    private int activePlayer;
    private int startingPlayer;
    private final List<Integer> winnerIds;
    private final ObjectiveCardsDeck objectiveCardsDeck;
    private final StarterCardsDeck starterCardsDeck;
    private final GoldCardsDeck goldCardsDeck;
    private final ResourceCardsDeck resourceCardsDeck;
    private final ObjectiveCard[] commonObjectiveCards;
    private final GoldCard[] commonGoldCards;
    private final ResourceCard[] commonResourceCards;
    private GameState state;
    private final Player[] players;

    /**
     * Creates a game, initializing its ID as well as its number of players to a chosen value, its
     * other attributes are set to standard values.
     * @param id The game's ID.
     * @param numPlayers The number of players expected inside the game.
     */
    public Game(String id, int numPlayers) {
        this.id = id;
        this.numPlayers = numPlayers;
        this.startingPlayer = -1;
        this.activePlayer = -1;
        this.winnerIds = new ArrayList<>();
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

    /**
     *
     * @return The game's ID.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Adds a new player into the game. The number of players cannot exceed numPlayers.
     * @param username The player's username.
     * @throws UnexpectedActionException TODO write doc
     */
    @Override
    public void addPlayer(String username) throws UnexpectedActionException {
        if (getCurrentPlayerCount() >= getNumPlayers())
            throw new UnexpectedActionException("Maximum player count reached");
        if (state != GameState.INIT)
            throw new UnexpectedActionException("Game already started");

        Player player = new Player(getCurrentPlayerCount(), username);
        players[getCurrentPlayerCount()] = player;
        setCurrentPlayerCount(getCurrentPlayerCount() + 1); //FIXME for real?
    }

    /**
     *
     * @return The non-variable number of players expected to play the game.
     */
    @Override
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     *
     * @return The number of players who joined the game.
     */
    @Override
    public int getCurrentPlayerCount(){
        return currentPlayerCount;
    }

    /**
     * Updates the number of players who joined the game.
     * @param currentPlayerCount The number of players who joined the game.
     */
    private void setCurrentPlayerCount(int currentPlayerCount){
        this.currentPlayerCount = currentPlayerCount;
    }

    /**
     *
     * @return The id of the player who has to finish their turn.
     */
    @Override
    public int getActivePlayer() {
        return activePlayer;
    }

    /**
     *
     * @return The id of the player whose turn is the first.
     */
    @Override
    public int getStartingPlayer() {
        return startingPlayer;
    }

    /**
     *
     * @return The id(s) of the player(s) who won.
     */
    @Override
    public List<Integer> getWinnerIds() {
        return winnerIds;
    }

    /**
     * TODO write doc
     */
    @Override
    public void initializeGame() throws UnexpectedActionException {
        if (state != GameState.INIT)
            throw new UnexpectedActionException("Game already started");
        if (currentPlayerCount != numPlayers)
            throw new UnexpectedActionException("Missing players");
        drawCommonCards();
        drawStarterCards();
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
        for (int i = 0; i < numPlayers; i++) {
           players[i].setStarterCard(starterCardsDeck.drawCard());
        }
    }

    /**
     * Lets the player choose the side of their starter card. It can be either front or back.
     * @param playerId The player's ID.
     * @param side The card's side.
     */
    @Override
    public void setPlayerStarterSide(int playerId, SideType side) {
        players[playerId].chooseStarterCardSide(side);
    }

    /**
     * Sets the color of a player.
     * @param playerId The player's ID.
     * @param color The color a player chose.
     */
    @Override
    public void setPlayerColor(int playerId, PlayerColor color) {
        players[playerId].setColor(color);
    }

    /**
     *
     * @return Whether all the players have chosen the side of their starter card.
     */
    @Override
    public boolean allPlayersChoseStarterSide() {
        for(int i = 0; i < numPlayers; i++){
            if(!players[i].getChoseStarterCardSide()) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return Whether all the players have chosen their color.
     */
    @Override
    public boolean allPlayersChoseColor() {
        for(int i = 0; i < numPlayers; i++){
            if(!players[i].getChoseColor()) {
                return false;
            }
        }
        return true;
    }

    /**
     * TODO write doc
     */
    @Override
    public void initializeObjectives() {
        drawCommonObjectives();
        distributePersonalObjectives();
    }

    /**
     * Distributes two resource cards and a gold card so that the game can start.
     */
    private void distributeCards() {
        for(int i = 0; i < numPlayers; i++) {
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
        for(int i = 0; i < numPlayers; i++) {
            players[i].giveObjectiveOptions(objectiveCardsDeck.drawCard(), objectiveCardsDeck.drawCard());
        }
    }

    /**
     * Sets the chosen objective card for a specific player.
     * @param playerId The player's ID.
     * @param objectiveCard The chosen objective card.
     * @throws UnknownObjectiveCardException Thrown when the objective card is unknown.
     */
    @Override
    public void setPlayerObjective(int playerId, ObjectiveCard objectiveCard) throws UnknownObjectiveCardException {
        players[playerId].setObjectiveCard(objectiveCard);
    }


    /**
     *
     * @return Whether all the players have chosen their personal objective.
     */
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
        startingPlayer = chooseStartingPlayer();
        activePlayer = startingPlayer;
        state = GameState.STARTED;
        distributeCards();
    }

    /**
     * Chooses the starting player randomly.
     * @return The starting player's ID.
     */
    private int chooseStartingPlayer() {
        return RNG.getRNG().nextInt(numPlayers);
    }

    /**
     * Lets a player place a card.
     * @param playerId The player's ID.
     * @param placedCard The card the player wants to place.
     * @param side The chosen card's side.
     * @param newCardPos The position of the card.
     * @throws IllegalMoveException Thrown if the player made an illegal move.
     * @throws UnexpectedActionException TODO write doc
     */
    @Override
    public void placeCard(int playerId, PlayableCard placedCard, SideType side, Position newCardPos) throws IllegalMoveException, UnexpectedActionException {
        if (state != GameState.STARTED) throw new UnexpectedActionException("Game not started");
        players[playerId].playCard(placedCard, side, newCardPos);
    }

    /**
     * Lets the player draw a card. A card can be drawn from the deck or from the currently visible cards.
     * @param playerId The player's ID.
     * @param drawType The place a player wants to draw a card from.
     */
    @Override
    public void drawCard(int playerId, DrawType drawType) throws UnexpectedActionException {
        if (state != GameState.STARTED) throw new UnexpectedActionException("Game not started");
        switch (drawType) {
            case GOLD_1 -> {
                players[playerId].giveCard(commonGoldCards[0]);
                commonGoldCards[0] = goldCardsDeck.drawCard();
            }
            case GOLD_2 -> {
                players[playerId].giveCard(commonGoldCards[1]);
                commonGoldCards[1] = goldCardsDeck.drawCard();
            }
            case GOLD_DECK -> {
                players[playerId].giveCard(goldCardsDeck.drawCard());
            }
            case RESOURCE_1 -> {
                players[playerId].giveCard(commonResourceCards[0]);
                commonResourceCards[0] = resourceCardsDeck.drawCard();
            }
            case RESOURCE_2 -> {
                players[playerId].giveCard(commonResourceCards[1]);
                commonResourceCards[1] = resourceCardsDeck.drawCard();
            }
            case RESOURCE_DECK -> {
                players[playerId].giveCard(resourceCardsDeck.drawCard());
            }
        }
    }

    @Override
    public void advanceTurn() throws UnexpectedActionException {
        if (state != GameState.STARTED && state != GameState.FINAL_ROUND)
            throw new UnexpectedActionException("Game has not started or has already ended");

        activePlayer++;
        activePlayer %= numPlayers;
    }

    /**
     *
     * @return Whether the game switched to the "End Game" state.
     */
    @Override
    public boolean checkFinalRound() {
        if (resourceCardsDeck.isEmpty() && goldCardsDeck.isEmpty())
            return true;

        for(int i = 0; i < numPlayers; i++){
            if(players[i].getGamePoints() >= 20) {
                return true;
            }
        }
        return false;
    }

    /**
     * Evaluates every player's points earned by fulfilling the objective cards' (both common and personal) requests.
     */
    private void evaluateObjectivePoints() {
        for(int i = 0; i < numPlayers; i++) {
            players[i].evaluateCommonObjective(getCommonObjectiveCards()[0]);
            players[i].evaluateCommonObjective(getCommonObjectiveCards()[1]);
            players[i].evaluatePersonalObjective();
        }
    }

    /**
     * TODO write doc
     */
    @Override
    public void triggerFinalRound() throws UnexpectedActionException {
        if (state != GameState.STARTED) throw new UnexpectedActionException("Game has not started or already ended");
        if (!checkFinalRound()) return;

        state = GameState.FINAL_ROUND;
    }

    /**
     * TODO write doc
     * @throws UnexpectedActionException
     */
    @Override
    public void endGame() throws UnexpectedActionException {
        if (state != GameState.FINAL_ROUND) throw new UnexpectedActionException("Final round not triggered yet");
        state = GameState.ENDED;
        evaluateObjectivePoints();
        selectWinners();
    }

    /**
     * Chooses the winner(s) of the game.
     */
    private void selectWinners() {
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

    /**
     *
     * @return The players inside the game.
     */
    @Override
    public PlayerModel[] getPlayers() {
        return players;
    }

    /**
     *
     * @return The common objective cards.
     */
    @Override
    public ObjectiveCard[] getCommonObjectiveCards() {
        return commonObjectiveCards.clone();
    }

    /**
     *
     * @return The visible and drawable gold cards.
     */
    @Override
    public GoldCard[] getCommonGoldCards() {
        return commonGoldCards.clone();
    }

    /**
     *
     * @return The visible and drawable resource cards.
     */
    @Override
    public ResourceCard[] getCommonResourceCards() {
        return commonResourceCards.clone();
    }

    /**
     * TODO write doc
     * @return
     */
    @Override
    public GameState getState() {
        return state;
    }
}

