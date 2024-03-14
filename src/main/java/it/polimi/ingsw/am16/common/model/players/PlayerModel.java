package it.polimi.ingsw.am16.common.model.players;

import it.polimi.ingsw.am16.common.model.PlayAreaModel;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.players.hand.HandModel;

public interface PlayerModel {
    public PlayerColor getPlayerColor();
    public int getPlayerId();
    public int getTotalPoints();
    public int getGamePoints();
    public int getObjectivePoints();
    public HandModel getHand();
    public ObjectiveCard getPersonalObjective();
    public PlayAreaModel getPlayArea();
}
