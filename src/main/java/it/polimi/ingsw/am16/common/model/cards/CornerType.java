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

    /**
     * Method that must be called at least once that creates associations between {@link CornerType} and {@link ResourceType} and {@link ObjectType} where appropriate.
     */
    public static void bindToResourcesAndObjects() {
        FUNGI.resource = ResourceType.FUNGI;
        PLANT.resource = ResourceType.PLANT;
        ANIMAL.resource = ResourceType.ANIMAL;
        INSECT.resource = ResourceType.INSECT;
        INKWELL.object = ObjectType.INKWELL;
        MANUSCRIPT.object = ObjectType.MANUSCRIPT;
        QUILL.object = ObjectType.QUILL;
    }

    /**
     * @return the corresponding {@link ResourceType}, or <code>null</code> if <code>this</code> is not a resource.
     */
    public ResourceType mappedResource() {
        return resource;
    }

    /**
     * @return the corresponding {@link ObjectType}, or <code>null</code> if <code>this</code> is not an object.
     */
    public ObjectType mappedObject() {
        return object;
    }
}
