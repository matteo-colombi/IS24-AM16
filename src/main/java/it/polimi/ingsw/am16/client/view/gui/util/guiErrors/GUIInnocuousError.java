package it.polimi.ingsw.am16.client.view.gui.util.guiErrors;

import it.polimi.ingsw.am16.client.view.gui.controllers.elements.ErrorController;
import it.polimi.ingsw.am16.common.util.FilePaths;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.Objects;

/**
 * Implementation of {@link GUIError} for error popups that can just be closed without further consequences.
 */
public class GUIInnocuousError implements GUIError{

    private ErrorController errorController;

    /**
     * This implementation is meant to be used when the error doesn't require the user
     * to change screen (e.g. an illegal move is performed during a game),
     * and thus won't require the error controller to perform any action
     * @param errorController The error controller.
     */
    @Override
    public void configurePopup(ErrorController errorController) {
        this.errorController = errorController;
        errorController.getButtonText().setText("Close");
        errorController.getButtonIcon().setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(FilePaths.GUI_ICONS + "/close.png"))));
        errorController.getErrorButton().setOnAction(errorController::close);
    }

    @Override
    public void show(Pane parent) {
        errorController.show(parent);
    }
}
