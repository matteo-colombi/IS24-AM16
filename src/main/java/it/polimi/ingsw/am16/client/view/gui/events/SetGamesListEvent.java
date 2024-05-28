package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.game.LobbyState;
import javafx.event.Event;

import java.io.Serial;
import java.util.List;
import java.util.Map;

public class SetGamesListEvent extends Event {

    @Serial
    private static final long serialVersionUID = -1275183389134780777L;

    private final List<String> gameIds;
    private final Map<String, Integer> currentPlayers;
    private final Map<String, Integer> maxPlayers;
    private final Map<String, LobbyState> lobbyStates;

    public SetGamesListEvent(List<String> gameIds, Map<String, Integer> currentPlayers, Map<String, Integer> maxPlayers, Map<String, LobbyState> lobbyStates) {
        super(GUIEventTypes.SET_GAMES_LIST_EVENT);
        this.gameIds = gameIds;
        this.currentPlayers = currentPlayers;
        this.maxPlayers = maxPlayers;
        this.lobbyStates = lobbyStates;
    }

    public List<String> getGameIds() {
        return gameIds;
    }

    public Map<String, Integer> getCurrentPlayers() {
        return currentPlayers;
    }

    public Map<String, Integer> getMaxPlayers() {
        return maxPlayers;
    }

    public Map<String, LobbyState> getLobbyStates() {
        return lobbyStates;
    }
}
