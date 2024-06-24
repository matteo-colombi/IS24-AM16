package it.polimi.ingsw.am16.common.util;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The possible types of errors that the client can encounter.
 */
public enum ErrorType {
    @JsonProperty("join_error")
    JOIN_ERROR,

    @JsonProperty("connection_dead")
    CONNECTION_DEAD,

    @JsonProperty("other_player_disconnected")
    OTHER_PLAYER_DISCONNECTED,

    @JsonProperty("generic_error")
    GENERIC_ERROR
    
}
