package it.polimi.ingsw.am16.client.view.gui.util.guiErrors;

import it.polimi.ingsw.am16.client.view.gui.controllers.elements.ErrorController;
import javafx.scene.layout.Pane;

/**
 * Interface for the GUI errors. It is used to configure error popups.
 */
public interface GUIError {

    /**
     * Configures the error popup, setting up its button.
     * @param errorController The error controller.
     */
    void configurePopup(ErrorController errorController);

    /**
     * Displays the error.
     * @param parent The parent node in which this error should be displayed.
     */
    void show(Pane parent);
}
