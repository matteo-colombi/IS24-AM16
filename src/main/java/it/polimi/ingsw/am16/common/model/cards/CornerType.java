package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum used to define entities which can occupy a card's corners.
 */
public enum CornerType {
    @JsonProperty("animal")
    ANIMAL(ResourceType.ANIMAL),
    @JsonProperty("plant")
    PLANT(ResourceType.PLANT),
    @JsonProperty("insect")
    INSECT(ResourceType.INSECT),
    @JsonProperty("fungi")
    FUNGI(ResourceType.FUNGI),
    @JsonProperty("inkwell")
    INKWELL(ObjectType.INKWELL),
    @JsonProperty("manuscript")
    MANUSCRIPT(ObjectType.MANUSCRIPT),
    @JsonProperty("quill")
    QUILL(ObjectType.QUILL),
    @JsonProperty("empty")
    EMPTY(),
    @JsonProperty("blocked")
    BLOCKED();

    private final ResourceType resource;
    private final ObjectType object;

    CornerType(ResourceType resource) {
        this.resource = resource;
        this.object = null;
    }

    CornerType(ObjectType object) {
        this.resource = null;
        this.object = object;
    }

    CornerType() {
        this.resource = null;
        this.object = null;
    }

    public ResourceType mappedResource() {
        return resource;
    }

    public ObjectType mappedObject() {
        return object;
    }
}
