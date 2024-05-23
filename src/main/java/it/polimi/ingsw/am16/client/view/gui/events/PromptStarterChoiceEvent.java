package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.StarterCard;
import javafx.event.Event;

import java.io.Serial;

public class PromptStarterChoiceEvent extends Event {

    @Serial
    private static final long serialVersionUID = -2777217106081385245L;

    private final StarterCard starterCard;

    public PromptStarterChoiceEvent(StarterCard starterCard) {
        super(GUIEventTypes.PROMPT_STARTER_CHOICE_EVENT);
        this.starterCard = starterCard;
    }

    public StarterCard getStarterCard() {
        return starterCard;
    }
}
