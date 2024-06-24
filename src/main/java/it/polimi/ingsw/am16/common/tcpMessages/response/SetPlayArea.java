package it.polimi.ingsw.am16.common.tcpMessages.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import it.polimi.ingsw.am16.common.model.cards.BoardCard;
import it.polimi.ingsw.am16.common.model.cards.ObjectType;
import it.polimi.ingsw.am16.common.model.cards.ResourceType;
import it.polimi.ingsw.am16.common.model.cards.SideType;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.JsonMapper;
import it.polimi.ingsw.am16.common.util.Position;

import java.io.IOException;
import java.io.Serial;
import java.util.*;

/**
 * Message sent by the server to tell the client a player's play area state.
 */
@JsonDeserialize(using = SetPlayArea.Deserializer.class)
public class SetPlayArea extends Payload {
    private final String username;
    private final List<Position> cardPlacementOrder;
    @JsonSerialize(keyUsing = Position.Serializer.class)
    private final Map<Position, BoardCard> field;
    @JsonSerialize(keyUsing = BoardCard.BoardCardSerializer.class)
    private final Map<BoardCard, SideType> activeSides;
    private final Set<Position> legalPositions;
    private final Set<Position> illegalPositions;
    private final Map<ResourceType, Integer> resourceCounts;
    private final Map<ObjectType, Integer> objectCounts;

    /**
     *
     * @param username The username of the player whose play area information is being sent.
     * @param cardPlacementOrder The order in which cards were played on this play area.
     * @param field The field of cards.
     * @param activeSides The side on which each card was played.
     * @param legalPositions The set of positions where a card can be placed, expected where specified otherwise by the set of illegal positions.
     * @param illegalPositions The set of positions where a card cannot be placed under any circumstance.
     * @param resourceCounts The amount of each resource currently visible on the player's field.
     * @param objectCounts The amount of each object currently visible on the player's field.
     */
    @JsonCreator
    public SetPlayArea(
            @JsonProperty("username") String username,
            @JsonProperty("cardPlacementOrder") List<Position> cardPlacementOrder,
            @JsonProperty("field") Map<Position, BoardCard> field,
            @JsonProperty("activeSides") Map<BoardCard, SideType> activeSides,
            @JsonProperty("legalPositions") Set<Position> legalPositions,
            @JsonProperty("illegalPositions") Set<Position> illegalPositions,
            @JsonProperty("resourceCounts") Map<ResourceType, Integer> resourceCounts,
            @JsonProperty("objectCounts") Map<ObjectType, Integer> objectCounts) {
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
     * @return The username of the player whose play area information is being sent.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return The order in which cards were played on this play area.
     */
    public List<Position> getCardPlacementOrder() {
        return cardPlacementOrder;
    }

    /**
     * @return The field of cards.
     */
    public Map<Position, BoardCard> getField() {
        return field;
    }

    /**
     * @return The side on which each card was played.
     */
    public Map<BoardCard, SideType> getActiveSides() {
        return activeSides;
    }

    /**
     * @return The set of positions where a card can be placed, expected where specified otherwise by the set of illegal positions.
     */
    public Set<Position> getLegalPositions() {
        return legalPositions;
    }

    /**
     * @return The set of positions where a card cannot be placed under any circumstance.
     */
    public Set<Position> getIllegalPositions() {
        return illegalPositions;
    }

    /**
     * @return The amount of each resource currently visible on the player's field.
     */
    public Map<ResourceType, Integer> getResourceCounts() {
        return resourceCounts;
    }

    /**
     * @return The amount of each object currently visible on the player's field.
     */
    public Map<ObjectType, Integer> getObjectCounts() {
        return objectCounts;
    }

    /**
     * Custom deserializer for {@link SetPlayArea}. Used for reloading a saved game from JSON.
     */
    static class Deserializer extends StdDeserializer<SetPlayArea> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();
        
        @Serial
        private static final long serialVersionUID = 3996686422519830172L;

        public Deserializer() {
            super(SetPlayArea.class);
        }

        /**
         * Deserializes the {@link SetPlayArea} from the given JSON.
         * @param p Parser used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link SetPlayArea}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public SetPlayArea deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode setPlayAreaNode = p.getCodec().readTree(p);

            String username = setPlayAreaNode.get("username").asText();

            TypeReference<ArrayList<Position>> typeReferenceCardPlacementOrder = new TypeReference<>() {};
            List<Position> cardPlacementOrder = mapper.readValue(setPlayAreaNode.get("cardPlacementOrder").toString(), typeReferenceCardPlacementOrder);

            TypeReference<HashMap<Position, BoardCard>> typeReferenceField = new TypeReference<>() {};
            Map<Position, BoardCard> field = mapper.readValue(setPlayAreaNode.get("field").toString(), typeReferenceField);

            TypeReference<HashMap<BoardCard, SideType>> typeReferenceActiveSides = new TypeReference<>() {};
            Map<BoardCard, SideType> activeSideTypes = mapper.readValue(setPlayAreaNode.get("activeSides").toString(), typeReferenceActiveSides);

            TypeReference<HashSet<Position>> typeReferenceLegalPositions = new TypeReference<>() {};
            Set<Position> legalPositions = mapper.readValue(setPlayAreaNode.get("legalPositions").toString(), typeReferenceLegalPositions);

            TypeReference<HashSet<Position>> typeReferenceIllegalPositions = new TypeReference<>() {};
            Set<Position> illegalPositions = mapper.readValue(setPlayAreaNode.get("illegalPositions").toString(), typeReferenceIllegalPositions);

            TypeReference<HashMap<ResourceType, Integer>> typeReferenceResourceCounts = new TypeReference<>() {};
            Map<ResourceType, Integer> resourceCounts = mapper.readValue(setPlayAreaNode.get("resourceCounts").toString(), typeReferenceResourceCounts);

            TypeReference<HashMap<ObjectType, Integer>> typeReferenceObjectCounts = new TypeReference<>() {};
            Map<ObjectType, Integer> objectCounts = mapper.readValue(setPlayAreaNode.get("objectCounts").toString(), typeReferenceObjectCounts);

            return new SetPlayArea(username, cardPlacementOrder, field, activeSideTypes, legalPositions, illegalPositions, resourceCounts, objectCounts);
        }
    }
}
