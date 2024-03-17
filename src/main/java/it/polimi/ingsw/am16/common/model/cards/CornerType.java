package it.polimi.ingsw.am16.common.model.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum used to define entities which can occupy a card's corners.
 */
public enum CornerType {
    @JsonProperty("animal")
    ANIMAL,
    @JsonProperty("plant")
    PLANT,
    @JsonProperty("insect")
    INSECT,
    @JsonProperty("fungi")
    FUNGI,
    @JsonProperty("inkwell")
    INKWELL,
    @JsonProperty("manuscript")
    MANUSCRIPT,
    @JsonProperty("quill")
    QUILL,
    @JsonProperty("empty")
    EMPTY,
    @JsonProperty("blocked")
    BLOCKED;

    private ResourceType resource;
    private ObjectType object;

    public static void bindToResources() {
        FUNGI.resource = ResourceType.FUNGI;
        PLANT.resource = ResourceType.PLANT;
        ANIMAL.resource = ResourceType.ANIMAL;
        INSECT.resource = ResourceType.INSECT;
        INKWELL.object = ObjectType.INKWELL;
        MANUSCRIPT.object = ObjectType.MANUSCRIPT;
        QUILL.object = ObjectType.QUILL;
    }

    public ResourceType mappedResource() {
        return resource;
    }

    public ObjectType mappedObject() {
        return object;
    }


}
