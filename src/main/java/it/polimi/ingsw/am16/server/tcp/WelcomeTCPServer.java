package it.polimi.ingsw.am16.server.tcp;

import it.polimi.ingsw.am16.server.lobby.LobbyManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * DOCME
 */
public class WelcomeTCPServer implements Runnable {
    private final int port;
    private final LobbyManager lobbyManager;

    public WelcomeTCPServer(int port, LobbyManager lobbyManager) {
        this.port = port;
        this.lobbyManager = lobbyManager;
    }

    @Override
    public void run() {
        startServer();
    }

    public void startServer() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("TCP server ready");
            while (true) {
                try {
                    final Socket socket = serverSocket.accept();
                    new Thread(new TCPClientHandler(socket, lobbyManager)).start();
                } catch (IOException e) {
                    System.err.println("TCP server shutting down: " + e.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
        }
    }
}
