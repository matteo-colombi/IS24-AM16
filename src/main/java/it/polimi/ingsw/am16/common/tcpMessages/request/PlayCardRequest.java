package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.Position;

public class PlayCardRequest extends Payload {
    private final PlayableCard playedCard;
    private final SideType side;
    private final Position pos;

    @JsonCreator
    public PlayCardRequest(@JsonProperty("playedCard") PlayableCard playedCard, @JsonProperty("side") SideType side, @JsonProperty("pos") Position pos) {
        this.playedCard = playedCard;
        this.side = side;
        this.pos = pos;
    }

    public PlayableCard getPlayedCard() {
        return playedCard;
    }

    public SideType getSide() {
        return side;
    }

    public Position getPos() {
        return pos;
    }
}
