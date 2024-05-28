package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import javafx.event.Event;

import java.io.Serial;
import java.util.List;

/**
 * Event that is fired in the GUI when it is the player's turn to choose their color.
 */
public class PromptColorChoiceEvent extends Event {

    @Serial
    private static final long serialVersionUID = -2777217106081385245L;

    private final List<PlayerColor> colorChoices;

    /**
     * @param colorChoices The list of colors from which the player can choose from.
     */
    public PromptColorChoiceEvent(List<PlayerColor> colorChoices) {
        super(GUIEventTypes.PROMPT_COLOR_CHOICE_EVENT);
        this.colorChoices = colorChoices;
    }

    /**
     * @return The list of colors from which the player can choose from.
     */
    public List<PlayerColor> getColorChoices() {
        return colorChoices;
    }
}
