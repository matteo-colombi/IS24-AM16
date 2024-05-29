package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.StarterCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to inform the client that the player has to choose the side on which to play their starter card.
 */
public class PromptStarterChoice extends Payload {
    private final StarterCard starterCard;

    /**
     * @param starterCard The starter card that was assigned to the player.
     */
    @JsonCreator
    public PromptStarterChoice(@JsonProperty("starterCard") StarterCard starterCard) {
        this.starterCard = starterCard;
    }

    /**
     * @return The starter card that was assigned to the player.
     */
    public StarterCard getStarterCard() {
        return starterCard;
    }
}
