package it.polimi.ingsw.am16.client.view.gui.controllers.elements;

import it.polimi.ingsw.am16.client.view.gui.CodexGUI;
import it.polimi.ingsw.am16.client.view.gui.util.GUIAssetRegistry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.List;
import java.util.Objects;

/**
 * Controller class for the popup that displays game rules.
 */
public class RulesPopupController {
    @FXML
    StackPane rootPopup;
    @FXML
    ImageView rulesBook;

    private int idx;

    private static final List<String> paths = GUIAssetRegistry.getGUIRulebookPaths();

    /**
     * Initializes the element for later use.
     */
    @FXML
    public void initialize() {
        idx = 0;

        rootPopup.setPrefWidth(CodexGUI.getGUI().getStage().getWidth());
        rootPopup.setPrefHeight(CodexGUI.getGUI().getStage().getHeight());
    }

    /**
     * Flips to the previous page of the rulebook.
     */
    @FXML
    public void prev(ActionEvent ignored) {
        idx = (paths.size() + idx - 1) % paths.size();

        rulesBook.setImage(new Image(Objects.requireNonNull(RulesPopupController.class.getResourceAsStream(paths.get(idx)))));
    }

    /**
     * Flips to the next page of the rulebook.
     */
    @FXML
    public void next(ActionEvent ignored) {
        idx = (paths.size() + idx + 1) % paths.size();

        rulesBook.setImage(new Image(Objects.requireNonNull(RulesPopupController.class.getResourceAsStream(paths.get(idx)))));
    }

    /**
     * Closes the popup.
     */
    @FXML
    public void back(ActionEvent ignored) {
        ((StackPane) rootPopup.getParent()).getChildren().remove(rootPopup);
    }

    /**
     * @return The root node of this element.
     */
    public Parent getRoot() {
        return rootPopup;
    }
}
