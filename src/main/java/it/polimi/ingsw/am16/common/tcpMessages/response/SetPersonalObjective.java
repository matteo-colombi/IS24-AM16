package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to tell the client what the player's personal objective is.
 */
public class SetPersonalObjective extends Payload {
    private final ObjectiveCard personalObjective;

    /**
     * @param personalObjective The player's personal objective.
     */
    @JsonCreator
    public SetPersonalObjective(@JsonProperty("personalObjective") ObjectiveCard personalObjective) {
        this.personalObjective = personalObjective;
    }

    /**
     * @return The player's personal objective.
     */
    public ObjectiveCard getPersonalObjective() {
        return personalObjective;
    }
}
