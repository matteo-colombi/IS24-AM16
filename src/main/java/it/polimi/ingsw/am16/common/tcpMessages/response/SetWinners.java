package it.polimi.ingsw.am16.common.tcpMessages.response;

import it.polimi.ingsw.am16.common.tcpMessages.Payload;

import java.util.List;

public class SetWinners extends Payload {
    private final List<String> winnerUsernames;

    public SetWinners(List<String> winnerUsernames) {
        this.winnerUsernames = winnerUsernames;
    }
}
