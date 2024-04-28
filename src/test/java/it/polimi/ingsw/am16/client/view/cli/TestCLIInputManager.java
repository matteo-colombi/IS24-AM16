package it.polimi.ingsw.am16.client.view.cli;

import it.polimi.ingsw.am16.common.util.RNG;
import it.polimi.ingsw.am16.server.lobby.LobbyManager;
import org.junit.jupiter.api.Test;

import java.io.*;

public class TestCLIInputManager {
    private static final int SLEEP = 1000;

    @Test
    void testCliInputManager() throws InterruptedException, IOException {
        RNG.setRNGSeed(420);
        CLI cli1 = new CLI();
        CLI cli2 = new CLI();
        LobbyManager lobbyManager = new LobbyManager();
        String id = lobbyManager.createGame(2);

        PipedInputStream input1 = new PipedInputStream();
        PipedOutputStream outputTo1 = new PipedOutputStream();
        input1.connect(outputTo1);

        PipedInputStream input2 = new PipedInputStream();
        PipedOutputStream outputTo2 = new PipedOutputStream();
        input2.connect(outputTo2);

        CLIInputManager inputManager1 = new CLIInputManager(cli1, input1, lobbyManager.getGame(id));
        CLIInputManager inputManager2 = new CLIInputManager(cli2, input2, lobbyManager.getGame(id));
        Thread t1 = new Thread(inputManager1);
        Thread t2 = new Thread(inputManager2);
        t1.start();
        t2.start();

        outputTo1.write("help\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo1.write(("join_game matteo " + id + "\n").getBytes());
        Thread.sleep(SLEEP);
        outputTo1.write("players\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo2.write("players\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo2.write(("join_game andrea " + id + "\n").getBytes());
        Thread.sleep(SLEEP);
        outputTo1.write("starter\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo1.write("starter front\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo2.write("help\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo2.write("starter front\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo1.write("color green\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo2.write("color green\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo2.write("color red\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo1.write("players\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo1.write("objective 1\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo2.write("objective 2\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo1.write("draw_options\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo2.write("play_area matteo\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo2.write("play_area\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo1.write("hand\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo1.write("hand andrea\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo2.write("play_area PepperOne\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo2.write("hand afjwkjanw\n".getBytes());
        Thread.sleep(SLEEP);
        outputTo1.write("color\n".getBytes());


        t1.join();
        t2.join();
        input1.close();
        input2.close();
        outputTo1.close();
        outputTo2.close();
    }
}
