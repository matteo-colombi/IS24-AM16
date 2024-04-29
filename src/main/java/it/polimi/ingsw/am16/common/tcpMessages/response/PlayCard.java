package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.cards.BoardCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.Position;

public class PlayCard extends Payload {
    private final String username;
    private final BoardCard card;
    private final SideType side;
    private final Position pos;

    public PlayCard(String username, BoardCard card, SideType side, Position pos) {
        this.username = username;
        this.card = card;
        this.side = side;
        this.pos = pos;
    }
}
