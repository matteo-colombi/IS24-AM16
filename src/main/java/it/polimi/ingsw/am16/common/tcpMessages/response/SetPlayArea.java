package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.cards.BoardCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.Position;

import java.util.List;
import java.util.Map;

public class SetPlayArea extends Payload {
    private final String username;
    private final List<Position> cardPlacementOrder;
    private final Map<Position, BoardCard> field;
    private final Map<BoardCard, SideType> activeSides;

    public SetPlayArea(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides) {
        this.username = username;
        this.cardPlacementOrder = cardPlacementOrder;
        this.field = field;
        this.activeSides = activeSides;
    }
}
