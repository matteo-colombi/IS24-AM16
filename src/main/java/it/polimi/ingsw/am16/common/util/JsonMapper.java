package it.polimi.ingsw.am16.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMapper {

    private static ObjectMapper instance = null;

    private JsonMapper() { }

    public static ObjectMapper getObjectMapper() {
        if(instance == null) {
            instance = new ObjectMapper();
        }
        return instance;
    }
}
