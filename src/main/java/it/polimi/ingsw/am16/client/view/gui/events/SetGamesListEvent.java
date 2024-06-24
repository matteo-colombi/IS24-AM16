package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.game.LobbyState;
import javafx.event.Event;

import java.io.Serial;
import java.util.List;
import java.util.Map;

/**
 * Event fired in the GUI when the list of active games is communicated by the server.
 */
public class SetGamesListEvent extends Event {

    @Serial
    private static final long serialVersionUID = -1275183389134780777L;

    private final List<String> gameIds;
    private final Map<String, Integer> currentPlayers;
    private final Map<String, Integer> maxPlayers;
    private final Map<String, LobbyState> lobbyStates;

    /**
     * @param gameIds The ids of the games that are currently active on the server.
     * @param currentPlayers A map containing the amount of players currently in each game.
     * @param maxPlayers A map containing the maximum number of players allowed in each game.
     * @param lobbyStates A map containing each game's {@link LobbyState}.
     */
    public SetGamesListEvent(List<String> gameIds, Map<String, Integer> currentPlayers, Map<String, Integer> maxPlayers, Map<String, LobbyState> lobbyStates) {
        super(GUIEventTypes.SET_GAMES_LIST_EVENT);
        this.gameIds = gameIds;
        this.currentPlayers = currentPlayers;
        this.maxPlayers = maxPlayers;
        this.lobbyStates = lobbyStates;
    }

    /**
     * @return The ids of the games that are currently active on the server.
     */
    public List<String> getGameIds() {
        return gameIds;
    }

    /**
     * @return A map containing the amount of players currently in each game.
     */
    public Map<String, Integer> getCurrentPlayers() {
        return currentPlayers;
    }

    /**
     * @return A map containing the maximum number of players allowed in each game.
     */
    public Map<String, Integer> getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * @return A map containing each game's {@link LobbyState}.
     */
    public Map<String, LobbyState> getLobbyStates() {
        return lobbyStates;
    }
}
