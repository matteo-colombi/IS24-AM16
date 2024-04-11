package it.polimi.ingsw.am16.common.model.lobby;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am16.common.model.game.Game;
import it.polimi.ingsw.am16.common.model.game.GameModel;
import it.polimi.ingsw.am16.common.util.FilePaths;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.util.RNG;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Class to manage multiple games.
 */
public class LobbyManager {

    private static final int LOBBY_ID_LENGTH = 6;
    private final Map<String, Game> games;
    private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

    /**
     * Constructs a new Lobby Manager which contains no lobbies.
     */
    public LobbyManager() {
        games = new HashMap<>();
    }

    /**
     * Creates a new game with the given number of players.
     * @param numPlayers The number of players.
     * @return The created lobby's id.
     */
    public String createGame(int numPlayers) {
        String id;
        do {
            id = RNG.getRNG().nextAlphNumString(LOBBY_ID_LENGTH);
        } while (games.containsKey(id));
        Game newGame = new Game(id, numPlayers);
        games.put(id, newGame);
        return id;
    }

    /**
     * Removes the game with the given id from the lobby manager, if present; does nothing if the game with the given id does not exist in this manager.
     * @param id The id of the game to remove.
     */
    public void removeGame(String id) {
        games.remove(id);
    }

    /**
     * Retrieves the lobby with the given id.
     * @param id The lobby's id.
     * @return The lobby with the given id.
     */
    public GameModel getGame(String id) {
        return games.get(id);
    }

    /**
     * @return A {@link Set} containing all the current ids of games.
     */
    public Set<String> getGameIds() {
        return games.keySet();
    }

    /**
     * Saves all the current games in this {@link LobbyManager} to the given directory.
     * @param directoryPath The directory path to save the games to.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored") //Suppressing because we only want to create a new file if it doesn't exist, but write to it regardless of whether it exists or not.
    public void saveGames(String directoryPath) throws IOException {
        File dir = new File(directoryPath);
        dir.mkdirs();
        for(String id : games.keySet()) {
            saveGame(id);
        }
    }

    /**
     * Loads all the games in the given directory.
     * @param directoryPath The directory path to load the games from.
     */
    public void loadGames(String directoryPath) throws IOException {
        File dir = new File(directoryPath);
        if (!dir.exists()) throw new IOException("Save file directory doesn't exist: " + directoryPath);

        File[] gameFiles = dir.listFiles();

        if (gameFiles == null) throw new IOException("Invalid directory: " + directoryPath);

        try(ExecutorService service = Executors.newFixedThreadPool(4)) {
            for(final File f : gameFiles) {
                service.submit(() -> loadGame(f));
            }
            if(!service.awaitTermination(200L * gameFiles.length, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Couldn't reload games from memory: timeout expired.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Saves the given game to the given file.
     * @param id The game to save to memory.
     */
    public void saveGame(final String id) {
        String savedGame = null;
        try {
            savedGame = mapper.writeValueAsString(games.get(id));
        } catch (JsonProcessingException e) {
            System.out.printf("Failed to serialize save game data for game %s.\n", id);
        }

        final String finalSavedGame = savedGame;
        new Thread(() -> {
            File saveFile = new File(String.format("%s/%s.json", FilePaths.SAVE_DIRECTORY, id));

            try (PrintWriter writer = new PrintWriter(saveFile)) {
                writer.print(finalSavedGame);
                writer.flush();
            } catch (IOException e) {
                System.out.printf("Failed to write save game data for game %s.\n", id);
            }
        }).start();
    }

    /**
     * Loads a game from the given {@link File}.
     * @param saveFile The file to load the game from.
     */
    private void loadGame(File saveFile) {
        try {
            Game game = mapper.readValue(saveFile, Game.class);
            games.put(game.getId(), game);
        } catch (IOException e) {
            System.out.printf("Couldn't reload game from file %s.\n", saveFile.getPath());
        }
    }
}
