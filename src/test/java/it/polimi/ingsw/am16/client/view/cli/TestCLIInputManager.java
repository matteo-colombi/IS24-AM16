//package it.polimi.ingsw.am16.client.view.cli;
//
//import it.polimi.ingsw.am16.common.util.RNG;
//import it.polimi.ingsw.am16.server.controller.GameController;
//import it.polimi.ingsw.am16.server.lobby.LobbyManager;
//import org.junit.jupiter.api.Test;
//
//import java.io.*;
//
//public class TestCLIInputManager {
//    private static final int SLEEP = 2000;
//
//    @Test
//    void testCliInputManager() throws InterruptedException, IOException {
//        RNG.setRNGSeed(420);
//        CLI cli1 = new CLI();
//        CLI cli2 = new CLI();
////        CLI cli3 = new CLI();
//
//        LobbyManager lobbyManager = new LobbyManager();
//        String id = lobbyManager.createGame(3);
//
//        PipedInputStream input1 = new PipedInputStream();
//        PipedOutputStream outputTo1 = new PipedOutputStream();
//        input1.connect(outputTo1);
//
//        PipedInputStream input2 = new PipedInputStream();
//        PipedOutputStream outputTo2 = new PipedOutputStream();
//        input2.connect(outputTo2);
//
////        PipedInputStream input3 = new PipedInputStream();
////        PipedOutputStream outputTo3 = new PipedOutputStream();
////        input3.connect(outputTo3);
//
//        GameController controller = lobbyManager.getGame(id);
//
//        CLIInputManager inputManager1 = new CLIInputManager(cli1, input1, controller, lobbyManager);
//        CLIInputManager inputManager2 = new CLIInputManager(cli2, input2, controller, lobbyManager);
////        CLIInputManager inputManager3 = new CLIInputManager(cli3, input3, controller, lobbyManager);
//        Thread t1 = new Thread(inputManager1);
//        Thread t2 = new Thread(inputManager2);
////        Thread t3 = new Thread(inputManager3);
//        t1.start();
//        t2.start();
////        t3.start();
//
//        outputTo1.write("help\n".getBytes());
//        Thread.sleep(SLEEP);
//        outputTo1.write(("join_game matteo " + id + "\n").getBytes());
//        Thread.sleep(SLEEP);
//        outputTo1.write("players\n".getBytes());
//        Thread.sleep(SLEEP);
//        outputTo2.write("players\n".getBytes());
//        Thread.sleep(SLEEP);
//        outputTo2.write(("join_game andrea " + id + "\n").getBytes());
//        Thread.sleep(SLEEP);
//
////        outputTo1.write("starter\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("starter front\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("help\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("starter front\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("color green\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("color green\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("color red\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("players\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("objective 1\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("objective 2\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("draw_options\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("play_area matteo\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("play_area\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("hand\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("hand andrea\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("scroll_view center\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("scroll_view left 1\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("scroll_view right 5\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("scroll_view left\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("scroll_view right\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("scroll_view center 5\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("play_area PepperOne\n".getBytes());
////        outputTo1.write("chat Ciao a tutti!!!!\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("chat\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("chat_private l2c Ciao leo!!!\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("chat_private andrea Ciao andreee!!!\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo3.write("chat\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("chat_history\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("chat\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("chat_history\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("points\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("objectives\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("play_card 1 front 1;1\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("play_area\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("draw_card common gold 1\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo2.write("draw_options\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("play_card 2 back -1,-1\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("play_area\n".getBytes());
////        Thread.sleep(SLEEP);
////        outputTo1.write("draw_card deck resources\n".getBytes());
//
//        t1.join();
//        t2.join();
//        input1.close();
//        input2.close();
//        outputTo1.close();
//        outputTo2.close();
//    }
//}
