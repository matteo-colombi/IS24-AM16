package it.polimi.ingsw.am16.server.lobby;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am16.common.model.game.Game;
import it.polimi.ingsw.am16.common.model.game.GameModel;
import it.polimi.ingsw.am16.common.util.FilePaths;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.util.RNG;
import it.polimi.ingsw.am16.server.controller.GameController;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.*;

/**
 * Class to manage multiple games.
 */
public class LobbyManager {

    private static final int LOBBY_ID_LENGTH = 6;
    private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

    private final Map<String, GameController> games;

    /**
     * DOCME
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public LobbyManager() {
        this.games = new ConcurrentHashMap<>();
        File saveDir = new File(FilePaths.SAVE_DIRECTORY);
        saveDir.mkdirs();
    }

    /**
     * Creates a new game with the given number of players.
     *
     * @param numPlayers The number of players.
     * @return The created lobby's id.
     */
    public String createGame(int numPlayers) {
        if ((numPlayers != 1) && numPlayers < 2 || numPlayers > 4) { //FIXME 1 is only allowed for testing
            throw new IllegalArgumentException("Number of players must be between 2 and 4");
        }

        String id;
        do {
            id = RNG.getRNG().nextAlphNumString(LOBBY_ID_LENGTH);
        } while (games.containsKey(id));
        Game newGame = new Game(id, numPlayers);
        GameController newController = new GameController(this, newGame);
        games.put(id, newController);
        return id;
    }

    /**
     * Removes the game with the given id from the lobby manager, if present; does nothing if the game with the given id does not exist in this manager.
     *
     * @param id The id of the game to remove.
     */
    public void removeGame(String id) {
        games.remove(id);
    }

    /**
     * Retrieves the controller for the game with the given id.
     *
     * @param id The lobby's id.
     * @return The controller for the game with the given id, or <code>null</code> if a game with the given id doesn't exist.
     */
    public GameController getGame(String id) {
        return games.get(id);
    }

    /**
     * @return A {@link Set} containing all the current ids of games.
     */
    public Set<String> getGameIds() {
        return games.keySet();
    }

    /**
     * DOCME
     *
     * @param game
     */
    public void deleteGame(GameModel game) {
        String gameId = game.getId();
        games.remove(gameId);
        // FIXME removed for debugging purpose
//        File f = new File(String.format("%s/%s.json", FilePaths.SAVE_DIRECTORY, gameId));
//        try {
//            Files.deleteIfExists(f.toPath());
//        } catch (IOException e) {
//            System.err.printf("Save file for game %s could not be deleted.\n", gameId);
//        }
    }

    /**
     * Loads all the games in the given directory.
     *
     * @param directoryPath The directory path to load the games from.
     */
    public void loadGames(String directoryPath) throws IOException {
        File dir = new File(directoryPath);
        if (!dir.exists()) throw new IOException("Save file directory doesn't exist: " + directoryPath);

        File[] gameFiles = dir.listFiles();

        if (gameFiles == null) throw new IOException("Invalid directory: " + directoryPath);

        try (ExecutorService service = Executors.newFixedThreadPool(4)) {
            for (final File f : gameFiles) {
                service.submit(() -> loadGame(f));
            }
            service.shutdown();
            if (!service.awaitTermination(200L * gameFiles.length, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Couldn't reload games from memory: timeout expired.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a game from the given {@link File}.
     *
     * @param saveFile The file to load the game from.
     */
    private void loadGame(File saveFile) {
        try {
            Game game = mapper.readValue(saveFile, Game.class);
            GameController controller = new GameController(this, game);
            games.put(game.getId(), controller);
        } catch (IOException e) {
            System.out.printf("Couldn't reload game from file %s.\n", saveFile.getPath());
        }
    }

    /**
     * DOCME
     *
     * @param game
     */
    public void saveGame(GameModel game) {
        String savedGame;
        try {
            savedGame = JsonMapper.getObjectMapper().writeValueAsString(game);
        } catch (JsonProcessingException e) {
            System.out.printf("Failed to serialize save game data for game %s.\n", game.getId());
            return;
        }

        final String finalSavedGame = savedGame;
        new Thread(() -> {
            File saveFile = new File(String.format("%s/%s.json", FilePaths.SAVE_DIRECTORY, game.getId()));
            try (PrintWriter writer = new PrintWriter(saveFile)) {
                writer.print(finalSavedGame);
                writer.flush();
            } catch (IOException e) {
                System.out.printf("Failed to write save game data for game %s.\n", game.getId());
            }
        }).start();
    }
}
