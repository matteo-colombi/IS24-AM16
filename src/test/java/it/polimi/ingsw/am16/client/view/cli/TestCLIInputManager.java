package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.server.lobby.LobbyManager;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class TestCLIInputManager {
    @Test
    void testCliInputManager() throws InterruptedException {
        CLI cli = new CLI();
        LobbyManager lobbyManager = new LobbyManager();
        String id = lobbyManager.createGame(2);
        CLIInputManager inputManager = new CLIInputManager(cli, lobbyManager.getGame(id));
        Thread t = new Thread(inputManager);
        t.start();
        InputStream testInput = new ByteArrayInputStream(
                ("help\njoin_game matteo test\njoin_game andrea test\n").getBytes());
        System.setIn(testInput);
        t.join();
    }
}
