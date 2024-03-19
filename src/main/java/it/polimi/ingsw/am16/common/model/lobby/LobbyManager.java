package it.polimi.ingsw.am16.common.model.lobby;

import it.polimi.ingsw.am16.common.util.RNG;

import java.util.HashMap;
import java.util.Map;

//DOCME
public class LobbyManager {

    private static final int LOBBY_ID_LENGTH = 6;
    private final Map<String, Lobby> lobbies;

    public LobbyManager() {
        lobbies = new HashMap<>();
    }

    public String createLobby(int numPlayers) {
        String id;
        do {
            id = RNG.getRNG().nextAlphNumString(LOBBY_ID_LENGTH);
        } while (lobbies.containsKey(id));
        Lobby newLobby = new Lobby(id, numPlayers);
        lobbies.put(id, newLobby);
        return id;
    }

    public Lobby getLobby(String id) {
        return lobbies.get(id);
    }
}
