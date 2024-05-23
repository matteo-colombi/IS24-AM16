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

    public String getUsername() {
        return username;
    }

    public List<Position> getCardPlacementOrder() {
        return cardPlacementOrder;
    }

    public Map<Position, BoardCard> getField() {
        return field;
    }

    public Map<BoardCard, SideType> getActiveSides() {
        return activeSides;
    }

    public Set<Position> getLegalPositions() {
        return legalPositions;
    }

    public Set<Position> getIllegalPositions() {
        return illegalPositions;
    }

    public Map<ResourceType, Integer> getResourceCounts() {
        return resourceCounts;
    }

    public Map<ObjectType, Integer> getObjectCounts() {
        return objectCounts;
    }
}
