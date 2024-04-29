package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;

import java.util.List;

public class PromptColorChoice extends Payload {
    private final List<PlayerColor> colorChoices;

    public PromptColorChoice(List<PlayerColor> colorChoices) {
        this.colorChoices = colorChoices;
    }

}
