package it.polimi.ingsw.am16.server;

import it.polimi.ingsw.am16.common.model.cards.CardRegistry;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;
import it.polimi.ingsw.am16.server.tcp.WelcomeTCPServer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestTCPServer {
    @Test
    void testTCPServer() {
        CardRegistry.getRegistry();
        LobbyManager lobbyManager = new LobbyManager();
        WelcomeTCPServer tcpServer = new WelcomeTCPServer(2345, lobbyManager);
        tcpServer.run();
    }
}
