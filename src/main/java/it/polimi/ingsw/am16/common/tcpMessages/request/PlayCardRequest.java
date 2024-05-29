package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.PlayableCard;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.Position;

/**
 * Message sent by the client to the server to play a card.
 */
public class PlayCardRequest extends Payload {
    private final PlayableCard playedCard;
    private final SideType side;
    private final Position pos;

    /**
     *
     * @param playedCard The card that the player wants to play.
     * @param side The side on which the player wants to play the card.
     * @param pos The position on the field where the player wants to play the card.
     */
    @JsonCreator
    public PlayCardRequest(@JsonProperty("playedCard") PlayableCard playedCard, @JsonProperty("side") SideType side, @JsonProperty("pos") Position pos) {
        this.playedCard = playedCard;
        this.side = side;
        this.pos = pos;
    }

    /**
     *
     * @return The card that the player wants to play.
     */
    public PlayableCard getPlayedCard() {
        return playedCard;
    }

    /**
     *
     * @return The side on which the player wants to play the card.
     */
    public SideType getSide() {
        return side;
    }

    /**
     *
     * @return The position on the field where the player wants to play the card.
     */
    public Position getPos() {
        return pos;
    }
}
