package it.polimi.ingsw.am16.common.model.lobby;

import it.polimi.ingsw.am16.common.exceptions.NoStarterCardException;
import it.polimi.ingsw.am16.common.exceptions.UnexpectedActionException;
import it.polimi.ingsw.am16.common.exceptions.UnknownObjectiveCardException;
import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.model.game.GameModel;
import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TestLobby {

    @Test
    public void testLobby() throws UnexpectedActionException, NoStarterCardException, UnknownObjectiveCardException, IOException {
        CardRegistry.initializeRegistry();
        LobbyManager lobbyManager = new LobbyManager();
        String newId = lobbyManager.createLobby(2);
        Lobby lobby = lobbyManager.getLobby(newId);
        lobby.addPlayer("leonardo");
        lobby.addPlayer("andrea");
        GameModel game = lobby.getGame();
        game.initializeGame();
        game.setPlayerStarterSide(0, SideType.FRONT);
        game.setPlayerStarterSide(1, SideType.FRONT);
        game.setPlayerColor(0, PlayerColor.BLUE);
        game.setPlayerColor(1, PlayerColor.RED);
        game.initializeObjectives();
        game.setPlayerObjective(0, game.getPlayers()[0].getPersonalObjectiveOptions().getFirst());
        lobbyManager.saveLobbies("src/main/resources/testSaves");
    }
}
