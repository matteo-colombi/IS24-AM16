package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.StarterCard;
import javafx.event.Event;

import java.io.Serial;

/**
 * Event that is fired in the GUI when the player has to choose on which side to place their starter card.
 */
public class PromptStarterChoiceEvent extends Event {

    @Serial
    private static final long serialVersionUID = -2777217106081385245L;

    private final StarterCard starterCard;

    /**
     *
     * @param starterCard The starter card that was given to the player.
     */
    public PromptStarterChoiceEvent(StarterCard starterCard) {
        super(GUIEventTypes.PROMPT_STARTER_CHOICE_EVENT);
        this.starterCard = starterCard;
    }

    /**
     *
     * @return The starter card that was given to the player.
     */
    public StarterCard getStarterCard() {
        return starterCard;
    }
}
