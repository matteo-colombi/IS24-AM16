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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Message sent by the server to inform the client that a player has played a card.
 * This payload also contains the information required by the client to update the view.
 */
@JsonDeserialize(using = PlayCardResponse.Deserializer.class)
public class PlayCardResponse extends Payload {
    private final String username;
    private final BoardCard card;
    private final SideType side;
    private final Position pos;
    private final Set<Position> addedLegalPositions;
    private final Set<Position> removedLegalPositions;
    private final Map<ResourceType, Integer> resourceCounts;
    private final Map<ObjectType, Integer> objectCounts;

    /**
     *
     * @param username The username of the player who played the card.
     * @param card The card that was played.
     * @param side The side on which the card was played.
     * @param pos The position where the card was played on the player's field.
     * @param addedLegalPositions The set of new positions where a card can now be played.
     * @param removedLegalPositions The set of positions where it is no longer possible to play a card.
     * @param resourceCounts The amount of each resource that the player has currently visible on their field.
     * @param objectCounts The amount of each object that the player has currently visible on their field.
     */
    @JsonCreator
    public PlayCardResponse(
            @JsonProperty("username") String username,
            @JsonProperty("card") BoardCard card,
            @JsonProperty("side") SideType side,
            @JsonProperty("pos") Position pos,
            @JsonProperty("addedLegalPositions") Set<Position> addedLegalPositions,
            @JsonProperty("removedLegalPositions") Set<Position> removedLegalPositions,
            @JsonProperty("resourceCounts") Map<ResourceType, Integer> resourceCounts,
            @JsonProperty("objectCounts") Map<ObjectType, Integer> objectCounts) {
        this.username = username;
        this.card = card;
        this.side = side;
        this.pos = pos;
        this.addedLegalPositions = addedLegalPositions;
        this.removedLegalPositions = removedLegalPositions;
        this.resourceCounts = resourceCounts;
        this.objectCounts = objectCounts;
    }

    /**
     * @return The username of the player who played the card.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return The card that was played.
     */
    public BoardCard getCard() {
        return card;
    }

    /**
     * @return The side on which the card was played.
     */
    public SideType getSide() {
        return side;
    }

    /**
     * @return The position where the card was played on the player's field.
     */
    public Position getPos() {
        return pos;
    }

    /**
     * @return The set of new positions where a card can now be played.
     */
    public Set<Position> getAddedLegalPositions() {
        return addedLegalPositions;
    }

    /**
     * @return The set of positions where it is no longer possible to play a card.
     */
    public Set<Position> getRemovedLegalPositions() {
        return removedLegalPositions;
    }

    /**
     * @return The amount of each resource that the player has currently visible on their field.
     */
    public Map<ResourceType, Integer> getResourceCounts() {
        return resourceCounts;
    }

    /**
     * @return The amount of each object that the player has currently visible on their field.
     */
    public Map<ObjectType, Integer> getObjectCounts() {
        return objectCounts;
    }

    /**
     * Custom deserializer for {@link PlayCardResponse}.
     */
    static class Deserializer extends StdDeserializer<PlayCardResponse> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();
        
        @Serial
        private static final long serialVersionUID = 102992346601770629L;

        public Deserializer() {
            super(PlayCardResponse.class);
        }

        /**
         * Deserializes the {@link PlayCardResponse} from the given JSON.
         * @param p Parser used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *   this deserialization activity.
         *
         * @return The deserialized {@link PlayCardResponse}.
         * @throws IOException Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public PlayCardResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode setPlayAreaNode = p.getCodec().readTree(p);

            String username = setPlayAreaNode.get("username").asText();

            BoardCard card = mapper.readValue(setPlayAreaNode.get("card").toString(), BoardCard.class);
            SideType side = mapper.readValue(setPlayAreaNode.get("side").toString(), SideType.class);
            Position pos = mapper.readValue(setPlayAreaNode.get("pos").toString(), Position.class);

            TypeReference<HashSet<Position>> typeReferenceAddedLegal = new TypeReference<>() {};
            Set<Position> addedLegalPositions = mapper.readValue(setPlayAreaNode.get("addedLegalPositions").toString(), typeReferenceAddedLegal);

            TypeReference<HashSet<Position>> typeReferenceRemovedLegal = new TypeReference<>() {};
            Set<Position> removedLegalPositions = mapper.readValue(setPlayAreaNode.get("removedLegalPositions").toString(), typeReferenceRemovedLegal);

            TypeReference<HashMap<ResourceType, Integer>> typeReferenceResourceCounts = new TypeReference<>() {};
            Map<ResourceType, Integer> resourceCounts = mapper.readValue(setPlayAreaNode.get("resourceCounts").toString(), typeReferenceResourceCounts);

            TypeReference<HashMap<ObjectType, Integer>> typeReferenceObjectCounts = new TypeReference<>() {};
            Map<ObjectType, Integer> objectCounts = mapper.readValue(setPlayAreaNode.get("objectCounts").toString(), typeReferenceObjectCounts);

            return new PlayCardResponse(username, card, side, pos, addedLegalPositions, removedLegalPositions, resourceCounts, objectCounts);
        }
    }
}
