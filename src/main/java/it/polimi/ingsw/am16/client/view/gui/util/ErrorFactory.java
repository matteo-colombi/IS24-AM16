package it.polimi.ingsw.am16.client.view.gui.util;

import it.polimi.ingsw.am16.client.view.gui.util.guiErrors.GUIError;
import it.polimi.ingsw.am16.client.view.gui.util.guiErrors.GUIHomeError;
import it.polimi.ingsw.am16.client.view.gui.util.guiErrors.GUIInnocuousError;
import it.polimi.ingsw.am16.client.view.gui.util.guiErrors.GUIQuitError;
import it.polimi.ingsw.am16.common.util.ErrorType;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory class to convert errors sent by the server into GUI errors.
 */
public class ErrorFactory {
    /**
     * Data structure to map server errors to GUI errors.
     */
    private final Map<ErrorType, GUIError> errorMap;

    public ErrorFactory() {
        errorMap = new HashMap<>();
        errorMap.put(ErrorType.JOIN_ERROR, new GUIHomeError());
        errorMap.put(ErrorType.CONNECTION_DEAD, new GUIQuitError());
        errorMap.put(ErrorType.OTHER_PLAYER_DISCONNECTED, new GUIHomeError());
    }

    /**
     * Returns the GUI error corresponding to the server error.
     * @param errorType The type of error sent by the server
     * @return The corresponding GUI error
     */
    public GUIError getError(ErrorType errorType) {
        if(errorMap.containsKey(errorType)) {
            return errorMap.get(errorType);
        }
        return new GUIInnocuousError();
    }
}
