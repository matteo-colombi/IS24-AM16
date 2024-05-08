package it.polimi.ingsw.am16.common.model.cards;

import java.io.Serializable;

public record RestrictedCard(PlayableCardType cardType, ResourceType resourceType) implements Serializable {
    @Override
    public String toString() {
        return "RestrictedCard{" +
                "cardType=" + cardType +
                ", resourceType=" + resourceType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RestrictedCard that = (RestrictedCard) o;
        return cardType == that.cardType && resourceType == that.resourceType;
    }

    @Override
    public int hashCode() {
        int result = cardType.hashCode();
        result = 31 * result + resourceType.hashCode();
        return result;
    }
}
