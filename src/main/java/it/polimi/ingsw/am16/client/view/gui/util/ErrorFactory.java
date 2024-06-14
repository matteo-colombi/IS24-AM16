package it.polimi.ingsw.am16.client.view.gui.util;

import it.polimi.ingsw.am16.client.view.gui.util.guiErrors.GUIError;
import it.polimi.ingsw.am16.client.view.gui.util.guiErrors.GUIHomeError;
import it.polimi.ingsw.am16.client.view.gui.util.guiErrors.GUIInnocuousError;
import it.polimi.ingsw.am16.client.view.gui.util.guiErrors.GUIQuitError;
import it.polimi.ingsw.am16.common.util.ErrorType;

import java.util.HashMap;
import java.util.Map;

public class ErrorFactory {
    private Map<ErrorType, GUIError> errorMap;

    public ErrorFactory() {
        errorMap = new HashMap<>();
        errorMap.put(ErrorType.JOIN_ERROR, new GUIHomeError());
        errorMap.put(ErrorType.CONNECTION_DEAD, new GUIQuitError());
        errorMap.put(ErrorType.OTHER_PLAYER_DISCONNECTED, new GUIHomeError());
    }

    public GUIError getError(ErrorType errorType) {
        if(errorMap.containsKey(errorType)) {
            return errorMap.get(errorType);
        }
        return new GUIInnocuousError();
    }
}
