package it.polimi.ingsw.am16.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public enum JsonMapper {
    INSTANCE;

    JsonMapper() {}

    private final ObjectMapper mapper = new ObjectMapper();

    public ObjectMapper getObjectMapper() {
        return mapper;
    }
}
