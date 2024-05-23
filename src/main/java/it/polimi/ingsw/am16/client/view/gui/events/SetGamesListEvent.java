package it.polimi.ingsw.am16.client.view.gui.events;

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

    public SetGamesListEvent(List<String> gameIds, Map<String, Integer> currentPlayers, Map<String, Integer> maxPlayers) {
        super(GUIEventTypes.SET_GAMES_LIST_EVENT);
        this.gameIds = gameIds;
        this.currentPlayers = currentPlayers;
        this.maxPlayers = maxPlayers;
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
}
