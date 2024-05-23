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

    public String getUsername() {
        return username;
    }

    public BoardCard getCard() {
        return card;
    }

    public SideType getSide() {
        return side;
    }

    public Position getPos() {
        return pos;
    }

    public Set<Position> getAddedLegalPositions() {
        return addedLegalPositions;
    }

    public Set<Position> getRemovedLegalPositions() {
        return removedLegalPositions;
    }

    public Map<ResourceType, Integer> getResourceCounts() {
        return resourceCounts;
    }

    public Map<ObjectType, Integer> getObjectCounts() {
        return objectCounts;
    }
}
