package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.ObjectiveCard;
import javafx.event.Event;

import java.io.Serial;
import java.util.List;
import java.util.Map;

/**
 * Event fired in the GUI when the game ends and info about the winners and all the personal objectives are sent to the clients.
 */
public class SetEndGameInfoEvent extends Event {

    @Serial
    private static final long serialVersionUID = -2539754234054233423L;

    private final List<String> winnerUsernames;
    private final Map<String, ObjectiveCard> personalObjectives;

    /**
     * @param winnerUsernames The list of players who won the game.
     * @param personalObjectives A map containing all the player's personal objectives.
     */
    public SetEndGameInfoEvent(List<String> winnerUsernames, Map<String, ObjectiveCard> personalObjectives) {
        super(GUIEventTypes.SET_END_GAME_INFO_EVENT);
        this.winnerUsernames = winnerUsernames;
        this.personalObjectives = personalObjectives;
    }

    /**
     * @return The list of players who won the game.
     */
    public List<String> getWinnerUsernames() {
        return winnerUsernames;
    }

    /**
     * @return A map containing all the player's personal objectives.
     */
    public Map<String, ObjectiveCard> getPersonalObjectives() {
        return personalObjectives;
    }
}
