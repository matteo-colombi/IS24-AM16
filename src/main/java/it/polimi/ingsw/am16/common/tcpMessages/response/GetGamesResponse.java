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
import it.polimi.ingsw.am16.common.model.game.LobbyState;
import it.polimi.ingsw.am16.common.tcpMessages.Payload;
import it.polimi.ingsw.am16.common.util.JsonMapper;

import java.io.IOException;
import java.io.Serial;
import java.util.Map;
import java.util.Set;

/**
 * Message sent by the server to inform the client on what games are currently active on the server.
 */
@JsonDeserialize(using = GetGamesResponse.Deserializer.class)
public class GetGamesResponse extends Payload {
    private final Set<String> gameIds;
    private final Map<String, Integer> currentPlayers;
    private final Map<String, Integer> maxPlayers;
    private final Map<String, LobbyState> lobbyStates;

    /**
     *
     * @param gameIds The set of game ids currently active on the server.
     * @param currentPlayers The number of players currently in each lobby/game.
     * @param maxPlayers The expected number of players in each lobby/game.
     * @param lobbyStates The state of each lobby.
     */
    @JsonCreator
    public GetGamesResponse(@JsonProperty("gameIds") Set<String> gameIds, @JsonProperty("currentPlayers") Map<String, Integer> currentPlayers, @JsonProperty("maxPlayers") Map<String, Integer> maxPlayers, @JsonProperty("lobbyStates") Map<String, LobbyState> lobbyStates) {
        this.gameIds = gameIds;
        this.currentPlayers = currentPlayers;
        this.maxPlayers = maxPlayers;
        this.lobbyStates = lobbyStates;
    }

    /**
     *
     * @return The set of game ids currently active on the server.
     */
    public Set<String> getGameIds() {
        return gameIds;
    }

    /**
     *
     * @return The number of players currently in each lobby/game.
     */
    public Map<String, Integer> getCurrentPlayers() {
        return currentPlayers;
    }

    /**
     *
     * @return The expected number of players in each lobby/game.
     */
    public Map<String, Integer> getMaxPlayers() {
        return maxPlayers;
    }

    /**
     *
     * @return The state of each lobby.
     */
    public Map<String, LobbyState> getLobbyStates() {
        return lobbyStates;
    }

    /**
     * Deserializer used to reload a GetGamesResponse message from a JSON file.
     */
    public static class Deserializer extends StdDeserializer<GetGamesResponse> {

        private static final ObjectMapper mapper = JsonMapper.getObjectMapper();

        @Serial
        private static final long serialVersionUID = -3603618098733715971L;

        protected Deserializer() {
            super(GetGamesResponse.class);
        }

        /**
         * Reloads a {@link GetGamesResponse} object from the given JSON.
         *
         * @param p    Parsed used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *             this deserialization activity.
         * @return The deserialized {@link GetGamesResponse}.
         * @throws IOException      Thrown if an exception occurs when reading from the input data.
         * @throws JacksonException Thrown if an exception occurs during JSON parsing.
         */
        @Override
        public GetGamesResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode node = p.getCodec().readTree(p);

            TypeReference<Set<String>> gameIdsTypeRef = new TypeReference<>() {};
            TypeReference<Map<String, Integer>> currentPlayersTypeRef = new TypeReference<>() {};
            TypeReference<Map<String, Integer>> maxPlayersTypeRef = new TypeReference<>() {};
            TypeReference<Map<String, LobbyState>> lobbyStatesTypeRef = new TypeReference<>() {};
            Set<String> gameIds = mapper.readValue(node.get("gameIds").toString(), gameIdsTypeRef);
            Map<String, Integer> currentPlayers = mapper.readValue(node.get("currentPlayers").toString(), currentPlayersTypeRef);
            Map<String, Integer> maxPlayers = mapper.readValue(node.get("maxPlayers").toString(), maxPlayersTypeRef);
            Map<String, LobbyState> lobbyStates = mapper.readValue(node.get("lobbyStates").toString(), lobbyStatesTypeRef);
            return new GetGamesResponse(
                    gameIds, currentPlayers, maxPlayers, lobbyStates
            );
        }
    }
}
