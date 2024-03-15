package it.polimi.ingsw.am16.common.model.players;

import it.polimi.ingsw.am16.common.model.PlayAreaModel;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.model.players.hand.HandModel;

public interface PlayerModel {
    PlayerColor getPlayerColor();
    int getPlayerId();
    int getTotalPoints();
    int getGamePoints();
    int getObjectivePoints();
    HandModel getHand();
    ObjectiveCard getPersonalObjective();
    PlayAreaModel getPlayArea();
    ObjectiveCard[] getPersonalObjectiveOptions();
}
