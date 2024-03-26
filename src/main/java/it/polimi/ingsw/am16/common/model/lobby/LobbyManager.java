package it.polimi.ingsw.am16.common.model.lobby;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am16.common.model.game.Game;
import it.polimi.ingsw.am16.common.model.game.GameModel;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.util.RNG;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class to manage multiple games.
 */
public class LobbyManager {

    private static final int LOBBY_ID_LENGTH = 6;
    private final Map<String, Game> games;
    private static final ObjectMapper mapper = JsonMapper.INSTANCE.getObjectMapper();

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
     * Removes the game with the given id from the lobby manager, if present.
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
        for(String id : games.keySet()) {
            File f = new File(directoryPath + "/" + id + ".json");
            f.createNewFile();
            saveGame(id, f);
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

        for(File f : gameFiles) {
            loadGame(f);
        }
    }

    /**
     * Saves the given game to the given file.
     * @param id The game to save to memory.
     * @param saveFile The file to save the lobby to.
     */
    private void saveGame(String id, File saveFile) throws IOException {
        mapper.writeValue(saveFile, games.get(id));
    }

    /**
     * Loads a game from the given {@link File}.
     * @param saveFile The file to load the game from.
     */
    private void loadGame(File saveFile) throws IOException {
        Game game = mapper.readValue(saveFile, Game.class);
        games.put(game.getId(), game);
    }
}
