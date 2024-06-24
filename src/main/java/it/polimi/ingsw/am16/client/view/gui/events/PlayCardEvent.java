package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.BoardCard;
import it.polimi.ingsw.am16.common.model.cards.ObjectType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.util.Position;
import javafx.event.Event;

import java.io.Serial;
import java.util.Map;
import java.util.Set;

/**
 * Event that is fired in the GUI when a new card is played.
 */
public class PlayCardEvent extends Event {

    @Serial
    private static final long serialVersionUID = 7964915117025833737L;

    private final String username;
    private final BoardCard card;
    private final SideType side;
    private final Position pos;
    private final Set<Position> addedLegalPositions;
    private final Set<Position> removedLegalPositions;
    private final Map<ResourceType, Integer> resourceCounts;
    private final Map<ObjectType, Integer> objectCounts;

    /**
     * @param username The username of the player who played the card.
     * @param card The card that was played.
     * @param side The side on which the new card was played.
     * @param pos The position in which the card was played.
     * @param addedLegalPositions The new positions where a card can now be placed.
     * @param removedLegalPositions The positions where a card can no longed be placed.
     * @param resourceCounts The amounts of all the resources that the player now has visible on their play field.
     * @param objectCounts The amounts of all the objects that the player now has visible on their play field.
     */
    public PlayCardEvent(String username, BoardCard card, SideType side, Position pos, Set<Position> addedLegalPositions, Set<Position> removedLegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        super(GUIEventTypes.PLAY_CARD_EVENT);
        this.username = username;
        this.card = card;
        this.side = side;
        this.pos = pos;
        this.addedLegalPositions = addedLegalPositions;
        this.removedLegalPositions = removedLegalPositions;
        this.resourceCounts = resourceCounts;
        this.objectCounts = objectCounts;
    }

    /**
     *
     * @return The username of the player who played the card.
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return The card that was played.
     */
    public BoardCard getCard() {
        return card;
    }

    /**
     *
     * @return The side on which the new card was played.
     */
    public SideType getSide() {
        return side;
    }

    /**
     *
     * @return The position in which the card was played.
     */
    public Position getPos() {
        return pos;
    }

    /**
     *
     * @return The new positions where a card can now be placed.
     */
    public Set<Position> getAddedLegalPositions() {
        return addedLegalPositions;
    }

    /**
     *
     * @return The positions where a card can no longed be placed.
     */
    public Set<Position> getRemovedLegalPositions() {
        return removedLegalPositions;
    }

    /**
     *
     * @return The amounts of all the resources that the player now has visible on their play field.
     */
    public Map<ResourceType, Integer> getResourceCounts() {
        return resourceCounts;
    }

    /**
     *
     * @return The amounts of all the objects that the player now has visible on their play field.
     */
    public Map<ObjectType, Integer> getObjectCounts() {
        return objectCounts;
    }
}
