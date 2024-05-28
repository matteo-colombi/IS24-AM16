package it.polimi.ingsw.am16.common.tcpMessages;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Record that contains information about a TCP message.
 * @param messageType The type of the message.
 * @param payload The payload of the message, or <code>null</code> if no payload is required.
 */
public record TCPMessage(@JsonProperty("messageType") MessageType messageType, @JsonProperty("payload") Payload payload) {}
