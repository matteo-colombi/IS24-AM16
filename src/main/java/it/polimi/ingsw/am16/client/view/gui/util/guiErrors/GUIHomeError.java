package it.polimi.ingsw.am16.client.view.gui.util.guiErrors;

import it.polimi.ingsw.am16.client.view.gui.controllers.elements.ErrorController;
import it.polimi.ingsw.am16.common.util.FilePaths;
import javafx.scene.image.Image;

/**
 * Implementation of GUIError for errors that require the user to go back to the home screen.
 */
public class GUIHomeError implements GUIError{
    /**
     * Configures the error popup, setting up its button.
     * @param errorController The error controller.
     */
    @Override
    public void configurePopup(ErrorController errorController) {
        errorController.getButtonText().setText("Home");
        errorController.getButtonIcon().setImage(new Image(getClass().getResource(FilePaths.GUI_ICONS + "/home.png").toExternalForm()));
        errorController.getErrorButton().setOnAction(errorController::goHome);
    }
}
