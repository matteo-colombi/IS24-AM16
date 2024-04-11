package it.polimi.ingsw.am16.common.view;

import it.polimi.ingsw.am16.common.model.cards.GoldCard;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.cards.ResourceCard;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import it.polimi.ingsw.am16.common.model.game.GameState;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.model.players.PlayerModel;
import it.polimi.ingsw.am16.common.model.players.RestrictedPlayerModel;

import java.util.List;

/**
 * DOCME
 */
public interface GameView {

    String getUsername();

    int getPlayerId();

    void setGameId(String gameId);

    void setAvailableColors(List<PlayerColor> availableColors);

    void showJoinFailure();

    void setNumPlayers(int numPlayers);

    void setCurrNumPlayers(int currNumPlayers);

    void setPlayer(PlayerModel players);

    void setOtherPlayers(RestrictedPlayerModel[] otherPlayers);

    void setGameState(GameState state);

    void setStartingPlayer(int startingPlayer);

    void setCurrentPlayer(int currentPlayer);

    void setWinnerIds(List<Integer> winnerIds);

    void setCommonObjectiveCards(ObjectiveCard[] commonObjectiveCards);

    void setCommonResourceCards(ResourceCard[] commonResourceCards);

    void setCommonGoldCards(GoldCard[] commonGoldCards);

    void setResourceDeckTopType(ResourceType resourceDeckTopType);

    void setGoldDeckTopType(ResourceType goldDeckTopType);

    void promptChooseColor();

}
