package it.polimi.ingsw.am16.common.tcpMessages.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the client to inform the server that the player has chosen their personal objective.
 */
public class ChooseObjective extends Payload {
    private final ObjectiveCard objective;

    /**
     *
     * @param objective The chosen objective.
     */
    @JsonCreator
    public ChooseObjective(@JsonProperty("objective") ObjectiveCard objective) {
        this.objective = objective;
    }

    /**
     *
     * @return The chosen objective.
     */
    public ObjectiveCard getObjective() {
        return objective;
    }
}
