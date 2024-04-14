package it.polimi.ingsw.am16.common.model.players.hand;

import it.polimi.ingsw.am16.common.model.cards.PlayableCardType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to provide players with only the back sides of cards. Used to hide specific information about player's hands.
 */
public class RestrictedHand {

    private final List<ResourceType> resourceTypes;
    private final List<PlayableCardType> cardTypes;

    /**
     * Constructs a new empty restricted hand.
     */
    public RestrictedHand() {
        resourceTypes = new ArrayList<>();
        cardTypes = new ArrayList<>();
    }

    /**
     * Adds a card to this restricted hand.
     * @param resourceType The card's {@link ResourceType}.
     * @param cardType The card's {@link PlayableCardType}.
     */
    public void addCard(ResourceType resourceType, PlayableCardType cardType) {
        this.resourceTypes.add(resourceType);
        this.cardTypes.add(cardType);
    }

    /**
     * @return The number of cards in this restricted hand.
     */
    public int size() {
        return resourceTypes.size();
    }

    /**
     * @param index The card's index.
     * @return The {@link ResourceType} of the card at the given index, or <code>null</code> if the index is out of bounds.
     */
    public ResourceType getResourceType(int index) {
        try {
            return resourceTypes.get(index);
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    /**
     * @param index The card's index.
     * @return The {@link PlayableCardType} of the card at the given index, or <code>null</code> if the index is out of bounds.
     */
    public PlayableCardType getCardType(int index) {
        try {
            return cardTypes.get(index);
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for(int i = 0 ; i<resourceTypes.size(); i++) {
            builder.append(cardTypes.get(i)).append(" ").append(resourceTypes.get(i)).append(", ");
        }
        builder.append(']');
        return builder.toString();
    }
}
