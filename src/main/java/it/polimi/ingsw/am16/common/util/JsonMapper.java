package it.polimi.ingsw.am16.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Singleton that contains the only instance required of {@link ObjectMapper}, used for serializing and deserializing objects into JSON and from JSON.
 */
public class JsonMapper {

    private static ObjectMapper instance = null;

    private JsonMapper() { }

    /**
     * @return The {@link ObjectMapper} instance.
     */
    public static ObjectMapper getObjectMapper() {
        if(instance == null) {
            instance = new ObjectMapper();
        }
        return instance;
    }
}
