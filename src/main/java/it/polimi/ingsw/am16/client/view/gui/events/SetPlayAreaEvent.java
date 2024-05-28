package it.polimi.ingsw.am16.client.view.gui.events;

import it.polimi.ingsw.am16.common.model.cards.BoardCard;
import it.polimi.ingsw.am16.common.model.cards.ObjectType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.util.Position;
import javafx.event.Event;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Event that is fired in the GUI when an entire play area is being communicated by the server.
 */
public class SetPlayAreaEvent extends Event {

    @Serial
    private static final long serialVersionUID = -8107318796139852625L;

    private final String username;
    private final List<Position> cardPlacementOrder;
    private final java.util.Map<Position, BoardCard> field;
    private final Map<BoardCard, SideType> activeSides;
    private final Set<Position> legalPositions;
    private final Set<Position> illegalPositions;
    private final Map<ResourceType, Integer> resourceCounts;
    private final Map<ObjectType, Integer> objectCounts;

    /**
     * @param username The username of the player whose play area is being given.
     * @param cardPlacementOrder The order in which the cards were played.
     * @param field The play area's field of cards.
     * @param activeSides The map containing information about the sides on which each card was played.
     * @param legalPositions The set of positions on which a card can be placed.
     * @param illegalPositions The set of position on which a card cannot be played.
     * @param resourceCounts The amount of each resource currently visible on the player's play field.
     * @param objectCounts The amount of each object currently visible on the player's play field.
     */
    public SetPlayAreaEvent(String username, List<Position> cardPlacementOrder, Map<Position, BoardCard> field, Map<BoardCard, SideType> activeSides, Set<Position> legalPositions, Set<Position> illegalPositions, Map<ResourceType, Integer> resourceCounts, Map<ObjectType, Integer> objectCounts) {
        super(GUIEventTypes.SET_PLAY_AREA_EVENT);
        this.username = username;
        this.cardPlacementOrder = cardPlacementOrder;
        this.field = field;
        this.activeSides = activeSides;
        this.legalPositions = legalPositions;
        this.illegalPositions = illegalPositions;
        this.resourceCounts = resourceCounts;
        this.objectCounts = objectCounts;
    }

    /**
     * @return The username of the player whose play area is being given.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return The order in which the cards were played.
     */
    public List<Position> getCardPlacementOrder() {
        return cardPlacementOrder;
    }

    /**
     * @return The play area's field of cards.
     */
    public Map<Position, BoardCard> getField() {
        return field;
    }

    /**
     * @return The map containing information about the sides on which each card was played.
     */
    public Map<BoardCard, SideType> getActiveSides() {
        return activeSides;
    }

    /**
     * @return The set of positions on which a card can be placed.
     */
    public Set<Position> getLegalPositions() {
        return legalPositions;
    }

    /**
     * @return The set of position on which a card cannot be played.
     */
    public Set<Position> getIllegalPositions() {
        return illegalPositions;
    }

    /**
     * @return The amount of each resource currently visible on the player's play field.
     */
    public Map<ResourceType, Integer> getResourceCounts() {
        return resourceCounts;
    }

    /**
     * @return The amount of each object currently visible on the player's play field.
     */
    public Map<ObjectType, Integer> getObjectCounts() {
        return objectCounts;
    }
}
