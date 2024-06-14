package it.polimi.ingsw.am16.client.view.gui.util.guiErrors;

import it.polimi.ingsw.am16.client.view.gui.controllers.elements.ErrorController;

/**
 * Interface for the GUI errors. It is used to configure the error popup.
 */
public interface GUIError {
    /**
     * Configures the error popup, setting up its button.
     * @param errorController The error controller.
     */
    void configurePopup(ErrorController errorController);
}
