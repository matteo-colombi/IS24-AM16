package it.polimi.ingsw.am16.client.view.gui.util.guiErrors;

import it.polimi.ingsw.am16.client.view.gui.controllers.elements.ErrorController;
import it.polimi.ingsw.am16.common.util.FilePaths;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.Objects;

/**
 * Implementation of GUIError for critical errors that require the user to quit the application.
 */
public class GUIQuitError implements GUIError{

    private ErrorController errorController;

    @Override
    public void configurePopup(ErrorController errorController) {
        this.errorController = errorController;
        errorController.getButtonText().setText("Quit");
        errorController.getButtonIcon().setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(FilePaths.GUI_ICONS + "/door.png"))));
        errorController.getErrorButton().setOnAction(errorController::quit);
    }

    @Override
    public void show(Pane parent) {
        errorController.show(parent);
    }
}
