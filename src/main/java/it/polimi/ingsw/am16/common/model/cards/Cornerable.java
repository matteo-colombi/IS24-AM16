package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Interface implemented by all classes that represent an element that can be on one of a card's corners.
 */
@Deprecated
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SpecialType.class, name = "special")
})
public interface Cornerable {
    // RIP Cornerable, we hardly knew ya. 16/03/2024
}
