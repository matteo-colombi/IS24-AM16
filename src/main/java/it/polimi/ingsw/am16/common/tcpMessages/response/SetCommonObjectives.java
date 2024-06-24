package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

/**
 * Message sent by the server to inform the client on what the common objectives for this game are.
 */
public class SetCommonObjectives extends Payload {
    private final ObjectiveCard[] commonObjectives;

    /**
     * @param commonObjectives The common objectives for this game.
     */
    @JsonCreator
    public SetCommonObjectives(@JsonProperty("commonObjectives") ObjectiveCard[] commonObjectives) {
        this.commonObjectives = commonObjectives;
    }

    /**
     * @return The common objectives for this game.
     */
    public ObjectiveCard[] getCommonObjectives() {
        return commonObjectives;
    }
}
