package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.BoardCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.Position;

public class PlayCardResponse extends Payload {
    private final String username;
    private final BoardCard card;
    private final SideType side;
    private final Position pos;

    @JsonCreator
    public PlayCardResponse(@JsonProperty("username") String username, @JsonProperty("card") BoardCard card, @JsonProperty("side") SideType side, @JsonProperty("pos") Position pos) {
        this.username = username;
        this.card = card;
        this.side = side;
        this.pos = pos;
    }

    public String getUsername() {
        return username;
    }

    public BoardCard getCard() {
        return card;
    }

    public SideType getSide() {
        return side;
    }

    public Position getPos() {
        return pos;
    }
}
