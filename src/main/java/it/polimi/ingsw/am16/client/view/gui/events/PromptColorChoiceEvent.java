package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.players.PlayerColor;
import javafx.event.Event;

import java.io.Serial;
import java.util.List;

public class PromptColorChoiceEvent extends Event {

    @Serial
    private static final long serialVersionUID = -2777217106081385245L;

    private final List<PlayerColor> colorChoices;

    public PromptColorChoiceEvent(List<PlayerColor> colorChoices) {
        super(GUIEventTypes.PROMPT_COLOR_CHOICE_EVENT);
        this.colorChoices = colorChoices;
    }

    public List<PlayerColor> getColorChoices() {
        return colorChoices;
    }
}
