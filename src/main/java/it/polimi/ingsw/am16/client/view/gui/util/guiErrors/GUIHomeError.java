package it.polimi.ingsw.am16.client.view.gui.util.guiErrors;

import it.polimi.ingsw.am16.client.view.gui.controllers.elements.ErrorController;
import it.polimi.ingsw.am16.common.util.FilePaths;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.Objects;

/**
 * Implementation of GUIError for errors that require the user to go back to the home screen.
 */
public class GUIHomeError implements GUIError{

    private ErrorController errorController;

    @Override
    public void configurePopup(ErrorController errorController) {
        this.errorController = errorController;
        errorController.getButtonText().setText("Home");
        errorController.getButtonIcon().setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(FilePaths.GUI_ICONS + "/home.png"))));
        errorController.getErrorButton().setOnAction(errorController::goHome);
    }

    @Override
    public void show(Pane parent) {
        errorController.show(parent);
    }
}
