package it.polimi.ingsw.am16.common.tcpMessages;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TCPMessage(@JsonProperty("messageType") MessageType messageType, @JsonProperty("payload") Payload payload) {

}
