package it.polimi.ingsw.am16.client.view.gui.util.guiErrors;

import it.polimi.ingsw.am16.client.view.gui.controllers.elements.ErrorController;
import it.polimi.ingsw.am16.common.util.FilePaths;
import javafx.scene.image.Image;

public class GUIInnocuousError implements GUIError{
    /**
     * This implementation is meant to be used when the error doesn't require the user
     * to change screen (e.g. an illegal move is performed during a game),
     * and thus won't require the error controller to perform any action
     * @param errorController The error controller.
     */
    @Override
    public void configurePopup(ErrorController errorController) {
        return;
    }
}
