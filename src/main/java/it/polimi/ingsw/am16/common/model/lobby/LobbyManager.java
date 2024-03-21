package it.polimi.ingsw.am16.common.model.lobby;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.am16.common.util.RNG;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage multiple games.
 */
public class LobbyManager {

    private static final int LOBBY_ID_LENGTH = 6;
    private final Map<String, Lobby> lobbies;
    private final ObjectMapper mapper;

    /**
     * Constructs a new Lobby Manager which contains no lobbies.
     */
    public LobbyManager() {
        lobbies = new HashMap<>();
        mapper = new ObjectMapper();
    }

    /**
     * Creates a new game with the given number of players.
     * @param numPlayers The number of players.
     * @return The created lobby's id.
     */
    public String createLobby(int numPlayers) {
        String id;
        do {
            id = RNG.getRNG().nextAlphNumString(LOBBY_ID_LENGTH);
        } while (lobbies.containsKey(id));
        Lobby newLobby = new Lobby(id, numPlayers);
        lobbies.put(id, newLobby);
        return id;
    }

    /**
     * Retrieves the lobby with the given id.
     * @param id The lobby's id.
     * @return The lobby with the given id.
     */
    public Lobby getLobby(String id) {
        return lobbies.get(id);
    }

    /**
     * Saves all the current games in this {@link LobbyManager} to the given directory.
     * @param directoryPath The directory path to save the lobbies to.
     */
    public void saveLobbies(String directoryPath) throws IOException {
        for(String id : lobbies.keySet()) {
            File f = new File(directoryPath + "/" + id + ".json");
            f.createNewFile();
            saveLobby(id, f);
        }
    }

    /**
     * Loads all the games in the given directory.
     * @param directoryPath The directory path to load the lobbies from.
     */
    public void loadLobbies(String directoryPath) throws IOException {
        //TODO implement
    }

    /**
     * Saves the given lobby to the given file.
     * @param id The lobby to save to memory.
     * @param saveFile The file to save the lobby to.
     */
    private void saveLobby(String id, File saveFile) throws IOException {
        mapper.writeValue(saveFile, lobbies.get(id));
    }

    /**
     * Loads a lobby from the given {@link File}.
     * @param saveFile The file to load the lobby from.
     */
    private void loadLobby(File saveFile) throws IOException {
        //TODO implement
    }
}
